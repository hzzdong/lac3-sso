package com.linkallcloud.sso.portal.ticket;

import java.util.ArrayList;
import java.util.List;

import com.linkallcloud.core.principal.SimpleService;

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
	// private String proxyId;
	private SimpleService proxyId;

	// *********************************************************************
	// Constructor

	public ProxyGrantingTicket() {
		super();
	}

	/** Constructs a new, immutable ProxyGrantingTicket. */
	public ProxyGrantingTicket(ActiveTicket<?> parent, String pgtAppCode, String pgtUrl) {
		super(parent.getUsername());
		this.parent = parent;
		this.proxyId = new SimpleService(pgtUrl, pgtAppCode);
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
	public SimpleService getProxyService() {
		return proxyId;
	}

	public SimpleService getProxyId() {
		return proxyId;
	}

	public void setProxyId(SimpleService proxyId) {
		this.proxyId = proxyId;
	}

	public void setParent(ActiveTicket<?> parent) {
		this.parent = parent;
	}

	/** Retrieves trust chain. */
	public List<SimpleService> getProxies() {
		List<SimpleService> l = new ArrayList<SimpleService>();
		l.add(getProxyService());
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
