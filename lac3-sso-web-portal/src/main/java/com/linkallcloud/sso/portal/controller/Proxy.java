package com.linkallcloud.sso.portal.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.sso.exception.SiteException;
import com.linkallcloud.sso.exception.TicketException;
import com.linkallcloud.sso.oapi.dto.ProxyResult;
import com.linkallcloud.sso.ticket.ProxyGrantingTicket;
import com.linkallcloud.sso.ticket.ProxyTicket;
import com.linkallcloud.sso.ticket.cache.ProxyTicketCache;

@Controller
@RequestMapping
@Module(name = "代理")
public class Proxy extends BaseController {

	private static final String INVALID_REQUEST = "INVALID_REQUEST";
	private static final String BAD_PGT = "BAD_PGT";
	// private static final String INTERNAL_ERROR = "INTERNAL_ERROR";

	@Autowired
	private ProxyTicketCache ptCache;

	@ResponseBody
	@RequestMapping(value = "/proxy")
	public Object proxy(@RequestParam(value = "pgt", required = false) String pgtId,
			@RequestParam(value = "targetFrom", required = false) String targetAppCode,
			@RequestParam(value = "targetService", required = false) String targetService, HttpServletRequest request,
			HttpServletResponse response, Trace t) throws SiteException, TicketException {
		if (Strings.isBlank(pgtId) || Strings.isBlank(targetAppCode) || Strings.isBlank(targetService)) {
			return proxyFailure(INVALID_REQUEST,
					"'pgt', 'targetAppCode' and 'targetService' parameters are both required");
		} else {
			ProxyGrantingTicket pgt = (ProxyGrantingTicket) pgtCache.getTicket(pgtId);
			if (pgt == null)
				return proxyFailure(BAD_PGT, "unrecognized pgt: '" + pgtId + "'");
			else {
				ProxyTicket pt = new ProxyTicket(pgt, targetAppCode, targetService);
				String token = ptCache.addTicket(pt);
				return proxySuccess(token);
			}
		}
	}

	private ProxyResult proxySuccess(String token) {
		return new ProxyResult(token);
	}

	private ProxyResult proxyFailure(String errorCode, String errorMessage) {
		return new ProxyResult(errorCode, errorMessage);
	}

}
