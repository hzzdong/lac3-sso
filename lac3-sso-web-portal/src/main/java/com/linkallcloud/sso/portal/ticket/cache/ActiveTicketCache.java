package com.linkallcloud.sso.portal.ticket.cache;

import com.linkallcloud.sso.portal.exception.TicketException;
import com.linkallcloud.sso.portal.redis.ticket.RedisTicketCache;
import com.linkallcloud.sso.portal.ticket.Ticket;

/**
 * Represents a cache of tickets that each expire after a configurable period of
 * inactivity (i.e., not being retrieved).
 */
public abstract class ActiveTicketCache<T extends Ticket, C extends RedisTicketCache<T>> implements TicketCache<T> {

	/** Generates and returns a new, unique ticket ID */
	protected abstract String newTicketId();

	/** Stores the given ticket, associating it with the given identifier. */
	protected abstract void storeTicket(String ticketId, T t) throws TicketException;

	/** Retrieves the ticket with the given identifier. */
	protected abstract T retrieveTicket(String ticketId);

	/** Number of seconds after which the current cache will expire tickets. */
	protected abstract int getTolerance();

	protected abstract C getCache();

	@Override
	public synchronized String addTicket(T t) throws TicketException {
		String ticketId = newTicketId();
		// resetTimer(ticketId);
		storeTicket(ticketId, t);
		return ticketId;
	}

	@Override
	public T getTicket(String ticketId) {
		// resetTimer(ticketId);
		return retrieveTicket(ticketId);
	}

	@Override
	public int getSerialNumber() {
		return getCache().increment(getTicketPrefix());
	}

}
