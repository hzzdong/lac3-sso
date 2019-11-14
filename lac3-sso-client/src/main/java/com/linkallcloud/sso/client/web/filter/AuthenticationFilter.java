/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.client.web.filter.AuthenticationFilter.java 
 *
 * 2011-5-22
 * 
 */
package com.linkallcloud.sso.client.web.filter;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.linkallcloud.core.principal.Assertion;
import com.linkallcloud.sso.client.util.CommonUtils;
import com.linkallcloud.sso.client.util.UrlPattern;

/**
 * Filter implementation to intercept all requests and attempt to authenticate the user by redirecting them to SSO
 * (unless the user has a ticket).
 * 
 * 2011-6-14
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
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
    
    private final UrlPattern excludePattern;

    /**
     * 
     * @param siteCode
     * @param serverName
     * @param serviceUrl
     * @param ssoServerLoginUrl
     * @param proxyCallbackUrl
     * @param excludePattern
     */
    public AuthenticationFilter(final String siteCode, final String serverName, final String serviceUrl,
            final String ssoServerLoginUrl, final String proxyCallbackUrl, UrlPattern excludePattern) {
        this(siteCode, serverName, serviceUrl, true, ssoServerLoginUrl, proxyCallbackUrl, false, false, excludePattern);
    }

    /**
     * 
     * @param siteCode
     * @param serverName
     * @param serviceUrl
     * @param ssoServerLoginUrl
     * @param proxyCallbackUrl
     * @param renew
     * @param gateway
     * @param excludePattern
     */
    public AuthenticationFilter(final String siteCode, final String serverName, final String serviceUrl,
            final String ssoServerLoginUrl, final String proxyCallbackUrl, boolean renew, boolean gateway, UrlPattern excludePattern) {
        this(siteCode, serverName, serviceUrl, true, ssoServerLoginUrl, proxyCallbackUrl, renew, gateway, excludePattern);
    }

    /**
     * 
     * @param siteCode
     * @param serverName
     * @param serviceUrl
     * @param useSession
     * @param ssoServerLoginUrl
     * @param proxyCallbackUrl
     * @param renew
     * @param gateway
     * @param excludePattern
     */
    public AuthenticationFilter(String siteCode, String serverName, String serviceUrl, boolean useSession,
            String ssoServerLoginUrl, String proxyCallbackUrl, boolean renew, boolean gateway, UrlPattern excludePattern ) {
        super(siteCode, serverName, serviceUrl, useSession);
        CommonUtils.assertNotNull(ssoServerLoginUrl, "the SSO Server Login URL cannot be null.");
        this.ssoServerLoginUrl = ssoServerLoginUrl;
        this.proxyCallbackUrl = proxyCallbackUrl;
        this.renew = renew;
        this.gateway = gateway;
        this.excludePattern = excludePattern;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.zj.pubinfo.sso.client.web.filter.AbstractCasFilter#doFilterInternal(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain filterChain) throws IOException, ServletException {

        if (isNotProtectedResource((HttpServletRequest) request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Is this a request for the proxy callback listener? If so, pass it through
        if (proxyCallbackUrl != null && proxyCallbackUrl.endsWith(((HttpServletRequest) request).getRequestURI())
                && request.getParameter("pgtId") != null && request.getParameter("pgtIou") != null) {
            log.trace("passing through what we hope is SSO's request for proxy ticket receptor.");
            filterChain.doFilter(request, response);
            return;
        }

        final HttpSession session = request.getSession(isUseSession());
        final String ticket = request.getParameter(PARAM_TICKET);
        final Assertion assertion = session != null ? (Assertion) session.getAttribute(Assertion.ASSERTION_KEY) : null;
        final boolean wasGatewayed = session != null && session.getAttribute(CONST_GATEWAY) != null;

        if (CommonUtils.isBlank(ticket) && assertion == null && !wasGatewayed) {
            log.debug("no ticket and no assertion found");
            if (this.gateway && session != null) {
                log.debug("setting gateway attribute in session");
                session.setAttribute(CONST_GATEWAY, "yes");
            }

            final String serviceUrl = constructServiceUrl(request, response);
            final String urlToRedirectTo =
                    this.ssoServerLoginUrl + "?service=" + URLEncoder.encode(serviceUrl, "UTF-8") + "&from="
                            + this.siteCode + (this.renew ? "&renew=true" : "") + (this.gateway ? "&gateway=true" : "");

            if (log.isDebugEnabled()) {
                log.debug("redirecting to \"" + urlToRedirectTo + "\"");
            }

            response.sendRedirect(urlToRedirectTo);
            return;
        }

        if (session != null) {
            log.debug("removing gateway attribute from session");
            session.setAttribute(CONST_GATEWAY, null);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 不需要登录保护的资源？
     * 
     * @param request
     * @return boolean
     */
    protected boolean isNotProtectedResource(HttpServletRequest request) {
        return isMatcherExclude(request) || isNotNeedLogin(request) || isLogout(request) || isProxyCallback(request);
    }
    
    private boolean isMatcherExclude(HttpServletRequest request) {
        return excludePattern.isMatcher(request.getServletPath());
    }
    
    /**
     * @param request
     * @return
     */
    protected boolean isNotNeedLogin(HttpServletRequest request) {
        boolean l = false;
        if (request.getServletPath().indexOf("/nnl/") != -1) {
            l = true;
            log.trace("passing through what we hope is not need login.");
        }
        return l;
    }

    /**
     * Is logout?
     * 
     * @param request
     * @return boolean
     */
    protected boolean isLogout(HttpServletRequest request) {
        boolean logout = false;
        if (request.getServletPath().indexOf("/logout") != -1) {
            logout = true;
            log.trace("passing through what we hope is logout.");
        }
        return logout;
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
