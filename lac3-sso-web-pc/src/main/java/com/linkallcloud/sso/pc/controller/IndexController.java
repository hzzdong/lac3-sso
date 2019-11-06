package com.linkallcloud.sso.pc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.www.ISessionUser;
import com.linkallcloud.sso.pc.kiss.um.YwUserKiss;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;

@Controller
public class IndexController {
	// private Log log = Logs.get();

	@Autowired
	private YwUserKiss ywUserKiss;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String list(Trace t, ModelMap modelMap) {
		ISessionUser user = Controllers.getSessionUser();
		modelMap.put("user", user);
		modelMap.put("userType", user.getUserType());

		List<Tree> menuItems = ywUserKiss.getUserAppMenu(t, Long.parseLong(user.getId()),
				Long.parseLong(user.getAppId()));
		ignoreButtons(menuItems);
		modelMap.put("items", menuItems);

		String pwdStrength = (String) Controllers.getSessionObject("pwdStrength");
		modelMap.put("pwdStrength", pwdStrength);

		return "page/home";
	}

	private void ignoreButtons(List<Tree> menuItems) {
		if (menuItems != null && menuItems.size() > 0) {
			int size = menuItems.size();
			for (int i = size - 1; i >= 0; i--) {
				Tree item = menuItems.get(i);
				if (item.getType().equals("1")) {
					menuItems.remove(item);
				} else {
					if (item.getChildren() != null && item.getChildren().size() > 0) {
						ignoreButtons(item.getChildren());
					}
				}
			}
		}
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
