package com.linkallcloud.sso.exception;

import com.linkallcloud.core.exception.BizException;

public class SsoException extends BizException {
	private static final long serialVersionUID = 3060232218291004549L;

	public static final String ARG_CODE_SSO = "e-sso";

	public SsoException() {
		super(ARG_CODE_SSO, "SSO系统错误");
	}

	public SsoException(String code) {
		super(code);
	}

	public SsoException(String code, String message) {
		super(code, message);
	}

	public SsoException(String code, Object[] args) {
		super(code, args);
	}

	public SsoException(String code, String msgFormat, Object[] args) {
		super(code, msgFormat, args);
	}

	public SsoException(String code, Throwable cause) {
		super(code, cause);
	}

	public SsoException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}
}
