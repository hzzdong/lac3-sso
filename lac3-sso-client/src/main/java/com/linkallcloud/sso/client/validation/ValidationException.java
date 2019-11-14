/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.client.validation.ValidationException.java 
 *
 * 2011-6-14
 * 
 */
package com.linkallcloud.sso.client.validation;

/**
 * Implementation of Exception to be thrown when there is an error validating the Ticket returned from the SSO server.
 * 
 * 2011-6-14
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public class ValidationException extends Exception {
    private static final long serialVersionUID = 3543115702310234640L;

    /**
     * Default constructor.
     */
    public ValidationException() {
        super();
    }

    /**
     * Constructor that accepts a message and a chained exception.
     * 
     * @param message
     *            the error message.
     * @param cause
     *            the exception we are chaining with.
     */
    public ValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor that accepts a message.
     * 
     * @param message
     *            the error message.
     */
    public ValidationException(final String message) {
        super(message);
    }

    /**
     * Constructor that accepts a chained exception.
     * 
     * @param cause
     *            the exception we are chaining with.
     */
    public ValidationException(final Throwable cause) {
        super(cause);
    }
}
