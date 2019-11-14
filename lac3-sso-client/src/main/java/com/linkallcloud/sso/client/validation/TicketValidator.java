/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.client.validation.TicketValidator.java 
 *
 * 2011-6-14
 * 
 */
package com.linkallcloud.sso.client.validation;

import com.linkallcloud.core.principal.Assertion;
import com.linkallcloud.core.principal.Service;

/**
 * Interface to encapsulate the validation of a ticket. 
 * 
 * 2011-6-14
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public interface TicketValidator {

    /**
     * Method to validate a ticket for a give Service.
     * 
     * @param ticketId
     *            the ticket to validate
     * @param service
     *            the service to validate the ticket for
     * @return the Assertion about the ticket (never null)
     * @throws ValidationException
     *             if there is a problem validating the ticket.
     */
    Assertion validate(String ticketId, Service service) throws ValidationException;
}
