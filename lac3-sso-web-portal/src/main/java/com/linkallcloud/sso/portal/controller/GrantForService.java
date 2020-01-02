package com.linkallcloud.sso.portal.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.www.utils.WebUtils;
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

		String gourl = appUrl;
		try {
			gourl = WebUtils.urlAppend(gourl, "token", token);
		} catch (UnsupportedEncodingException e) {
		}
		modelMap.put("go", gourl);

		return "confirm";
	}

}
