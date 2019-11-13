package com.linkallcloud.sso.oapi.dto;

import com.linkallcloud.core.dto.Result;

public class ServiceAuthenticationResult extends Result<String> {
	private static final long serialVersionUID = 3323906180281674154L;

	private String user;
	private String proxyGrantingTicket;

	public ServiceAuthenticationResult() {
		super();
		this.setCode("0");
	}

	public ServiceAuthenticationResult(String user) {
		super();
		this.setCode("0");
		this.user = user;
	}

	public ServiceAuthenticationResult(String errorCode, String errorMessage) {
		super();
		this.setCode(errorCode);
		this.setMessage(errorMessage);
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getProxyGrantingTicket() {
		return proxyGrantingTicket;
	}

	public void setProxyGrantingTicket(String proxyGrantingTicket) {
		this.proxyGrantingTicket = proxyGrantingTicket;
	}

}
