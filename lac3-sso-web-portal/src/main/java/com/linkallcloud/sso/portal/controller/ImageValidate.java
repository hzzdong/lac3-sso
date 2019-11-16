package com.linkallcloud.sso.portal.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.sso.portal.utils.LacSessionValidateCode;

@Controller
@RequestMapping
public class ImageValidate {

	@Autowired
	private LacSessionValidateCode sessionValidateCode;

	@RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
	public String verifyCode(HttpServletRequest request, HttpServletResponse response) {
		sessionValidateCode.generate(request, response);
		return null;
	}

}
