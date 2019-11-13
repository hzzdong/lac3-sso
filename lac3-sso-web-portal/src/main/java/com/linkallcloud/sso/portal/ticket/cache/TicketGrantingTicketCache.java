package com.linkallcloud.sso.portal.ticket.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.linkallcloud.sso.portal.ticket.TicketGrantingTicket;

@Component
public class TicketGrantingTicketCache extends GrantorCache<TicketGrantingTicket> {

	@Value("${lac.sso.tgt.timeout:7200}")
	private int tolerance;

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
