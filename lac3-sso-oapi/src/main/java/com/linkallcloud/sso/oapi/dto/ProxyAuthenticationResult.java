package com.linkallcloud.sso.oapi.dto;

import java.util.ArrayList;
import java.util.List;

import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.principal.SimpleService;

public class ProxyAuthenticationResult extends Result<String> {
	private static final long serialVersionUID = -8036149193912999535L;

	private String user;
	private String proxyGrantingTicket;
	private List<SimpleService> proxies;

	public ProxyAuthenticationResult() {
		super();
		this.setCode("0");
	}

	public ProxyAuthenticationResult(String user) {
		super();
		this.setCode("0");
		this.user = user;
	}

	public ProxyAuthenticationResult(String errorCode, String errorMessage) {
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

	public List<SimpleService> getProxies() {
		return proxies;
	}

	public void setProxies(List<SimpleService> proxies) {
		this.proxies = proxies;
	}

	public void addProxy(SimpleService proxy) {
		if (proxy != null) {
			if (proxies == null) {
				proxies = new ArrayList<SimpleService>();
			}
			proxies.add(proxy);
		}
	}

}
