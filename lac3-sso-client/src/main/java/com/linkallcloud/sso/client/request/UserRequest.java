/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.request.UserRequest.java 
 *
 * 2011-5-25
 * 
 */
package com.linkallcloud.sso.client.request;

/**
 * 2011-5-25
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public class UserRequest {

    private String siteCode;
    private String ssoLoginName;

    /**
     * 
     */
    public UserRequest() {
        super();
    }

    /**
     * 
     */
    public UserRequest(String siteCode, String ssoLoginName) {
        super();
        this.siteCode = siteCode;
        this.ssoLoginName = ssoLoginName;
    }

    /**
     * @return the siteCode
     */
    public String getSiteCode() {
        return siteCode;
    }

    /**
     * @param siteCode
     *            the siteCode to set
     */
    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    /**
     * @return the ssoLoginName
     */
    public String getSsoLoginName() {
        return ssoLoginName;
    }

    /**
     * @param ssoLoginName
     *            the ssoLoginName to set
     */
    public void setSsoLoginName(String ssoLoginName) {
        this.ssoLoginName = ssoLoginName;
    }

}
