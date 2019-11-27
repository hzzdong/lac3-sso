package com.linkallcloud.sso.pc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.www.ISessionUser;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;

@Controller
public class IndexController extends BaseManagerController {
	// private Log log = Logs.get();

	public IndexController() {
		super();
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String list(Trace t, ModelMap modelMap) {
		ISessionUser user = Controllers.getSessionUser();
		modelMap.put("user", user);

		List<Tree> menuItems = menuRoot.getChildren();
		modelMap.put("items", menuItems);

		String pwdStrength = (String) Controllers.getSessionObject("pwdStrength");
		modelMap.put("pwdStrength", pwdStrength);

		return "page/home";
	}

	@RequestMapping(value = "/getPermes", method = RequestMethod.GET)
	public @ResponseBody String[] getPermes(Trace t) throws IllegalParameterException {
		SessionUser user = (SessionUser) Controllers.getSessionUser();
		return user.getMenuPermissions();
	}

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String main(Trace t, ModelMap modelMap) {
		return "page/welcome";
	}

	@RequestMapping(value = "/base", method = RequestMethod.GET)
	public String bg() {
		return "page/base :: global";
	}

	@RequestMapping(value = "/pub/noPermission", method = RequestMethod.GET)
	public String noPermission() {
		return "page/system/noPermission";
	}

	@RequestMapping(value = "/help", method = RequestMethod.GET)
	public String help() {
		return "page/help";
	}

}
