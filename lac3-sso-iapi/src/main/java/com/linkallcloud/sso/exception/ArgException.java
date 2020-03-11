package com.linkallcloud.sso.exception;

public class ArgException extends SsoException {
	private static final long serialVersionUID = 4020885110087092747L;

	public static final String ARG_CODE_ARG = "e-arg";

	public ArgException() {
		super(ARG_CODE_ARG, "参数错误");
	}

	public ArgException(String message) {
		super(ARG_CODE_ARG, message);
	}

	public ArgException(String code, String message) {
		super(code, message);
	}

	public ArgException(String code, Object[] args) {
		super(code, args);
	}

	public ArgException(String code, String msgFormat, Object[] args) {
		super(code, msgFormat, args);
	}

	public ArgException(String code, Throwable cause) {
		super(code, cause);
	}

	public ArgException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}

}
