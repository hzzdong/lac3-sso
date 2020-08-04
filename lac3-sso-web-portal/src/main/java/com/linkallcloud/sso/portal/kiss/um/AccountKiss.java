package com.linkallcloud.sso.portal.kiss.um;

import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.www.utils.HttpClientFactory;
import com.linkallcloud.sso.portal.kiss.UmBaseKiss;
import com.linkallcloud.um.domain.sys.Account;
import com.linkallcloud.um.face.account.AccountValidateRequest;
import com.linkallcloud.um.face.account.ModifyPasswordRequest;

public abstract class AccountKiss<T extends Account> extends UmBaseKiss {

	protected abstract String getAccountOapiUrl();

	/**
	 * 根据账号名获取Account
	 * 
	 * @param account
	 * @return
	 */
	public T fetchByAccount(Trace t, String account) {
		String sendMsgPkg = packMessage(t, account);
		String responseJson = HttpClientFactory.me(false).post(getAccountOapiUrl() + "/fetchByAccount", sendMsgPkg);
		T result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<T>>() {
		});
		return result;
	}

	/**
	 * 根据手机号获取Account
	 * 
	 * @param mobile
	 * @return
	 */
	public T fetchByMobile(Trace t, String mobile) {
		String sendMsgPkg = packMessage(t, mobile);
		String responseJson = HttpClientFactory.me(false).post(getAccountOapiUrl() + "/fetchByMobile", sendMsgPkg);
		T result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<T>>() {
		});
		return result;
	}

	/**
	 * 登录验证
	 * 
	 * @param req
	 * @return
	 */
	public T loginValidate(Trace t, AccountValidateRequest req) {
		String sendMsgPkg = packMessage(t, req);
		String responseJson = HttpClientFactory.me(false).post(getAccountOapiUrl() + "/loginValidate", sendMsgPkg);
		T result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<T>>() {
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
		String responseJson = HttpClientFactory.me(false).post(getAccountOapiUrl() + "/modifyPassword", sendMsgPkg);
		Boolean result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<Boolean>>() {
		});
		return result;
	}

}
