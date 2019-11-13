package com.linkallcloud.sso.portal.ticket;

public class ActiveTicket<G extends GrantingTicket> extends Ticket {

	private G grantor;
	private String service;
	private boolean fromNewLogin;

	/** Constructs a new, immutable service ticket. */
	public ActiveTicket(G t, String service, boolean fromNewLogin) {
		this.grantor = t;
		this.service = service;
		this.fromNewLogin = fromNewLogin;
	}

	@Override
	public String getUsername() {
		return grantor.getUsername();
	}

	/** Retrieves the ticket's service. */
	public String getService() {
		return service;
	}

	/**
	 * Returns true if this service ticket was generated in response to a dialogue
	 * with a user during which the user supplied primary credentials. (Returns
	 * false, by contrast, if the ticket was generated in response to a request
	 * where a TGC was used.)
	 */
	public boolean isFromNewLogin() {
		return fromNewLogin;
	}

	/**
	 * Returns true if it would be appropriate to confer access to the service
	 * returned by getService() at the present point in time, false otherwise.
	 */
	public boolean isValid() {
		return (!grantor.isExpired());
	}

	/** Returns the ticket's grantor. */
	public G getGrantor() {
		return grantor;
	}

}
