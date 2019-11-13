package com.linkallcloud.sso.portal.ticket;

/**
 * Represents a service ticket (ST).
 */
public class ServiceTicket extends ActiveTicket<TicketGrantingTicket> {

	/** Constructs a new, immutable service ticket. */
	public ServiceTicket(TicketGrantingTicket t, String appCode, String service, boolean fromNewLogin, String siteUser,
			int siteMaping) {
		super(t, appCode, service, fromNewLogin, siteUser, siteMaping);
	}

	/** Constructs a new, immutable service ticket. */
	public ServiceTicket(TicketGrantingTicket t, String appCode, String service, boolean fromNewLogin) {
		super(t, appCode, service, fromNewLogin);
	}

}
