package com.linkallcloud.sso.portal.redis;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;

public class RedisCache {

	protected Log log = Logs.get();

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	public RedisTemplate<String, Object> getObjectTemplate() {
		return redisTemplate;
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
	 * 判断redis缓存中是否有对应的key
	 * 
	 * @param key
	 * @return
	 */
	public boolean existObject(final String key) {
		try {
			return getObjectTemplate().hasKey(key);
		} catch (Exception e) {
			log.errorf("判断redis缓存中是否有对应的key(%s)失败！错误信息为：%s", key, e.getMessage());
		}
		return false;
	}

	public Object removeObject(final String key) {
		try {
			if (existObject(key)) {
				Object o = this.getObject(key);
				getObjectTemplate().delete(key);
				return o;
			}
		} catch (Exception e) {
			log.errorf("redis删除key(%s)失败！错误信息为：%s", key, e.getMessage());
		}
		return null;
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
		if (this.existObject(key)) {
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
