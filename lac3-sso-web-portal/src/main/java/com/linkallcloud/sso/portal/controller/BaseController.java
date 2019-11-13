package com.linkallcloud.sso.portal.controller;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.www.utils.HttpClientFactory;
import com.linkallcloud.core.www.utils.WebUtils;
import com.linkallcloud.sso.exception.AppException;
import com.linkallcloud.sso.exception.ArgException;
import com.linkallcloud.sso.portal.exception.SiteException;
import com.linkallcloud.sso.portal.exception.TicketException;
import com.linkallcloud.sso.portal.kiss.um.ApplicationKiss;
import com.linkallcloud.sso.portal.ticket.ActiveTicket;
import com.linkallcloud.sso.portal.ticket.ProxyGrantingTicket;
import com.linkallcloud.sso.portal.ticket.TicketGrantingTicket;
import com.linkallcloud.sso.portal.ticket.cache.ProxyGrantingTicketCache;
import com.linkallcloud.sso.portal.ticket.cache.TicketGrantingTicketCache;
import com.linkallcloud.sso.portal.utils.IParams;
import com.linkallcloud.sso.portal.utils.Util;
import com.linkallcloud.um.domain.sys.Application;

public abstract class BaseController {
	private static final Log log = Logs.get();

	// PGT IOU length
	private static final int PGT_IOU_LENGTH = 50;

	@Value("${lac.sso.mode}")
	protected String ssoMode;

	@Autowired
	protected TicketGrantingTicketCache tgcCache;

	@Autowired
	private ProxyGrantingTicketCache pgtCache;

	@Autowired
	protected ApplicationKiss applicationKiss;

	/**
	 * avoid caching (in the stupidly numerous ways we must)
	 * 
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	public void setCharacterEncoding(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
	}

	/**
	 * Is site valid.
	 * 
	 * @param t
	 * @param appCode
	 * @param appUrl
	 * @return boolean
	 * @throws SiteException
	 */
	protected void checkSitePass(Trace t, String appCode, String appUrl) throws SiteException {
		if (Strings.isBlank(appCode) && Strings.isBlank(appUrl)) {// 都为空，直接登录SSO方式
			return;
		} else if (!Strings.isBlank(appCode) && !Strings.isBlank(appUrl)) {// 都不为空
			Application app = applicationKiss.fetchByCode(t, appCode);
			if (null != app && app.isValid() && !Strings.isBlank(app.getHost())) {
				String server = WebUtils.parseServerFromUrl(appUrl);
				if (!Strings.isBlank(server) && app.getHost().toLowerCase().indexOf(server.toLowerCase()) != -1) {
					return;
				}
			}
			throw new SiteException(AppException.ERROR_CODE_APP, "应用检查失败，可能是应用状态异常或者应用域名/ip未设置白名单");
		} else {// 一个为空，一个不为空，参数填写错误
			throw new SiteException(ArgException.ARG_CODE_ARG, "方法(checkSite)参数fromSite,siteServiceUrl都不能为空");
		}
	}

	/**
	 * Creates, sends (to the given ServletResponse), and returns a
	 * TicketGrantingTicket for the given username.
	 */
	protected TicketGrantingTicket sendTgc(String username, HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		try {
			TicketGrantingTicket t = new TicketGrantingTicket(username);
			String token = tgcCache.addTicket(t);
			Cookie tgc = new Cookie(IParams.TGC_ID, token);
			if (!Strings.isBlank(ssoMode) && ssoMode.equalsIgnoreCase("https")) {
				tgc.setSecure(true);
			}
			tgc.setMaxAge(-1);
			tgc.setPath(request.getContextPath());
			response.addCookie(tgc);
			return t;
		} catch (TicketException ex) {
			throw new ServletException(ex.toString());
		}
	}

	/**
	 * Get valid TGC from cookies.
	 * 
	 * @param request
	 * @return TGT
	 */
	protected TicketGrantingTicket getTgc(HttpServletRequest request) {
		// check to see whether we've been sent a valid TGC
		Cookie[] cookies = request.getCookies();
		TicketGrantingTicket tgt = null;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(IParams.TGC_ID)) {
					tgt = (TicketGrantingTicket) tgcCache.getTicket(cookies[i].getValue());
					if (tgt == null) {
						continue;
					}
					return tgt;
				}
			}
		}
		return null;
	}

	/** Creates and sends a new PGT, returning a unique IOU for this PGT. */
	protected String sendPgt(ActiveTicket<?> st, String pgtAppCode, String callbackUrl) throws TicketException {
		// first, create the PGT and save it to the cache
		ProxyGrantingTicket pgt = new ProxyGrantingTicket(st, pgtAppCode, callbackUrl);
		String pgtToken = pgtCache.addTicket(pgt);

		// now, create an IOU (with a serial and a random component)
		byte[] b = new byte[PGT_IOU_LENGTH];
		SecureRandom sr = new SecureRandom();
		sr.nextBytes(b);
		String pgtIou = "PGTIOU-" + pgtCache.getPGTIOUSerialNumber() + "-" + Util.toPrintable(b);

		// now, send this PGT/IOU pair to our callback URL
		boolean sent = callbackWithPgt(callbackUrl, pgtToken, pgtIou);

		// return the IOU if appropriate
		if (sent)
			return pgtIou;
		else
			return null;
	}

	/**
	 * Contacts the URL with a PGT and an IOU, but only if the URL's server's
	 * certificate appears appropriate for the URL. Returns <tt>true</tt> on
	 * success, <tt>false</tt> on failure of any kind.
	 */
	protected boolean callbackWithPgt(String callbackUrl, String pgtId, String iouId) {
		try {
			String target = null;
			if (callbackUrl.indexOf('?') == -1)
				target = callbackUrl + "?pgtIou=" + iouId + "&pgtId=" + pgtId;
			else
				target = callbackUrl + "&pgtIou=" + iouId + "&pgtId=" + pgtId;
			// SecureURL.retrieve(target);
			if (!Strings.isBlank(ssoMode) && ssoMode.equalsIgnoreCase("https")) {
				HttpClientFactory.me(true).get(target);
			} else {
				HttpClientFactory.me(false).get(target);
			}

			// we succeeded!
			return true;

		} catch (Throwable ex) {
			log.error("PGT callback failed: " + ex.toString());
			return false;
		}
	}

}
