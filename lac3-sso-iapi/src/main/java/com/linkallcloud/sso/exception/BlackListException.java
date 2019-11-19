package com.linkallcloud.sso.exception;

public class BlackListException extends SsoException {
	private static final long serialVersionUID = 8392311663170392592L;

	public static final String ERROR_BLACK = "e.black";

	public BlackListException() {
		super(ERROR_BLACK, "由于安全问题，您的账号被冻结，请联系管理员！");
	}

	public BlackListException(String message) {
		super(ERROR_BLACK, message);
	}

	public BlackListException(String code, String message) {
		super(code, message);
	}

	public BlackListException(String code, String msgFormat, Object[] args) {
		super(code, msgFormat, args);
	}

}
