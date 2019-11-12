package com.linkallcloud.sso.exception;

public class AppException extends SsoException {
	private static final long serialVersionUID = -3903761199647886701L;
	
	public static final String ERROR_CODE_APP = "e-app";

	public AppException() {
		super(ERROR_CODE_APP, "应用错误");
	}

	public AppException(String code) {
		super(code);
	}

	public AppException(String code, String message) {
		super(code, message);
	}

	public AppException(String code, Object[] args) {
		super(code, args);
	}

	public AppException(String code, String msgFormat, Object[] args) {
		super(code, msgFormat, args);
	}

	public AppException(String code, Throwable cause) {
		super(code, cause);
	}

	public AppException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}

}
