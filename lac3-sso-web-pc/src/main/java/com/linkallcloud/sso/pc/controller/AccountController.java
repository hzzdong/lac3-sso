package com.linkallcloud.sso.pc.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.sso.domain.Account;
import com.linkallcloud.sso.manager.IAccountManager;
import com.linkallcloud.web.controller.BaseLController;

@Controller
@RequestMapping(value = "/Account", method = RequestMethod.POST)
@Module(name = "账号")
public class AccountController extends BaseLController<Account, IAccountManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IAccountManager accountManager;

	@Override
	protected IAccountManager manager() {
		return accountManager;
	}

}
