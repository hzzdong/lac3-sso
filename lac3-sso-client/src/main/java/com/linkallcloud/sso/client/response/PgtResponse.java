/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.response.PgtResponse.java 
 *
 * 2011-5-22
 * 
 */
package com.linkallcloud.sso.client.response;

/**
 * 2011-5-22
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public class PgtResponse extends ServiceResponse {

    private String proxyTicket;

    /**
     * 
     */
    public PgtResponse() {
        super();
    }

    /**
     * 
     * @param proxyTicket
     */
    public PgtResponse(String proxyTicket) {
        super();
        this.proxyTicket = proxyTicket;
    }

    /**
     * @return the proxyTicket
     */
    public String getProxyTicket() {
        return proxyTicket;
    }

    /**
     * @param proxyTicket
     *            the proxyTicket to set
     */
    public void setProxyTicket(String proxyTicket) {
        this.proxyTicket = proxyTicket;
    }

}
