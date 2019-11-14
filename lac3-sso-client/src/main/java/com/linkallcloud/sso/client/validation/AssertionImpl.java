/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.client.validation.AssertionImpl.java 
 *
 * 2011-6-14
 * 
 */
package com.linkallcloud.sso.client.validation;

import java.util.HashMap;
import java.util.Map;

import com.linkallcloud.core.principal.Assertion;
import com.linkallcloud.core.principal.Principal;
import com.linkallcloud.core.principal.Service;
import com.linkallcloud.sso.client.proxy.ProxyRetriever;
import com.linkallcloud.sso.client.util.CommonUtils;

/**
 * Concrete implementation of an Assertion.
 * 
 * 2011-6-14
 * 
 */
public class AssertionImpl implements Assertion {
    private static final long serialVersionUID = -5150115981033901283L;

    /**
     * Map of the attributes returned by the SSO server. This is optional as the SSO server spec makes no mention of
     * attributes.
     */
    private final Map<String, Object> attributes;

    /**
     * The principal who was authenticated.
     */
    private final Principal principal;

    /**
     * The Proxy Granting Ticket Id returned by the server.
     */
    private final String proxyGrantingTicketId;

    /**
     * Reference to ProxyRetriever so that clients can retrieve proxy tickets for a service.
     */
    private final ProxyRetriever proxyRetriever;

    /**
     * Simple constructor that accepts a Principal.
     * 
     * @param principal
     *            the Principal this assertion is for.
     */
    public AssertionImpl(final Principal principal) {
        this(principal, null, null, null);
    }

    /**
     * Constructor that accepts a Principal and a map of attributes.
     * 
     * @param principal
     *            the Principal this assertion is for.
     * @param attributes
     *            a map of attributes about the principal.
     */
    public AssertionImpl(final Principal principal, final Map<String, Object> attributes) {
        this(principal, attributes, null, null);
    }

    /**
     * @param principal
     *            the Principal this assertion is for.
     * @param attributes
     *            a map of attributes about the principal.
     * @param proxyRetriever
     *            used to retrieve proxy tickets from SSO Server.
     * @param proxyGrantingTicketId
     *            the Id to use to request proxy tickets.
     */
    public AssertionImpl(final Principal principal, final Map<String, Object> attributes,
            final ProxyRetriever proxyRetriever, final String proxyGrantingTicketId) {
        CommonUtils.assertNotNull(principal, "principal cannot be null");

        this.principal = principal;
        this.attributes = attributes == null ? new HashMap<String, Object>() : attributes;
        this.proxyGrantingTicketId = CommonUtils.isNotEmpty(proxyGrantingTicketId) ? proxyGrantingTicketId : null;
        this.proxyRetriever = proxyRetriever;
    }

    public final Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public String getProxyTicketFor(final Service service) {
        if (proxyRetriever == null || proxyGrantingTicketId == null) {
            return null;
        }

        return this.proxyRetriever.getProxyTicketIdFor(this.proxyGrantingTicketId, service);
    }

    public final Principal getPrincipal() {
        return this.principal;
    }
}
