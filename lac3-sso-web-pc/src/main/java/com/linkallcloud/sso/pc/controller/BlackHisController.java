package com.linkallcloud.sso.pc.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.BlackHis;
import com.linkallcloud.sso.manager.IBlackHisManager;
import com.linkallcloud.web.controller.BaseLController;

@Controller
@RequestMapping(value = "/BlackHis", method = RequestMethod.POST)
@Module(name = "黑名单历史")
public class BlackHisController extends BaseLController<BlackHis, IBlackHisManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IBlackHisManager blackHisManager;

	@Override
	protected IBlackHisManager manager() {
		return blackHisManager;
	}

	@Override
	protected String doView(Long id, String uuid, ModelMap modelMap, Trace t, AppVisitor av) {
		BlackHis entity = manager().fetchByIdUuid(t, id, uuid);
		modelMap.put("entity", entity);
		return super.doView(id, uuid, modelMap, t, av);
	}

}
