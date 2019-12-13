package com.linkallcloud.sso.ticket;

public class GrantingTicket extends Ticket {

	private String username;
	private boolean expired;

	public GrantingTicket() {
		super();
	}

	public GrantingTicket(String username) {
		this.username = username;
		this.expired = false;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	/**
	 * Markes the ticket as expired, preventing its further use and the validity of
	 * subordinate tickets "downstream" from it.
	 */
	public void expire() {
		this.expired = true;
	}

	/** Returns true if the ticket is expired, false otherwise. */
	public boolean isExpired() {
		return expired;
	}

}
