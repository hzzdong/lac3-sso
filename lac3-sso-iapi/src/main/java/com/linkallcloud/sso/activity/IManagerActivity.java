package com.linkallcloud.sso.activity;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.Manager;

public interface IManagerActivity extends IActivity<Manager> {

	Manager fecthByMobile(Trace t, String mobile);

	Manager fecthByLoginName(Trace t, String loginName);

	Manager loginValidate(Trace t, String loginName, String password);

	boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd);

}
