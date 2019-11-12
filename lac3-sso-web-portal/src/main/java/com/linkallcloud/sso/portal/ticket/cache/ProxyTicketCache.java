package com.linkallcloud.sso.portal.ticket.cache;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.linkallcloud.sso.portal.exception.DuplicateTicketException;
import com.linkallcloud.sso.portal.exception.TicketException;
import com.linkallcloud.sso.portal.ticket.ServiceTicket;
import com.linkallcloud.sso.portal.ticket.Ticket;
import com.linkallcloud.sso.portal.utils.Util;

@Component
public class ProxyTicketCache extends OTUTicketCache {

	// *********************************************************************
	// Constants

	/** Length of random ticket identifiers. */
	private static final int TICKET_ID_LENGTH = 20;

	// *********************************************************************
	// Private state

	/** The actual cache of tickets (ticketId -> Ticket map) */
	private Map ticketCache;

	/** The specific type of tickets the cache stores. */
//	  private Class ticketType;

	/** Monotonically increasing serial number for tickets. */
	private static int serial = 0;

	// *********************************************************************
	// Constructor

	public ProxyTicketCache() {
		this(300);
	}

	/**
	 * Constructs a new ServiceTicketCache that is intended to store cookies of the
	 * given specific ticket type.
	 */
	public ProxyTicketCache(int tolerance) {
		super(tolerance);
//	    if (!ServiceTicket.class.isAssignableFrom(ticketType))
//	      throw new IllegalArgumentException(
//	        "ServiceTicketCache may only store service or proxy tickets");
//	    this.ticketType = ticketType;
		this.ticketCache = Collections.synchronizedMap(new HashMap());
	}

	// *********************************************************************
	// Cache-management logic

	/** Generates and returns a new, unique ticket ID */
	protected String newTicketId() {
		// determine appropriate ticketId prefix
		String prefix = "PT";

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
		if (o == null || !((ServiceTicket) o).isValid())
			return null;
		else
			return (Ticket) o;
	}

	/** Removes the ticket from the cache. */
	public void deleteTicket(String ticketId) {
		Object o = ticketCache.remove(ticketId);
	}

	/** Returns the current ticket serial number (for monitoring) */
	public int getSerialNumber() {
		return this.serial;
	}

	/** Returns the current number of tickets in the ticket cache. */
	public int getCacheSize() {
		return ticketCache.size();
	}

}
