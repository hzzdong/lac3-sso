package com.linkallcloud.sso.oapi.dto;

import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.principal.Principal;
import com.linkallcloud.sso.oapi.enums.MappingType;

public class ServiceAuthenticationResult extends Result<String> implements Principal {
	private static final long serialVersionUID = 3323906180281674154L;

	private String id;
	private String siteId;
	private int appingType;// 账号映射类型：MappingType

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
		if (MappingType.Mapping.getCode().intValue() == siteMaping) {
			this.appingType = siteMaping;
		} else {
			this.appingType = MappingType.Unified.getCode();
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

	public int getAppingType() {
		return appingType;
	}

	public void setAppingType(int appingType) {
		this.appingType = appingType;
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

	@Override
	public int getMappingType() {
		return appingType;
	}

}
