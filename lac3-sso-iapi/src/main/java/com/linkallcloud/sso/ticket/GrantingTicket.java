package com.linkallcloud.sso.ticket;

public abstract class GrantingTicket extends Ticket {

	private String username;
	private boolean expired;
	private int appClazz;// 登录的应用类别，0：运维，1:客户

	public GrantingTicket() {
		super();
	}

	public GrantingTicket(String username, int appClazz) {
		this.username = username;
		this.appClazz = appClazz;
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

	public int getAppClazz() {
		return appClazz;
	}

	public void setAppClazz(int appClazz) {
		this.appClazz = appClazz;
	}

}
