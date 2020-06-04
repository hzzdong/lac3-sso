package com.linkallcloud.sso.portal.kiss.um;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.www.utils.HttpClientFactory;
import com.linkallcloud.sso.portal.kiss.BaseKiss;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.face.account.SessionUserRequest;
import com.linkallcloud.um.face.account.UserAppRequest;
import com.linkallcloud.web.session.SessionUser;

@Component
public class KhUserKiss extends BaseKiss {

	public KhUser fecthByAccount(Trace t, String account) {
		String sendMsgPkg = packMessage(t, account);
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/KhUser/fecthByAccount", sendMsgPkg);
		KhUser result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<KhUser>>() {
		});
		return result;
	}

	public List<KhUser> findByMobile(Trace t, String mobile) {
		String sendMsgPkg = packMessage(t, mobile);
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/KhUser/findByMobile", sendMsgPkg);
		List<KhUser> result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<List<KhUser>>>() {
		});
		return result;
	}

	public KhUser fetchByGovCode(Trace t, String govCode) {
		String sendMsgPkg = packMessage(t, govCode);
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/KhUser/fetchByGovCode", sendMsgPkg);
		KhUser result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<KhUser>>() {
		});
		return result;
	}

	public KhUser fetchById(Trace t, Long id) {
		String sendMsgPkg = packMessage(t, new IdFaceRequest(id, null));
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/KhUser/fetchById", sendMsgPkg);
		KhUser result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<KhUser>>() {
		});
		return result;
	}

	public SessionUser assembleSessionUser(Trace t, String account, String appCode) {
		String sendMsgPkg = packMessage(t, new SessionUserRequest(account, appCode));
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/KhUser/assembleSessionUser",
				sendMsgPkg);
		SessionUser result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<SessionUser>>() {
		});
		return result;
	}

	public List<Tree> getUserAppMenu(Trace t, Long userId, Long appId) {
		String sendMsgPkg = packMessage(t, new UserAppRequest(userId, appId));
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/KhUser/getUserAppMenu", sendMsgPkg);
		List<Tree> result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<List<Tree>>>() {
		});
		return result;
	}

	public String[] getUserAppPermissions4Menu(Trace t, Long userId, Long appId) {
		String sendMsgPkg = packMessage(t, new UserAppRequest(userId, appId));
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/KhUser/getUserAppPermissions4Menu",
				sendMsgPkg);
		String[] result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<String[]>>() {
		});
		return result;
	}

}
