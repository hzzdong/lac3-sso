package com.linkallcloud.sso.manager;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.sso.domain.AppAccount;

public interface IAppAccountManager extends IManager<AppAccount> {

	AppAccount fetch(Trace t, Long appId, String ssoLoginName);
	AppAccount fetchByAppCode(Trace t, String  appCode, String ssoLoginName);

	AppAccount validBind(Trace t, Long appId, String ssoLoginName, String appLoginName, String appPassword);

}
