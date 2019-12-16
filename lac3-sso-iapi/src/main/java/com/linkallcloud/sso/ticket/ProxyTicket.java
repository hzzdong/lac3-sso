package com.linkallcloud.sso.ticket;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.principal.SimpleService;
import com.linkallcloud.sso.redis.ticket.RedisTicketCache;

/**
 * Represents a proxy ticket (PT).
 */
public class ProxyTicket extends ActiveTicket<ProxyGrantingTicket> {

	@JSONField(serialize = false, deserialize = false)
	private ProxyGrantingTicket grantor;

	public ProxyTicket() {
		super();
	}

	/** Constructs a new, immutable proxy ticket. */
	public ProxyTicket(ProxyGrantingTicket t, String appCode, String service, String siteUser, int siteMaping) {
		super(t, appCode, service, false, siteUser, siteMaping);
	}

	/** Constructs a new, immutable proxy ticket. */
	public ProxyTicket(ProxyGrantingTicket t, String appCode, String service) {
		/*
		 * By convention, a proxy ticket is never taken to proceed from an initial
		 * login. (That is, "renew=true" will always fail for a proxy ticket.) Because
		 * of this, we pass "false" to the parent class's constructor.
		 */
		super(t, appCode, service, false);
	}

	/** Retrieves the proxy ticket's lineage -- its chain of "trust." */
	@JSONField(serialize = false, deserialize = false)
	public List<SimpleService> getProxies() {
		return getGrantor().getProxies();
	}

	@JSONField(serialize = false, deserialize = false)
	@Override
	public ProxyGrantingTicket getGrantor() {
		return grantor;
	}

	@Override
	public void setGrantor(ProxyGrantingTicket grantor) {
		this.grantor = grantor;
	}
	
	public void loadReference(RedisTicketCache<? extends Ticket> cache) {
		if (!Strings.isBlank(this.getGrantorId())) {
			ProxyGrantingTicket pgt = cache.getTicket(this.getGrantorId(), ProxyGrantingTicket.class);
			this.setGrantor(pgt);
			pgt.loadReference(cache);
		}
	}

}
