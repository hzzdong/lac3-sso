package com.linkallcloud.sso.oapi.kiss.um;

import java.util.List;

import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ListFaceRequest;
import com.linkallcloud.core.face.message.request.PageFaceRequest;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.www.utils.HttpClientFactory;
import com.linkallcloud.sso.oapi.kiss.UmBaseKiss;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.face.account.SessionUserRequest;
import com.linkallcloud.um.face.account.UserAppRequest;
import com.linkallcloud.web.session.SessionUser;

public abstract class UserKiss<T extends User> extends UmBaseKiss {

	protected abstract String getUserBaseOapiUrl();

	protected abstract TypeReference<ObjectFaceResponse<T>> getDomainTypeReference();

	protected abstract TypeReference<ObjectFaceResponse<List<T>>> getDomainListTypeReference();

	protected abstract TypeReference<ObjectFaceResponse<Page<T>>> getDomainPageTypeReference();

	public T fecthByAccount(Trace t, String account) {
		String sendMsgPkg = packMessage(t, account);
		String responseJson = HttpClientFactory.me(false).post(getUserBaseOapiUrl() + "/fecthByAccount", sendMsgPkg);
		T result = unpackMessage(responseJson, getDomainTypeReference());
		return result;
	}

	public List<T> findByMobile(Trace t, String mobile) {
		String sendMsgPkg = packMessage(t, mobile);
		String responseJson = HttpClientFactory.me(false).post(getUserBaseOapiUrl() + "/findByMobile", sendMsgPkg);
		List<T> result = unpackMessage(responseJson, getDomainListTypeReference());
		return result;
	}

	public T fetchByGovCode(Trace t, String govCode) {
		String sendMsgPkg = packMessage(t, govCode);
		String responseJson = HttpClientFactory.me(false).post(getUserBaseOapiUrl() + "/fetchByGovCode", sendMsgPkg);
		T result = unpackMessage(responseJson, getDomainTypeReference());
		return result;
	}

	public T fetchById(Trace t, Long id) {
		String sendMsgPkg = packMessage(t, new IdFaceRequest(id, null));
		String responseJson = HttpClientFactory.me(false).post(getUserBaseOapiUrl() + "/fetchById", sendMsgPkg);
		T result = unpackMessage(responseJson, getDomainTypeReference());
		return result;
	}

