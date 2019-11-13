package com.linkallcloud.sso.portal.ticket.cache.redis;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.linkallcloud.sso.portal.ticket.TicketGrantingTicket;

@Component
public class RedisTicketGrantingTicketCache extends RedisTicketCache<TicketGrantingTicket> {

	@Resource
	private RedisTemplate<String, TicketGrantingTicket> redisTGTTemplate;

	@Override
	protected RedisTemplate<String, TicketGrantingTicket> getTicketTemplate() {
		return redisTGTTemplate;
	}

}
