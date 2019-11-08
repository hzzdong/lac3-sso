package com.linkallcloud.sso.exception;

public class BlackListException extends SsoException {
	private static final long serialVersionUID = 8392311663170392592L;

	public static final String ARG_CODE_BLACKLIST = "e-black";

	public BlackListException() {
		super(ARG_CODE_BLACKLIST, "黑名单错误");
	}

	public BlackListException(String code) {
		super(code);
	}

	public BlackListException(String code, String message) {
		super(code, message);
	}

	public BlackListException(String code, Object[] args) {
		super(code, args);
	}

	public BlackListException(String code, String msgFormat, Object[] args) {
		super(code, msgFormat, args);
	}

	public BlackListException(String code, Throwable cause) {
		super(code, cause);
	}

	public BlackListException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}

}
