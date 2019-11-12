package com.linkallcloud.sso.portal.exception;

/**
 * A cache may throw InvalidTicketException when it encounters a ticket
 * inappropriate for the specific set of tickets it stores. For instance, an
 * attempt to store an ST in a TGC cache might lead to an InvalidTicket
 * Exception.
 */
public class InvalidTicketException extends TicketException {
	private static final long serialVersionUID = 1284485739398694994L;

	public InvalidTicketException() {
		super(TicketException.ERROR_TICKET, "无效的票据");
	}

	public InvalidTicketException(String messege) {
		super(TicketException.ERROR_TICKET, messege);
	}

}
