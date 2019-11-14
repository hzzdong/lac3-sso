/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.response.STResponse.java 
 *
 * 2011-5-22
 * 
 */
package com.linkallcloud.sso.client.response;

import com.linkallcloud.core.principal.Principal;

/**
 * 2011-5-22
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public class STResponse extends ServiceResponse implements Principal {
    private static final long serialVersionUID = 7820646920539862639L;

    /** The unique identifier for the principal. */
    private String id;

    /** The unique identifier for the site account loginname. */
    private String siteId;

    /** The account mapping type. */
    private int mappingType;

    private String proxyGrantingTicket;

    /**
     * 
     */
    public STResponse() {
        super();
    }

    /**
     * 
     * @param user
     */
    public STResponse(String ssoUser, String siteUser, int mapingType) {
        super();
        this.id = ssoUser;
        this.siteId = siteUser;
        this.mappingType = mapingType;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the siteId
     */
    public String getSiteId() {
        return siteId;
    }

    /**
     * @param siteId
     *            the siteId to set
     */
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return the mappingType
     */
    public int getMappingType() {
        return mappingType;
    }

    /**
     * @param mappingType
     *            the mappingType to set
     */
    public void setMappingType(int mappingType) {
        this.mappingType = mappingType;
    }

    /**
     * @return the proxyGrantingTicket
     */
    public String getProxyGrantingTicket() {
        return proxyGrantingTicket;
    }

    /**
     * @param proxyGrantingTicket
     *            the proxyGrantingTicket to set
     */
    public void setProxyGrantingTicket(String proxyGrantingTicket) {
        this.proxyGrantingTicket = proxyGrantingTicket;
    }

}
