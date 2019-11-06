package com.linkallcloud.sso.pc.aop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.pc.kiss.um.ApplicationKiss;
import com.linkallcloud.web.interceptors.PermissionInterceptor;

public class LacPermissionInterceptor extends PermissionInterceptor {

	private static Map<Long, Map<String, String[]>> appUriRescodeMap = new HashMap<>();

	@Autowired
	private ApplicationKiss applicationKiss;

	public LacPermissionInterceptor() {
		super();
	}

	public LacPermissionInterceptor(List<String> ignoreRes, boolean override, String login, String noPermission) {
		super(ignoreRes, override, login, noPermission);
	}

	public LacPermissionInterceptor(List<String> ignoreRes, boolean override) {
		super(ignoreRes, override);
	}

	public LacPermissionInterceptor(String login, String noPermission) {
		super(login, noPermission);
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

}
