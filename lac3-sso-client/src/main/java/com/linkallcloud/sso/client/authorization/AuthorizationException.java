/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.client.authorization.AuthorizationException.java 
 *
 * 2011-6-15
 * 
 */
package com.linkallcloud.sso.client.authorization;

/**
 * Exception to be thrown if the user is not authorized to use the system.
 * 
 * 2011-6-15
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public final class AuthorizationException extends RuntimeException {
    private static final long serialVersionUID = 5912038088650643442L;

    public AuthorizationException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public AuthorizationException(String arg0) {
        super(arg0);
    }

    public AuthorizationException(Throwable arg0) {
        super(arg0);
    }
}
