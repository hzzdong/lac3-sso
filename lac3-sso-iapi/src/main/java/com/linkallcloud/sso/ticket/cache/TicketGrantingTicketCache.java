package com.linkallcloud.sso.ticket.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.linkallcloud.sso.redis.ticket.RedisTicketGrantingTicketCache;
import com.linkallcloud.sso.ticket.TicketGrantingTicket;

@Component
public class TicketGrantingTicketCache extends GrantorCache<TicketGrantingTicket, RedisTicketGrantingTicketCache> {

	@Autowired
	private RedisTicketGrantingTicketCache tgtCache;

	@Value("${lac.sso.tgt.timeout:7200}")
	private int tolerance;

	public TicketGrantingTicketCache() {
		super();
	}

	@Override
	protected int getTolerance() {
		return tolerance;
	}

	@Override
	protected RedisTicketGrantingTicketCache getCache() {
		return tgtCache;
	}

	@Override
	public String getTicketPrefix() {
		return "TGC";
	}

}
