/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.client.proxy.ProxyRetriever.java 
 *
 * 2011-6-15
 * 
 */
package com.linkallcloud.sso.client.proxy;

import com.linkallcloud.core.principal.Service;

/**
 * Interface to abstract the retrieval of a proxy ticket to make the implementation a black box to the client.
 * 
 * 2011-6-15
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public interface ProxyRetriever {

    /**
     * Retrieves a proxy ticket for a specific targetService.
     * 
     * @param proxyGrantingTicketId
     *            the ProxyGrantingTicketId
     * @param targetService
     *            the service we want to proxy.
     * @return the ProxyTicket Id if Granted, null otherwise.
     */
    String getProxyTicketIdFor(String proxyGrantingTicketId, Service targetService);
}
