package com.linkallcloud.sso.portal.ticket.cache;

import org.springframework.stereotype.Component;

@Component
public class ProxyGrantingTicketCache extends GrantorCache {

	public ProxyGrantingTicketCache() {
		super(7200);
	}

	@Override
	protected String getTicketPrefix() {
		return "PGT";
	}

}
