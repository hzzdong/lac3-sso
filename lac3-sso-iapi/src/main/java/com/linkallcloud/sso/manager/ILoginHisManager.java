package com.linkallcloud.sso.manager;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.sso.domain.LoginHis;

public interface ILoginHisManager extends IManager<LoginHis> {

	void loginSuccess(Trace t, String username, String ip, String from, String serviceId);

	//void loginFailure(Trace t, String username, String ip, String from, String serviceId);

}
