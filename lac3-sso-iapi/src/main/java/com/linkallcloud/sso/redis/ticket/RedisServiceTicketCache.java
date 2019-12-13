package com.linkallcloud.sso.redis.ticket;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.linkallcloud.sso.ticket.ServiceTicket;

@Component
public class RedisServiceTicketCache extends RedisTicketCache<ServiceTicket> {

	@Resource(name = "redisSTTemplate")
	private RedisTemplate<String, ServiceTicket> redisSTTemplate;

	@Override
	protected RedisTemplate<String, ServiceTicket> getTicketTemplate() {
		return redisSTTemplate;
	}

}
