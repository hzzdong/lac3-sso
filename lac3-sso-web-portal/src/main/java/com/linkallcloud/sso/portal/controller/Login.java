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

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Client;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.principal.AccountMapping;
import com.linkallcloud.core.vo.LoginVo;
import com.linkallcloud.core.www.utils.WebUtils;
import com.linkallcloud.sso.domain.Account;
import com.linkallcloud.sso.exception.AccountException;
import com.linkallcloud.sso.exception.InvalidTicketException;
import com.linkallcloud.sso.exception.SiteException;
import com.linkallcloud.sso.exception.TicketException;
import com.linkallcloud.sso.manager.ILoginHisManager;
import com.linkallcloud.sso.portal.auth.PasswordHandler;
import com.linkallcloud.sso.portal.auth.TrustHandler;
import com.linkallcloud.sso.portal.auth.provider.DbPasswordHandler;
import com.linkallcloud.sso.portal.utils.IParams;
import com.linkallcloud.sso.portal.utils.LacSessionValidateCode;
import com.linkallcloud.sso.ticket.ServiceTicket;
import com.linkallcloud.sso.ticket.TicketGrantingTicket;
import com.linkallcloud.sso.ticket.cache.LoginTicketCache;
import com.linkallcloud.sso.ticket.cache.ServiceTicketCache;
import com.linkallcloud.um.domain.sys.Application;

@Controller
@RequestMapping
@Module(name = "登录")
public class Login extends BaseController {
	private static final Log log = Logs.get();

	@Autowired
	private ServiceTicketCache stCache;
	@Autowired
	private LoginTicketCache ltCache;
	@Autowired
	private DbPasswordHandler handler;

	@Autowired
	private LacSessionValidateCode sessionValidateCode;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private ILoginHisManager loginHisManager;

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
		Application app = checkSiteCanPass(t, appCode, appUrl);

		// check to see whether we've been sent a valid TGC
		TicketGrantingTicket tgt = getTgc(request);

		// unless RENEW is set, let the user through to the service.
		// otherwise, fall through and we'll be handled by authentication
		// below. Note that tgt is still active.

		if (tgt != null && Strings.isBlank(renew)) {
			return grantForService(t, request, response, modelMap, tgt, app, appUrl, false);
		}

		// if not, but if we're passed "gateway", then simply bounce back
		if (!Strings.isBlank(appUrl) && !Strings.isBlank(gateway)) {
			if (tgt == null) {
				modelMap.put("serviceId", appUrl);
				return "redirect";
			} else {
				return grantForService(t, request, response, modelMap, tgt, app, appUrl, false);
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

		// get remember me
		String me = getRememberMe(request);
		modelMap.put("me", me);

		return "login";
	}

//	@ResponseBody
//	@RequestMapping(value = "/login", method = RequestMethod.POST)
//	public Object doLogin(@RequestParam(value = "from", required = false) String appCode,
//			@RequestParam(value = "service", required = false) String appUrl,
//			@RequestParam(value = "username", required = false) String username,
//			@RequestParam(value = "password", required = false) String password,
//			@RequestParam(value = "lt", required = false) String lt,
//			@RequestParam(value = "vcode", required = false) String vcode,
//			@RequestParam(value = "warn", required = false) String warn,
//			@RequestParam(value = "pwdStrength", required = false) String pwdStrength,
//			@RequestParam(value = "rememberMe", required = false) String rememberMe, 
//			@RequestBody Client client,HttpServletRequest request,
//			HttpServletResponse response, Trace t)
//			throws ServletException, SiteException, IOException, AccountException, InvalidTicketException {

	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Object doLogin(@RequestBody LoginVo lvo, HttpServletRequest request, HttpServletResponse response, Trace t)
			throws ServletException, SiteException, IOException, AccountException, InvalidTicketException {
		// avoid caching (in the stupidly numerous ways we must)
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", -1);

		if (!sessionValidateCode.validate(request, response, lvo.getVcode())) {
			// horrible way of logging, I know
			log.error("验证码错误， " + request.getRemoteAddr());
			// failure: record invalid login ticket
			return authFailure(t, request, lvo.getLoginName(), "Lt-VCODE", "验证码错误，请重新输入", lvo.getFrom(),
					lvo.getService());
		}

		// blacklist and lock check
		checkBlackAndLock(t, lvo.getLoginName(), WebUtils.getIpAddress(request));

		// The servie can pass?
		checkSiteCanPass(t, lvo.getFrom(), lvo.getService());

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
				sendPrivacyCookie(lvo.getWarn(), request, response);
				return authSuccess(t, request, tgt, lvo.getFrom(), lvo.getService(), true, lvo.getPwdStrength(),
						lvo.getClient());
			} else {
				// failure: nothing else to be done
				return authFailure(t, request, lvo.getLoginName(), "TrustHandler", "无法验证用户", lvo.getFrom(),
						lvo.getService());
			}
		} else if (handler instanceof PasswordHandler && lvo.getLoginName() != null && lvo.getPassword() != null
				&& lvo.getLt() != null) {
			// do we have a valid login ticket?
			if (ltCache.getTicket(lvo.getLt()) != null) {
				// do we have a valid username and password?
				try {
					Account account = handler.authenticate(t, request, lvo.getLoginName(), lvo.getPassword());
					lvo.setLoginName(account.getLoginname());
					// success: send a new TGC if we don't have a valid TGT from above
					if (tgt == null) {
						tgt = sendTgc(lvo.getLoginName(), request, response);
					} else if (!tgt.getUsername().equals(lvo.getLoginName())) {
						// we're coming into a renew=true as a different user...
						// expire the old tgt
						tgt.expire();
						// and send a new one
						tgt = sendTgc(lvo.getLoginName(), request, response);
					}

					// set remember me
					setRememberMe(lvo.getRememberMe(), lvo.getLoginName(), request, response);

					sendPrivacyCookie(lvo.getWarn(), request, response);
					return authSuccess(t, request, tgt, lvo.getFrom(), lvo.getService(), true, lvo.getPwdStrength(),
							lvo.getClient());
				} catch (Throwable e) {
					// failure: record failed password authentication
					return authFailure(t, request, lvo.getLoginName(), "Account", "登录名或者密码错误", lvo.getFrom(),
							lvo.getService());
				}
			} else {
				// horrible way of logging, I know
				log.error("Invalid login ticket from " + request.getRemoteAddr());
				// failure: record invalid login ticket
				return authFailure(t, request, lvo.getLoginName(), "Lt-VCODE", "验证码错误，请重新输入", lvo.getFrom(),
						lvo.getService());
			}
		}

