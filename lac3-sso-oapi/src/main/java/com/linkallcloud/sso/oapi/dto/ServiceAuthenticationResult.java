package com.linkallcloud.sso.oapi.dto;

import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.principal.AccountMapping;
import com.linkallcloud.core.principal.Principal;

public class ServiceAuthenticationResult extends Result<String> implements Principal {
	private static final long serialVersionUID = 3323906180281674154L;

	/** The unique identifier for the principal. */
	private String id;

	/** The unique identifier for the site account loginname. */
	private String siteId;

	/** The account mapping type. */
	private int mappingType;

	private String proxyGrantingTicket;

	public ServiceAuthenticationResult() {
		super();
		this.setCode("0");
	}

	public ServiceAuthenticationResult(String user) {
		super();
		this.setCode("0");
		this.id = user;
	}

	public ServiceAuthenticationResult(String user, String siteUser, int siteMaping) {
		this(user);
		this.siteId = siteUser;
		if (AccountMapping.Mapping.getCode().intValue() == siteMaping) {
			this.mappingType = siteMaping;
		} else {
			this.mappingType = AccountMapping.Unified.getCode();
		}
	}

	public ServiceAuthenticationResult(String user, String siteUser, int siteMaping, String proxyGrantingTicket) {
		this(user, siteUser, siteMaping);
		this.proxyGrantingTicket = proxyGrantingTicket;
	}

	public ServiceAuthenticationResult(String errorCode, String errorMessage) {
		super();
		this.setCode(errorCode);
		this.setMessage(errorMessage);
	}

	public String getProxyGrantingTicket() {
		return proxyGrantingTicket;
	}

	public void setProxyGrantingTicket(String proxyGrantingTicket) {
		this.proxyGrantingTicket = proxyGrantingTicket;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	@Override
	public String getSiteId() {
		return siteId;
	}

	public void setMappingType(int mappingType) {
		this.mappingType = mappingType;
	}

	@Override
	public int getMappingType() {
		return mappingType;
	}

}
