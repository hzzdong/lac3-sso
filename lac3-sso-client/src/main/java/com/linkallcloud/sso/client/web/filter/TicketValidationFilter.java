package com.linkallcloud.sso.client.web.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.linkallcloud.core.principal.Assertion;
import com.linkallcloud.core.principal.Principal;
import com.linkallcloud.core.principal.SimpleService;
import com.linkallcloud.sso.client.util.CommonUtils;
import com.linkallcloud.sso.client.validation.TicketValidator;
import com.linkallcloud.sso.client.validation.ValidationException;

/**
 * Implementation of a Filter that checks for a "ticket" and if one is found,
 * will attempt to validate the ticket. On a successful validation, it sets the
 * Assertion object into the session. On an unsuccessful validation attempt, it
 * sets the response code to 403.
 * <p/>
 * This filter needs to be configured after the authentication filter (if that
 * filter exists in the chain).
 * 
 */
public final class TicketValidationFilter extends AbstractSSOFilter {

	/**
	 * Instance of the ticket validator.
	 */
	private final TicketValidator ticketValidator;

	/**
	 * Specify whether the filter should redirect the user agent after a successful
	 * validation to remove the ticket parameter from the query string.
	 */
	private final boolean redirectAfterValidation;

	/**
	 * Constructor that takes the severName (or serviceUrl) and the TicketValidator.
	 * Either serveName or serviceUrl is required (but not both).
	 * 
	 * @param siteCode
	 * @param serverName      the name of the server in <hostname>:<port>
	 *                        combination, if using a non-standard port.
	 * @param serviceUrl      the url to always redirect to.
	 * @param ticketValidator the validator to validate the tickets.
	 * @param excludePattern
	 */
	public TicketValidationFilter(final int siteClazz, final String siteCode, final String serverName,
			final String serviceUrl, final TicketValidator ticketValidator) {
		this(siteClazz, siteCode, serverName, serviceUrl, ticketValidator, null);
	}

	public TicketValidationFilter(final int siteClazz, final String siteCode, final String serverName,
			final String serviceUrl, final TicketValidator ticketValidator, final List<String> ignoreRes) {
		this(siteClazz, siteCode, serverName, serviceUrl, true, ticketValidator, false, ignoreRes, false);
	}

	/**
	 * Constructor that takes the severName (or serviceUrl), TicketValidator,
	 * useSession and redirectAfterValidation. Either serveName or serviceUrl is
	 * required (but not both).
	 * 
	 * @param siteCode
	 * @param serverName              the name of the server in <hostname>:<port>
	 *                                combination, if using a non-standard port.
	 * @param serviceUrl              the url to always redirect to.
	 * @param useSession              flag to set whether to store stuff in the
	 *                                session.
	 * @param ticketValidator         the validator to validate the tickets.
	 * @param redirectAfterValidation whether to redirect to remove the ticket.
	 * @param excludePattern
	 */
	public TicketValidationFilter(final int siteClazz, final String siteCode, final String serverName,
			final String serviceUrl, final boolean useSession, final TicketValidator ticketValidator,
			final boolean redirectAfterValidation, final List<String> ignoreRes, boolean override) {
		super(siteClazz, siteCode, serverName, serviceUrl, useSession, ignoreRes, override);
		CommonUtils.assertNotNull(ticketValidator, "ticketValidator cannot be null.");
		this.ticketValidator = ticketValidator;
		this.redirectAfterValidation = redirectAfterValidation;

		log.info("Initialized with the following properties:  " + "ticketValidator="
				+ this.ticketValidator.getClass().getName() + "; " + "redirectAfterValidation="
				+ this.redirectAfterValidation);
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
		final String ticket = request.getParameter(PARAM_TICKET);

		if (CommonUtils.isNotBlank(ticket)) {
			if (log.isDebugEnabled()) {
				log.debug("Attempting to validate ticket: " + ticket);
			}

			try {
				final Assertion assertion = this.ticketValidator.validate(ticket, new SimpleService(
						constructServiceUrl(request, response), this.getSiteCode(), this.getSiteClazz()));

				if (log.isDebugEnabled()) {
					log.debug("Successfully authenticated user: " + assertion.getPrincipal().getId());
				}

				request.setAttribute(Principal.PRINCIPAL_KEY, assertion.getPrincipal());
				request.setAttribute(this.getSiteCode() + Assertion.ASSERTION_KEY, assertion);

				if (isUseSession()) {
					request.getSession().setAttribute(this.getSiteCode() + Assertion.ASSERTION_KEY, assertion);
				}
			} catch (final ValidationException e) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				log.warn(e, e);
				throw new ServletException(e);
			}

			if (this.redirectAfterValidation) {
				response.sendRedirect(response.encodeRedirectURL(constructServiceUrl(request, response)));
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

}
