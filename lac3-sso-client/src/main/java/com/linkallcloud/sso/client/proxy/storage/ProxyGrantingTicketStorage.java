/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.client.proxy.ProxyGrantingTicketStorage.java 
 *
 * 2011-6-15
 * 
 */
package com.linkallcloud.sso.client.proxy.storage;

/**
 * Interface for the storage and retrieval of ProxyGrantingTicketIds by mapping them to a specific
 * ProxyGrantingTicketIou.
 * 
 * 2011-6-15
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public interface ProxyGrantingTicketStorage {

    /**
     * Method to save the ProxyGrantingTicket to the backing storage facility.
     * 
     * @param proxyGrantingTicketIou
     *            used as the key
     * @param proxyGrantingTicket
     *            used as the value
     */
    public void save(String proxyGrantingTicketIou, String proxyGrantingTicket);

    /**
     * Method to retrieve a ProxyGrantingTicket based on the ProxyGrantingTicketIou. Note that implementations are not
     * guaranteed to return the same result if retrieve is called twice with the same proxyGrantingTicketIou.
     * 
     * @param proxyGrantingTicketIou
     *            used as the key
     * @return the ProxyGrantingTicket Id or null if it can't be found
     */
    public String retrieve(String proxyGrantingTicketIou);
}
