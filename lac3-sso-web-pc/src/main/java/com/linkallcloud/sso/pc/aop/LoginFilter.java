package com.linkallcloud.sso.pc.aop;

import java.util.List;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.principal.AccountMapping;
import com.linkallcloud.core.principal.Principal;
import com.linkallcloud.sso.domain.Manager;
import com.linkallcloud.sso.manager.IManagerManager;
import com.linkallcloud.web.filter.AbstractPrincipalFilter;
import com.linkallcloud.web.session.SessionUser;

public class LoginFilter extends AbstractPrincipalFilter {

	private String myAppId;
	private String myAppCode;
	private IManagerManager managerManager;

	private String loginUrl;

	public LoginFilter() {
		super();
	}

	public LoginFilter(String myAppId, String myAppCode, IManagerManager managerManager, String loginUrl) {
		super();
		this.myAppId = myAppId;
		this.myAppCode = myAppCode;
		this.managerManager = managerManager;
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

	@Override
	protected SessionUser getSessionUserByPrincipal(Principal ssoPrincipal) {
		if (ssoPrincipal != null) {
			String loginName = getLoginName(ssoPrincipal);
			return getUserByLoginName(loginName);
		}
		return null;
	}

	private SessionUser getUserByLoginName(String loginName) {
		Manager manager = managerManager.fecthByLoginName(new Trace(true), loginName);
		if (manager != null) {
			SessionUser su = new SessionUser(manager.getId().toString(), manager.getUuid(), manager.getLoginname(),
					manager.getName(), "");
			su.setAppInfo(myAppId, myAppCode);
			return su;
		}
		return null;
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
