package com.linkallcloud.sso.portal.ticket;

import java.util.List;

/**
 * Represents a proxy ticket (PT).
 */
public class ProxyTicket extends ActiveTicket<ProxyGrantingTicket> {

	/** Constructs a new, immutable proxy ticket. */
	public ProxyTicket(ProxyGrantingTicket t, String service) {
		/*
		 * By convention, a proxy ticket is never taken to proceed from an initial
		 * login. (That is, "renew=true" will always fail for a proxy ticket.) Because
		 * of this, we pass "false" to the parent class's constructor.
		 */
		super(t, service, false);
	}

	/** Retrieves the proxy ticket's lineage -- its chain of "trust." */
	public List<String> getProxies() {
		return getGrantor().getProxies();
	}

}
