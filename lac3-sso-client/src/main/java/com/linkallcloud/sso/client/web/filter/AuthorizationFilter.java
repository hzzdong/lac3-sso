package com.linkallcloud.sso.client.web.filter;

import java.io.IOException;

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
import org.springframework.web.util.WebUtils;

import com.linkallcloud.core.principal.Assertion;
import com.linkallcloud.core.principal.Principal;
import com.linkallcloud.sso.client.authorization.AuthorizationException;
import com.linkallcloud.sso.client.authorization.AuthorizedDecider;
import com.linkallcloud.sso.client.util.CommonUtils;

/**
 * Simple filter that attempts to determine if someone is authorized to use the system. Assumes that you are protecting
 * the application with the AuthenticationFilter such that the Assertion is set in the session.
 * <p/>
 * If a user is not authorized to use the application, the response code of 403 will be sent to the browser.
 * <p/>
 * This filter needs to be configured after both the authentication filter and the validation filter.
 * 
 */
public final class AuthorizationFilter implements Filter {
    private final Log log = LogFactory.getLog(this.getClass());

    /**
     * Decider that determines whether a specified principal has access to the resource or not.
     */
    private final AuthorizedDecider decider;

    /**
     * @param ssoAuthorizedDecider
     *            the thing actually deciding to grant access or not.
     */
    public AuthorizationFilter(final AuthorizedDecider ssoAuthorizedDecider) {
        CommonUtils.assertNotNull(ssoAuthorizedDecider, "the ssoAuthorizedDecider cannot be null.");
        this.decider = ssoAuthorizedDecider;
    }

    public void destroy() {
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
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final Assertion assertion = (Assertion) WebUtils.getRequiredSessionAttribute(request, Assertion.ASSERTION_KEY);
        final Principal principal = assertion.getPrincipal();

        final boolean authorized = this.decider.isAuthorizedToUseApplication(principal);

        if (!authorized) {
            log.debug("User not authorized to access application.");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            throw new AuthorizationException(principal.getId() + " is not authorized to use this application.");
        }

        log.debug("User successfully authorized.");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void init(final FilterConfig filterConfig) throws ServletException {
        // nothing to do here
    }
}
