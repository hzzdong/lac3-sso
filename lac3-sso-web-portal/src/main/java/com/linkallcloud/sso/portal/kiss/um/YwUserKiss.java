package com.linkallcloud.sso.portal.kiss.um;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.domain.party.YwUser;

@Component
public class YwUserKiss extends UserKiss<YwUser> {

	@Override
	protected String getUserBaseOapiUrl() {
		return getOapiUrl() + "/face/YwUser";
	}

	@Override
	protected TypeReference<ObjectFaceResponse<YwUser>> getDomainTypeReference() {
		return new TypeReference<ObjectFaceResponse<YwUser>>() {
		};
	}

	@Override
	protected TypeReference<ObjectFaceResponse<List<YwUser>>> getDomainListTypeReference() {
		return new TypeReference<ObjectFaceResponse<List<YwUser>>>() {
		};
	}

	@Override
	protected TypeReference<ObjectFaceResponse<Page<YwUser>>> getDomainPageTypeReference() {
		return new TypeReference<ObjectFaceResponse<Page<YwUser>>>() {
		};
	}

}
