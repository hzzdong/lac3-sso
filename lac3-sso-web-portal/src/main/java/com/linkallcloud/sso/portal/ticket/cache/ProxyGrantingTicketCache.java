package com.linkallcloud.sso.portal.ticket.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.linkallcloud.sso.portal.ticket.ProxyGrantingTicket;

@Component
public class ProxyGrantingTicketCache extends GrantorCache<ProxyGrantingTicket> {

	@Value("${lac.sso.pgt.timeout:7200}")
	private int tolerance;

	public ProxyGrantingTicketCache() {
		super();
	}

	@Override
	protected String getTicketPrefix() {
		return "PGT";
	}

	@Override
	protected int getSerialNumber() {
		return ticketCache.increment("PGT");
	}

	@Override
	protected int getTolerance() {
		return tolerance;
	}

}
