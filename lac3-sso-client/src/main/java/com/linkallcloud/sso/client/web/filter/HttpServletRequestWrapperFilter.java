/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.client.web.filter.HttpServletRequestWrapperFilter.java 
 *
 * 2011-6-14
 * 
 */
package com.linkallcloud.sso.client.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.web.util.WebUtils;

import com.linkallcloud.core.principal.Assertion;
import com.linkallcloud.core.principal.Principal;

/**
 * Implementation of a filter that wraps the normal HttpServletRequest with a wrapper that overrides the getRemoteUser
 * method to retrieve the user from the SSO Assertion.
 * <p/>
 * This filter needs to be configured in the chain so that it executes after both the authentication and the validation
 * filters.
 * 
 * 2011-6-14
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public final class HttpServletRequestWrapperFilter implements Filter {

    public void destroy() {
        // nothing to do
    }

    /*
     * Wraps the HttpServletRequest in a wrapper class that delegates <code>request.getRemoteUser</code> to the
     * underlying Assertion object stored in the user session.
     * 
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
     * javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
            final FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(new SsoHttpServletRequestWrapper((HttpServletRequest) servletRequest), servletResponse);
    }

    public void init(final FilterConfig filterConfig) throws ServletException {
        // nothing to do
    }

    final class SsoHttpServletRequestWrapper extends HttpServletRequestWrapper {

        SsoHttpServletRequestWrapper(final HttpServletRequest request) {
            super(request);
        }

        public String getRemoteUser() {
            final Principal p = (Principal) this.getAttribute(Principal.PRINCIPAL_KEY);

            if (p != null) {
                return p.getId();
            }

            final Assertion assertion = (Assertion) WebUtils.getSessionAttribute(this, Assertion.ASSERTION_KEY);

            if (assertion != null) {
                return assertion.getPrincipal().getId();
            }

            return null;
        }
    }
}
