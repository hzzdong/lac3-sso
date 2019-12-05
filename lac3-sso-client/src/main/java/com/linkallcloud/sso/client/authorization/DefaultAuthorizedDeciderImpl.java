package com.linkallcloud.sso.client.authorization;

import java.util.List;

import com.linkallcloud.core.principal.Principal;
import com.linkallcloud.sso.client.util.CommonUtils;

/**
 * Default implementation of the AuthorizedDecider that delegates to a list to check if someone is authorized.
 * 
 */
public final class DefaultAuthorizedDeciderImpl implements AuthorizedDecider {

    /**
     * The list of users authorized to use the system.
     */
    private final List<String> users;

    /**
     * Constructor that takes the list of acceptable users as its parameters.
     * 
     * @param users
     *            the list of acceptable users.
     */
    public DefaultAuthorizedDeciderImpl(final List<String> users) {
        CommonUtils.assertNotEmpty(users, "users cannot be empty.");
        this.users = users;
    }

    public boolean isAuthorizedToUseApplication(final Principal principal) {
        return this.users.contains(principal.getId());
    }
}
