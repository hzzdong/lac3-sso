package com.linkallcloud.sso.portal.ticket.cache;

import org.springframework.stereotype.Component;

import com.linkallcloud.sso.portal.ticket.TicketGrantingTicket;

@Component
public class TicketGrantingTicketCache extends GrantorCache<TicketGrantingTicket> {

	private int tolerance = 7200;

	public TicketGrantingTicketCache() {
		super();
	}

	@Override
	protected String getTicketPrefix() {
		return "TGC";
	}

	@Override
	protected int getSerialNumber() {
		return ticketCache.increment("TGT");
	}

	@Override
	protected int getTolerance() {
		return tolerance;
	}

}
