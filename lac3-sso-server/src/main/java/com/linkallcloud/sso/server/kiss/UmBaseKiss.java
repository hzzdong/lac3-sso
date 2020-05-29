package com.linkallcloud.sso.server.kiss;

import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.www.utils.HttpClientFactory;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.sys.Area;

public abstract class UmBaseKiss extends BaseKiss {

	@Value("${oapi.url.um}")
	protected String umBaseUrl;

	@Override
	public String getOapiUrl() {
		return umBaseUrl;
	}

	public String getYwCompanyGovCodeById(Trace t, Long companyId) {
		YwCompany myCompany = fetchYwCompanyById(t, companyId);
		if (myCompany != null) {
			String govCode = null;
			if (myCompany.getAreaId() != null) {
				Area area = fetchAreaById(t, myCompany.getAreaId());
				govCode = area.getGovCode();
			} else {
				govCode = myCompany.getGovCode();
			}
			return govCode;
		}
		return null;
	}

	public YwCompany fetchYwCompanyById(Trace t, Long id) {
		String sendMsgPkg = packMessage(t, new IdFaceRequest(id, null));
		String responseJson = HttpClientFactory.me(false).post(getOapiUrl() + "/face/YwCompany/fetchById", sendMsgPkg);
		YwCompany result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<YwCompany>>() {
		});
		return result;
	}

	public YwCompany fetchYwCompanyByGovCode(Trace t, String govCode) {
		String sendMsgPkg = packMessage(t, govCode);
		String responseJson = HttpClientFactory.me(false).post(getOapiUrl() + "/face/YwCompany/fetchByGovCode",
				sendMsgPkg);
		YwCompany result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<YwCompany>>() {
		});
		return result;
	}

	public Area fetchAreaById(Trace t, Long id) {
		String sendMsgPkg = packMessage(t, new IdFaceRequest(id, null));
		String responseJson = HttpClientFactory.me(false).post(getOapiUrl() + "/face/Area/fetchById", sendMsgPkg);
		Area result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<Area>>() {
		});
		return result;
	}

}
