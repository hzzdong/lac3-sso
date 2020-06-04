package com.linkallcloud.sso.server.redis;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.lang.Strings;

@Component
public class RedisLockBlackCache extends RedisCache {

	@Resource
	private RedisTemplate<String, Date> lockBlackTemplate;

	public static String getBlackKey(int appClazz, String blackTarget) {
		return "Black:" + appClazz + ":" + blackTarget;
	}

	public static String getLockKey(int appClazz, String lockTarget) {
		return "Lock:" + appClazz + ":" + lockTarget;
	}

	/**
	 * 写入redis缓存（设置expire存活时间）
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public boolean put(final String key, Date value) {
		try {
			lockBlackTemplate.opsForValue().set(key, value);
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
	public boolean put(final String key, Date value, long expire) {
		if (expire > 0) {
			try {
				lockBlackTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
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
	public Date get(final String key) {
		if (!Strings.isBlank(key)) {
			try {
				return lockBlackTemplate.opsForValue().get(key);
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
			return lockBlackTemplate.hasKey(key);
		} catch (Exception e) {
			log.errorf("判断redis缓存中是否有对应的key(%s)失败！错误信息为：%s", key, e.getMessage());
		}
		return false;
	}

	public Date remove(final String key) {
		try {
			if (exists(key)) {
				Date o = this.get(key);
				lockBlackTemplate.delete(key);
				return o;
			}
		} catch (Exception e) {
			log.errorf("redis删除key(%s)失败！错误信息为：%s", key, e.getMessage());
		}
		return null;
	}

}
