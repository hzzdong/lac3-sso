package com.linkallcloud.sso.pc.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.sso.domain.Manager;
import com.linkallcloud.sso.manager.IManagerManager;
import com.linkallcloud.web.controller.BaseLController;

@Controller
@RequestMapping(value = "/Manager", method = RequestMethod.POST)
@Module(name = "管理员")
public class ManagerController extends BaseLController<Manager, IManagerManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IManagerManager managerManager;

	@Override
	protected IManagerManager manager() {
		return managerManager;
	}

}
