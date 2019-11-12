package com.linkallcloud.sso.portal.ticket.cache;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.linkallcloud.sso.portal.exception.DuplicateTicketException;
import com.linkallcloud.sso.portal.exception.InvalidTicketException;
import com.linkallcloud.sso.portal.exception.TicketException;
import com.linkallcloud.sso.portal.ticket.ProxyGrantingTicket;
import com.linkallcloud.sso.portal.ticket.Ticket;
import com.linkallcloud.sso.portal.ticket.TicketGrantingTicket;
import com.linkallcloud.sso.portal.utils.Util;

/**
 * Represents a generic cache of granting tickets. Can be used as a store for
 * TGCs or PGTs.
 */
public abstract class GrantorCache extends ActiveTicketCache {

	// *********************************************************************
	// Constants

	/** Length of random ticket identifiers. */
	private static final int TICKET_ID_LENGTH = 50;

	// *********************************************************************
	// Private state

	/** The actual cache of tickets (ticketId -> Ticket map) */
	private Map ticketCache;

	/** The specific type of tickets the cache stores. */
	// private Class ticketType;

	/** Monotonically increasing serial number for tickets. */
	private static int serial = 0;

	// *********************************************************************
	// Constructor

	public GrantorCache() {
		this(7200);
	}

	/**
	 * Constructs a new GrantorCache that is intended to store cookies of the given
	 * specific ticket type.
	 */
	public GrantorCache(/* Class ticketType, */ int tolerance) {
		super(tolerance);
//    if (!TicketGrantingTicket.class.isAssignableFrom(ticketType))
//      throw new IllegalArgumentException(
//        "GrantorCache may only store granting tickets");
		// this.ticketType = ticketType;
		this.ticketCache = Collections.synchronizedMap(new HashMap());
	}

	// *********************************************************************
	// Cache-management logic

	protected abstract String getTicketPrefix();

	/** Generates and returns a new, unique ticket ID */
	protected String newTicketId() {
		// determine appropriate ticketId prefix
		String prefix = getTicketPrefix();

		// produce the random identifier
		byte[] b = new byte[TICKET_ID_LENGTH];
		SecureRandom sr = new SecureRandom();
		sr.nextBytes(b);
		String ticketId = prefix + "-" + (serial++) + "-" + Util.toPrintable(b);

		// make sure the identifier isn't already used
		if (ticketCache.get(ticketId) != null)
			return newTicketId(); // tail-recurse
		else
			return ticketId;
	}

	/** Stores the given ticket, associating it with the given identifier. */
	protected void storeTicket(String ticketId, Ticket t) throws TicketException {
		// make sure the ticket is valid and new
		if (ticketCache.get(ticketId) != null)
			throw new DuplicateTicketException();
//		if (!t.getClass().equals(ticketType))
//			throw new InvalidTicketException("got " + t.getClass() + "; needed " + ticketType);

		// if it's okay, then store it
		ticketCache.put(ticketId, t);
	}

	/** Retrieves the ticket with the given identifier. */
	protected Ticket retrieveTicket(String ticketId) {
		Object o = ticketCache.get(ticketId);
		if (o == null || ((TicketGrantingTicket) o).isExpired())
			return null;
		else
			return (Ticket) o;
	}

	/** Removes the ticket from the cache, and expires the ticket itself. */
	public void deleteTicket(String ticketId) {
		Object o = ticketCache.remove(ticketId);
		if (o == null)
			return;
		TicketGrantingTicket t = (TicketGrantingTicket) o;
		t.expire();
	}

	/** Returns the number of grantors in the grantor cache. */
	public int getCacheSize() {
		return ticketCache.size();
	}
}
