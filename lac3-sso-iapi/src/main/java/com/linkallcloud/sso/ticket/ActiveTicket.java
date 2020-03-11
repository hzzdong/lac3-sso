package com.linkallcloud.sso.ticket;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.principal.AccountMapping;
import com.linkallcloud.core.principal.SimpleService;
import com.linkallcloud.sso.util.Util;

public abstract class ActiveTicket<G extends GrantingTicket> extends Ticket {

	// private G grantor;
	private String grantorId;

	private boolean fromNewLogin;

	private SimpleService service;
	private String siteUser;
	private int siteMaping;// 账号映射类型：com.linkallcloud.core.principal.AccountMapping

	public ActiveTicket() {
		super();
	}

	/** Constructs a new, immutable service ticket. */
	public ActiveTicket(G grantor, String appCode, String service, boolean fromNewLogin) {
		this.setGrantor(grantor);
		this.grantorId = Util.getInnerTicketId(grantor.getId());

		this.fromNewLogin = fromNewLogin;
		this.service = new SimpleService(service, appCode);
		this.siteMaping = AccountMapping.Unified.getCode();
		this.siteUser = grantor.getUsername();
	}

	/** Constructs a new, immutable service ticket. */
	public ActiveTicket(G t, String appCode, String service, boolean fromNewLogin, String siteUser, int siteMaping) {
		this(t, appCode, service, fromNewLogin);
		this.siteUser = siteUser;
		this.siteMaping = AccountMapping.Mapping.getCode().equals(siteMaping) ? siteMaping
				: AccountMapping.Unified.getCode();
	}

	@JSONField(serialize = false)
	@Override
	public String getUsername() {
		return getGrantor() == null ? "" : getGrantor().getUsername();
	}

	@JSONField(serialize = false)
	public String getAppCode() {
		return service == null ? "" : service.getCode();
	}

	@JSONField(serialize = false)
	public String getAppServiceUrl() {
		return service == null ? "" : service.getUrl();
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
		return (!getGrantor().isExpired());
	}

	/** Returns the ticket's grantor. */
	@JSONField(serialize = false, deserialize = false)
	public abstract G getGrantor();

	public abstract void setGrantor(G grantor);

	public String getGrantorId() {
		return grantorId;
	}

	public void setGrantorId(String grantorId) {
		this.grantorId = grantorId;
	}

	public String getSiteUser() {
		if (siteMaping != AccountMapping.Mapping.getCode().intValue()) {
			return getGrantor().getUsername();
		} else {
			return siteUser;
		}
	}

	public int getSiteMaping() {
		return siteMaping;
	}

	public void setSite(int siteMaping, String siteUser) {
		this.siteMaping = siteMaping == AccountMapping.Mapping.getCode().intValue() ? siteMaping
				: AccountMapping.Unified.getCode();
		this.siteUser = siteUser;
	}

	public SimpleService getService() {
		return service;
	}

	public void setService(SimpleService service) {
		this.service = service;
	}

	public void setSiteUser(String siteUser) {
		this.siteUser = siteUser;
	}

	public void setSiteMaping(int siteMaping) {
		this.siteMaping = siteMaping;
	}

}
