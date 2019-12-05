package com.linkallcloud.sso.oapi.dto;

import java.util.ArrayList;
import java.util.List;

public class ProxyAuthenticationResult extends ServiceAuthenticationResult {
	private static final long serialVersionUID = -8036149193912999535L;

	private List<String> proxies;

	public ProxyAuthenticationResult() {
		super();
		this.setCode("0");
	}

	public ProxyAuthenticationResult(String ssoUser, String siteUser, int mapingType) {
		this(ssoUser, siteUser, mapingType, null);
	}

	public ProxyAuthenticationResult(String ssoUser, String siteUser, int mapingType, String proxyGrantingTicket) {
		super(ssoUser, siteUser, mapingType, proxyGrantingTicket);
	}

	public ProxyAuthenticationResult(String errorCode, String errorMessage) {
		super();
		this.setCode(errorCode);
		this.setMessage(errorMessage);
	}

	public List<String> getProxies() {
		return proxies;
	}

	public void setProxies(List<String> proxies) {
		this.proxies = proxies;
	}

	public void addProxy(String proxy) {
		if (proxy != null) {
			if (proxies == null) {
				proxies = new ArrayList<String>();
			}
			proxies.add(proxy);
		}
	}

}
