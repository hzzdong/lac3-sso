package com.linkallcloud.sso.pc.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.sso.domain.SysSetup;
import com.linkallcloud.sso.manager.ISysSetupManager;
import com.linkallcloud.web.controller.BaseLController;

@Controller
@RequestMapping(value = "/SysSetup", method = RequestMethod.POST)
@Module(name = "系统设置")
public class SysSetupController extends BaseLController<SysSetup, ISysSetupManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private ISysSetupManager sysSetupManager;

	@Override
	protected ISysSetupManager manager() {
		return sysSetupManager;
	}

}
