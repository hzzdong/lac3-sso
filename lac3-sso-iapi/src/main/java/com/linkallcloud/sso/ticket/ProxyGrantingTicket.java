package com.linkallcloud.sso.ticket;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
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
	// private ActiveTicket<?> parent;
	private ServiceTicket stParent;
	private ProxyTicket ptParent;

	/**
	 * A PGT is also consturcted with the ID of the proxy. In CAS 2.0.1, this ID
	 * corresponds to the callback URL to which the PGT's ID is sent.
	 */
	private SimpleService proxyId;

	// *********************************************************************
	// Constructor

	public ProxyGrantingTicket() {
		super();
	}

	/** Constructs a new, immutable ProxyGrantingTicket. */
	public ProxyGrantingTicket(ActiveTicket<?> parent, String pgtUrl, String pgtAppCode) {
		super(parent.getUsername());
		if (parent instanceof ServiceTicket) {
			this.stParent = (ServiceTicket) parent;
		} else {
			this.ptParent = (ProxyTicket) parent;
		}
		this.proxyId = new SimpleService(pgtUrl, pgtAppCode);
	}

	// *********************************************************************
	// Public interface

	/** Retrieves the ticket's username. */
	public String getUsername() {
		return getParent().getUsername();
	}

	/** Returns the ticket that was exchanged for this ProxyGrantingTicket. */
	@JSONField(serialize = false, deserialize = false)
	public ActiveTicket<?> getParent() {
		return stParent != null ? stParent : ptParent;
	}

	public ServiceTicket getStParent() {
		return stParent;
	}

	public ProxyTicket getPtParent() {
		return ptParent;
	}

	public void setStParent(ServiceTicket stParent) {
		this.stParent = stParent;
	}

	public void setPtParent(ProxyTicket ptParent) {
		this.ptParent = ptParent;
	}

	/**
	 * Returns the identifier for the service to whom this ticket will grant proxy
	 * tickets.
	 */
	public SimpleService getProxyId() {
		return proxyId;
	}

	/** Retrieves trust chain. */
	@JSONField(serialize = false, deserialize = false)
	public List<SimpleService> getProxies() {
		List<SimpleService> l = new ArrayList<SimpleService>();
		l.add(getProxyId());
		GrantingTicket grantor = getParent().getGrantor();
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
		return super.isExpired() || getParent().getGrantor().isExpired();
	}
}
