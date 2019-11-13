package com.linkallcloud.sso.portal.ticket.cache;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.linkallcloud.sso.portal.exception.DuplicateTicketException;
import com.linkallcloud.sso.portal.exception.TicketException;
import com.linkallcloud.sso.portal.ticket.ServiceTicket;
import com.linkallcloud.sso.portal.utils.Util;

/**
 * Represents a generic cache of service tickets. Can be used as a store for STs
 * or PTs.
 */
@Component
public class ServiceTicketCache extends OTUTicketCache<ServiceTicket> {

	// *********************************************************************
	// Constants

	/** Length of random ticket identifiers. */
	private static final int TICKET_ID_LENGTH = 20;

	@Value("${lac.sso.st.timeout:300}")
	private int tolerance;

	public ServiceTicketCache() {
		super();
	}

	/** Generates and returns a new, unique ticket ID */
	protected String newTicketId() {
		// determine appropriate ticketId prefix
		String prefix = "ST";

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
	protected void storeTicket(String ticketId, ServiceTicket t) throws TicketException {
		// make sure the ticket is valid and new
		if (ticketCache.get(ticketId) != null)
			throw new DuplicateTicketException();
//		if (!t.getClass().equals(ticketType))
//			throw new InvalidTicketException("got " + t.getClass() + "; needed " + ticketType);

		// if it's okay, then store it
		ticketCache.put(ticketId, t, getTolerance());
	}

	/** Retrieves the ticket with the given identifier. */
	protected ServiceTicket retrieveTicket(String ticketId) {
		ServiceTicket o = ticketCache.get(ticketId);
		if (o == null || !o.isValid())
			return null;
		else
			return o;
	}

	/** Removes the ticket from the cache. */
	public void deleteTicket(String ticketId) {
		ticketCache.remove(ticketId);
	}

	/** Returns the current ticket serial number (for monitoring) */
	public int getSerialNumber() {
		return ticketCache.increment("ST");
	}

	@Override
	protected int getTolerance() {
		return tolerance;
	}

}
