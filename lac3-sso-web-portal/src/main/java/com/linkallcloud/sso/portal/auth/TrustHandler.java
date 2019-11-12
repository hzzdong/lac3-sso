package com.linkallcloud.sso.portal.auth;

import com.linkallcloud.core.dto.Trace;

/** Interface for server-based authentication handlers. */
public interface TrustHandler extends AuthHandler {

  /**
   * Allows arbitrary logic to compute an authenticated user from
   * a ServletRequest.
   */
  String getUsername(Trace t, javax.servlet.ServletRequest request);

}
