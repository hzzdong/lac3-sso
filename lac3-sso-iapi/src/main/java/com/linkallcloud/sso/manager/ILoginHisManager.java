package com.linkallcloud.sso.manager;

import com.linkallcloud.core.dto.Client;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.sso.domain.LoginHis;

public interface ILoginHisManager extends IManager<LoginHis> {

	void loginSuccess(Trace t, String username, String ip, String from, String serviceId, String md5Tgt, Client client);

	//void loginFailure(Trace t, String username, String ip, String from, String serviceId);
	
	void logout(Trace t, String md5Tgt);
	void logout(Trace t, Long id);
	LoginHis fetchByTgt(Trace t, String md5Tgt);

	Boolean offline(Trace t, Long id, String uuid);

}
