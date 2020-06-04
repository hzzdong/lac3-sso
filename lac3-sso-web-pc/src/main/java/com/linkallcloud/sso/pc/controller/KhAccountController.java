package com.linkallcloud.sso.pc.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.sso.domain.KhAccount;
import com.linkallcloud.sso.manager.IKhAccountManager;
import com.linkallcloud.web.controller.BaseLController;

@Controller
@RequestMapping(value = "/KhAccount", method = RequestMethod.POST)
@Module(name = "客户账号")
public class KhAccountController extends BaseLController<KhAccount, IKhAccountManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhAccountManager khAccountManager;

	@Override
	protected IKhAccountManager manager() {
		return khAccountManager;
	}
}
