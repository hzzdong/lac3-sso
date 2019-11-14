/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.response.PTResponse.java 
 *
 * 2011-5-22
 * 
 */
package com.linkallcloud.sso.client.response;

import java.util.ArrayList;
import java.util.List;

import com.linkallcloud.core.principal.SimpleService;

/**
 * 2011-5-22
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public class PTResponse extends STResponse {
    private static final long serialVersionUID = -2616748978026727039L;

    private List<SimpleService> proxies;

    /**
     * 
     */
    public PTResponse() {
        super();
    }

    /**
     * 
     * @param ssoUser
     * @param siteUser
     * @param mapingType
     */
    public PTResponse(String ssoUser, String siteUser, int mapingType) {
        super(ssoUser, siteUser, mapingType);
    }

    /**
     * @return the proxies
     */
    public List<SimpleService> getProxies() {
        return proxies;
    }

    /**
     * @param proxies
     *            the proxies to set
     */
    public void setProxies(List<SimpleService> proxies) {
        this.proxies = proxies;
    }

    public void addAll(List<SimpleService> p) {
        if (p != null && !p.isEmpty()) {
            proxies = new ArrayList<SimpleService>();
            proxies.addAll(p);
        }
    }

}
