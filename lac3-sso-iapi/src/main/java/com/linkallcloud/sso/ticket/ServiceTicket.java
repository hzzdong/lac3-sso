package com.linkallcloud.sso.ticket;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.sso.redis.ticket.RedisTicketCache;

/**
 * Represents a service ticket (ST).
 */
public class ServiceTicket extends ActiveTicket<TicketGrantingTicket> {

	@JSONField(serialize = false, deserialize = false)
	private TicketGrantingTicket grantor;

	public ServiceTicket() {
		super();
	}

	/** Constructs a new, immutable service ticket. */
	public ServiceTicket(TicketGrantingTicket t, String appCode, String service, boolean fromNewLogin, String siteUser,
			int siteMaping) {
		super(t, appCode, service, fromNewLogin, siteUser, siteMaping);
	}

	/** Constructs a new, immutable service ticket. */
	public ServiceTicket(TicketGrantingTicket t, String appCode, String service, boolean fromNewLogin) {
		super(t, appCode, service, fromNewLogin);
	}

	@JSONField(serialize = false, deserialize = false)
	@Override
	public TicketGrantingTicket getGrantor() {
		return grantor;
	}

	@Override
	public void setGrantor(TicketGrantingTicket grantor) {
		this.grantor = grantor;
	}

	public void loadReference(RedisTicketCache<? extends Ticket> cache) {
		if (!Strings.isBlank(this.getGrantorId())) {
			TicketGrantingTicket tgt = cache.getTicket(this.getGrantorId(), TicketGrantingTicket.class);
			this.setGrantor(tgt);
		}
	}

}
