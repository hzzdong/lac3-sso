/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.client.authorization.AuthorizedDecider.java 
 *
 * 2011-6-14
 * 
 */
package com.linkallcloud.sso.client.authorization;

import com.linkallcloud.core.principal.Principal;

/**
 * Simple interface for determining whether a Principal is authorized to use the application or not.
 * 
 * 2011-6-14
 * 
 */
public interface AuthorizedDecider {

    /**
     * Determines whether someone can use the system or not.
     * 
     * @param principal
     *            the person we are checking
     * @return true if authorized, false otherwise.
     */
    boolean isAuthorizedToUseApplication(final Principal principal);
}
