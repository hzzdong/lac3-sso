package com.linkallcloud.sso.server.oapi.um;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.core.www.utils.HttpClientFactory;

@Component
public class ApplicationOapi extends BaseOapi {

	public Application fetchByCode(String appCode) {
		String sendMsgPkg = packMessage(appCode);
		String responseJson = HttpClientFactory.me(false).post(umOapiUrl + "/face/Application/fetch", sendMsgPkg);
		Application app = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<Application>>() {
		});
		return app;
	}

	public List<Application> findAll() {
		String sendMsgPkg = packMessage(null);
		String responseJson = HttpClientFactory.me(false).post(umOapiUrl + "/face/Application/findAll", sendMsgPkg);
		List<Application> apps = unpackMessage(responseJson,
				new TypeReference<ObjectFaceResponse<List<Application>>>() {
				});
		return apps;
	}

}
