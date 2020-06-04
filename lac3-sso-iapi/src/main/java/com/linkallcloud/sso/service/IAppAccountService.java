package com.linkallcloud.sso.service;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.sso.domain.AppAccount;

public interface IAppAccountService extends IService<AppAccount> {

	AppAccount fetch(Trace t, Long appId, String ssoLoginName);
	AppAccount fetchByAppCode(Trace t, String  appCode, String ssoLoginName);

	AppAccount validBind(Trace t, Long appId, String ssoLoginName, String appLoginName, String appPassword);

}
