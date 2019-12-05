package com.linkallcloud.sso.pc.aop;

import java.util.List;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.web.filter.CommonLoginFilter;

public class LoginFilter extends CommonLoginFilter {

	private String myAppId;
	private String myAppCode;

	private String loginUrl;

	public LoginFilter() {
		super();
	}

	public LoginFilter(String myAppId, String myAppCode, String loginUrl) {
		super();
		this.myAppId = myAppId;
		this.myAppCode = myAppCode;
		this.loginUrl = loginUrl;
	}

	public LoginFilter(List<String> ignoreRes, boolean override) {
		super(ignoreRes, override);
	}

	@Override
	protected String getAppCode() {
		return myAppCode;
	}

	@Override
	protected String getLoginUrl() {
		return Strings.isBlank(loginUrl) ? "/login" : loginUrl;
	}

	public String getMyAppId() {
		return myAppId;
	}

}
