package com.linkallcloud.sso.client.response;

import java.util.ArrayList;
import java.util.List;

public class PTResponse extends STResponse {
	private static final long serialVersionUID = -2616748978026727039L;

	private List<String> proxies;

	public PTResponse() {
		super();
	}

	public PTResponse(String ssoUser, String siteUser, int mapingType) {
		super(ssoUser, siteUser, mapingType);
	}

	public List<String> getProxies() {
		return proxies;
	}

	public void setProxies(List<String> proxies) {
		this.proxies = proxies;
	}

	public void addAll(List<String> p) {
		if (p != null && !p.isEmpty()) {
			proxies = new ArrayList<String>();
			proxies.addAll(p);
		}
	}

}
