/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.response.ErrorServiceResponse.java 
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
public class ErrorResponse extends ServiceResponse {

    private String errorCode;
    private String errorMessage;

    /**
     * 
     */
    public ErrorResponse() {
    }

    /**
     * 
     * @param errorCode
     * @param errorMessage
     */
    public ErrorResponse(String errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * @return the errorCode
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode
     *            the errorCode to set
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage
     *            the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
