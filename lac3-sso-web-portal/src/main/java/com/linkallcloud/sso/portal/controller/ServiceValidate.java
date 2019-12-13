package com.linkallcloud.sso.portal.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.sso.exception.SiteException;
import com.linkallcloud.sso.exception.TicketException;
import com.linkallcloud.sso.oapi.dto.ServiceAuthenticationResult;
import com.linkallcloud.sso.ticket.ProxyGrantingTicket;
import com.linkallcloud.sso.ticket.ServiceTicket;
import com.linkallcloud.sso.ticket.TicketBox;
import com.linkallcloud.sso.ticket.cache.ServiceTicketCache;

/**
 * Handles ST validation and PGT acquisition.
 */
@Controller
@RequestMapping
public class ServiceValidate extends BaseController {

	// failure codes
	private static final String INVALID_REQUEST = "INVALID_REQUEST";
	private static final String INVALID_TICKET = "INVALID_TICKET";
	private static final String INVALID_SERVICE = "INVALID_SERVICE";
	// private static final String INTERNAL_ERROR = "INTERNAL_ERROR";

	@Autowired
	private ServiceTicketCache stCache;

	@ResponseBody
	@RequestMapping(value = "/serviceValidate")
	public Object serviceValidate(@RequestParam(value = "from", required = false) String appCode,
			@RequestParam(value = "service", required = false) String appUrl,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "pgtUrl", required = false) String pgtUrl,
			@RequestParam(value = "proxyCode", required = false) String pgtAppCode,
			@RequestParam(value = "renew", required = false) String renew, HttpServletRequest request,
			HttpServletResponse response, Trace t) throws SiteException, TicketException {
		if (Strings.isBlank(appCode) || Strings.isBlank(appUrl) || Strings.isBlank(ticket)) {
			return validationFailure(INVALID_REQUEST, "'from' , 'service' and 'ticket' parameters are both required");
		} else if (!ticket.startsWith("ST")) {
			return validationFailure(INVALID_TICKET, "ticket not match this request.");
		} else {
			ServiceTicket st = stCache.getTicket(ticket);
			if (st == null) {
				return validationFailure(INVALID_TICKET, "ticket '" + ticket + "' not recognized");
			} else if (!st.getAppServiceUrl().equals(appUrl) || !st.getAppCode().equals(appCode)) {
				return validationFailure(INVALID_SERVICE, "ticket '" + ticket + "' does not match supplied service");
			} else if ("true".equals(renew) && !st.isFromNewLogin()) {
				return validationFailure(INVALID_TICKET, "ticket not backed by initial SSO login, as requested");
			} else {
				TicketBox<ProxyGrantingTicket> pgtIOU = null;
				if (!Strings.isBlank(pgtUrl) && !Strings.isBlank(pgtAppCode)) {
					try {
						checkSiteCanPass(t, pgtAppCode, pgtUrl);
					} catch (SiteException e) {
						return validationFailure(INVALID_SERVICE, String.format("代理服务(%,%)未许可", pgtAppCode, pgtUrl));
					}
					pgtIOU = sendPgt(st, pgtUrl, pgtAppCode);
				}

				return validationSuccess(t, st, appCode, appUrl, pgtIOU, pgtAppCode, pgtUrl);
			}
		}
	}

	private ServiceAuthenticationResult validationSuccess(Trace t, ServiceTicket st, String appCode, String appUrl,
			TicketBox<ProxyGrantingTicket> pgtIOU, String pgtAppCode, String pgtUrl) {
		// app login log
		appLoginAuthSuccess(t, st, appCode, appUrl, pgtIOU, pgtAppCode, pgtUrl);

		ServiceAuthenticationResult result = new ServiceAuthenticationResult(st.getUsername(), st.getSiteUser(),
				st.getSiteMaping());
		if (pgtIOU != null && !Strings.isBlank(pgtIOU.getId())) {
			result.setProxyGrantingTicket(pgtIOU.getId());
		}
		return result;
	}

	private ServiceAuthenticationResult validationFailure(String errorCode, String errorMessage) {
		return new ServiceAuthenticationResult(errorCode, errorMessage);
	}

}
