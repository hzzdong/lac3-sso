package com.linkallcloud.sso.client.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.linkallcloud.core.json.Json;

/**
 * Provides utility functions in support of SSO clients.
 * 
 */
public class Util {

	/**
	 * Returns a service ID (URL) as a composite of the preconfigured server name
	 * and the runtime request.
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static String getService(HttpServletRequest request, String server)
			throws ServletException, UnsupportedEncodingException {
		// ensure we have a server name
		if (server == null) {
			throw new IllegalArgumentException("name of server is required");
		}

		// now, construct our best guess at the string
		StringBuffer sb = new StringBuffer();
		if (request.isSecure()) {
			sb.append("https://");
		} else {
			sb.append("http://");
		}
		sb.append(server);
		sb.append(request.getRequestURI());

		if (request.getQueryString() != null) {
			// first, see whether we've got a 'ticket' at all
			int ticketLoc = request.getQueryString().indexOf("ticket=");

			// if ticketLoc == 0, then it's the only parameter and we ignore
			// the whole query string

			// if no ticket is present, we use the query string wholesale
			if (ticketLoc == -1) {
				sb.append("?" + request.getQueryString());
			} else if (ticketLoc > 0) {
				ticketLoc = request.getQueryString().indexOf("&ticket=");
				if (ticketLoc == -1) {
					// there was a 'ticket=' unrelated to a parameter named 'ticket'
					sb.append("?" + request.getQueryString());
				} else if (ticketLoc > 0) {
					// otherwise, we use the query string up to "&ticket="
					sb.append("?" + request.getQueryString().substring(0, ticketLoc));
				}
			}
		}
		return URLEncoder.encode(sb.toString(), "UTF-8");
	}

	/**
	 * Store the object in the session or cookie.
	 * 
	 * @param request
	 * @param response
	 * @param obj
	 * @param key
	 * @param userInCookie true:cookie, false:session.
	 */
	public static void storeInWeb(HttpServletRequest request, HttpServletResponse response, String key, Object obj,
			boolean userInCookie) {
		if (userInCookie) {
			storeObject2Cookie(request, response, key, obj);
		} else {
			storeObject2Session(request, response, key, obj);
		}
	}

	/**
	 * @param request
	 * @param response
	 * @param key
	 * @param obj
	 */
	private static void storeObject2Session(HttpServletRequest request, HttpServletResponse response, String key,
			Object obj) {
		HttpSession session = request.getSession();
		if (session != null) {
			session.setAttribute(key, obj);
		}
	}

	/**
	 * @param request
	 * @param response
	 * @param key
	 * @param obj
	 */
	private static void storeObject2Cookie(HttpServletRequest request, HttpServletResponse response, String key,
			Object obj) {
		String us = Json.toJson(obj);
		Cookie cookie = new Cookie(key, us);
		// cookie.setSecure(true);
		cookie.setMaxAge(-1);
		cookie.setPath(request.getContextPath());
		response.addCookie(cookie);
	}

	/**
	 * @param request
	 * @param objClass
	 * @param key
	 * @param userInCookie true:cookie, false:session.
	 * @return Object
	 */
	public static Object getoutFromWeb(HttpServletRequest request, Class<?> objClass, String key,
			boolean userInCookie) {
		return userInCookie ? getoutFromCookie(request, objClass, key) : getoutFromSession(request, objClass, key);
	}

	/**
	 * 
	 * @param request
	 * @param objClass
	 * @param key
	 * @return Object
	 */
	private static Object getoutFromSession(HttpServletRequest request, Class<?> objClass, String key) {
		HttpSession session = request.getSession();
		if (session != null) {
			return session.getAttribute(key);
		}
		return null;
	}

	/**
	 * 
	 * @param request
	 * @param objClass
	 * @param key
	 * @return Object
	 */
	private static Object getoutFromCookie(HttpServletRequest request, Class<?> objClass, String key) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(key)) {
					String user = cookies[i].getValue();
					if (user == null || user.length() <= 0) {
						continue;
					}
					return Json.fromJson(objClass, user);
				}
			}
		}
		return null;
	}

	/**
	 * @param request
	 * @param objClass
	 * @param key
	 * @param userInCookie true:cookie, false:session.
	 */
	public static void removeFromWeb(HttpServletRequest request, HttpServletResponse response, String key,
			boolean userInCookie) {
		if (userInCookie) {
			removeFromCookie(request, response, key);
		} else {
			removeFromSession(request, response, key);
		}
	}

	/**
	 * @param request
	 * @param response
	 * @param key
	 */
	private static void removeFromCookie(HttpServletRequest request, HttpServletResponse response, String key) {
		Cookie cookie = new Cookie(key, "");
		// cookie.setSecure(true);
		cookie.setMaxAge(-1);
		cookie.setPath(request.getContextPath());
		response.addCookie(cookie);
	}

	/**
	 * @param request
	 * @param response
	 * @param key
	 */
	private static void removeFromSession(HttpServletRequest request, HttpServletResponse response, String key) {
		HttpSession session = request.getSession();
		if (session != null) {
			session.removeAttribute(key);
		}
	}

}
