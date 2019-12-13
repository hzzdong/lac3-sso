package com.linkallcloud.sso.exception;

public class AccountException extends SsoException {
	private static final long serialVersionUID = 4590389897255145255L;

	public static final String ARG_CODE_ACCOUNT = "e.account";

	public AccountException() {
		super(ARG_CODE_ACCOUNT, "账号或者密码错误");
	}

	public AccountException(String message) {
		super(ARG_CODE_ACCOUNT, message);
	}

	public AccountException(String code, String message) {
		super(code, message);
	}

}
