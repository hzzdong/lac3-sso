package com.linkallcloud.sso.portal.ticket.cache;

import com.linkallcloud.sso.portal.redis.ticket.RedisTicketCache;
import com.linkallcloud.sso.portal.ticket.Ticket;

/**
 * Represents a cache of tickets, each of which may be retrieved only once. That
 * is, retrieval entails deletion. Expiration also occurs for inactivity.
 */
public abstract class OTUTicketCache<T extends Ticket, C extends RedisTicketCache<T>> extends ActiveTicketCache<T, C> {

	public OTUTicketCache() {
		super();
	}

	@Override
	public T getTicket(String ticketId) {
		T t = super.getTicket(ticketId);
		deleteTicket(ticketId);
		return t;
	}
}
