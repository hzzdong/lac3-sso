package com.linkallcloud.sso.client.support;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.principal.Assertion;
import com.linkallcloud.core.principal.Service;
import com.linkallcloud.core.principal.SimpleService;
import com.linkallcloud.sso.client.proxy.ProxyRetriever;
import com.linkallcloud.sso.client.proxy.SsoProxyRetriever;
import com.linkallcloud.web.utils.Controllers;

public class PtResolver implements HandlerMethodArgumentResolver {
    private static Log log = Logs.get();

    private ProxyRetriever proxyRetriever;

    public PtResolver(String ssoServerUrl) {
        this.proxyRetriever = new SsoProxyRetriever(ssoServerUrl);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.getParameterType().equals(String.class) && parameter.hasParameterAnnotation(Pt.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        Pt ptAnnotation = parameter.getParameterAnnotation(Pt.class);
        String appCode = ptAnnotation.code();
        String url = ptAnnotation.url();
        if (!Strings.isBlank(appCode) && !Strings.isBlank(url)) {
            Service targetService = new SimpleService(url, appCode);
            Assertion as = (Assertion) Controllers.getSessionObject(Assertion.ASSERTION_KEY);
            if (as != null) {
                return proxyRetriever.getProxyTicketIdFor(as.getProxyGrantingTicketId(), targetService);
            } else {
                log.error("########## 无法获取Pt，因为session中不存在Assertion");
            }
        } else {
            log.error("########## 无法获取Pt，注解中code和url不能为空");
        }
        return null;
    }
}
