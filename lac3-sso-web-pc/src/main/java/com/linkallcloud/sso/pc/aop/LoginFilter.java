package com.linkallcloud.sso.pc.aop;

import java.util.List;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.principal.AccountMapping;
import com.linkallcloud.core.principal.Principal;
import com.linkallcloud.sso.pc.kiss.um.YwUserKiss;
import com.linkallcloud.web.filter.AbstractPrincipalFilter;
import com.linkallcloud.web.session.SessionUser;

public class LoginFilter extends AbstractPrincipalFilter {

	private String myAppCode;
	private YwUserKiss ywUserKiss;

	private String loginUrl;

	public LoginFilter() {
		super();
	}

	public LoginFilter(String myAppCode, YwUserKiss ywUserKiss, String loginUrl) {
		super();
		this.myAppCode = myAppCode;
		this.ywUserKiss = ywUserKiss;
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
		return loginUrl;
	}

	@Override
	protected SessionUser getSessionUserByPrincipal(Principal ssoPrincipal) {
		if (ssoPrincipal != null) {
			String loginName = getLoginName(ssoPrincipal);
			return getUserByLoginName(loginName);
		}
		return null;
	}

	private SessionUser getUserByLoginName(String loginName) {
		return ywUserKiss.assembleSessionUser(new Trace(true), loginName, ywUserKiss.getMyAppCode());
	}

	private String getLoginName(Principal ssoPrincipal) {
		if (ssoPrincipal.getMappingType() == AccountMapping.Mapping.getCode().intValue()
				&& !com.linkallcloud.core.lang.Strings.isBlank(ssoPrincipal.getSiteId())) {
			return ssoPrincipal.getSiteId();
		} else {
			return ssoPrincipal.getSiteId();
		}
	}

}
