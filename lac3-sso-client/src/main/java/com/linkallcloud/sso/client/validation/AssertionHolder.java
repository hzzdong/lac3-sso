/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.client.validation.AssertionHolder.java 
 *
 * 2011-6-14
 * 
 */
package com.linkallcloud.sso.client.validation;

import com.linkallcloud.core.principal.Assertion;

/**
 * Static holder that places Assertion in a threadlocal.
 * 
 * 2011-6-14
 *
 */
public class AssertionHolder {

    /**
     * ThreadLocal to hold the Assertion for Threads to access.
     */
    private static final ThreadLocal<Assertion> threadLocal = new ThreadLocal<Assertion>();

    /**
     * Retrieve the assertion from the ThreadLocal.
     */
    public static Assertion getAssertion() {
        return (Assertion) threadLocal.get();
    }

    /**
     * Add the Assertion to the ThreadLocal.
     */
    public static void setAssertion(final Assertion assertion) {
        threadLocal.set(assertion);
    }

    /**
     * Clear the ThreadLocal.
     */
    public static void clear() {
        threadLocal.set(null);
    }
}
