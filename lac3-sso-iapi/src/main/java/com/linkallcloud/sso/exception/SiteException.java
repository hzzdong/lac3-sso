package com.linkallcloud.sso.exception;

public class SiteException extends SsoException {
	private static final long serialVersionUID = -4669090088180588519L;

	public static final String ERROR_SITE = "e.site";

	public SiteException() {
		super(ERROR_SITE, "接入站点异常");
	}

	public SiteException(String code, String message) {
		super(code, message);
	}

}
