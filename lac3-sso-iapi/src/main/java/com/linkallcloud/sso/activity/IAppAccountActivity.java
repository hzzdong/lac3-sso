package com.linkallcloud.sso.activity;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.AppAccount;

public interface IAppAccountActivity extends IActivity<AppAccount> {

	AppAccount fetch(Trace t, Long appId, String ssoLoginName);
	AppAccount fetchByAppCode(Trace t, String  appCode, String ssoLoginName);

	AppAccount bind(Trace t, Long appId, String ssoLoginName, String appLoginName);

}
