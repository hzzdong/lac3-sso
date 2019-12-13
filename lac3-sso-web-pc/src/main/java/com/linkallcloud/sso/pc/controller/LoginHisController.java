package com.linkallcloud.sso.pc.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.pagination.WebPage;
import com.linkallcloud.core.query.rule.IsNull;
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

	@RequestMapping(value = "/online", method = RequestMethod.GET)
	public String online(Trace t, ModelMap modelMap, AppVisitor av) {
		return "page/LoginHis/online";
	}

	@RequestMapping(value = "/onlinePage", method = RequestMethod.POST)
	public @ResponseBody Result<Object> onlinePage(@RequestBody WebPage webPage, Trace t, AppVisitor av) {
		Page<LoginHis> page = webPage.toPage();
		page.addRule(new IsNull("logoutTime"));
		page = manager().findPage(t, page);
		return new Result<Object>(page);
	}

	@WebLog(db = true, desc = "管理员([(${visitor.name})])强制下线了用户，登录日志([(${id})]), TID:[(${t.tid})]")
	@RequestMapping(value = "/offline", method = RequestMethod.POST)
	public @ResponseBody Result<Object> offline(@RequestParam(value = "id") Long id,
			@RequestParam(value = "uuid") String uuid, Trace t, AppVisitor av) {
		Boolean ret = manager().offline(t, id, uuid);
		return new Result<Object>(!ret, Exceptions.CODE_ERROR_DELETE, "强制下线失败");
	}

}
