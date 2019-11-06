package com.linkallcloud.sso.portal.kiss.um;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.www.utils.HttpClientFactory;
import com.linkallcloud.sso.portal.kiss.BaseKiss;
import com.linkallcloud.um.domain.sys.Account;
import com.linkallcloud.um.face.account.AccountValidateRequest;
import com.linkallcloud.um.face.account.ModifyPasswordRequest;

@Component
public class AccountKiss extends BaseKiss {

	/**
	 * 根据账号名获取Account
	 * 
	 * @param account
	 * @return
	 */
	public Account fetchByAccount(Trace t, String account) {
		String sendMsgPkg = packMessage(t, account);
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/Account/fetchByAccount",
				sendMsgPkg);
		Account result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<Account>>() {
		});
		return result;
	}

	/**
	 * 根据手机号获取Account
	 * 
	 * @param mobile
	 * @return
	 */
	public Account fetchByMobile(Trace t, String mobile) {
		String sendMsgPkg = packMessage(t, mobile);
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/Account/fetchByMobile",
				sendMsgPkg);
		Account result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<Account>>() {
		});
		return result;
	}

	/**
	 * 登录验证
	 * 
	 * @param req
	 * @return
	 */
	public Account loginValidate(Trace t, AccountValidateRequest req) {
		String sendMsgPkg = packMessage(t, req);
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/Account/loginValidate",
				sendMsgPkg);
		Account result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<Account>>() {
		});
		return result;
	}

	/**
	 * 修改密码
	 * 
	 * @param req
	 * @return
	 */
	public Boolean modifyPassword(Trace t, ModifyPasswordRequest req) {
		String sendMsgPkg = packMessage(t, req);
		String responseJson = HttpClientFactory.me(false).post(umBaseUrl + "/face/Account/modifyPassword",
				sendMsgPkg);
		Boolean result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<Boolean>>() {
		});
		return result;
	}

}
