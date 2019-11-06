package com.linkallcloud.sso.portal.kiss.um;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.www.utils.HttpClientFactory;
import com.linkallcloud.sso.portal.kiss.BaseKiss;
import com.linkallcloud.um.domain.sys.Application;

@Component
public class ApplicationKiss extends BaseKiss {

	public Application fetchByCode(Trace t, String appCode) {
		String sendMsgPkg = packMessage(t, appCode);
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/Application/fetch", sendMsgPkg);
		Application app = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<Application>>() {
		});
		return app;
	}

	public Map<String, String[]> loadAppUriRescodeMap(Trace t, Long appId) {
		String sendMsgPkg = packMessage(t, new IdFaceRequest(appId.toString(), null));
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/Application/loadAppUriRescodeMap",
				sendMsgPkg);
		Map<String, String[]> result = unpackMessage(responseJson,
				new TypeReference<ObjectFaceResponse<Map<String, String[]>>>() {
				});
		return result;
	}

}
