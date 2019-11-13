package com.linkallcloud.sso.portal.ticket;

/**
 * Represents a service ticket (ST).
 */
public class ServiceTicket extends ActiveTicket<TicketGrantingTicket> {

	/** Constructs a new, immutable service ticket. */
	public ServiceTicket(TicketGrantingTicket t, String service, boolean fromNewLogin) {
		super(t, service, fromNewLogin);
	}

}
