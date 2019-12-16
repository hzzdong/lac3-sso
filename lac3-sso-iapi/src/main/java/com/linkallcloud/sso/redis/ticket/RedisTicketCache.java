package com.linkallcloud.sso.redis.ticket;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.sso.redis.RedisCache;
import com.linkallcloud.sso.ticket.Ticket;

public abstract class RedisTicketCache<T extends Ticket> extends RedisCache {

	protected Mirror<T> mirror;

	@Resource
	protected StringRedisTemplate stringRedisTemplate;

	@SuppressWarnings("unchecked")
	public RedisTicketCache() {
		super();
		try {
			mirror = Mirror.me((Class<T>) Mirror.getTypeParams(getClass())[0]);
		} catch (Throwable e) {
			if (log.isWarnEnabled()) {
				log.warn("!!!Fail to get TypeParams for self!", e);
			}
		}
	}

	public Class<T> getTicketClass() {
		return (null == mirror) ? null : mirror.getType();
	}

	public String ticket2String(T ticket) {
		return JSON.toJSONString(ticket);
	}

	public <RT extends Ticket> RT string2Ticket(String ticketStr, Class<RT> cl) {
		return JSON.parseObject(ticketStr, cl);
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
			stringRedisTemplate.opsForValue().set(key, ticket2String(value));
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
				stringRedisTemplate.opsForValue().set(key, ticket2String(value), expire, TimeUnit.SECONDS);
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
				String tjson = stringRedisTemplate.opsForValue().get(key);
				if (!Strings.isBlank(tjson)) {
					T ticket = string2Ticket(tjson, getTicketClass());
					if (ticket != null) {
						ticket.loadReference(this);
					}
					return ticket;
				}
			} catch (Exception e) {
				log.errorf("读取redis缓存(%s)失败！错误信息为：%s", key, e.getMessage());
			}
		}
		return null;
	}

	public <RT extends Ticket> RT getTicket(final String key, Class<RT> tc) {
		if (!Strings.isBlank(key)) {
			try {
				String tjson = stringRedisTemplate.opsForValue().get(key);
				RT ticket = string2Ticket(tjson, tc);
				return ticket;
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
			return stringRedisTemplate.hasKey(key);
		} catch (Exception e) {
			log.errorf("判断redis缓存中是否有对应的key(%s)失败！错误信息为：%s", key, e.getMessage());
		}
		return false;
	}

	public T remove(final String key) {
		try {
			if (exists(key)) {
				T ticket = this.get(key);
				stringRedisTemplate.delete(key);
				return ticket;
			}
		} catch (Exception e) {
			log.errorf("redis删除key(%s)失败！错误信息为：%s", key, e.getMessage());
		}
		return null;
	}

}
