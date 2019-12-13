package com.linkallcloud.sso.ticket;

/**
 * Represents a service ticket (ST).
 */
public class ServiceTicket extends ActiveTicket<TicketGrantingTicket> {

	private TicketGrantingTicket grantor;

	public ServiceTicket() {
		super();
	}

	/** Constructs a new, immutable service ticket. */
	public ServiceTicket(TicketGrantingTicket t, String appCode, String service, boolean fromNewLogin, String siteUser,
			int siteMaping) {
		super(t, appCode, service, fromNewLogin, siteUser, siteMaping);
	}

	/** Constructs a new, immutable service ticket. */
	public ServiceTicket(TicketGrantingTicket t, String appCode, String service, boolean fromNewLogin) {
		super(t, appCode, service, fromNewLogin);
	}

	@Override
	public TicketGrantingTicket getGrantor() {
		return grantor;
	}

	@Override
	public void setGrantor(TicketGrantingTicket grantor) {
		this.grantor = grantor;
	}

}
