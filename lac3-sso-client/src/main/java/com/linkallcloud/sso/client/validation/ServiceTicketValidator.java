/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.client.validation.ServiceTicketValidator.java 
 *
 * 2011-6-14
 * 
 */
package com.linkallcloud.sso.client.validation;

import com.linkallcloud.core.json.Json;
import com.linkallcloud.core.principal.Assertion;
import com.linkallcloud.core.principal.Service;
import com.linkallcloud.core.principal.SimplePrincipal;
import com.linkallcloud.sso.client.proxy.ProxyGrantingTicketStorage;
import com.linkallcloud.sso.client.proxy.ProxyRetriever;
import com.linkallcloud.sso.client.response.ErrorResponse;
import com.linkallcloud.sso.client.response.STResponse;
import com.linkallcloud.sso.client.response.ServiceResponse;
import com.linkallcloud.sso.client.util.CommonUtils;

/**
 * Implementation of TicketValidator that follows the SSO protocol without
 * proxying.
 * 
 * 2011-6-15
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
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

	/**
	 * 
	 * @param ssoServerUrl
	 * @param renew
	 */
	public ServiceTicketValidator(final String ssoServerUrl, final boolean renew) {
		this(ssoServerUrl, renew, null, null, null);
	}

	/**
	 * 
	 * @param ssoServerUrl
	 * @param renew
	 * @param proxyCallbackUrl
	 * @param proxyGrantingTicketStorage
	 * @param proxyRetriever
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zj.pubinfo.sso.client.validation.AbstractUrlBasedTicketValidator#
	 * constructURL(java.lang.String, cn.zj.pubinfo.principal.Service)
	 */
	@Override
	protected String constructURL(final String ticketId, final Service service) {
		return getSsoServerUrl() + getValidationUrlName() + "?ticket=" + ticketId + (isRenew() ? "&renew=true" : "")
				+ "&service=" + getEncodedService(service) + "&from=" + service.getCode()
				+ (this.proxyCallbackUrl != null ? "&pgtUrl=" + getEncodedService(this.proxyCallbackUrl) : "");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zj.pubinfo.sso.client.validation.AbstractUrlBasedTicketValidator#
	 * parseResponse(java.lang.String)
	 */
	@Override
	protected final Assertion parseResponse(final String response) throws ValidationException {
		ServiceResponse sr = parseSSOResponse(response);
		if (sr == null) {
			log.debug("Validation of ticket failed, response:" + response);
			throw new ValidationException("No principal found.");
		} else if (sr instanceof ErrorResponse) {
			ErrorResponse er = (ErrorResponse) sr;
			log.debug("Validation of ticket failed: " + er.getErrorCode() + "," + er.getErrorMessage());
			throw new ValidationException(er.getErrorMessage());
		} else {
			STResponse str = (STResponse) sr;
			return getValidAssertionInternal(response, str);
		}
	}

	/**
	 * 把SSO验证返回结果转换成ServiceResponse对象
	 * 
	 * @param response
	 * @return ServiceResponse
	 */
	protected ServiceResponse parseSSOResponse(final String response) {
		ServiceResponse result = null;
		if (response != null && response.length() > 0) {
			if (response.indexOf("errorCode") != -1 && response.indexOf("errorMessage") != -1) {
				result = Json.fromJson(ErrorResponse.class, response);
			} else {
				result = Json.fromJson(STResponse.class, response);
			}
		}
		return result;
	}

	protected String getValidationUrlName() {
		return "serviceValidate.pi";
	}

	/**
	 * 
	 * @param str
	 * @return Assertion
	 */
	protected final Assertion getAssertionBasedOnProxyGrantingTicketIou(final STResponse str) {
		if (CommonUtils.isNotBlank(str.getProxyGrantingTicket())) {
			return new AssertionImpl(new SimplePrincipal(str.getId(), str.getSiteId(), str.getMappingType()), null,
					this.proxyRetriever, this.proxyGrantingTicketStorage == null ? null
							: this.proxyGrantingTicketStorage.retrieve(str.getProxyGrantingTicket()));
		} else {
			return new AssertionImpl(new SimplePrincipal(str.getId(), str.getSiteId(), str.getMappingType()));
		}
	}

	/**
	 * 
	 * @param response
	 * @param str
	 * @return Assertion
	 * @throws ValidationException
	 */
	protected Assertion getValidAssertionInternal(final String response, final STResponse str)
			throws ValidationException {
		return getAssertionBasedOnProxyGrantingTicketIou(str);
	}

}
