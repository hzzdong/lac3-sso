/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.client.validation.AbstractUrlBasedTicketValidator.java 
 *
 * 2011-6-14
 * 
 */
package com.linkallcloud.sso.client.validation;

import java.net.URLEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.linkallcloud.core.principal.Assertion;
import com.linkallcloud.core.principal.Service;
import com.linkallcloud.core.www.utils.HttpClientFactory;
import com.linkallcloud.sso.client.util.CommonUtils;

/**
 * Abstract class for validating tickets that defines a workflow that all ticket
 * validation should follow.
 * 
 * 2011-6-15
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public abstract class AbstractUrlBasedTicketValidator implements TicketValidator {
	protected final Log log = LogFactory.getLog(this.getClass());

	/**
	 * Url to SSO server. Generally of the form https://server:port/sso/
	 */
	private final String ssoServerUrl;

	/**
	 * Whether this client is looking for an authentication from renew.
	 */
	private final boolean renew;

	public final Assertion validate(final String ticketId, final Service service) throws ValidationException {
		final String url = constructURL(ticketId, service);
		final String response = getResponseFromURL(url);

		return parseResponse(response);
	}

	/**
	 * Constructs the URL endpoint for contacting SSO for ticket validation.
	 * 
	 * @param ticketId the opaque ticket id.
	 * @param service  the service we are validating for.
	 * @return the fully constructed url.
	 */
	protected abstract String constructURL(final String ticketId, final Service service);

	/**
	 * Parses the response retrieved from the url endpoint.
	 * 
	 * @param response the String response.
	 * @return an Assertion based on the response.
	 * @throws ValidationException if there was an error validating the ticket.
	 */
	protected abstract Assertion parseResponse(final String response) throws ValidationException;

	private String getResponseFromURL(final String url) throws ValidationException {
		if (url.startsWith("https")) {
			return HttpClientFactory.me(true).get(url);
		} else {
			return HttpClientFactory.me(false).get(url);
		}
	}

	protected AbstractUrlBasedTicketValidator(final String ssoServerUrl, final boolean renew) {
		CommonUtils.assertNotNull(ssoServerUrl, "the validationUrl cannot be null");
		this.ssoServerUrl = ssoServerUrl;
		this.renew = renew;
	}

	/**
	 * Helper method to encode the service url.
	 * 
	 * @param service the service url to encode.
	 * @return the encoded service url.
	 */
	protected final String getEncodedService(final Service service) {
		try {
			return URLEncoder.encode(service.getUrl(), "UTF-8");
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Helper method to encode the service url.
	 * 
	 * @param service the service url to encode.
	 * @return the encoded service url.
	 */
	protected final String getEncodedUrl(final String url) {
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected final String getSsoServerUrl() {
		return this.ssoServerUrl;
	}

	protected final boolean isRenew() {
		return this.renew;
	}
}
