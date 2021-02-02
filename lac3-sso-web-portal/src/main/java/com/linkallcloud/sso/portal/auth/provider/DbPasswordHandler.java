package com.linkallcloud.sso.portal.auth.provider;

import javax.servlet.ServletRequest;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.Account;
import com.linkallcloud.sso.exception.AuthException;
import com.linkallcloud.sso.manager.IAccountManager;
import com.linkallcloud.sso.manager.IKhAccountManager;
import com.linkallcloud.sso.manager.IYwAccountManager;

@Component
public class DbPasswordHandler extends WatchfulPasswordHandler {

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwAccountManager ywAccountManager;

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhAccountManager khAccountManager;

	private IAccountManager<? extends Account> getAccountManager(int appClazz) {
		if (appClazz == 0) {
			return ywAccountManager;
		} else {
			return khAccountManager;
		}
	}

	@Override
	public synchronized Account doAuthenticate(Trace t, ServletRequest request, String netid, String password,
			int appClazz) {
		try {
			Account account = getAccountManager(appClazz).loginValidate(t, netid, password);
			if (account != null) {
				return account;
			}
		} catch (Throwable e) {
		}
		registerFailure(request);
		throw new AuthException(AuthException.ARG_CODE_AUTH, "登录名或者密码错误，请重新输入！");
	}

}
