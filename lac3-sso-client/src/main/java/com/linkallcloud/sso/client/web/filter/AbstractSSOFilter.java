/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.client.web.filter.AbstractSSOFilter.java 
 *
 * 2011-6-15
 * 
 */
package com.linkallcloud.sso.client.web.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.linkallcloud.sso.client.util.CommonUtils;

/**
 * Abstract class that contains common functionality amongst SSO filters.
 * <p/>
 * You must specify the serverName (format: hostname:port) or the serviceUrl. If you specify both, the serviceUrl is
 * used over the serverName.
 * 
 * 2011-6-15
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public abstract class AbstractSSOFilter implements Filter {

    /**
     * Constant string representing the ticket parameter.
     */
    public static final String PARAM_TICKET = "ticket";

    /**
     * Constant representing where we flag a gatewayed request in the session.
     */
    public static final String CONST_GATEWAY = "_sso_gateway_";

    /**
     * Instance of Commons Logging.
     */
    protected final Log log = LogFactory.getLog(this.getClass());

    /**
     * The name of the server in the following format: <hostname>:<port> where port is optional if its a standard port.
     */
    private final String serverName;

    protected final String siteCode;

    /**
     * The exact service url to match to.
     */
    private final String serviceUrl;

    /**
     * Whether to store the entry in session or not. Defaults to true.
     */
    private final boolean useSession;

    /**
     * 
     * @param siteCode
     * @param serverName
     * @param serviceUrl
     */
    protected AbstractSSOFilter(final String siteCode, final String serverName, final String serviceUrl) {
        this(siteCode, serverName, serviceUrl, true);
    }

    /**
     * 
     * @param siteCode
     * @param serverName
     * @param serviceUrl
     * @param useSession
     */
    protected AbstractSSOFilter(final String siteCode, final String serverName, final String serviceUrl,
            final boolean useSession) {
        CommonUtils.assertTrue(CommonUtils.isNotBlank(serverName) || CommonUtils.isNotBlank(serviceUrl),
                "either serverName or serviceUrl must be set");

        this.siteCode = siteCode;
        this.serverName = serverName;
        this.serviceUrl = serviceUrl;
        this.useSession = useSession;

        log.info("Site code set to: " + this.siteCode + ";Service Name set to: " + this.serverName
                + "; Service Url  set to: " + this.serviceUrl + "Use Session set to: " + this.useSession);
    }

    public final void destroy() {
        // nothing to do
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
     * javax.servlet.FilterChain)
     */
    @Override
    public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
            final FilterChain filterChain) throws IOException, ServletException {
        doFilterInternal((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    /**
     * 
     * @param request
     * @param response
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    protected abstract void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws IOException, ServletException;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        // nothing to do here
    }

    /**
     * Constructs a service url from the HttpServletRequest or from the given serviceUrl. Prefers the serviceUrl
     * provided if both a serviceUrl and a serviceName.
     * 
     * @param request
     *            the HttpServletRequest
     * @param response
     *            the HttpServletResponse
     * @return the service url to use.
     * @throws UnsupportedEncodingException
     */
    protected final String constructServiceUrl(final HttpServletRequest request, final HttpServletResponse response)
            throws UnsupportedEncodingException {
        if (CommonUtils.isNotBlank(this.serviceUrl)) {
            return this.serviceUrl;// response.encodeURL(this.serviceUrl);
        }

        final StringBuffer buffer = new StringBuffer();

        synchronized (buffer) {
            buffer.append(request.isSecure() ? "https://" : "http://");
            buffer.append(this.serverName);
            buffer.append(request.getRequestURI());

            if (CommonUtils.isNotBlank(request.getQueryString())) {
                final int location = request.getQueryString().indexOf(PARAM_TICKET + "=");

                if (location == 0) {
                    final String returnValue = buffer.toString();// response.encodeURL(buffer.toString());
                    if (log.isDebugEnabled()) {
                        log.debug("serviceUrl generated: " + returnValue);
                    }
                    return returnValue;
                }

                buffer.append("?");

                if (location == -1) {
                    buffer.append(request.getQueryString());
                } else if (location > 0) {
                    final int actualLocation = request.getQueryString().indexOf("&" + PARAM_TICKET + "=");

                    if (actualLocation == -1) {
                        buffer.append(request.getQueryString());
                    } else if (actualLocation > 0) {
                        buffer.append(request.getQueryString().substring(0, actualLocation));
                    }
                }
            }
        }

        final String returnValue = buffer.toString();// response.encodeURL(buffer.toString());
        if (log.isDebugEnabled()) {
            log.debug("serviceUrl generated: " + returnValue);
        }
        return returnValue;
    }

    protected final boolean isUseSession() {
        return this.useSession;
    }
}
