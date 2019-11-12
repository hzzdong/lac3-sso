package com.linkallcloud.sso.portal.ticket.cache;

import org.springframework.stereotype.Component;

@Component
public class UNKGrantorCache extends GrantorCache {

	public UNKGrantorCache() {
		super(7200);
	}

	@Override
	protected String getTicketPrefix() {
		return "UNK";
	}

}
