package com.linkallcloud.sso.portal.exception;

import com.linkallcloud.core.exception.BaseException;

public class AccountException extends BaseException {
	private static final long serialVersionUID = 4590389897255145255L;

	public static final String ERROR_ACCOUNT = "e.account";

	public AccountException() {
		super(ERROR_ACCOUNT,"账号或者密码错误");
	}

	public AccountException(String message) {
		super(ERROR_ACCOUNT, message);
	}

	public AccountException(String code, String message) {
		super(code, message);
	}

}
