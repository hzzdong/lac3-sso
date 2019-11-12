package com.linkallcloud.sso.portal.utils;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.sso.portal.ticket.Ticket;

@Component
public class RedisTicketCache {
	private Log log = Logs.get();

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	public RedisTemplate<String, Object> getRedisTemplate() {
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
	public boolean put(final String key, Object value) {
		try {
			getRedisTemplate().opsForValue().set(key, value);
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
	public <T extends Ticket> boolean put(final String key, T value) {
		return this.put(key, value);
	}

	/**
	 * 写入redis缓存（设置expire存活时间）
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public boolean put(final String key, Object value, long expire) {
		if (expire > 0) {
			try {
				getRedisTemplate().opsForValue().set(key, value, expire, TimeUnit.SECONDS);
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
	 * 写入redis缓存（设置expire存活时间）
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public <T extends Ticket> boolean put(final String key, T value, long expire) {
		return this.put(key, value, expire);
	}

	/**
	 * 读取redis缓存
	 * 
	 * @param key
	 * @return
	 */
	public Object getObject(final String key) {
		if (!Strings.isBlank(key)) {
			try {
				return getRedisTemplate().opsForValue().get(key);
			} catch (Exception e) {
				log.errorf("读取redis缓存(%s)失败！错误信息为：%s", key, e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 读取redis缓存
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Ticket> T get(final String key) {
		return (T) this.get(key);
	}

	/**
	 * 判断redis缓存中是否有对应的key
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		try {
			return getRedisTemplate().hasKey(key);
		} catch (Exception e) {
			log.errorf("判断redis缓存中是否有对应的key(%s)失败！错误信息为：%s", key, e.getMessage());
		}
		return false;
	}

	public boolean removeObject(final String key) {
		try {
			if (exists(key)) {
				getRedisTemplate().delete(key);
			}
			return true;
		} catch (Exception e) {
			log.errorf("redis删除key(%s)失败！错误信息为：%s", key, e.getMessage());
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Ticket> T remove(final String key) {
		try {
			if (exists(key)) {
				T ticket = (T) this.get(key);
				getRedisTemplate().delete(key);
				return ticket;
			}
		} catch (Exception e) {
			log.errorf("redis删除key(%s)失败！错误信息为：%s", key, e.getMessage());
		}
		return null;
	}

	/**
	 * redis根据keys批量删除对应的value
	 * 
	 * @param keys
	 * @return
	 */
	public void remove(final String... keys) {
		for (String key : keys) {
			remove(key);
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
		this.put(key, count);
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
		this.put(key, count);
		return count;
	}

}
