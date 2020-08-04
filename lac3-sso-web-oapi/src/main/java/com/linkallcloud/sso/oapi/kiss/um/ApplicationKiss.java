package com.linkallcloud.sso.oapi.kiss.um;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ListFaceRequest;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.www.utils.HttpClientFactory;
import com.linkallcloud.sso.oapi.kiss.UmBaseKiss;
import com.linkallcloud.um.domain.sys.Application;

@Component
public class ApplicationKiss extends UmBaseKiss {

	public Application fetchByCode(Trace t, String appCode) {
		String sendMsgPkg = packMessage(t, appCode);
		String responseJson = HttpClientFactory.me(false).post(getOapiUrl() + "/face/Application/fetch", sendMsgPkg);
		Application app = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<Application>>() {
		});
		return app;
	}

	public List<Application> find(Trace t, ListFaceRequest req) {
		String sendMsgPkg = packMessage(t, req);
		String responseJson = HttpClientFactory.me(false).post(getOapiUrl() + "/face/Application/find", sendMsgPkg);
		List<Application> result = unpackMessage(responseJson,
				new TypeReference<ObjectFaceResponse<List<Application>>>() {
				});
		return result;
	}

	public Map<String, String[]> loadAppUriRescodeMap(Trace t, Long appId) {
		String sendMsgPkg = packMessage(t, new IdFaceRequest(appId, null));
		String responseJson = HttpClientFactory.me(false).post(getOapiUrl() + "/face/Application/loadAppUriRescodeMap",
				sendMsgPkg);
		Map<String, String[]> result = unpackMessage(responseJson,
				new TypeReference<ObjectFaceResponse<Map<String, String[]>>>() {
				});
		return result;
	}

}
