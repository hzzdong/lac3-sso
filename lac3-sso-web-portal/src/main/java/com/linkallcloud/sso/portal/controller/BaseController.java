package com.linkallcloud.sso.portal.controller;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.www.utils.HttpClientFactory;
import com.linkallcloud.core.www.utils.WebUtils;
import com.linkallcloud.sso.domain.AppLoginHis;
import com.linkallcloud.sso.exception.AppException;
import com.linkallcloud.sso.exception.ArgException;
import com.linkallcloud.sso.exception.SiteException;
import com.linkallcloud.sso.exception.TicketException;
import com.linkallcloud.sso.manager.IAppLoginHisManager;
import com.linkallcloud.sso.manager.IBlackManager;
import com.linkallcloud.sso.manager.ILockManager;
import com.linkallcloud.sso.portal.kiss.um.ApplicationKiss;
import com.linkallcloud.sso.portal.utils.IParams;
import com.linkallcloud.sso.ticket.ActiveTicket;
import com.linkallcloud.sso.ticket.ProxyGrantingTicket;
import com.linkallcloud.sso.ticket.ProxyTicket;
import com.linkallcloud.sso.ticket.ServiceTicket;
import com.linkallcloud.sso.ticket.TicketBox;
import com.linkallcloud.sso.ticket.TicketGrantingTicket;
import com.linkallcloud.sso.ticket.cache.ProxyGrantingTicketCache;
import com.linkallcloud.sso.ticket.cache.TicketGrantingTicketCache;
import com.linkallcloud.sso.util.Util;
import com.linkallcloud.um.domain.sys.Application;

public abstract class BaseController {
	protected static final Log log = Logs.get();

	// PGT IOU length
	private static final int PGT_IOU_LENGTH = 50;

	@Value("${lac.sso.mode}")
	protected String ssoMode;

	@Autowired
	protected TicketGrantingTicketCache tgcCache;

	@Autowired
	protected ProxyGrantingTicketCache pgtCache;

	@Autowired
	protected ApplicationKiss applicationKiss;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	protected IBlackManager blackManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	protected ILockManager lockManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	protected IAppLoginHisManager appLoginHisManager;

