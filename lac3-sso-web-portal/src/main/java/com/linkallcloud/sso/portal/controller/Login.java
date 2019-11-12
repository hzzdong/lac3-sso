package com.linkallcloud.sso.portal.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.www.utils.WebUtils;
import com.linkallcloud.sso.domain.Account;
import com.linkallcloud.sso.portal.auth.PasswordHandler;
import com.linkallcloud.sso.portal.auth.TrustHandler;
import com.linkallcloud.sso.portal.auth.provider.DbPasswordHandler;
import com.linkallcloud.sso.portal.exception.AccountException;
import com.linkallcloud.sso.portal.exception.InvalidTicketException;
import com.linkallcloud.sso.portal.exception.SiteException;
import com.linkallcloud.sso.portal.exception.TicketException;
import com.linkallcloud.sso.portal.ticket.ServiceTicket;
import com.linkallcloud.sso.portal.ticket.TicketGrantingTicket;
import com.linkallcloud.sso.portal.ticket.cache.LoginTicketCache;
import com.linkallcloud.sso.portal.ticket.cache.ServiceTicketCache;

@Controller
@RequestMapping
@Module(name = "登录")
public class Login extends BaseController {
	private static final Log log = Logs.get();

	// *********************************************************************
	// Constants

	// cookie IDs
	private static final String PRIVACY_ID = "SSOPRIVACY";

	// parameters
//	private static final String SERVICE = "service";
//	private static final String RENEW = "renew";
//	private static final String GATEWAY = "gateway";

	// *********************************************************************

	// *********************************************************************
	// Private state

