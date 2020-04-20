package com.linkallcloud.sso.client.web.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.principal.Assertion;
import com.linkallcloud.sso.client.util.CommonUtils;

/**
 * Filter implementation to intercept all requests and attempt to authenticate
 * the user by redirecting them to SSO (unless the user has a ticket).
 * 
 * @author <a href="mailto:hzzdong@qq.com">ZhouDong</a>
 */
public final class AuthenticationFilter extends AbstractSSOFilter {

	/**
	 * The URL to the SSO Server login.
	 */
	private final String ssoServerLoginUrl;

	/**
	 * Whether to send the renew request or not.
	 */
	private final boolean renew;

	/**
	 * Whether to send the gateway request or not.
	 */
	private final boolean gateway;

	/**
	 * Secure URL whereto this filter should ask SSO to send Proxy Granting Tickets.
	 */
	private final String proxyCallbackUrl;

	public AuthenticationFilter(final String siteCode, final String serverName, final String ssoServer) {
		this(siteCode, serverName, ssoServer, null);
	}

	public AuthenticationFilter(final String siteCode, final String serverName, final String ssoServer,
			final List<String> ignoreRes) {
		this(siteCode, serverName, null, true, ssoServer, null, false, false, ignoreRes, false);
	}

	public AuthenticationFilter(String siteCode, String serverName, String serviceUrl, boolean useSession,
			String ssoServer, String proxyCallbackUrl, boolean renew, boolean gateway, List<String> ignoreRes,
			boolean override) {
		super(siteCode, serverName, serviceUrl, useSession, ignoreRes, override);
		CommonUtils.assertNotNull(ssoServer, "the SSO Server Login URL cannot be null.");
		if (ssoServer.endsWith("/login")) {
			this.ssoServerLoginUrl = ssoServer;
		} else {
			this.ssoServerLoginUrl = ssoServer + "/login";
		}
		this.proxyCallbackUrl = proxyCallbackUrl;
		this.renew = renew;
		this.gateway = gateway;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.zj.pubinfo.sso.client.web.filter.AbstractCasFilter#doFilterInternal(javax.
	 * servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws IOException, ServletException {
		// Is this a request for the proxy callback listener? If so, pass it through
		if (isProxyCallback(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		if (!Strings.isBlank(getLacToken(request))) {
			filterChain.doFilter(request, response);
			return;
		}

		final HttpSession session = request.getSession(isUseSession());
		final String ticket = request.getParameter(PARAM_TICKET);
		final Assertion assertion = session != null
				? (Assertion) session.getAttribute(siteCode + Assertion.ASSERTION_KEY)
				: null;
		final boolean wasGatewayed = session != null && session.getAttribute(CONST_GATEWAY) != null;

		if (CommonUtils.isBlank(ticket) && assertion == null && !wasGatewayed) {
			log.debug("no ticket and no assertion found");
			if (this.gateway && session != null) {
				log.debug("setting gateway attribute in session");
				session.setAttribute(CONST_GATEWAY, "yes");
			}

			final String serviceUrl = constructServiceUrl(request, response);
			final String urlToRedirectTo = this.ssoServerLoginUrl + "?service=" + URLEncoder.encode(serviceUrl, "UTF-8")
					+ "&from=" + this.siteCode + (this.renew ? "&renew=true" : "")
					+ (this.gateway ? "&gateway=true" : "");

			if (log.isDebugEnabled()) {
				log.debug("redirecting to \"" + urlToRedirectTo + "\"");
			}

			// response.sendRedirect(urlToRedirectTo);
			toLogin(urlToRedirectTo, request, response);
			return;
		}

		if (session != null) {
			log.debug("removing gateway attribute from session");
			session.setAttribute(CONST_GATEWAY, null);
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * Is this a request for the proxy callback listener?
	 * 
	 * @param request
	 * @return boolean
	 */
	protected boolean isProxyCallback(HttpServletRequest request) {
		// Is this a request for the proxy callback listener? If so, pass it through
		if (proxyCallbackUrl != null && proxyCallbackUrl.endsWith(request.getRequestURI())
				&& request.getParameter("pgtId") != null && request.getParameter("pgtIou") != null) {
			log.trace("passing through what we hope is SSO's request for proxy ticket receptor.");
			return true;
		}
		return false;
	}

}