	public SessionUser assembleSessionUser(Trace t, String account, String appCode, Sid org) {
		String sendMsgPkg = packMessage(t, new SessionUserRequest(account, appCode, org));
		String responseJson = HttpClientFactory.me(false).post(getUserBaseOapiUrl() + "/assembleSessionUser",
				sendMsgPkg);
		SessionUser result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<SessionUser>>() {
		});
		return result;
	}

	public List<Tree> getUserAppMenu(Trace t, Long userId, Long appId, Long loginCompanyId) {
		String sendMsgPkg = packMessage(t, new UserAppRequest(userId, appId, loginCompanyId));
		String responseJson = HttpClientFactory.me(false).post(getUserBaseOapiUrl() + "/getUserAppMenu", sendMsgPkg);
		List<Tree> result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<List<Tree>>>() {
		});
		return result;
	}

	public String[] getUserAppPermissions4Menu(Trace t, Long userId, Long appId, Long loginCompanyId) {
		String sendMsgPkg = packMessage(t, new UserAppRequest(userId, appId, loginCompanyId));
		String responseJson = HttpClientFactory.me(false).post(getUserBaseOapiUrl() + "/getUserAppMenuRes", sendMsgPkg);
		String[] result = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<String[]>>() {
		});
		return result;
	}

	/**
	 * 根据用户得到用户所在公司下一级部门ID（省、地市一级部门挂在 -bm 部门下，区县直接挂在公司下）
	 * 
	 * @param user
	 * @return
	 */
	public Long parseUserTopDepartmentId(Trace t, User user) {
		String govCode = getCompanyGovCodeById(t, user.getCompanyId(), user.getClass().getSimpleName().substring(0, 2));
		if (!Strings.isBlank(govCode)) {
			return parseUserTopDepartmentId(govCode, user.getCode());
		}
		return null;
	}

	/**
	 * 根据用户code得到用户所在公司下一级部门ID（省、地市一级部门挂在 -bm 部门下，区县直接挂再公司下）
	 * 
	 * @param userCode
	 * @return
	 */
	public static Long parseUserTopDepartmentId(String companyGovCode, String userCode) {// 1#6#68-24-100-200-39@
		if (!Strings.isBlank(companyGovCode) && !Strings.isBlank(userCode)) {
			if (companyGovCode.length() < 6) {// 省级、地市级:一级部门挂在 -bm 部门下
				return parseUserLevel2DepartmentId(userCode);
			} else {// 区县级
				return parseUserLevel1DepartmentId(userCode);
			}
		}
		return null;
	}

	/**
	 * 得到用户所在公司所在的第一级部门id
	 * 
	 * @param userCode
	 * @return
	 */
	private static Long parseUserLevel1DepartmentId(String userCode) {
		if (!Strings.isBlank(userCode)) {
			// 1#6#15#25#57#58#1311-242@
			int idx = userCode.lastIndexOf("#");
			int lidx = userCode.indexOf("-");
			if (idx != -1 && lidx != -1) {
				return Long.valueOf(userCode.substring(idx + 1, lidx));// 68
			}
		}
		return null;
	}

	/**
	 * 得到用户所在公司所在的第二级部门id
	 * 
	 * @param userCode
	 * @return
	 */
	private static Long parseUserLevel2DepartmentId(String userCode) {
		if (!Strings.isBlank(userCode)) {
			int idx = userCode.indexOf("-");
			int lidx = userCode.lastIndexOf("-");
			if (idx != -1 && lidx != -1) {
				if (idx == lidx) {// 是一个一级部门， 1#6#68-39@
					int lidx2 = userCode.lastIndexOf("#");
					if (lidx2 != -1) {
						return Long.valueOf(userCode.substring(lidx2 + 1, idx));// 68
					}
				} else {// 1#6#68-24-100-200-39@
					String temp = userCode.substring(idx + 1);// 24-100-200-39@
					int idx2 = temp.indexOf("-");
					if (idx2 != -1) {
						return Long.valueOf(temp.substring(0, idx2));// 24
					}
				}
			}
		}
		return null;
	}

	public List<T> find(Trace t, ListFaceRequest req) {
		String sendMsgPkg = packMessage(t, req);
		String responseJson = HttpClientFactory.me(false).post(getUserBaseOapiUrl() + "/find", sendMsgPkg);
		List<T> result = unpackMessage(responseJson, getDomainListTypeReference());
		return result;
	}

	public Page<T> findPage(Trace t, PageFaceRequest req) {
		String sendMsgPkg = packMessage(t, req);
		String responseJson = HttpClientFactory.me(false).post(getUserBaseOapiUrl() + "/findPage", sendMsgPkg);
		Page<T> result = unpackMessage(responseJson, getDomainPageTypeReference());
		return result;
	}

	public Page<T> findPage4Select(Trace t, PageFaceRequest req) {
		String sendMsgPkg = packMessage(t, req);
		String responseJson = HttpClientFactory.me(false).post(getUserBaseOapiUrl() + "/findPage4Select", sendMsgPkg);
		Page<T> result = unpackMessage(responseJson, getDomainPageTypeReference());
		return result;
	}

//	public List<T> findByRoleCompany(Trace t, Long companyId, Long roleId) {
//		String sendMsgPkg = packMessage(t, new CompanyEntityRequest(companyId, roleId));
//		String responseJson = HttpClientFactory.me(false).post(getUserBaseOapiUrl() + "/findByRoleCompany", sendMsgPkg);
//		List<T> result = unpackMessage(responseJson, getDomainListTypeReference());
//		return result;
//	}

}
