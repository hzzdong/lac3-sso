package com.linkallcloud.sso.portal.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.portal.ticket.TicketGrantingTicket;
import com.linkallcloud.sso.portal.utils.IParams;
import com.linkallcloud.web.utils.Controllers;

@Controller
@RequestMapping
@Module(name = "登出")
public class Logout extends BaseController {

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, Trace t) {
		// avoid caching (in the stupidly numerous ways we must)
		response.setHeader("pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);

		// see if the user sent us a valid TGC
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(IParams.TGC_ID)) {
					TicketGrantingTicket tgt = (TicketGrantingTicket) tgcCache.getTicket(cookies[i].getValue());
					if (tgt == null)
						continue;

					// ticket found!
					tgcCache.deleteTicket(cookies[i].getValue());
					destroyTgc(request, response);
				}
			}
		}
		return Controllers.redirect("/login");
	}

	/** Destroys the browser's TGC. */
	private void destroyTgc(HttpServletRequest request, HttpServletResponse response) {
		Cookie tgcOverwrite = new Cookie(IParams.TGC_ID, "destroyed");
		tgcOverwrite.setPath(request.getContextPath());
		tgcOverwrite.setMaxAge(0);
		tgcOverwrite.setSecure(true);
		response.addCookie(tgcOverwrite);
	}

}
