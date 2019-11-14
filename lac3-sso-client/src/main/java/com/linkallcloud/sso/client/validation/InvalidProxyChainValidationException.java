/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.client.validation.InvalidProxyChainValidationException.java 
 *
 * 2011-6-14
 * 
 */
package com.linkallcloud.sso.client.validation;

/**
 * Specific instance of a ValidationException that is thrown when the proxy chain does not match what is returned.
 * 
 * 2011-6-14
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public final class InvalidProxyChainValidationException extends ValidationException {
    private static final long serialVersionUID = -6729350057007618506L;

    public InvalidProxyChainValidationException() {
        super();
    }

    public InvalidProxyChainValidationException(String message) {
        super(message);
    }
}
