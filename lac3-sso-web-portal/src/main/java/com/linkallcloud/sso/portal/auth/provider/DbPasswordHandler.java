package com.linkallcloud.sso.portal.auth.provider;

import javax.servlet.ServletRequest;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.Account;
import com.linkallcloud.sso.exception.AuthException;
import com.linkallcloud.sso.manager.IAccountManager;

@Component
public class DbPasswordHandler extends WatchfulPasswordHandler {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IAccountManager accountManager;

	@Override
	public synchronized Account doAuthenticate(Trace t, ServletRequest request, String netid, String password) {
		try {
			Account account = accountManager.loginValidate(t, netid, password);
			if (account != null) {
				return account;
			}
		} catch (Throwable e) {
		}
		throw new AuthException(AuthException.ARG_CODE_AUTH, "登录名或者密码错误，请重新输入！");
	}

}
