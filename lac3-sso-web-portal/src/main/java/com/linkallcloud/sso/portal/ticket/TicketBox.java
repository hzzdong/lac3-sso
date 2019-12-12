package com.linkallcloud.sso.portal.ticket;

import com.linkallcloud.core.lang.Lang;

public class TicketBox<T extends Ticket> {

	private String id;
	private T ticket;

	public TicketBox() {
		super();
	}

	public TicketBox(String id, T ticket) {
		super();
		this.id = id;
		this.ticket = ticket;
	}

	public String getId() {
		return id;
	}

	public String md5Id() {
		return Lang.md5(id);
	}

	public void setId(String id) {
		this.id = id;
	}

	public T getTicket() {
		return ticket;
	}

	public void setTicket(T ticket) {
		this.ticket = ticket;
	}

	public String getUsername() {
		return ticket != null ? ticket.getUsername() : null;
	}

	public void expire() {
		if (ticket != null && ticket instanceof GrantingTicket) {
			((GrantingTicket) ticket).expire();
		}
	}

}
