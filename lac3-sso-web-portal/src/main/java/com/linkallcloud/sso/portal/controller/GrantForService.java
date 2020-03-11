package com.linkallcloud.sso.portal.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.vo.BindVo;
import com.linkallcloud.core.www.UrlPattern;
import com.linkallcloud.sso.domain.AppAccount;
import com.linkallcloud.sso.exception.AccountException;
import com.linkallcloud.sso.exception.SiteException;
import com.linkallcloud.sso.portal.kiss.um.YwUserKiss;
import com.linkallcloud.sso.ticket.TicketGrantingTicket;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.web.utils.Controllers;

@Controller
@RequestMapping
@Module(name = "登录")
public class GrantForService extends BaseController {

	@Autowired
	private YwUserKiss ywUserKiss;

	@RequestMapping(value = "/generic", method = RequestMethod.GET)
	public String generic(HttpServletRequest request, ModelMap modelMap, Trace t) {
		TicketGrantingTicket tgt = getTgc(request);
		if (tgt != null && !Strings.isBlank(tgt.getUsername())) {
			modelMap.put("userName", tgt.getUsername());
			YwUser user = ywUserKiss.fecthByAccount(t, tgt.getUsername());
			if (user != null) {
				List<Application> apps = applicationKiss.findByYwUserId(t, user.getId());
				modelMap.put("apps", apps);
				modelMap.put("user", user);
			}
			return "generic";
		} else {
			return Controllers.redirect("/login");
		}
	}

	@RequestMapping(value = "/confirm", method = RequestMethod.GET)
	public String confirm(@RequestParam(value = "from") String appCode,
			@RequestParam(value = "serviceId") String appUrl, @RequestParam(value = "token") String token,
			HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, Trace t) {
		modelMap.put("from", appCode);
		modelMap.put("serviceId", appUrl);
		modelMap.put("token", token);

		try {
			String gourl = new UrlPattern(appUrl).append("token", token).url();
			modelMap.put("go", gourl);
		} catch (UnsupportedEncodingException e) {
		}

		return "confirm";
	}

	@RequestMapping(value = "/bind", method = RequestMethod.GET)
	public String bind(@RequestParam(value = "from") String appCode, @RequestParam(value = "serviceId") String appUrl,
			HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, Trace t) {
		modelMap.put("from", appCode);
		modelMap.put("serviceId", appUrl);

		// The servie can pass?
		Application app = checkSiteCanPass(t, appCode, appUrl);
		modelMap.put("app", app);

		// check to see whether we've been sent a valid TGC
		TicketGrantingTicket tgt = getTgc(request);
		if (tgt != null) {
			modelMap.put("loginname", tgt.getUsername());
			return "bind";
		} else {
			return toLogin(appCode, appUrl);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/doBind", method = RequestMethod.POST)
	public Object doBind(@RequestBody BindVo bindVo, HttpServletRequest request, HttpServletResponse response, Trace t)
			throws ServletException, SiteException, IOException, AccountException {
		// avoid caching (in the stupidly numerous ways we must)
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", -1);

		if (!sessionValidateCode.validate(request, response, bindVo.getVcode())) {
			// horrible way of logging, I know
			log.error("验证码错误， " + request.getRemoteAddr());
			// failure: record invalid login ticket
			return failure(t, "VCODE", "验证码错误，请重新输入");
		}

		// The servie can pass?
		Application app = checkSiteCanPass(t, bindVo.getFrom(), bindVo.getService());
		if (app == null || !app.getId().equals(Long.parseLong(bindVo.getFromId()))) {
			return failure(t, "App", "绑定失败，应用不匹配！");
		}

		TicketGrantingTicket tgt = getTgc(request);
		if (tgt != null) {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", "0");

			// 调用应用认证接口验证账号是否正确
			AppAccount appAccount = appAccountManager.validBind(t, app.getId(), tgt.getUsername(),
					bindVo.getLoginName(), bindVo.getPassword());
			if (appAccount != null) {
				doGrantForService(t, request, tgt, app, bindVo.getService(), true, result);
			} else {
				result.put("code", "1");
				result.put("message", "应用账号验证失败，请核对后重试！");
			}
			return result;
		} else {
			return toLogin(bindVo.getFrom(), bindVo.getService());
		}
	}

	private Map<String, String> failure(Trace t, String code, String message) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("code", code);
		result.put("message", message);
		return result;
	}

	private String toLogin(String appCode, String appUrl) {
		String service = appUrl;
		try {
			service = URLEncoder.encode(appUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return Controllers.redirect("/login?service={0}&from={1}", service, appCode);
	}

}
