package com.linkallcloud.sso.client.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.WebUtils;

import com.linkallcloud.core.principal.Assertion;
import com.linkallcloud.sso.client.validation.AssertionHolder;

/**
 * Places the assertion in a ThreadLocal such that other resources can access it that do not have access to the web tier
 * session.
 * 
 */
public final class AssertionThreadLocalFilter implements Filter {

    public void init(final FilterConfig filterConfig) throws ServletException {
        // nothing to do here
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
     * javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
            final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final Assertion assertion = (Assertion) WebUtils.getSessionAttribute(request, Assertion.ASSERTION_KEY);

        try {
            AssertionHolder.setAssertion(assertion);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            AssertionHolder.clear();
        }
    }

    public void destroy() {
        // nothing to do
    }
}
