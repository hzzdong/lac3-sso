package com.linkallcloud.sso.pc.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.LockHis;
import com.linkallcloud.sso.manager.ILockHisManager;
import com.linkallcloud.web.controller.BaseLController;

@Controller
@RequestMapping(value = "/LockHis", method = RequestMethod.POST)
@Module(name = "锁/解锁历史")
public class LockHisController extends BaseLController<LockHis, ILockHisManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private ILockHisManager lockHisManager;

	@Override
	protected ILockHisManager manager() {
		return lockHisManager;
	}

	@Override
	protected String doView(Long id, String uuid, ModelMap modelMap, Trace t, AppVisitor av) {
		LockHis entity = manager().fetchByIdUuid(t, id, uuid);
		modelMap.put("entity", entity);
		return super.doView(id, uuid, modelMap, t, av);
	}

}
