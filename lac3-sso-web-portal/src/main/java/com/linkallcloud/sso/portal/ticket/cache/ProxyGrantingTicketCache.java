package com.linkallcloud.sso.portal.ticket.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.linkallcloud.sso.portal.redis.ticket.RedisProxyGrantingTicketCache;
import com.linkallcloud.sso.portal.ticket.ProxyGrantingTicket;

@Component
public class ProxyGrantingTicketCache extends GrantorCache<ProxyGrantingTicket, RedisProxyGrantingTicketCache> {

	@Autowired
	private RedisProxyGrantingTicketCache pgtCache;

	@Value("${lac.sso.pgt.timeout:7200}")
	private int tolerance;

	public ProxyGrantingTicketCache() {
		super();
	}

	@Override
	public String getTicketPrefix() {
		return "PGT";
	}

	@Override
	protected int getTolerance() {
		return tolerance;
	}

	@Override
	protected RedisProxyGrantingTicketCache getCache() {
		return pgtCache;
	}

	public int getPGTIOUSerialNumber() {
		return getCache().increment("PGTIOU");
	}

}
