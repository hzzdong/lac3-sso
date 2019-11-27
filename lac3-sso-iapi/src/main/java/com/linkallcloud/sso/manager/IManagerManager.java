package com.linkallcloud.sso.manager;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.sso.domain.Manager;

public interface IManagerManager extends IManager<Manager> {

	Manager fecthByMobile(Trace t, String mobile);

	Manager fecthByLoginName(Trace t, String loginName);

	Manager loginValidate(Trace t, String loginName, String password);

	boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd);

}
