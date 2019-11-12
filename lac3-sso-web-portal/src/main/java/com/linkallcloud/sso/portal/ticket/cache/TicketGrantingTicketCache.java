package com.linkallcloud.sso.portal.ticket.cache;

import org.springframework.stereotype.Component;

@Component
public class TicketGrantingTicketCache extends GrantorCache {

	public TicketGrantingTicketCache() {
		super(7200);
	}

	@Override
	protected String getTicketPrefix() {
		return "TGC";
	}

}
