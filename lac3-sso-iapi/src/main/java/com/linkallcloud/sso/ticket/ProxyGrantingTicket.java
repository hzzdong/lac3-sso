package com.linkallcloud.sso.ticket;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.principal.SimpleService;
import com.linkallcloud.sso.exception.AuthException;
import com.linkallcloud.sso.redis.ticket.RedisTicketCache;

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
	@JSONField(serialize = false, deserialize = false)
	private ActiveTicket<?> parent;
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
	public ProxyGrantingTicket(ActiveTicket<?> parent, String pgtUrl, String pgtAppCode, int pgtAppClazz) {
		super(parent.getUsername(), pgtAppClazz);
		if (pgtAppClazz != parent.getService().getClazz()) {
			throw new AuthException("本次访问应用类型和上次访问应用类型不一致。");
		}
		this.parent = parent;
		// this.parentId = Util.getInnerTicketId(parent.getId());
		if (parent instanceof ServiceTicket) {
			this.stParent = (ServiceTicket) parent;
		} else {
			this.ptParent = (ProxyTicket) parent;
		}
		this.proxyId = new SimpleService(pgtUrl, pgtAppCode, pgtAppClazz);
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

	public void setParent(ActiveTicket<?> parent) {
		this.parent = parent;
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

	public void loadReference(RedisTicketCache<? extends Ticket> cache) {
//		if (!Strings.isBlank(this.getParentId())) {
//			if (this.getParentId().startsWith(Util.INNER_TICKET_ID_FREFIX + "ST-")) {
//				ServiceTicket st = cache.getTicket(this.getParentId(), ServiceTicket.class);
//				this.setParent(st);
//				st.loadReference(cache);
//			} else if (this.getParentId().startsWith(Util.INNER_TICKET_ID_FREFIX + "PT-")) {
//				ProxyTicket pt = cache.getTicket(this.getParentId(), ProxyTicket.class);
//				this.setParent(pt);
//				pt.loadReference(cache);
//			}
//		}

		if (this.getParent() != null) {
			this.getParent().loadReference(cache);
		}

	}
}
