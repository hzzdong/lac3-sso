package com.linkallcloud.sso.server.manager;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Client;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.sso.domain.LoginHis;
import com.linkallcloud.sso.manager.ILoginHisManager;
import com.linkallcloud.sso.service.ILoginHisService;

@Service(interfaceClass = ILoginHisManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "登录日志")
public class LoginHisManager extends BaseManager<LoginHis, ILoginHisService> implements ILoginHisManager {

	@Autowired
	private ILoginHisService loginHisService;

	@Override
	protected ILoginHisService service() {
		return loginHisService;
	}

	@Override
	public void loginSuccess(Trace t, String username, String ip, String from, String serviceId, String md5Tgt,
			Client client) {
		try {
			LoginHis entity = new LoginHis(username, ip, from, serviceId, md5Tgt, client);
			service().insert(t, entity);
		} catch (Throwable e) {
			log.errorf("记录登录成功日志失败：%s", username);
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void logout(Trace t, String md5Tgt) {
		service().logout(t, md5Tgt);
	}

	@Override
	public void logout(Trace t, Long id) {
		service().logout(t, id);
	}

	@Override
	public LoginHis fetchByTgt(Trace t, String md5Tgt) {
		return service().fetchByTgt(t, md5Tgt);
	}

	@Override
	public Boolean offline(Trace t, Long id, String uuid) {
		return service().offline(t, id, uuid);
	}

}
