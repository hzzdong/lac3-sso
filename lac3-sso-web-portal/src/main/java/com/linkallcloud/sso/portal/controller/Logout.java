package com.linkallcloud.sso.portal.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.sso.portal.kiss.um.ApplicationKiss;
import com.linkallcloud.sso.portal.kiss.um.YwUserKiss;
import com.linkallcloud.sso.portal.utils.IParams;
import com.linkallcloud.sso.ticket.TicketGrantingTicket;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.Application;

@Controller
@RequestMapping
@Module(name = "登出")
public class Logout extends BaseController {

	@Autowired
	private ApplicationKiss applicationKiss;
	@Autowired
	private YwUserKiss ywUserKiss;

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, Trace t) {
		// avoid caching (in the stupidly numerous ways we must)
		response.setHeader("pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);

		String loginName = null;
		// see if the user sent us a valid TGC
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(IParams.TGC_ID)) {
					TicketGrantingTicket tgt = (TicketGrantingTicket) tgcCache.getTicket(cookies[i].getValue());
					if (tgt == null)
						continue;

					// ticket found!
					loginName = tgt.getUsername();
					tgcCache.deleteTicket(cookies[i].getValue());
					destroyTgc(request, response);
				}
			}
		}

		if (!Strings.isBlank(loginName)) {
			YwUser user = ywUserKiss.fecthByAccount(t, loginName);
			if (user != null) {
				List<Application> apps = applicationKiss.findByYwUserId(t, user.getId());
				modelMap.put("apps", apps);
			}
		}
		return "logout";
	}

	/** Destroys the browser's TGC. */
	private void destroyTgc(HttpServletRequest request, HttpServletResponse response) {
		Cookie tgcOverwrite = new Cookie(IParams.TGC_ID, "destroyed");
		tgcOverwrite.setPath(request.getContextPath());
		tgcOverwrite.setMaxAge(0);
		if (!Strings.isBlank(ssoMode) && ssoMode.equalsIgnoreCase("https")) {
			tgcOverwrite.setSecure(true);
		}
		response.addCookie(tgcOverwrite);
	}
}
