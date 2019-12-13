package com.linkallcloud.sso.ticket.cache;

import java.security.SecureRandom;

import com.linkallcloud.sso.exception.DuplicateTicketException;
import com.linkallcloud.sso.exception.TicketException;
import com.linkallcloud.sso.redis.ticket.RedisTicketCache;
import com.linkallcloud.sso.ticket.GrantingTicket;
import com.linkallcloud.sso.util.Util;

/**
 * Represents a generic cache of granting tickets. Can be used as a store for
 * TGCs or PGTs.
 */
public abstract class GrantorCache<T extends GrantingTicket, C extends RedisTicketCache<T>>
		extends ActiveTicketCache<T, C> {

	/** Length of random ticket identifiers. */
	private static final int TICKET_ID_LENGTH = 50;

	public GrantorCache() {
		super();
	}

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
		if (getCache().get(ticketId) != null)
			return newTicketId(); // tail-recurse
		else
			return ticketId;
	}

	/** Stores the given ticket, associating it with the given identifier. */
	protected void storeTicket(String ticketId, T t) throws TicketException {
		// make sure the ticket is valid and new
		if (getCache().get(ticketId) != null)
			throw new DuplicateTicketException();
//		if (!t.getClass().equals(ticketType))
//			throw new InvalidTicketException("got " + t.getClass() + "; needed " + ticketType);

		// if it's okay, then store it
		getCache().put(ticketId, t, getTolerance());
	}

	/** Retrieves the ticket with the given identifier. */
	protected T retrieveTicket(String ticketId) {
		T o = getCache().get(ticketId);
		if (o == null || o.isExpired())
			return null;
		else
			return o;
	}

	/** Removes the ticket from the cache, and expires the ticket itself. */
	public void deleteTicket(String ticketId) {
		T t = getCache().remove(ticketId);
		if (t != null) {
			t.expire();
		}
	}

}
