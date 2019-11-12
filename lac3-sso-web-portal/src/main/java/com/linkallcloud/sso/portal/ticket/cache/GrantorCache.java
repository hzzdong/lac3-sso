package com.linkallcloud.sso.portal.ticket.cache;

import java.security.SecureRandom;

import com.linkallcloud.sso.portal.exception.DuplicateTicketException;
import com.linkallcloud.sso.portal.exception.TicketException;
import com.linkallcloud.sso.portal.ticket.GrantingTicket;
import com.linkallcloud.sso.portal.utils.Util;

/**
 * Represents a generic cache of granting tickets. Can be used as a store for
 * TGCs or PGTs.
 */
public abstract class GrantorCache<T extends GrantingTicket> extends ActiveTicketCache<T> {

	/** Length of random ticket identifiers. */
	private static final int TICKET_ID_LENGTH = 50;

	public GrantorCache() {
		super();
	}

	protected abstract String getTicketPrefix();

	protected abstract int getSerialNumber();

	/** Generates and returns a new, unique ticket ID */
	protected String newTicketId() {
		// determine appropriate ticketId prefix
		String prefix = getTicketPrefix();

		// produce the random identifier
		byte[] b = new byte[TICKET_ID_LENGTH];
		SecureRandom sr = new SecureRandom();
		sr.nextBytes(b);
		String ticketId = prefix + "-" + getSerialNumber() + "-" + Util.toPrintable(b);

		// make sure the identifier isn't already used
		if (ticketCache.get(ticketId) != null)
			return newTicketId(); // tail-recurse
		else
			return ticketId;
	}

	/** Stores the given ticket, associating it with the given identifier. */
	protected void storeTicket(String ticketId, T t) throws TicketException {
		// make sure the ticket is valid and new
		if (ticketCache.get(ticketId) != null)
			throw new DuplicateTicketException();
//		if (!t.getClass().equals(ticketType))
//			throw new InvalidTicketException("got " + t.getClass() + "; needed " + ticketType);

		// if it's okay, then store it
		ticketCache.put(ticketId, t, getTolerance());
	}

	/** Retrieves the ticket with the given identifier. */
	protected T retrieveTicket(String ticketId) {
		T o = ticketCache.get(ticketId);
		if (o == null || o.isExpired())
			return null;
		else
			return o;
	}

	/** Removes the ticket from the cache, and expires the ticket itself. */
	public void deleteTicket(String ticketId) {
		T t = ticketCache.remove(ticketId);
		if (t != null) {
			t.expire();
		}
	}

}
