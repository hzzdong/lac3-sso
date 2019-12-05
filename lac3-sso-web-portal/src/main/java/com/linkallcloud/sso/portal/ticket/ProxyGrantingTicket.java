package com.linkallcloud.sso.portal.ticket;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a CAS proxy-granting ticket (PGT), used to retrieve proxy tickets
 * (PTs). Note that we extend TicketGrantingTicket; this doesn't mean we can act
 * as a TGC: a TicketCache for TGCs will never store a PGT.
 */
public class ProxyGrantingTicket extends GrantingTicket {

	// *********************************************************************
	// Private, ticket state

	/**
	 * A PGT is constructed with a ServiceTicket (potentially a ProxyTicket). We
	 * store this ticket to inherit ticket expiration.
	 */
	private ActiveTicket<?> parent;

	/**
	 * A PGT is also consturcted with the ID of the proxy. In CAS 2.0.1, this ID
	 * corresponds to the callback URL to which the PGT's ID is sent.
	 */
	private String proxyId;
	// private SimpleService proxyId;

	// *********************************************************************
	// Constructor

	public ProxyGrantingTicket() {
		super();
	}

	/** Constructs a new, immutable ProxyGrantingTicket. */
	public ProxyGrantingTicket(ActiveTicket<?> parent, String pgtUrl) {// String pgtAppCode,
		super(parent.getUsername());
		this.parent = parent;
		// this.proxyId = new SimpleService(pgtUrl, pgtAppCode);
		this.proxyId = pgtUrl;
	}

	// *********************************************************************
	// Public interface

	/** Retrieves the ticket's username. */
	public String getUsername() {
		return parent.getUsername();
	}

	/** Returns the ticket that was exchanged for this ProxyGrantingTicket. */
	public ActiveTicket<?> getParent() {
		return parent;
	}

	/**
	 * Returns the identifier for the service to whom this ticket will grant proxy
	 * tickets.
	 */
	public String getProxyId() {
		return proxyId;
	}

	public void setParent(ActiveTicket<?> parent) {
		this.parent = parent;
	}

	/** Retrieves trust chain. */
	public List<String> getProxies() {
		List<String> l = new ArrayList<String>();
		l.add(getProxyId());
		GrantingTicket grantor = parent.getGrantor();
		if (grantor instanceof ProxyGrantingTicket) {
			ProxyGrantingTicket p = (ProxyGrantingTicket) grantor;
			l.addAll(p.getProxies());
		}
		return l;
	}

	/**
	 * Returns true if the ticket (or any ticket in its grantor chain) is expired,
	 * false otherwise.
	 */
	public boolean isExpired() {
		return super.isExpired() || parent.getGrantor().isExpired();
	}
}