		return authFailure(t, request, lvo.getLoginName(), "Account", "登录名或者密码错误", lvo.getFrom(), lvo.getService());
	}

	/**
	 * Grants a service ticket for the given service, using the given
	 * TicketGrantingTicket. If no 'service' is specified, simply forward to message
	 * conveying generic success.
	 */
	private String grantForService(Trace t, HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			TicketGrantingTicket tgt, Application from, String serviceId, boolean first)
			throws ServletException, IOException {
		if (from != null && from.getMappingType() == AccountMapping.Mapping.getCode()) {
			modelMap.put("from", from.getCode());
			modelMap.put("serviceId", serviceId);
			return "mapping";
		}

		try {
			// loginSuccess(t, request, tgt, from, serviceId);

			if (!Strings.isBlank(serviceId) && from != null && !Strings.isBlank(from.getCode())) {
				ServiceTicket st = new ServiceTicket(tgt, from.getCode(), serviceId, first);
				String token = stCache.addTicket(st);
				modelMap.put("from", from.getCode());
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
						gourl = WebUtils.urlAppend(gourl, "ticket", token);
						gourl = WebUtils.urlAppend(gourl, "first", "false");
						modelMap.put("go", gourl);
						return "goservice";
					}
				} else {
					modelMap.put("first", "true");
					String gourl = serviceId;
					gourl = WebUtils.urlAppend(gourl, "ticket", token);
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

	private void ssoLoginSuccess(Trace t, HttpServletRequest request, TicketGrantingTicket tgt, String from,
			String serviceId, Client client) {
		String ip = WebUtils.getIpAddress(request);
		lockManager.dealAutoLock(t, true, tgt.getUsername(), ip, "登录验证成功");
		loginHisManager.loginSuccess(t, tgt.getUsername(), ip, from, serviceId, tgt.getId(), client);
	}

	private Map<String, String> authFailure(Trace t, HttpServletRequest request, String username, String code,
			String message, String from, String serviceId) {
		loginFailure(t, request, username, from, serviceId, message);

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

	private void loginFailure(Trace t, HttpServletRequest request, String username, String from, String serviceId,
			String message) {
		String ip = WebUtils.getIpAddress(request);
		lockManager.dealAutoLock(t, false, username, ip, message);
		// loginHisManager.loginFailure(t, username, ip, from, serviceId);
	}

	/**
	 * Grants a service ticket for the given service, using the given
	 * TicketGrantingTicket. If no 'service' is specified, simply forward to message
	 * conveying generic success.
	 */
	private Map<String, String> authSuccess(Trace t, HttpServletRequest request, TicketGrantingTicket tgt, String from,
			String serviceId, boolean first, String pwdStrength, Client client) throws ServletException, IOException {
		Map<String, String> result = new HashMap<String, String>();
		result.put("code", "0");
		try {
			ssoLoginSuccess(t, request, tgt, from, serviceId, client);

			if (!Strings.isBlank(serviceId) && !Strings.isBlank(from)) {
				ServiceTicket st = new ServiceTicket(tgt, from, serviceId, first);
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
						gourl = WebUtils.urlAppend(gourl, "ticket", token);
						gourl = WebUtils.urlAppend(gourl, "first", "false");
						result.put("go", gourl);// "page/service";
					}
				} else {
					result.put("first", "true");
					String gourl = serviceId;
					gourl = WebUtils.urlAppend(gourl, "ticket", token);
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
			Cookie privacy = new Cookie(IParams.PRIVACY_ID, "enabled");
			if (!Strings.isBlank(ssoMode) && ssoMode.equalsIgnoreCase("https")) {
				privacy.setSecure(true);
			}
			privacy.setMaxAge(-1);
			privacy.setPath(request.getContextPath());
			response.addCookie(privacy);
		} else if (privacyRequested(request)) {
			// delete the cookie if it's there but *not* requested this time
			Cookie privacy = new Cookie(IParams.PRIVACY_ID, "disabled");
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
				if (cookies[i].getName().equals(IParams.PRIVACY_ID) && cookies[i].getValue().equals("enabled"))
					return true;
		}
		return false;
	}

	private String getRememberMe(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				if (IParams.REMEMBER_COOKIE.equals(c.getName())) {
					return c.getValue();
				}
			}
		}
		return null;
	}

	private void setRememberMe(Integer remember, String userName, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Cookie cookie = new Cookie(IParams.REMEMBER_COOKIE, userName);
			if (!Strings.isBlank(ssoMode) && ssoMode.equalsIgnoreCase("https")) {
				cookie.setSecure(true);
			}
			cookie.setMaxAge((remember != null && remember.intValue() == 1) ? (60 * 60 * 24 * 365) : 0);
			cookie.setPath(request.getContextPath());
			cookie.setHttpOnly(true);
			response.addCookie(cookie);
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
	}
}
