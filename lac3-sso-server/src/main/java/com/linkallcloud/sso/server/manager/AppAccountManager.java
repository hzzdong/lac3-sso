package com.linkallcloud.sso.server.manager;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.sso.domain.AppAccount;
import com.linkallcloud.sso.manager.IAppAccountManager;
import com.linkallcloud.sso.service.IAppAccountService;

@DubboService(interfaceClass = IAppAccountManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "应用账号")
public class AppAccountManager extends BaseManager<AppAccount, IAppAccountService> implements IAppAccountManager {

	@Autowired
	private IAppAccountService appAccountService;

	@Override
	protected IAppAccountService service() {
		return appAccountService;
	}

	@Override
	public AppAccount fetch(Trace t, Long appId, String ssoLoginName) {
		return service().fetch(t, appId, ssoLoginName);
	}

	@Override
	public AppAccount validBind(Trace t, Long appId, String ssoLoginName, String appLoginName, String appPassword) {
		return service().validBind(t, appId, ssoLoginName, appLoginName, appPassword);
	}

	@Override
	public AppAccount fetchByAppCode(Trace t, String appCode, String ssoLoginName) {
		return service().fetchByAppCode(t, appCode, ssoLoginName);
	}

}
