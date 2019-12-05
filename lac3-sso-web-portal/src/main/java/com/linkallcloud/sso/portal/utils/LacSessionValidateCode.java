package com.linkallcloud.sso.portal.utils;

import org.springframework.stereotype.Component;

import com.linkallcloud.web.vc.SessionValidateCode;

@Component
public class LacSessionValidateCode extends SessionValidateCode {

	public LacSessionValidateCode() {
		this(true);
	}

	public LacSessionValidateCode(boolean numeric, int securityCodeLength, int imageWidth, int imageHight) {
		super(numeric, securityCodeLength, imageWidth, imageHight);
	}

	public LacSessionValidateCode(boolean numeric) {
		super(numeric);
	}

}