	public void checkBlackAndLock(Trace t, String account, String ip) {
		if (!Strings.isBlank(account)) {
			blackManager.check(t, account);
			lockManager.check(t, account);
		}

		if (!Strings.isBlank(ip)) {
			blackManager.check(t, ip);
			lockManager.check(t, ip);
		}
	}

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
	protected Application checkSiteCanPass(Trace t, String appCode, String appUrl) throws SiteException {
		if (Strings.isBlank(appCode) && Strings.isBlank(appUrl)) {// 都为空，直接登录SSO方式
			return null;
		} else if (!Strings.isBlank(appCode) && !Strings.isBlank(appUrl)) {// 都不为空
			try {
				Application app = applicationKiss.fetchByCode(t, appCode);
				if (null != app && app.isValid() && !Strings.isBlank(app.getHost())) {
					String server = WebUtils.parseServerFromUrl(appUrl);
					if (!Strings.isBlank(server) && app.getHost().toLowerCase().indexOf(server.toLowerCase()) != -1) {
						return app;
					}
				}
			} catch (Exception e) {// UM异常，为了尽量确保SSO正常使用，放弃检查，放行。
				log.errorf("########## 查询应用(code=%s)信息失败。", appCode);
			}
			throw new SiteException(AppException.ERROR_CODE_APP, "应用检查失败，可能是应用状态异常或者应用域名/ip未设置白名单");
		} else {// 一个为空，一个不为空，参数填写错误
			throw new SiteException(ArgException.ARG_CODE_ARG, "方法(checkSite)参数from,siteServiceUrl都不能为空");
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
					String ticketId = cookies[i].getValue();
					tgt = (TicketGrantingTicket) tgcCache.getTicket(ticketId);
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
	protected TicketBox<ProxyGrantingTicket> sendPgt(ActiveTicket<?> st, String callbackUrl, String pgtAppCode)
			throws TicketException {
		// first, create the PGT and save it to the cache
		ProxyGrantingTicket pgt = new ProxyGrantingTicket(st, callbackUrl, pgtAppCode);
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
			return new TicketBox<ProxyGrantingTicket>(pgtIou, pgt);
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
			String response = null;
			if (!Strings.isBlank(ssoMode) && ssoMode.equalsIgnoreCase("https")) {
				response = HttpClientFactory.me(true).get(target);
			} else {
				response = HttpClientFactory.me(false).get(target);
			}

			if (!Strings.isBlank(response)) {
				Result<String> ret = JSON.parseObject(response, new TypeReference<Result<String>>() {
				});
				if (ret != null && ret.getCode().equals("0")) {
					return true;// we succeeded!
				}
			}
			return false;

		} catch (Throwable ex) {
			log.error("PGT callback failed: " + ex.toString());
			return false;
		}
	}

	protected void appLoginAuthSuccess(Trace t, ServiceTicket st, String appCode, String appUrl,
			TicketBox<ProxyGrantingTicket> pgtIOU, String pgtAppCode, String pgtUrl) {
		if (Strings.isBlank(pgtAppCode) || appCode.equals(pgtAppCode)) {// 无代理，或者app自身是代理
			AppLoginHis alh = new AppLoginHis(Strings.isBlank(st.getSiteUser()) ? st.getUsername() : st.getSiteUser(),
					appCode, appUrl, st.getGrantor().getId());
			if (pgtIOU != null) {
				ProxyGrantingTicket pgt = pgtIOU.getTicket();
				alh.setProxy(true);
				alh.setProxyChain(JSON.toJSONString(pgt.getProxies()));
				alh.setPgt(pgt.getId());
			}
			appLoginHisManager.loginSuccess(t, alh);
		} else {// 有代理，app和代理分开是不同的应用
			AppLoginHis alh = new AppLoginHis(Strings.isBlank(st.getSiteUser()) ? st.getUsername() : st.getSiteUser(),
					appCode, appUrl, st.getGrantor().getId());
			appLoginHisManager.loginSuccess(t, alh);

			if (pgtIOU != null) {
				ProxyGrantingTicket pgt = pgtIOU.getTicket();
				AppLoginHis palh = new AppLoginHis(
						Strings.isBlank(st.getSiteUser()) ? st.getUsername() : st.getSiteUser(), pgtAppCode, pgtUrl,
						st.getGrantor().getId(), JSON.toJSONString(pgt.getProxies()), pgt.getId());
				appLoginHisManager.loginSuccess(t, palh);
			}
		}
	}

	protected void appProxyAuthSuccess(Trace t, ProxyTicket pt, String appCode, String appUrl,
			TicketBox<ProxyGrantingTicket> pgtIOU, String pgtAppCode, String pgtUrl) {
		TicketGrantingTicket tgt = getTgtFromPt(pt);

		if (Strings.isBlank(pgtAppCode) || appCode.equals(pgtAppCode)) {// 无后续代理，或者app自身是后续代理
			AppLoginHis alh = new AppLoginHis(Strings.isBlank(pt.getSiteUser()) ? pt.getUsername() : pt.getSiteUser(),
					appCode, appUrl, tgt.getId(), pt.getGrantor().getId());
			if (pgtIOU != null) {
				ProxyGrantingTicket pgt = pgtIOU.getTicket();
				alh.setProxy(true);
				alh.setProxyChain(JSON.toJSONString(pgt.getProxies()));
				alh.setPgt(pgt.getId());
			}
			appLoginHisManager.loginSuccess(t, alh);
		} else {// 有代理，app和代理分开是不同的应用
			AppLoginHis alh = new AppLoginHis(Strings.isBlank(pt.getSiteUser()) ? pt.getUsername() : pt.getSiteUser(),
					appCode, appUrl, tgt.getId(), pt.getGrantor().getId());
			appLoginHisManager.loginSuccess(t, alh);

			if (pgtIOU != null) {
				ProxyGrantingTicket pgt = pgtIOU.getTicket();
				AppLoginHis palh = new AppLoginHis(
						Strings.isBlank(pt.getSiteUser()) ? pt.getUsername() : pt.getSiteUser(), pgtAppCode, pgtUrl,
						tgt.getId(), pt.getGrantor().getId(), JSON.toJSONString(pgt.getProxies()), pgt.getId());
				appLoginHisManager.loginSuccess(t, palh);
			}
		}

	}

	protected TicketGrantingTicket getTgtFromPt(ProxyTicket pt) {
		ProxyGrantingTicket pgt = pt.getGrantor();
		if (pgt.getParent() instanceof ServiceTicket) {
			return ((ServiceTicket) pgt.getParent()).getGrantor();
		} else if (pgt.getParent() instanceof ProxyTicket) {
			return getTgtFromPt((ProxyTicket) pgt.getParent());
		}
		return null;
	}

}
