/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.client.proxy.AbstractProxyReceptorServlet.java 
 *
 * 2011-6-15
 * 
 */
package com.linkallcloud.sso.client.proxy;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.linkallcloud.sso.client.proxy.storage.ProxyGrantingTicketStorage;
import com.linkallcloud.sso.client.util.CommonUtils;

/**
 * Implementation of an HttpServlet that accepts ProxyGrantingTicketIous and ProxyGrantingTickets and stores them in an
 * implementation of {@link ProxyGrantingTicketStorage}.
 * 
 * 2011-6-15
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public abstract class AbstractProxyReceptorServlet extends HttpServlet {
    private static final long serialVersionUID = 3460736263410452108L;

    protected final Log logger = LogFactory.getLog(this.getClass());

    /**
     * The name we expect the instance of ProxyGrantingTicketStorage to be instanciated under in the applicationContext.
     */
    public static final String CONST_PROXY_GRANTING_TICKET_STORAGE_BEAN_NAME = "proxyGrantingTicketStorage";

    /**
     * Constant representing the ProxyGrantingTicket IOU Request Parameter.
     */
    private static final String PARAM_PROXY_GRANTING_TICKET_IOU = "pgtIou";

    /**
     * Constant representing the ProxyGrantingTicket Request Parameter.
     */
    private static final String PARAM_PROXY_GRANTING_TICKET = "pgtId";

    /**
     * Instance of ProxyGrantingTicketStorage to store ProxyGrantingTickets.
     */
    private ProxyGrantingTicketStorage proxyGrantingTicketStorage;

    protected final void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final String proxyGrantingTicketIou = request.getParameter(PARAM_PROXY_GRANTING_TICKET_IOU);
        final String proxyGrantingTicket = request.getParameter(PARAM_PROXY_GRANTING_TICKET);

        if (CommonUtils.isBlank(proxyGrantingTicket) || CommonUtils.isBlank(proxyGrantingTicketIou)) {
            response.getWriter().write("");
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Received proxyGrantingTicketId [" + proxyGrantingTicket + "] for proxyGrantingTicketIou ["
                    + proxyGrantingTicketIou + "]");
        }

        this.proxyGrantingTicketStorage.save(proxyGrantingTicketIou, proxyGrantingTicket);

        response.getWriter().write("{proxySuccess:\"true\"}");
    }

    public final void init(final ServletConfig servletConfig) throws ServletException {
        this.proxyGrantingTicketStorage = retrieveProxyGrantingTicketStorageFromConfiguration(servletConfig);
    }

    protected abstract ProxyGrantingTicketStorage retrieveProxyGrantingTicketStorageFromConfiguration(
            final ServletConfig servletConfig) throws ServletException;
}
