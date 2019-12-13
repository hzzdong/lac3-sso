package com.linkallcloud.sso.redis.ticket;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.linkallcloud.sso.ticket.ProxyGrantingTicket;

@Component
public class RedisProxyGrantingTicketCache extends RedisTicketCache<ProxyGrantingTicket> {

	@Resource
	private RedisTemplate<String, ProxyGrantingTicket> redisPGTTemplate;

	@Override
	protected RedisTemplate<String, ProxyGrantingTicket> getTicketTemplate() {
		return redisPGTTemplate;
	}

}
