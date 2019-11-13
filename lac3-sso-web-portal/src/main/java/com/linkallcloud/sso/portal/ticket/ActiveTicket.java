package com.linkallcloud.sso.portal.ticket;

import com.linkallcloud.core.principal.SimpleService;
import com.linkallcloud.sso.oapi.enums.MappingType;

public class ActiveTicket<G extends GrantingTicket> extends Ticket {

	private G grantor;
	private boolean fromNewLogin;

	private SimpleService service;
	private String siteUser;
	private int siteMaping;// 账号映射类型：MappingType

	/** Constructs a new, immutable service ticket. */
	public ActiveTicket(G t, String appCode, String service, boolean fromNewLogin) {
		this.grantor = t;
		this.fromNewLogin = fromNewLogin;
		this.service = new SimpleService(service, appCode);
		this.siteMaping = MappingType.Unified.getCode();
		this.siteUser = t.getUsername();
	}

	/** Constructs a new, immutable service ticket. */
	public ActiveTicket(G t, String appCode, String service, boolean fromNewLogin, String siteUser, int siteMaping) {
		this(t, appCode, service, fromNewLogin);
		this.siteUser = siteUser;
		this.siteMaping = siteMaping == MappingType.Mapping.getCode().intValue() ? siteMaping
				: MappingType.Unified.getCode();
	}

	@Override
	public String getUsername() {
		return grantor.getUsername();
	}

	/** Retrieves the ticket's app code. */
	public String getAppCode() {
		return service.getCode();
	}

	/** Retrieves the ticket's service. */
	public String getService() {
		return service.getId();
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

	public String getSiteUser() {
		if (siteMaping != MappingType.Mapping.getCode().intValue()) {
			return getGrantor().getUsername();
		} else {
			return siteUser;
		}
	}

	public int getSiteMaping() {
		return siteMaping;
	}

	public void setSite(int siteMaping, String siteUser) {
		this.siteMaping = siteMaping == MappingType.Mapping.getCode().intValue() ? siteMaping
				: MappingType.Unified.getCode();
		this.siteUser = siteUser;
	}

}
