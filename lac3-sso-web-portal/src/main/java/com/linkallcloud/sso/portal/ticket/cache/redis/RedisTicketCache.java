package com.linkallcloud.sso.portal.ticket.cache.redis;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.sso.portal.ticket.Ticket;

public abstract class RedisTicketCache<T extends Ticket> {
	private Log log = Logs.get();

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	protected abstract RedisTemplate<String, T> getTicketTemplate();

	public RedisTemplate<String, Object> getObjectTemplate() {
		return redisTemplate;
	}

	/**
	 * 写入redis缓存（设置expire存活时间）
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public boolean put(final String key, T value) {
		try {
			getTicketTemplate().opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			log.errorf("写入redis缓存(%s)失败！错误信息为：%s", key, e.getMessage());
			return false;
		}
	}

	/**
	 * 写入redis缓存（设置expire存活时间）
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public boolean put(final String key, T value, long expire) {
		if (expire > 0) {
			try {
				getTicketTemplate().opsForValue().set(key, value, expire, TimeUnit.SECONDS);
				return true;
			} catch (Exception e) {
				log.errorf("写入redis缓存(%s)失败！错误信息为：%s", key, e.getMessage());
				return false;
			}
		} else {
			return this.put(key, value);
		}
	}

	/**
	 * 读取redis缓存
	 * 
	 * @param key
	 * @return
	 */
	public T get(final String key) {
		if (!Strings.isBlank(key)) {
			try {
				return getTicketTemplate().opsForValue().get(key);
			} catch (Exception e) {
				log.errorf("读取redis缓存(%s)失败！错误信息为：%s", key, e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 判断redis缓存中是否有对应的key
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		try {
			return getTicketTemplate().hasKey(key);
		} catch (Exception e) {
			log.errorf("判断redis缓存中是否有对应的key(%s)失败！错误信息为：%s", key, e.getMessage());
		}
		return false;
	}

	public T remove(final String key) {
		try {
			if (exists(key)) {
				T ticket = this.get(key);
				getTicketTemplate().delete(key);
				return ticket;
			}
		} catch (Exception e) {
			log.errorf("redis删除key(%s)失败！错误信息为：%s", key, e.getMessage());
		}
		return null;
	}

	/*
	 * Object
	 *******************************************************************************************/

	/**
	 * 读取redis缓存
	 * 
	 * @param key
	 * @return
	 */
	public Object getObject(final String key) {
		if (!Strings.isBlank(key)) {
			try {
				return getObjectTemplate().opsForValue().get(key);
			} catch (Exception e) {
				log.errorf("读取redis缓存(%s)失败！错误信息为：%s", key, e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 写入redis缓存（设置expire存活时间）
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public boolean putObject(final String key, Object value) {
		try {
			getObjectTemplate().opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			log.errorf("写入redis缓存(%s)失败！错误信息为：%s", key, e.getMessage());
			return false;
		}
	}

	/**
	 * 写入redis缓存（设置expire存活时间）
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public boolean putObject(final String key, Object value, long expire) {
		if (expire > 0) {
			try {
				getObjectTemplate().opsForValue().set(key, value, expire, TimeUnit.SECONDS);
				return true;
			} catch (Exception e) {
				log.errorf("写入redis缓存(%s)失败！错误信息为：%s", key, e.getMessage());
				return false;
			}
		} else {
			return this.putObject(key, value);
		}
	}

	/**
	 * 计数器
	 * 
	 * @param key
	 * @param start
	 * @return
	 */
	public int increment(String key) {
		Integer count = 1;
		if (this.exists(key)) {
			count = (Integer) this.getObject(key);
			count++;
		}
		this.putObject(key, count);
		return count;
	}

	/**
	 * 启动或者重启计数器
	 * 
	 * @param key
	 * @param start
	 * @return
	 */
	public long increment(String key, Integer start) {
		Integer count = (start != null && start > 0) ? start : 1;
		this.putObject(key, count);
		return count;
	}

}
