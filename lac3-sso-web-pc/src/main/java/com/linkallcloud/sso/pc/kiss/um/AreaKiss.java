package com.linkallcloud.sso.pc.kiss.um;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ListFaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.www.utils.HttpClientFactory;
import com.linkallcloud.sso.pc.kiss.BaseKiss;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.face.area.ParentCodeAreaRequest;

@Component
public class AreaKiss extends BaseKiss {

	public List<Tree> getTreeNodes(Trace t, Boolean valid) {
		String sendMsgPkg = packMessage(t, new ObjectFaceRequest<Boolean>(valid));
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/Area/getTreeNodes", sendMsgPkg);
		List<Tree> result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<List<Tree>>>() {
		});
		return result;
	}

	public Area fetchById(Trace t, Long id) {
		String sendMsgPkg = packMessage(t, new IdFaceRequest(id.toString(), null));
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/Area/fetchById", sendMsgPkg);
		Area result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<Area>>() {
		});
		return result;
	}

	public Area fetchByGovCode(Trace t, String govCode) {
		String sendMsgPkg = packMessage(t, govCode);
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/Area/fetchByGovCode", sendMsgPkg);
		Area result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<Area>>() {
		});
		return result;
	}

	public List<Area> find(Trace t, ListFaceRequest req) {
		String sendMsgPkg = packMessage(t, req);
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/Area/find", sendMsgPkg);
		List<Area> result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<List<Area>>>() {
		});
		return result;
	}

	public List<Area> findChildren(Trace t, ParentCodeAreaRequest req) {
		String sendMsgPkg = packMessage(t, req);
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/Area/findChildren", sendMsgPkg);
		List<Area> result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<List<Area>>>() {
		});
		return result;
	}

}
