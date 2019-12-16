package com.linkallcloud.sso.ticket;

import com.linkallcloud.sso.redis.ticket.RedisTicketCache;

/**
 * Represents a CAS ticket-granting ticket, typically vended as a cookie (TGC).
 * This class represents, in the general sense, a ticket that is used to grant
 * other ticket; it becomes a "powerful" TGC only when vended as such and stored
 * in the TGC cache.
 */
public class TicketGrantingTicket extends GrantingTicket {

	public TicketGrantingTicket() {
		super();
	}

	public TicketGrantingTicket(String username) {
		super(username);
	}

	@Override
	public void loadReference(RedisTicketCache<? extends Ticket> cache) {
	}

}
