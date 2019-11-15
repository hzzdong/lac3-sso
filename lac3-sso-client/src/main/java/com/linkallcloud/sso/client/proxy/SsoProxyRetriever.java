/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.client.proxy.SsoProxyRetriever.java 
 *
 * 2011-6-14
 * 
 */
package com.linkallcloud.sso.client.proxy;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.linkallcloud.core.json.Json;
import com.linkallcloud.core.principal.Service;
import com.linkallcloud.core.www.utils.HttpClientFactory;
import com.linkallcloud.sso.client.response.PgtResponse;
import com.linkallcloud.sso.client.util.CommonUtils;

/**
 * Implementation of a ProxyRetriever that follows the SSO specification. In
 * general, this class will make a call to the SSO server with some specified
 * parameters and receive an XML response to parse.
 * 
 * 2011-6-14
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public final class SsoProxyRetriever implements ProxyRetriever {

	/**
	 * Instance of Commons Logging.
	 */
	protected final Log log = LogFactory.getLog(this.getClass());

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.zj.pubinfo.sso.client.proxy.ProxyRetriever#getProxyTicketIdFor(java.lang.
	 * String, cn.zj.pubinfo.principal.Service, cn.zj.pubinfo.principal.Service)
	 */
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
		if (pgtResponse != null) {
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
		if (response != null && response.length() > 0) {
			if (response.indexOf("errorCode") == -1) {
				return Json.fromJson(PgtResponse.class, response);
			}
		}
		return null;
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
			return this.ssoServerUrl + "proxy.pi" + "?pgt=" + proxyGrantingTicketId + "&targetFrom=" + targetSiteCode
					+ "&targetService=" + URLEncoder.encode(targetService, "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
