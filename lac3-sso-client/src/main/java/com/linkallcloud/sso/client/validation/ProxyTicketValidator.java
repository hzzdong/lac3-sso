/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.client.validation.ProxyTicketValidator.java 
 *
 * 2011-6-14
 * 
 */
package com.linkallcloud.sso.client.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.linkallcloud.core.principal.Assertion;
import com.linkallcloud.core.principal.Service;
import com.linkallcloud.core.principal.SimpleService;
import com.linkallcloud.sso.client.proxy.ProxyGrantingTicketStorage;
import com.linkallcloud.sso.client.proxy.ProxyRetriever;
import com.linkallcloud.sso.client.util.CommonUtils;
import com.linkallcloud.sso.oapi.dto.ProxyAuthenticationResult;
import com.linkallcloud.sso.oapi.dto.ServiceAuthenticationResult;

/**
 * Implementation of the TicketValidator interface that knows how to handle
 * proxy tickets.
 * <p/>
 * In your XML configuration file, proxy chains should be defined as follows:
 * &lt;list&gt; &lt;value&gt; proxy1,code1 proxy2,code2
 * proxy3,code3&lt;/value&gt; &lt;value&gt; proxy2,code2 proxy4,code4
 * proxy5,code5&lt;/value&gt; &lt;value&gt; proxy4,code4 proxy5,code5
 * proxy6,code6&lt;/value&gt; &lt;/list&gt;
 * 
 * 2011-6-14
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public class ProxyTicketValidator extends ServiceTicketValidator {

	/* List of proxy chains that we accept. */
	private final List<Service[]> proxyChains;

	/**
	 * Boolean whether we accept any proxy request or not.
	 */
	private final boolean acceptAnyProxy;

	/**
	 * @param ssoServerUrl   the url to the SSO server, minus the endpoint.
	 * @param renew          flag for whether we require authentication to be via an
	 *                       initial authentication.
	 * @param proxyChains    the chains of proxy lists that we accept tickets from.
	 * @param acceptAnyProxy flag on whether we accept any proxy or not.
	 */
	public ProxyTicketValidator(final String ssoServerUrl, final boolean renew, List<String> proxyChains,
			boolean acceptAnyProxy) {
		this(ssoServerUrl, renew, null, proxyChains, acceptAnyProxy, null, null);
	}

	/**
	 * 
	 * @param ssoServerUrl
	 * @param renew
	 * @param proxyCallbackUrl
	 * @param proxyChains
	 * @param acceptAnyProxy
	 * @param proxyGrantingTicketStorage
	 * @param proxyRetriever
	 */
	public ProxyTicketValidator(final String ssoServerUrl, final boolean renew, final Service proxyCallbackUrl,
			List<String> proxyChains, boolean acceptAnyProxy,
			final ProxyGrantingTicketStorage proxyGrantingTicketStorage, final ProxyRetriever proxyRetriever) {
		super(ssoServerUrl, renew, proxyCallbackUrl, proxyGrantingTicketStorage, proxyRetriever);

		CommonUtils.assertTrue(proxyChains != null || acceptAnyProxy,
				"proxyChains cannot be null or acceptAnyProxy must be true.");
		CommonUtils.assertTrue((proxyChains != null && !proxyChains.isEmpty()) || acceptAnyProxy,
				"proxyChains cannot be empty or acceptAnyProxy must be true.");

		// assume each proxy chain has a length of 2
		final List<Service[]> tempProxyChains = new ArrayList<Service[]>(proxyChains.size() * 2);
		for (final Iterator<String> iter = proxyChains.iterator(); iter.hasNext();) {
			final String[] values = (iter.next()).split(" ");
			final Service[] principals = new Service[values.length];

			for (int i = 0; i < principals.length; i++) {
				String[] servicePrarms = values[i].split(",");
				CommonUtils.assertTrue(servicePrarms != null && servicePrarms.length == 2,
						"proxyChains configirue format error.");
				principals[i] = new SimpleService(servicePrarms[0], servicePrarms[1]);
			}

			tempProxyChains.add(principals);
		}

		this.proxyChains = tempProxyChains;
		this.acceptAnyProxy = acceptAnyProxy;
	}

	@Override
	protected String getValidationUrlName() {
		return "/proxyValidate";
	}
	
	@Override
	protected Assertion getValidAssertionInternal(final String response, final ServiceAuthenticationResult str)
			throws ValidationException {
		if (str != null && str instanceof ProxyAuthenticationResult) {
			ProxyAuthenticationResult ptr = (ProxyAuthenticationResult) str;

			final List<SimpleService> proxies = ptr.getProxies();
			// this means there was nothing in the proxy chain, which is okay
			if (proxies == null || proxies.isEmpty() || this.acceptAnyProxy) {
				return getAssertionBasedOnProxyGrantingTicketIou(str);
			}

			final Service[] principals = new Service[proxies.size()];
			int i = 0;
			for (final Iterator<SimpleService> iter = proxies.iterator(); iter.hasNext();) {
				principals[i++] = iter.next();
			}

			for (Iterator<Service[]> iter = this.proxyChains.iterator(); iter.hasNext();) {
				if (Arrays.equals(principals, iter.next())) {
					return getAssertionBasedOnProxyGrantingTicketIou(str);
				}
			}
		}
		throw new InvalidProxyChainValidationException();
	}

	@Override
	protected ServiceAuthenticationResult parseResult(String response) {
		return JSON.parseObject(response, ProxyAuthenticationResult.class);
	}

}
