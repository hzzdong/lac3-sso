package com.linkallcloud.sso.exception;

public class LockException extends SsoException {
	private static final long serialVersionUID = -8625105511394409970L;

	public static final String ERROR_LOCK = "e.lock";

	public LockException() {
		super(ERROR_LOCK, "您的账号或者Ip暂时被锁定，请联系管理员！");
	}

	public LockException(String message) {
		super(ERROR_LOCK, message);
	}

	public LockException(String code, String message) {
		super(code, message);
	}

	public LockException(String code, String msgFormat, Object[] args) {
		super(code, msgFormat, args);
	}

}
