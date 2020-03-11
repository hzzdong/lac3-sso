package com.linkallcloud.sso.server.kiss.um;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.www.utils.HttpClientFactory;
import com.linkallcloud.sso.server.kiss.UmBaseKiss;
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

	public Application fetchById(Trace t, Long appId) {
		String sendMsgPkg = packMessage(t, new IdFaceRequest(appId.toString(), null));
		String responseJson = HttpClientFactory.me(false).post(getOapiUrl() + "/face/Application/fetchById",
				sendMsgPkg);
		Application app = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<Application>>() {
		});
		return app;
	}

	public List<Application> findAll(Trace t) {
		String sendMsgPkg = packMessage(t, null);
		String responseJson = HttpClientFactory.me(false).post(getOapiUrl() + "/face/Application/findAll", sendMsgPkg);
		List<Application> apps = unpackMessage(responseJson,
				new TypeReference<ObjectFaceResponse<List<Application>>>() {
				});
		return apps;
	}

	public Map<String, String[]> loadAppUriRescodeMap(Trace t, Long appId) {
		String sendMsgPkg = packMessage(t, new IdFaceRequest(appId.toString(), null));
		String responseJson = HttpClientFactory.me(false).post(getOapiUrl() + "/face/Application/loadAppUriRescodeMap",
				sendMsgPkg);
		Map<String, String[]> result = unpackMessage(responseJson,
				new TypeReference<ObjectFaceResponse<Map<String, String[]>>>() {
				});
		return result;
	}

	public Page<Application> findPageByYwUserId(Trace t, Long ywUserId) {
		String sendMsgPkg = packMessage(t, new IdFaceRequest(ywUserId.toString(), null));
		String responseJson = HttpClientFactory.me(false).post(getOapiUrl() + "/face/Application/findByYwUserId",
				sendMsgPkg);
		List<Application> apps = unpackMessage(responseJson,
				new TypeReference<ObjectFaceResponse<List<Application>>>() {
				});

		Page<Application> result = new Page<Application>(0, 100);
		if (apps != null && apps.size() > 0) {
			result.setData(apps);
			result.setRecordsTotal(apps.size());
			result.setRecordsFiltered(apps.size());
		}
		return result;
	}

	public List<Application> findByYwUserId(Trace t, Long ywUserId) {
		String sendMsgPkg = packMessage(t, new IdFaceRequest(ywUserId.toString(), null));
		String responseJson = HttpClientFactory.me(false).post(getOapiUrl() + "/face/Application/findByYwUserId",
				sendMsgPkg);
		List<Application> result = unpackMessage(responseJson,
				new TypeReference<ObjectFaceResponse<List<Application>>>() {
				});
		return result;
	}

}
