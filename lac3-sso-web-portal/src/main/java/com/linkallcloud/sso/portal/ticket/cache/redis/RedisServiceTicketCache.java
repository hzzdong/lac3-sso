package com.linkallcloud.sso.portal.ticket.cache.redis;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.linkallcloud.sso.portal.ticket.ServiceTicket;

@Component
public class RedisServiceTicketCache extends RedisTicketCache<ServiceTicket> {

	@Resource
	private RedisTemplate<String, ServiceTicket> redisSTTemplate;

	@Override
	protected RedisTemplate<String, ServiceTicket> getTicketTemplate() {
		return redisSTTemplate;
	}

}
