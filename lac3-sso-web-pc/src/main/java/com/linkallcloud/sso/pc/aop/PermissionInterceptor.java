package com.linkallcloud.sso.pc.aop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.pc.kiss.um.ApplicationKiss;
import com.linkallcloud.web.interceptors.AbstractPermissionInterceptor;

public class PermissionInterceptor extends AbstractPermissionInterceptor {

	private static Map<Long, Map<String, String[]>> appUriRescodeMap = new HashMap<>();

	@Autowired
	private ApplicationKiss applicationKiss;

	@Value("${oapi.appcode}")
	private String myAppCode;

	public PermissionInterceptor() {
		super();
	}

	public PermissionInterceptor(List<String> ignoreRes, boolean override) {
		super(ignoreRes, override);
	}

	@Override
	protected Map<String, String[]> loadAppUriRescodeMap(Trace t, Long appId) {
		if (appUriRescodeMap.containsKey(appId)) {
			return appUriRescodeMap.get(appId);
		} else {
			Map<String, String[]> thisRes = applicationKiss.loadAppUriRescodeMap(t, appId);
			appUriRescodeMap.put(appId, thisRes);
			return thisRes;
		}
	}

	@Override
	protected String getAppCode() {
		return myAppCode;
	}

}
