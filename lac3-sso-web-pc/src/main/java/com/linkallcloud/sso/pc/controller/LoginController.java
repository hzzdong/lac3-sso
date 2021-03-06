package com.linkallcloud.sso.pc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.linkallcloud.core.busilog.annotation.LacLog;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.vo.LoginVo;
import com.linkallcloud.sso.domain.Manager;
import com.linkallcloud.sso.manager.IManagerManager;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;
import com.linkallcloud.web.vc.SessionValidateCode;

@Controller
@RequestMapping
@Module(name = "用户登录")
public class LoginController extends BaseManagerController {
	private static final Log log = Logs.get();

	@Value("${lac.appid}")
	private Long myAppId;

	@Value("${lac.appcode}")
	private String myAppCode;

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IManagerManager managerManager;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String localLogin(HttpSession session, HttpServletRequest request, ModelMap modelMap) {
		SessionUser obj = (SessionUser) Controllers.getSessionUser(myAppCode);
		if (obj == null) {
			return "page/login";
		}
		return Controllers.redirect(getIndexUrl());
	}

	@LacLog(desc = "用户([(${lvo.loginName})])登录系统,TID:[(${t.tid})].")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Result<String> postLcLogin(@RequestBody LoginVo lvo, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, Trace t) {
		Result<String> result = oapiLogin(t, lvo, request, response);
		if (result.getCode().equals("0")) {
			result.setData(getIndexUrl());
		} else {
			log.errorf("****** 登录验证失败：%s", JSON.toJSONString(lvo));
		}
		return result;
	}

	private String getIndexUrl() {
		return "/index";
	}

	@RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
	public void getVerify(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		SessionValidateCode svc = new SessionValidateCode(true);
		svc.generate(request, response);
	}

	/**
	 * 退出本地登陆
	 */
	@RequestMapping(value = "/exit", method = RequestMethod.GET)
	public String exit(ModelMap modelMap, HttpSession session, HttpServletRequest request) {
		try {
			session.invalidate();
		} catch (Exception e) {
		}
		return localLogin(session, request, modelMap);
	}

	private Result<String> oapiLogin(Trace t, LoginVo lvo, HttpServletRequest request, HttpServletResponse response) {
//		SessionValidateCode svc = new SessionValidateCode(true);
//		if (!svc.validate(request, response, lvo.getVcode())) {
//			return new Result<String>("10002005", "验证码错误，请重新输入！");
//		}
		try {
			Manager manager = managerManager.loginValidate(t, lvo.getLoginName(), lvo.getPassword());
			if (manager != null) {
				SessionUser su = new SessionUser(manager.getId(), manager.getUuid(), manager.getLoginname(),
						manager.getName(), "");
				su.setAppInfo(myAppId, null, myAppCode, "");
				su.setMenuPermissions(getMenuPermissionResources());

				Controllers.login(myAppCode, su);
				Controllers.setSessionObject("pwdStrength", lvo.getPwdStrength());
				// setClientInfo2Cache(lvo.getLoginName(), lvo.getClient());
				// return WebUtils.makeSuccessResult(request.getContextPath() + getIndexUrl());
				return new Result<String>();
			}
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
		return new Result<String>("10002005", "账号或者密码错误，请重试！");
	}

}
