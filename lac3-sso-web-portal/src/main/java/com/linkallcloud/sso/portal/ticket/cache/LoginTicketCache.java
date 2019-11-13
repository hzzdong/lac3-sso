package com.linkallcloud.sso.portal.ticket.cache;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.linkallcloud.sso.portal.exception.DuplicateTicketException;
import com.linkallcloud.sso.portal.exception.TicketException;
import com.linkallcloud.sso.portal.ticket.LoginTicket;
import com.linkallcloud.sso.portal.utils.Util;

/**
 * Represents a cache of login tickets. These are one-time-use tickets provided
 * on the login page that must be posted back to CAS in order for a login to be
 * successful. This fixes a bug in IE and Safari where going back through the
 * history causes login credentials to be reposted to CAS.
 *
 * This class is almost identical to ServiceTicketCache, except it is made to
 * only hold LoginTickets.
 */
@Component
public class LoginTicketCache extends OTUTicketCache<LoginTicket> {

	/** Length of random ticket identifiers. */
	private static final int TICKET_ID_LENGTH = 20;

	@Value("${lac.sso.lt.timeout:86400}")
	private int tolerance;

	public LoginTicketCache() {
		super();
	}

	public synchronized String addTicket() throws TicketException {
		return addTicket(new LoginTicket());
	}

	/** Generates and returns a new, unique ticket ID */
	protected String newTicketId() {
		// determine appropriate ticketId prefix
		String prefix = "LT";

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
	protected void storeTicket(String ticketId, LoginTicket t) throws TicketException {
		// make sure the ticket is valid and new
		if (ticketCache.get(ticketId) != null){
			throw new DuplicateTicketException();
		}

		// if it's okay, then store it
		ticketCache.put(ticketId, t, getTolerance());
	}

	/** Retrieves the ticket with the given identifier. */
	protected LoginTicket retrieveTicket(String ticketId) {
		return ticketCache.get(ticketId);
	}

	/** Removes the ticket from the cache. */
	public void deleteTicket(String ticketId) {
		ticketCache.remove(ticketId);
	}

	/** Returns the current ticket serial number (for monitoring) */
	public int getSerialNumber() {
		return ticketCache.increment("LT");
	}

	@Override
	protected int getTolerance() {
		return tolerance;
	}

}
