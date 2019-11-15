package com.linkallcloud.sso.client.validation;

import com.alibaba.fastjson.JSON;
import com.linkallcloud.core.principal.Assertion;
import com.linkallcloud.core.principal.Service;
import com.linkallcloud.core.principal.SimplePrincipal;
import com.linkallcloud.sso.client.proxy.ProxyGrantingTicketStorage;
import com.linkallcloud.sso.client.proxy.ProxyRetriever;
import com.linkallcloud.sso.client.util.CommonUtils;
import com.linkallcloud.sso.oapi.dto.ServiceAuthenticationResult;

/**
 * Implementation of TicketValidator that follows the SSO protocol without
 * proxying.
 * 
 */
public class ServiceTicketValidator extends AbstractUrlBasedTicketValidator {

	/**
	 * Proxy callback url to send to the SSO server.
	 */
	private final Service proxyCallbackUrl;

	/**
	 * The storage mechanism for the ProxyGrantingTickets.
	 */
	private final ProxyGrantingTicketStorage proxyGrantingTicketStorage;

	/**
	 * Injected into Assertions to allow them to retrieve proxy tickets.
	 */
	private final ProxyRetriever proxyRetriever;

	public ServiceTicketValidator(final String ssoServerUrl, final boolean renew) {
		this(ssoServerUrl, renew, null, null, null);
	}

	public ServiceTicketValidator(final String ssoServerUrl, final boolean renew, final Service proxyCallbackUrl,
			final ProxyGrantingTicketStorage proxyGrantingTicketStorage, final ProxyRetriever proxyRetriever) {
		super(ssoServerUrl, renew);

		if (proxyCallbackUrl != null) {
			CommonUtils.assertNotNull(proxyGrantingTicketStorage, "proxyGrantingTicketStorage cannot be null");
			CommonUtils.assertNotNull(proxyRetriever, "proxyRetriever cannot be null.");
		}
		this.proxyCallbackUrl = proxyCallbackUrl;
		this.proxyGrantingTicketStorage = proxyGrantingTicketStorage;
		this.proxyRetriever = proxyRetriever;
	}

	@Override
	protected String constructURL(final String ticketId, final Service service) {
		return getSsoServerUrl() + getValidationUrlName() + "?ticket=" + ticketId + (isRenew() ? "&renew=true" : "")
				+ "&service=" + getEncodedService(service) + "&from=" + service.getCode()
				+ (this.proxyCallbackUrl != null ? "&pgtUrl=" + getEncodedService(this.proxyCallbackUrl) : "");
	}

	protected String getValidationUrlName() {
		return "/serviceValidate";
	}

	@Override
	protected final Assertion parseResponse(final String response) throws ValidationException {
		ServiceAuthenticationResult result = parseResult(response);
		if (result == null) {
			log.debug("Validation of ticket failed, response:" + response);
			throw new ValidationException("No principal found.");
		} else if (!"0".equals(result.getCode())) {
			log.debug("Validation of ticket failed: " + result.getCode() + "," + result.getMessage());
			throw new ValidationException(result.getMessage());
		} else {
			return getValidAssertionInternal(response, result);
		}
	}

	protected ServiceAuthenticationResult parseResult(final String response) {
		return JSON.parseObject(response, ServiceAuthenticationResult.class);
	}

	protected Assertion getValidAssertionInternal(final String response, final ServiceAuthenticationResult str)
			throws ValidationException {
		return getAssertionBasedOnProxyGrantingTicketIou(str);
	}

	protected final Assertion getAssertionBasedOnProxyGrantingTicketIou(final ServiceAuthenticationResult str) {
		if (CommonUtils.isNotBlank(str.getProxyGrantingTicket())) {
			return new AssertionImpl(new SimplePrincipal(str.getId(), str.getSiteId(), str.getMappingType()), null,
					this.proxyRetriever, this.proxyGrantingTicketStorage == null ? null
							: this.proxyGrantingTicketStorage.retrieve(str.getProxyGrantingTicket()));
		} else {
			return new AssertionImpl(new SimplePrincipal(str.getId(), str.getSiteId(), str.getMappingType()));
		}
	}

}
