package com.linkallcloud.sso.ticket.cache;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.linkallcloud.sso.exception.DuplicateTicketException;
import com.linkallcloud.sso.exception.TicketException;
import com.linkallcloud.sso.redis.ticket.RedisProxyTicketCache;
import com.linkallcloud.sso.ticket.ProxyTicket;
import com.linkallcloud.sso.util.Util;

@Component
public class ProxyTicketCache extends OTUTicketCache<ProxyTicket, RedisProxyTicketCache> {

	/** Length of random ticket identifiers. */
	private static final int TICKET_ID_LENGTH = 20;

	@Autowired
	private RedisProxyTicketCache ptCache;

	@Value("${lac.sso.pt.timeout:300}")
	private int tolerance;

	public ProxyTicketCache() {
		super();
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
		String ticketId = prefix + "-" + getSerialNumber() + "-" + Util.toPrintable(b);

		// make sure the identifier isn't already used
		String innerTicketId = Util.getInnerTicketId(ticketId);
		if (getCache().get(innerTicketId) != null)
			return newTicketId(); // tail-recurse
		else
			return ticketId;
	}

	/** Stores the given ticket, associating it with the given identifier. */
	protected void storeTicket(String ticketId, ProxyTicket t) throws TicketException {
		// make sure the ticket is valid and new
		if (getCache().get(ticketId) != null)
			throw new DuplicateTicketException();
//		if (!t.getClass().equals(ticketType))
//			throw new InvalidTicketException("got " + t.getClass() + "; needed " + ticketType);

		// if it's okay, then store it
		getCache().put(ticketId, t, getTolerance());
	}

	/** Retrieves the ticket with the given identifier. */
	protected ProxyTicket retrieveTicket(String ticketId) {
		ProxyTicket o = getCache().get(ticketId);
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
	public String getTicketPrefix() {
		return "PT";
	}

	@Override
	protected RedisProxyTicketCache getCache() {
		return ptCache;
	}

}
