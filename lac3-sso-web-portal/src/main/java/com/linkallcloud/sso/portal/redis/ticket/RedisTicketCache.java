package com.linkallcloud.sso.portal.redis.ticket;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.sso.portal.redis.RedisCache;
import com.linkallcloud.sso.portal.ticket.Ticket;

public abstract class RedisTicketCache<T extends Ticket> extends RedisCache {

	protected abstract RedisTemplate<String, T> getTicketTemplate();

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

}
