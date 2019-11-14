/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.request.SiteAccountAuthRequest.java 
 *
 * 2011-5-30
 * 
 */
package com.linkallcloud.sso.client.request;

/**
 * 2011-5-30
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public class SiteAccountAuthRequest {
    private String siteCode;
    private String loginName;
    private String password;

    /**
     * 
     */
    public SiteAccountAuthRequest() {
        super();
    }

    /**
     * 
     */
    public SiteAccountAuthRequest(String siteCode, String loginName, String password) {
        super();
        this.siteCode = siteCode;
        this.loginName = loginName;
        this.password = password;
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
     * @return the loginName
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * @param loginName
     *            the loginName to set
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
