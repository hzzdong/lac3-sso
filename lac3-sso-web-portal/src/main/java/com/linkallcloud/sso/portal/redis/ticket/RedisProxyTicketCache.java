package com.linkallcloud.sso.portal.redis.ticket;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.linkallcloud.sso.portal.ticket.ProxyTicket;

@Component
public class RedisProxyTicketCache extends RedisTicketCache<ProxyTicket> {
	
	@Resource
	private RedisTemplate<String, ProxyTicket> redisPTTemplate;

	@Override
	protected RedisTemplate<String, ProxyTicket> getTicketTemplate() {
		return redisPTTemplate;
	}

}
