package com.linkallcloud.sso.ticket.cache;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.linkallcloud.sso.exception.DuplicateTicketException;
import com.linkallcloud.sso.exception.TicketException;
import com.linkallcloud.sso.redis.ticket.RedisServiceTicketCache;
import com.linkallcloud.sso.ticket.ServiceTicket;
import com.linkallcloud.sso.util.Util;

/**
 * Represents a generic cache of service tickets. Can be used as a store for STs
 * or PTs.
 */
@Component
public class ServiceTicketCache extends OTUTicketCache<ServiceTicket, RedisServiceTicketCache> {

	/** Length of random ticket identifiers. */
	private static final int TICKET_ID_LENGTH = 20;

	@Autowired
	private RedisServiceTicketCache stCache;

	@Value("${lac.sso.st.timeout:300}")
	private int tolerance;

	public ServiceTicketCache() {
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
	protected void storeTicket(String ticketId, ServiceTicket t) throws TicketException {
		// make sure the ticket is valid and new
		if (getCache().get(ticketId) != null)
			throw new DuplicateTicketException();
//		if (!t.getClass().equals(ticketType))
//			throw new InvalidTicketException("got " + t.getClass() + "; needed " + ticketType);

		// if it's okay, then store it
		getCache().put(ticketId, t, getTolerance());
	}

	/** Retrieves the ticket with the given identifier. */
	protected ServiceTicket retrieveTicket(String ticketId) {
		ServiceTicket o = getCache().get(ticketId);
		if (o == null || !o.isValid())
			return null;
		else
			return o;
	}

	/** Removes the ticket from the cache. */
	public void deleteTicket(String ticketId) {
		getCache().remove(ticketId);
	}

	@Override
	protected int getTolerance() {
		return tolerance;
	}

	@Override
	protected RedisServiceTicketCache getCache() {
		return stCache;
	}

	@Override
	public String getTicketPrefix() {
		return "ST";
	}

}
