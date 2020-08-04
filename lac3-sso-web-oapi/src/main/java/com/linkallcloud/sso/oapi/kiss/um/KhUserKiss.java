package com.linkallcloud.sso.oapi.kiss.um;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.KhUser;

@Component
public class KhUserKiss extends UserKiss<KhUser> {

	@Override
	protected String getUserBaseOapiUrl() {
		return getOapiUrl() + "/face/KhUser";
	}

	@Override
	protected TypeReference<ObjectFaceResponse<KhUser>> getDomainTypeReference() {
		return new TypeReference<ObjectFaceResponse<KhUser>>() {
		};
	}

	@Override
	protected TypeReference<ObjectFaceResponse<List<KhUser>>> getDomainListTypeReference() {
		return new TypeReference<ObjectFaceResponse<List<KhUser>>>() {
		};
	}

	@Override
	protected TypeReference<ObjectFaceResponse<Page<KhUser>>> getDomainPageTypeReference() {
		return new TypeReference<ObjectFaceResponse<Page<KhUser>>>() {
		};
	}

}
