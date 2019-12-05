package com.linkallcloud.sso.client.response;

import com.linkallcloud.core.dto.Result;

public class PgtResponse extends Result<String> {
	private static final long serialVersionUID = 4111126248220899447L;

	public PgtResponse() {
		super();
	}

	public PgtResponse(String proxyTicket) {
		super();
		this.setData(proxyTicket);
	}

	public String getProxyTicket() {
		return getData();
	}

	public void setProxyTicket(String proxyTicket) {
		this.setData(proxyTicket);
	}

}
