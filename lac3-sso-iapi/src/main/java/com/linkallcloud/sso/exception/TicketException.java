package com.linkallcloud.sso.exception;

/**
 * Represents exceptions related to CAS tickets.
 */
public class TicketException extends SsoException {
	private static final long serialVersionUID = 6840771997336999517L;

	public static final String ERROR_TICKET = "e.ticket";

	public TicketException() {
		super(ERROR_TICKET, "票据错误");
	}

	public TicketException(String message) {
		super(ERROR_TICKET, message);
	}

	public TicketException(String code, String message) {
		super(code, message);
	}

}
