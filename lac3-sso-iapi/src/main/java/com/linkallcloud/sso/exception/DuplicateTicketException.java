package com.linkallcloud.sso.exception;

/**
 * A cache may throw DuplicateTicketException if it encounters an attempt to add
 * a duplicate ticket.
 */
public class DuplicateTicketException extends TicketException {
	private static final long serialVersionUID = -3455923531119839122L;

	public DuplicateTicketException() {
		super(TicketException.ERROR_TICKET, "重复票据");
	}

}
