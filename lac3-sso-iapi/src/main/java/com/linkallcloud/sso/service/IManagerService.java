package com.linkallcloud.sso.service;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.sso.domain.Manager;

public interface IManagerService extends IService<Manager> {
	
	Manager fecthByMobile(Trace t, String mobile);

	Manager fecthByLoginName(Trace t, String loginName);

	Manager loginValidate(Trace t, String loginName, String password);

	boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd);

}
