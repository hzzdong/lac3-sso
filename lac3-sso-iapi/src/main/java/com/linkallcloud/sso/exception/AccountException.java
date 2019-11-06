package com.linkallcloud.sso.exception;

public class AccountException extends SsoException {
	private static final long serialVersionUID = 7274277943213015471L;

	public static final String ARG_CODE_ACCOUNT = "e-account";

	public AccountException() {
		super(ARG_CODE_ACCOUNT, "账号错误");
	}

	public AccountException(String code) {
		super(code);
	}

	public AccountException(String code, String message) {
		super(code, message);
	}

	public AccountException(String code, Object[] args) {
		super(code, args);
	}

	public AccountException(String code, String msgFormat, Object[] args) {
		super(code, msgFormat, args);
	}

	public AccountException(String code, Throwable cause) {
		super(code, cause);
	}

	public AccountException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}

}
