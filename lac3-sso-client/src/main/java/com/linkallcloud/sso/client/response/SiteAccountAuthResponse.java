/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.response.SiteAccountAuthResponse.java 
 *
 * 2011-5-30
 * 
 */
package com.linkallcloud.sso.client.response;

/**
 * 2011-5-30
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public class SiteAccountAuthResponse extends ErrorResponse {

    private boolean success = false;

    /**
     * 
     */
    public SiteAccountAuthResponse() {
        success = true;
    }

    /**
     * @param errorCode
     * @param errorMessage
     */
    public SiteAccountAuthResponse(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
        success = false;
    }

    /**
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @param success
     *            the success to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

}
