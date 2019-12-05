package com.linkallcloud.sso.oapi.dto;

import com.linkallcloud.core.dto.Result;

public class ProxyResult extends Result<String> {
	private static final long serialVersionUID = -2779392486641955302L;

	public ProxyResult() {
		super();
		this.setCode("0");
	}

	public ProxyResult(String proxyTicket) {
		super();
		this.setCode("0");
		this.setData(proxyTicket);
	}

	public ProxyResult(String errorCode, String errorMessage) {
		super();
		this.setCode(errorCode);
		this.setMessage(errorMessage);
	}

	public String getProxyTicket() {
		return getData();
	}

	public void setProxyTicket(String proxyTicket) {
		this.setData(proxyTicket);
	}

}
