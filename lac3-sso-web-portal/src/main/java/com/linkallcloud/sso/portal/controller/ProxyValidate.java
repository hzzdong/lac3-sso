package com.linkallcloud.sso.portal.controller;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.sso.oapi.dto.ProxyAuthenticationResult;
import com.linkallcloud.sso.portal.exception.SiteException;
import com.linkallcloud.sso.portal.exception.TicketException;
import com.linkallcloud.sso.portal.ticket.ProxyTicket;
import com.linkallcloud.sso.portal.ticket.cache.ProxyTicketCache;

public class ProxyValidate extends BaseController {

	// failure codes
	private static final String INVALID_REQUEST = "INVALID_REQUEST";
	private static final String INVALID_TICKET = "INVALID_TICKET";
	private static final String INVALID_SERVICE = "INVALID_SERVICE";
	// private static final String INTERNAL_ERROR = "INTERNAL_ERROR";

	@Autowired
	private ProxyTicketCache ptCache;

	@ResponseBody
	@RequestMapping(value = "/proxyValidate")
	public Object proxyValidate(@RequestParam(value = "from", required = false) String appCode,
			@RequestParam(value = "service", required = false) String appUrl,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "pgtUrl", required = false) String pgtUrl,
			@RequestParam(value = "renew", required = false) String renew, HttpServletRequest request,
			HttpServletResponse response, Trace t) throws SiteException, TicketException {
		if (Strings.isBlank(appCode) || Strings.isBlank(appUrl) || Strings.isBlank(ticket)) {
			return validationFailure(INVALID_REQUEST, "'from' , 'service' and 'ticket' parameters are both required");
		} else if (!ticket.startsWith("PT")) {
			return validationFailure(INVALID_TICKET, "ticket not match this request.");
		} else {
			ProxyTicket pt = ptCache.getTicket(ticket);
			if (pt == null) {
				return validationFailure(INVALID_TICKET, "ticket '" + ticket + "' not recognized");
			} else if (!pt.getAppServiceUrl().equals(appUrl) || !pt.getAppCode().equals(appCode)) {
				return validationFailure(INVALID_SERVICE, "ticket '" + ticket + "' does not match supplied service");
			} else if ("true".equals(renew) && !pt.isFromNewLogin()) {
				return validationFailure(INVALID_TICKET, "ticket not backed by initial SSO login, as requested");
			} else {
				String pgtIOU = null;
				if (!Strings.isBlank(pgtUrl)) {
					pgtIOU = sendPgt(pt, pgtUrl);
				}
				return validationSuccess(pt, pgtIOU);
			}
		}
	}

	private ProxyAuthenticationResult validationSuccess(ProxyTicket pt, String pgtIOU) {
		ProxyAuthenticationResult result = new ProxyAuthenticationResult(pt.getUsername(), pt.getSiteUser(),
				pt.getSiteMaping());
		if (!Strings.isBlank(pgtIOU)) {
			result.setProxyGrantingTicket(pgtIOU);
		}
		Iterator<String> proxies = pt.getProxies().iterator();
		while (proxies.hasNext()) {
			result.addProxy(proxies.next());
		}

		return result;
	}

	private ProxyAuthenticationResult validationFailure(String errorCode, String errorMessage) {
		return new ProxyAuthenticationResult(errorCode, errorMessage);
	}

}
