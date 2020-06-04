package com.linkallcloud.sso.pc.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.sso.domain.YwAccount;
import com.linkallcloud.sso.manager.IYwAccountManager;
import com.linkallcloud.web.controller.BaseLController;

@Controller
@RequestMapping(value = "/YwAccount", method = RequestMethod.POST)
@Module(name = "运维账号")
public class YwAccountController extends BaseLController<YwAccount, IYwAccountManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwAccountManager ywAccountManager;

	@Override
	protected IYwAccountManager manager() {
		return ywAccountManager;
	}

}
