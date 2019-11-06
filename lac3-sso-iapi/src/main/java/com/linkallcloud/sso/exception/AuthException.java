package com.linkallcloud.sso.exception;

public class AuthException extends SsoException {
	private static final long serialVersionUID = 6545412089836610534L;

	public static final String ARG_CODE_AUTH = "e-auth";

	public AuthException() {
		super(ARG_CODE_AUTH, "验证错误");
	}

	public AuthException(String code) {
		super(code);
	}

	public AuthException(String code, String message) {
		super(code, message);
	}

	public AuthException(String code, Object[] args) {
		super(code, args);
	}

	public AuthException(String code, String msgFormat, Object[] args) {
		super(code, msgFormat, args);
	}

	public AuthException(String code, Throwable cause) {
		super(code, cause);
	}

	public AuthException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}

}
