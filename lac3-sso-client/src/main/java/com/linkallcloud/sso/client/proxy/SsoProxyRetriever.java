package com.linkallcloud.sso.client.proxy;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.alibaba.fastjson.JSON;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.principal.Service;
import com.linkallcloud.core.www.utils.HttpClientFactory;
import com.linkallcloud.sso.client.response.PgtResponse;
import com.linkallcloud.sso.client.util.CommonUtils;

/**
 * Implementation of a ProxyRetriever that follows the SSO specification. In
 * general, this class will make a call to the SSO server with some specified
 * parameters and receive an XML response to parse.
 */
public final class SsoProxyRetriever implements ProxyRetriever {
	protected final Log log = Logs.get();

	/**
	 * Url to SSO server.
	 */
	private final String ssoServerUrl;

	/**
	 * Main Constructor.
	 * 
	 * @param ssoServerUrl the URL to the SSO server (i.e. http://localhost/cas/)
	 */
	public SsoProxyRetriever(final String ssoServerUrl) {
		CommonUtils.assertNotNull(ssoServerUrl, "casServerUrl cannot be null.");
		this.ssoServerUrl = ssoServerUrl;
	}

	@Override
	public String getProxyTicketIdFor(final String proxyGrantingTicketId, final Service targetService) {

		final String url = constructUrl(proxyGrantingTicketId, targetService.getUrl(), targetService.getCode());
		String response = null;
		if (url.startsWith("https")) {
			response = HttpClientFactory.me(true).get(url);
		} else {
			response = HttpClientFactory.me(false).get(url);
		}

		// parse this response
		PgtResponse pgtResponse = getPgtResponse(response);
		if (pgtResponse != null && pgtResponse.getCode().equals("0")) {
			return pgtResponse.getProxyTicket();
		} else {
			log.error(
					"SSO server responded with error for request [" + url + "].  Full response was [" + response + "]");
			return null;
		}

	}

	/**
	 * 
	 * @param response
	 * @return PgtResponse
	 */
	private static PgtResponse getPgtResponse(String response) {
		return JSON.parseObject(response, PgtResponse.class);
	}

	/**
	 * 
	 * @param proxyGrantingTicketId
	 * @param targetService
	 * @return
	 */
	private String constructUrl(final String proxyGrantingTicketId, final String targetService,
			final String targetSiteCode) {
		try {
			return this.ssoServerUrl + "/proxy" + "?pgt=" + proxyGrantingTicketId + "&targetFrom=" + targetSiteCode
					+ "&targetService=" + URLEncoder.encode(targetService, "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
