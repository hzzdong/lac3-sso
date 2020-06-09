package com.linkallcloud.sso.oapi.dto;

import java.util.ArrayList;
import java.util.List;

import com.linkallcloud.core.principal.SimpleService;

public class ProxyAuthenticationResult extends ServiceAuthenticationResult {
	private static final long serialVersionUID = -8036149193912999535L;

	private List<SimpleService> proxies;

	public ProxyAuthenticationResult() {
		super();
		this.setCode("0");
	}

	public ProxyAuthenticationResult(String ssoUser, String siteUser, int siteClazz, int mapingType) {
		this(ssoUser, siteUser, siteClazz, mapingType, null);
	}

	public ProxyAuthenticationResult(String ssoUser, String siteUser, int siteClazz, int mapingType,
			String proxyGrantingTicket) {
		super(ssoUser, siteUser, siteClazz, mapingType, proxyGrantingTicket);
	}

	public ProxyAuthenticationResult(String errorCode, String errorMessage) {
		super();
		this.setCode(errorCode);
		this.setMessage(errorMessage);
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
