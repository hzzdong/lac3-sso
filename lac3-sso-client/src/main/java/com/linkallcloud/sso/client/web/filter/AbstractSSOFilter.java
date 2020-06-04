package com.linkallcloud.sso.client.web.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.www.utils.WebUtils;
import com.linkallcloud.sso.client.util.CommonUtils;

/**
 * Abstract class that contains common functionality amongst SSO filters.
 * <p/>
 * You must specify the serverName (format: hostname:port) or the serviceUrl. If
 * you specify both, the serviceUrl is used over the serverName.
 * 
 * 2011-6-15
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public abstract class AbstractSSOFilter implements Filter {
	protected final Log log = Logs.get();

	// 不过滤的uri
	protected List<String> notFilterResources = Lang.list("/static/", "/js/", "/css/", "/images/", "/img/", ".jpg",
			".png", ".jpeg", ".js", ".css", "/login", "/imageValidate", "/verifyCode", "/exit", "/nnl/", "/unsupport",
			"/error", "/pub/");

	/**
	 * Constant string representing the ticket parameter.
	 */
	public static final String PARAM_TICKET = "ticket";

	/**
	 * Constant representing where we flag a gatewayed request in the session.
	 */
	public static final String CONST_GATEWAY = "_sso_gateway_";

	/**
	 * The name of the server in the following format: <hostname>:<port> where port
	 * is optional if its a standard port.
	 */
	private final String serverName;

	private final int siteClazz;// 应用的类别，0：运维，1：客户

	private final String siteCode;

	/**
	 * The exact service url to match to.
	 */
	private final String serviceUrl;

	/**
	 * Whether to store the entry in session or not. Defaults to true.
	 */
	private final boolean useSession;

	protected AbstractSSOFilter(final int siteClazz, final String siteCode, final String serverName,
			final String serviceUrl) {
		this(siteClazz, siteCode, serverName, serviceUrl, true, null, false);
	}

	protected AbstractSSOFilter(final int siteClazz, final String siteCode, final String serverName,
			final String serviceUrl, List<String> ignoreRes) {
		this(siteClazz, siteCode, serverName, serviceUrl, true, ignoreRes, false);
	}

	protected AbstractSSOFilter(final int siteClazz, final String siteCode, final String serverName,
			final String serviceUrl, final boolean useSession, List<String> ignoreRes, boolean override) {
		CommonUtils.assertTrue(CommonUtils.isNotBlank(serverName) || CommonUtils.isNotBlank(serviceUrl),
				"either serverName or serviceUrl must be set");

		this.siteClazz = siteClazz;
		this.siteCode = siteCode;
		this.serverName = serverName;
		this.serviceUrl = serviceUrl;
		this.useSession = useSession;

		log.info("Site code set to: " + this.siteCode + ";Service Name set to: " + this.serverName
				+ "; Service Url  set to: " + this.serviceUrl + "Use Session set to: " + this.useSession);

		if (ignoreRes != null && ignoreRes.size() > 0) {
			if (override) {
				this.notFilterResources = ignoreRes;
			} else {
				for (String res : ignoreRes) {
					boolean exist = false;
					for (String uri : notFilterResources) {
						if (uri.equals(res)) {
							exist = true;
							break;
						}
					}
					if (!exist) {
						this.notFilterResources.add(res);
					}
				}
			}
		}
	}

	public final void destroy() {
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
			final FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String uri = request.getRequestURI();
		if (needFiltered(uri)) {
			doFilterInternal(request, response, filterChain);
		} else {
			filterChain.doFilter(request, response);
		}

	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param filterChain
	 * @throws IOException
	 * @throws ServletException
	 */
	protected abstract void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws IOException, ServletException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		// nothing to do here
	}

	/**
	 * 是否需要过滤
	 *
	 * @param uri
	 * @return
	 */
	private boolean needFiltered(String uri) {
		for (String s : notFilterResources) {
			if (uri.indexOf(s) != -1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Constructs a service url from the HttpServletRequest or from the given
	 * serviceUrl. Prefers the serviceUrl provided if both a serviceUrl and a
	 * serviceName.
	 * 
	 * @param request  the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @return the service url to use.
	 * @throws UnsupportedEncodingException
	 */
	protected final String constructServiceUrl(final HttpServletRequest request, final HttpServletResponse response)
			throws UnsupportedEncodingException {
		if (CommonUtils.isNotBlank(this.serviceUrl)) {
			return this.serviceUrl;// response.encodeURL(this.serviceUrl);
		}

		final StringBuffer buffer = new StringBuffer();

		synchronized (buffer) {
			buffer.append(request.isSecure() ? "https://" : "http://");
			buffer.append(this.serverName);
			buffer.append(request.getRequestURI());

			if (CommonUtils.isNotBlank(request.getQueryString())) {
				final int location = request.getQueryString().indexOf(PARAM_TICKET + "=");

				if (location == 0) {
					final String returnValue = buffer.toString();// response.encodeURL(buffer.toString());
					if (log.isDebugEnabled()) {
						log.debug("serviceUrl generated: " + returnValue);
					}
					return returnValue;
				}

				buffer.append("?");

				if (location == -1) {
					buffer.append(request.getQueryString());
				} else if (location > 0) {
					final int actualLocation = request.getQueryString().indexOf("&" + PARAM_TICKET + "=");

					if (actualLocation == -1) {
						buffer.append(request.getQueryString());
					} else if (actualLocation > 0) {
						buffer.append(request.getQueryString().substring(0, actualLocation));
					}
				}
			}
		}

		final String returnValue = buffer.toString();// response.encodeURL(buffer.toString());
		if (log.isDebugEnabled()) {
			log.debug("serviceUrl generated: " + returnValue);
		}
		return returnValue;
	}

	protected final boolean isUseSession() {
		return this.useSession;
	}

	protected void toLogin(String loginUrl, HttpServletRequest request, HttpServletResponse hResponse)
			throws IOException {
		if ((!Strings.isBlank(loginUrl)) && (!loginUrl.startsWith("http"))
				&& (!loginUrl.startsWith(request.getContextPath()))) {
			loginUrl = request.getContextPath() + loginUrl;
		}
		if (WebUtils.isAjax(request)) {
			hResponse.setCharacterEncoding("UTF-8");
			Result<Object> result = Exceptions.makeErrorResult(Exceptions.CODE_ERROR_SESSION_TIMEOUT, "会话超时");
			result.setData(loginUrl);

			WebUtils.out(hResponse, result);
		} else {
			hResponse.sendRedirect(loginUrl);
		}
	}

	protected String getLacToken(HttpServletRequest request) {
		String token = request.getParameter("token");
		log.info("################  paream token:" + token);
		if (Strings.isBlank(token)) {
			token = request.getHeader("token");
			try {
				if (!Strings.isBlank(token)) {
					token = URLDecoder.decode(token, "UTF8");
				}
			} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			}
			log.info("################ header token:" + token);
		}
		return token;
	}

	public int getSiteClazz() {
		return siteClazz;
	}

	public String getSiteCode() {
		return siteCode;
	}

}