	@Autowired
	private ServiceTicketCache stCache;
	@Autowired
	private LoginTicketCache ltCache;
	@Autowired
	private DbPasswordHandler handler;
	// private String loginForm, genericSuccess, serviceSuccess, confirmService,
	// redirect;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@RequestParam(value = "from", required = false) String appCode,
			@RequestParam(value = "service", required = false) String appUrl,
			@RequestParam(value = "renew", required = false) String renew,
			@RequestParam(value = "gateway", required = false) String gateway,
			@RequestParam(value = "warn", required = false) String warn, HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap, Trace t)
			throws ServletException, SiteException, IOException, AccountException, InvalidTicketException {
		// avoid caching (in the stupidly numerous ways we must)
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", -1);

		// The servie can pass?
		checkSitePass(t, appCode, appUrl);

		// check to see whether we've been sent a valid TGC
		TicketGrantingTicket tgt = getTgc(request);

		// unless RENEW is set, let the user through to the service.
		// otherwise, fall through and we'll be handled by authentication
		// below. Note that tgt is still active.
		if (tgt != null && Strings.isBlank(renew)) {
			return grantForService(request, response, modelMap, tgt, appCode, appUrl, false);
		}

		// if not, but if we're passed "gateway", then simply bounce back
		if (!Strings.isBlank(appUrl) && !Strings.isBlank(gateway)) {
			if (tgt == null) {
				modelMap.put("serviceId", appUrl);
				return "redirect";
			} else {
				return grantForService(request, response, modelMap, tgt, appCode, appUrl, false);
			}
		}

		// record the service in the request
		modelMap.put("from", appCode);
		modelMap.put("service", appUrl);
		modelMap.put("warn", warn);

		// no success yet, so generate a login ticket and forward to the login form
		try {
			String lt = ltCache.addTicket();
			modelMap.put("lt", lt);
		} catch (TicketException ex) {
			throw new ServletException(ex);
		}

		return "login";
	}

	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Object doLogin(@RequestParam(value = "from", required = false) String appCode,
			@RequestParam(value = "service", required = false) String appUrl,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "lt", required = false) String lt,
			@RequestParam(value = "warn", required = false) String warn, HttpServletRequest request,
			HttpServletResponse response, Trace t)
			throws ServletException, SiteException, IOException, AccountException, InvalidTicketException {
		// avoid caching (in the stupidly numerous ways we must)
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", -1);

		// The servie can pass?
		checkSitePass(t, appCode, appUrl);

		TicketGrantingTicket tgt = getTgc(request);

		// if not, then see if our AuthHandler can help
		if (handler instanceof TrustHandler) {
			// try to get a trusted username by interpreting the request
			String trustedUsername = ((TrustHandler) handler).getUsername(t, request);
			if (trustedUsername != null) {
				// success: send a new TGC if we don't have a valid TGT from above
				if (tgt == null) {
					tgt = sendTgc(trustedUsername, request, response);
				} else if (!tgt.getUsername().equals(trustedUsername)) {
					// we're coming into a renew=true as a different user...
					// expire the old tgt
					tgt.expire();
					// and send a new one
					tgt = sendTgc(trustedUsername, request, response);
				}
				sendPrivacyCookie(warn, request, response);
				return authSuccess(request, tgt, appCode, appUrl, true);
			} else {
				// failure: nothing else to be done
				return authFailure("TrustHandler", "无法验证用户");
			}
		} else if (handler instanceof PasswordHandler && username != null && password != null && lt != null) {
			// do we have a valid login ticket?
			if (ltCache.getTicket(lt) != null) {
				// do we have a valid username and password?
				try {
					Account account = handler.authenticate(t, request, username, password);
					username = account.getLoginname();
					// success: send a new TGC if we don't have a valid TGT from above
					if (tgt == null) {
						tgt = sendTgc(username, request, response);
					} else if (!tgt.getUsername().equals(username)) {
						// we're coming into a renew=true as a different user...
						// expire the old tgt
						tgt.expire();
						// and send a new one
						tgt = sendTgc(username, request, response);
					}
					sendPrivacyCookie(warn, request, response);
					return authSuccess(request, tgt, appCode, appUrl, true);
				} catch (Throwable e) {
					// failure: record failed password authentication
					return authFailure("Account", "登录名或者密码错误");
				}
			} else {
				// horrible way of logging, I know
				log.error("Invalid login ticket from " + request.getRemoteAddr());
				// failure: record invalid login ticket
				return authFailure("LoginTicke", "无效的登录票据");
			}
		}

		return authFailure("Account", "登录名或者密码错误");
	}

	/**
	 * Grants a service ticket for the given service, using the given
	 * TicketGrantingTicket. If no 'service' is specified, simply forward to message
	 * conveying generic success.
	 */
	private String grantForService(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			TicketGrantingTicket t, String from, String serviceId, boolean first) throws ServletException, IOException {
		try {
			if (!Strings.isBlank(serviceId) && !Strings.isBlank(from)) {
				ServiceTicket st = new ServiceTicket(t, serviceId, first);
				String token = stCache.addTicket(st);
				modelMap.put("from", from);
				modelMap.put("serviceId", serviceId);
				modelMap.put("token", token);
				if (!first) {
					if (privacyRequested(request)) {
						String gourl = serviceId;
						try {
							gourl = WebUtils.urlAppend(gourl, "token", token);
						} catch (UnsupportedEncodingException e) {
						}
						modelMap.put("go", gourl);
						return "confirm";
					} else {
						modelMap.put("first", "false");
						String gourl = serviceId;
						gourl = WebUtils.urlAppend(gourl, "token", token);
						gourl = WebUtils.urlAppend(gourl, "first", "false");
						modelMap.put("go", gourl);
						return "goservice";
					}
				} else {
					modelMap.put("first", "true");
					String gourl = serviceId;
					gourl = WebUtils.urlAppend(gourl, "token", token);
					gourl = WebUtils.urlAppend(gourl, "first", "true");
					modelMap.put("go", gourl);
					return "goservice";
				}
			} else {
				return "generic";
			}
		} catch (TicketException ex) {
			throw new ServletException(ex.toString());
		}
	}

	private Map<String, String> authFailure(String code, String message) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("code", code);
		result.put("message", message);
		try {
			String lt = ltCache.addTicket();
			result.put("lt", lt);
		} catch (TicketException ex) {
		}
		return result;
	}

	/**
	 * Grants a service ticket for the given service, using the given
	 * TicketGrantingTicket. If no 'service' is specified, simply forward to message
	 * conveying generic success.
	 */
	private Map<String, String> authSuccess(HttpServletRequest request, TicketGrantingTicket t, String from,
			String serviceId, boolean first) throws ServletException, IOException {
		Map<String, String> result = new HashMap<String, String>();
		result.put("code", "0");
		try {
			if (!Strings.isBlank(serviceId) && !Strings.isBlank(from)) {
				ServiceTicket st = new ServiceTicket(t, serviceId, first);
				String token = stCache.addTicket(st);

				result.put("from", from);
				result.put("serviceId", serviceId);
				result.put("token", token);
				if (!first) {
					if (privacyRequested(request)) {
						try {
							serviceId = URLEncoder.encode(serviceId, "UTF-8");
						} catch (UnsupportedEncodingException e) {
						}
						StringBuffer gourl = new StringBuffer(request.getContextPath()).append("/confirm?serviceId=")
								.append(serviceId).append("&from=").append(from).append("&token=").append(token);
						result.put("go", gourl.toString());// "page/confirm";
					} else {
						result.put("first", "false");
						String gourl = serviceId;
						gourl = WebUtils.urlAppend(gourl, "token", token);
						gourl = WebUtils.urlAppend(gourl, "first", "false");
						result.put("go", gourl);// "page/service";
					}
				} else {
					result.put("first", "true");
					String gourl = serviceId;
					gourl = WebUtils.urlAppend(gourl, "token", token);
					gourl = WebUtils.urlAppend(gourl, "first", "true");
					result.put("go", gourl);// "page/service";
				}
			} else {
				result.put("go", request.getContextPath() + "/generic");// "page/generic";
			}
		} catch (TicketException ex) {
			result.put("code", ex.getCode());
			result.put("message", ex.getMessage());
		}
		return result;
	}

	/**
	 * If the user has so requested, creates and sends (to the given
	 * ServletResponse) a cookie recording the fact that the user wants to be warned
	 * before using CAS's single-sign-on capabilities.
	 */
	private void sendPrivacyCookie(String warn, HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		if (!Strings.isBlank(warn)) {
			// send the cookie if it's requested
			Cookie privacy = new Cookie(PRIVACY_ID, "enabled");
			if (!Strings.isBlank(ssoMode) && ssoMode.equalsIgnoreCase("https")) {
				privacy.setSecure(true);
			}
			privacy.setMaxAge(-1);
			privacy.setPath(request.getContextPath());
			response.addCookie(privacy);
		} else if (privacyRequested(request)) {
			// delete the cookie if it's there but *not* requested this time
			Cookie privacy = new Cookie(PRIVACY_ID, "disabled");
			if (!Strings.isBlank(ssoMode) && ssoMode.equalsIgnoreCase("https")) {
				privacy.setSecure(true);
			}
			privacy.setMaxAge(0);
			privacy.setPath(request.getContextPath());
			response.addCookie(privacy);
		}
	}

	/**
	 * Returns true if privacy has been requested, false otherwise.
	 */
	private boolean privacyRequested(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++)
				if (cookies[i].getName().equals(PRIVACY_ID) && cookies[i].getValue().equals("enabled"))
					return true;
		}
		return false;
	}
}
