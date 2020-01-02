package com.linkallcloud.sso.portal.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.exception.AccountException;
import com.linkallcloud.sso.manager.IAccountManager;
import com.linkallcloud.sso.portal.kiss.um.YwUserKiss;
import com.linkallcloud.sso.portal.utils.LacSessionValidateCode;
import com.linkallcloud.sso.ticket.TicketGrantingTicket;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.web.utils.Controllers;

@Controller
@RequestMapping
@Module(name = "个人信息")
public class Profile extends BaseController {

	@Autowired
	private YwUserKiss ywUserKiss;

	@Autowired
	private LacSessionValidateCode sessionValidateCode;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IAccountManager accountManager;

	@RequestMapping(value = "/me", method = RequestMethod.GET)
	public String me(HttpServletRequest request, ModelMap modelMap, Trace t) {
		TicketGrantingTicket tgt = getTgc(request);
		if (tgt != null) {
			modelMap.put("userName", tgt.getUsername());
			YwUser user = ywUserKiss.fecthByAccount(t, tgt.getUsername());
			if (user != null) {
				modelMap.put("user", user);
			}
			return "me";
		} else {
			return Controllers.redirect("/login");
		}
	}

	@RequestMapping(value = "/mp", method = RequestMethod.GET)
	public String mp(HttpServletRequest request, ModelMap modelMap, Trace t) {
		TicketGrantingTicket tgt = getTgc(request);
		if (tgt != null) {
			modelMap.put("userName", tgt.getUsername());
			YwUser user = ywUserKiss.fecthByAccount(t, tgt.getUsername());
			if (user != null) {
				modelMap.put("user", user);
			}
			return "mp";
		} else {
			return Controllers.redirect("/login");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/modifyPassword", method = RequestMethod.POST)
	public Object modifyPassword(@RequestParam(value = "oldPassword", required = false) String oldPassword,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "vcode", required = false) String vcode, HttpServletRequest request,
			HttpServletResponse response, Trace t) throws AccountException {
		if (!sessionValidateCode.validate(request, response, vcode)) {
			log.error("验证码错误， " + request.getRemoteAddr());
			return new Result<Object>(true, "MP-VCODE", "验证码错误，请重新输入");
		}

		TicketGrantingTicket tgt = getTgc(request);
		if (tgt != null) {
			boolean ret = accountManager.updatePassword(t, tgt.getUsername(), oldPassword, password);
			return new Result<Object>(!ret, "MP-pwd", "修改密码失败，可能是能提供的原密码错误，请重试。");
		} else {
			return Controllers.redirect("/login");
		}

	}

}
