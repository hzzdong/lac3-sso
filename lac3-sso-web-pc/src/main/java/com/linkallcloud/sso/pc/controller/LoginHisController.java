package com.linkallcloud.sso.pc.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.LoginHis;
import com.linkallcloud.sso.manager.ILoginHisManager;
import com.linkallcloud.web.controller.BaseLController;

@Controller
@RequestMapping(value = "/LoginHis", method = RequestMethod.POST)
@Module(name = "登录日志")
public class LoginHisController extends BaseLController<LoginHis, ILoginHisManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private ILoginHisManager loginHisManager;

	@Override
	protected ILoginHisManager manager() {
		return loginHisManager;
	}

	@Override
	protected String doView(Long id, String uuid, ModelMap modelMap, Trace t, AppVisitor av) {
		LoginHis entity = manager().fetchByIdUuid(t, id, uuid);
		modelMap.put("entity", entity);
		return super.doView(id, uuid, modelMap, t, av);
	}

}
