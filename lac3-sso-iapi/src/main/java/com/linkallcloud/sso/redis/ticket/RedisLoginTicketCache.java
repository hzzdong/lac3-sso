package com.linkallcloud.sso.redis.ticket;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.linkallcloud.sso.ticket.LoginTicket;

@Component
public class RedisLoginTicketCache extends RedisTicketCache<LoginTicket> {
	
	@Resource
	private RedisTemplate<String, LoginTicket> redisLtTemplate;

	@Override
	protected RedisTemplate<String, LoginTicket> getTicketTemplate() {
		return redisLtTemplate;
	}

}
