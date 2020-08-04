package com.linkallcloud.sso.oapi.kiss;

import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.www.utils.HttpClientFactory;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.Area;

public abstract class UmBaseKiss extends BaseKiss {

	@Value("${oapi.url.um}")
	protected String umBaseUrl;

	@Override
	public String getOapiUrl() {
		return umBaseUrl;
	}
	
	public String getCompanyGovCodeById(Trace t, Long companyId, String companyType) {
		Company myCompany = fetchCompanyById(t, companyId, companyType);
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

	public Company fetchCompanyById(Trace t, Long id, String type) {
		String sendMsgPkg = packMessage(t, new IdFaceRequest(id, null));
		if (YwUser.class.getSimpleName().substring(0, 2).equals(type)) {
			String responseJson = HttpClientFactory.me(false).post(getOapiUrl() + "/face/YwCompany/fetchById",
					sendMsgPkg);
			return unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<YwCompany>>() {
			});
		} else {
			String responseJson = HttpClientFactory.me(false).post(getOapiUrl() + "/face/KhCompany/fetchById",
					sendMsgPkg);
			return unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<KhCompany>>() {
			});
		}
	}

	public Company fetchCompanyByGovCode(Trace t, String govCode, String type) {
		String sendMsgPkg = packMessage(t, govCode);
		if (YwUser.class.getSimpleName().substring(0, 2).equals(type)) {
			String responseJson = HttpClientFactory.me(false).post(getOapiUrl() + "/face/YwCompany/fetchByGovCode",
					sendMsgPkg);
			return unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<YwCompany>>() {
			});
		} else {
			String responseJson = HttpClientFactory.me(false).post(getOapiUrl() + "/face/KhCompany/fetchByGovCode",
					sendMsgPkg);
			return unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<KhCompany>>() {
			});
		}
	}

	public Area fetchAreaById(Trace t, Long id) {
		String sendMsgPkg = packMessage(t, new IdFaceRequest(id, null));
		String responseJson = HttpClientFactory.me(false).post(getOapiUrl() + "/face/Area/fetchById", sendMsgPkg);
		Area result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<Area>>() {
		});
		return result;
	}

}
