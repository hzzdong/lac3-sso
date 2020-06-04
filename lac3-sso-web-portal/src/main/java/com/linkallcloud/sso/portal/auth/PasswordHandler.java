package com.linkallcloud.sso.portal.auth;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.Account;

/** Interface for password-based authentication handlers. */
public interface PasswordHandler extends AuthHandler {

	/**
	 * Authenticates the given username/password pair, returning true on success and
	 * false on failure.
	 */
	Account authenticate(Trace t, javax.servlet.ServletRequest reqyest, String username, String password, int appClazz);

}
