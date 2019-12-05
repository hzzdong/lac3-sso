package com.linkallcloud.sso.client.proxy.storage;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;

public class ProxyGrantingTicketRedisStorage implements ProxyGrantingTicketStorage {

	/**
	 * Default timeout in seconds.
	 */
	private static final long DEFAULT_TIMEOUT = 60;

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	public ProxyGrantingTicketRedisStorage() {
		super();
	}

	public ProxyGrantingTicketRedisStorage(StringRedisTemplate stringRedisTemplate) {
		super();
		this.stringRedisTemplate = stringRedisTemplate;
	}

	@Override
	public void save(String proxyGrantingTicketIou, String proxyGrantingTicket) {
		stringRedisTemplate.opsForValue().set(proxyGrantingTicketIou, proxyGrantingTicket, DEFAULT_TIMEOUT,
				TimeUnit.SECONDS);
	}

	@Override
	public String retrieve(String proxyGrantingTicketIou) {
		if (stringRedisTemplate.hasKey(proxyGrantingTicketIou)) {
			String v = stringRedisTemplate.opsForValue().get(proxyGrantingTicketIou);
			stringRedisTemplate.delete(proxyGrantingTicketIou);
			return v;
		}
		return null;
	}

}
