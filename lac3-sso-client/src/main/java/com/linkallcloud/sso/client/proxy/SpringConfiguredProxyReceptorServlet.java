/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.client.proxy.SpringConfiguredProxyReceptorServlet.java 
 *
 * 2011-6-14
 * 
 */
package com.linkallcloud.sso.client.proxy;

import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Implementation of an HttpServlet that accepts ProxyGrantingTicketIous and ProxyGrantingTickets and stores them in an
 * implementation of {@link ProxyGrantingTicketStorage}.
 * <p/>
 * Note that <code>ProxyReceptorServlet</code> attempts to load a {@link ProxyGrantingTicketStorage} from the
 * ApplicationContext either via the name "proxyGrantingTicketStorage" or by type. One of these two must exist within
 * the applicationContext or the initialization of the ProxyReceptorServlet will fail.
 * 
 * 2011-6-14
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public final class SpringConfiguredProxyReceptorServlet extends AbstractProxyReceptorServlet {
    private static final long serialVersionUID = -5642050740265266568L;

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.zj.pubinfo.sso.client.proxy.AbstractProxyReceptorServlet#retrieveProxyGrantingTicketStorageFromConfiguration
     * (javax.servlet.ServletConfig)
     */
    @Override
    protected ProxyGrantingTicketStorage retrieveProxyGrantingTicketStorageFromConfiguration(
            final ServletConfig servletConfig) throws ServletException {
        final WebApplicationContext context =
                WebApplicationContextUtils.getRequiredWebApplicationContext(servletConfig.getServletContext());

        if (context.containsBean(CONST_PROXY_GRANTING_TICKET_STORAGE_BEAN_NAME)) {
            return (ProxyGrantingTicketStorage) context.getBean(CONST_PROXY_GRANTING_TICKET_STORAGE_BEAN_NAME,
                    ProxyGrantingTicketStorage.class);
        }

        final Map<?, ?> map = context.getBeansOfType(ProxyGrantingTicketStorage.class);

        if (map.isEmpty()) {
            throw new ServletException("No ProxyGrantingTicketStorage found!");
        }

        if (map.size() > 1) {
            throw new ServletException("Expecting one ProxyGrantingTicketStorage and found multiple instances.");
        }

        return (ProxyGrantingTicketStorage) map.get(map.keySet().iterator().next());
    }
}
