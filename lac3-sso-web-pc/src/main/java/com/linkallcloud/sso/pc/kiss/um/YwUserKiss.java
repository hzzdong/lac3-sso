package com.linkallcloud.sso.pc.kiss.um;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.www.utils.HttpClientFactory;
import com.linkallcloud.sso.pc.kiss.BaseKiss;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.face.account.SessionUserRequest;
import com.linkallcloud.um.face.account.UserAppRequest;
import com.linkallcloud.web.session.SessionUser;

@Component
public class YwUserKiss extends BaseKiss {

	public YwUser fecthByAccount(Trace t, String account) {
		String sendMsgPkg = packMessage(t, account);
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/YwUser/fecthByAccount",
				sendMsgPkg);
		YwUser result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<YwUser>>() {
		});
		return result;
	}

	public List<YwUser> findByMobile(Trace t, String mobile) {
		String sendMsgPkg = packMessage(t, mobile);
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/YwUser/findByMobile", sendMsgPkg);
		List<YwUser> result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<List<YwUser>>>() {
		});
		return result;
	}

	public YwUser fetchByGovCode(Trace t, String govCode) {
		String sendMsgPkg = packMessage(t, govCode);
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/YwUser/fetchByGovCode",
				sendMsgPkg);
		YwUser result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<YwUser>>() {
		});
		return result;
	}

	public YwUser fetchById(Trace t, Long id) {
		String sendMsgPkg = packMessage(t, new IdFaceRequest(id.toString(), null));
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/YwUser/fetchById", sendMsgPkg);
		YwUser result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<YwUser>>() {
		});
		return result;
	}

	public SessionUser assembleSessionUser(Trace t, String account, String appCode) {
		String sendMsgPkg = packMessage(t, new SessionUserRequest(account, appCode));
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/YwUser/assembleSessionUser",
				sendMsgPkg);
		SessionUser result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<SessionUser>>() {
		});
		return result;
	}

	public List<Tree> getUserAppMenu(Trace t, Long userId, Long appId) {
		String sendMsgPkg = packMessage(t, new UserAppRequest(userId, appId));
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/YwUser/getUserAppMenu",
				sendMsgPkg);
		List<Tree> result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<List<Tree>>>() {
		});
		return result;
	}

	public String[] getUserAppPermissions4Menu(Trace t, Long userId, Long appId) {
		String sendMsgPkg = packMessage(t, new UserAppRequest(userId, appId));
		String responseJson = HttpClientFactory.me(false)
				.post(umBaseUrl + "/face/YwUser/getUserAppPermissions4Menu", sendMsgPkg);
		String[] result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<String[]>>() {
		});
		return result;
	}

}
