package com.linkallcloud.sso.portal.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.www.utils.WebUtils;

@Controller
@RequestMapping
@Module(name = "登录")
public class GrantForService {

	@RequestMapping(value = "/generic", method = RequestMethod.GET)
	public String generic() {
		return "generic";
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
