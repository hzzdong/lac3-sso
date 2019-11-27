package com.linkallcloud.sso.server.manager;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.sso.domain.Manager;
import com.linkallcloud.sso.manager.IManagerManager;
import com.linkallcloud.sso.service.IManagerService;

@Service(interfaceClass = IManagerManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "管理员")
public class ManagerManager extends BaseManager<Manager, IManagerService> implements IManagerManager {

    @Autowired
    private IManagerService managerService;

    @Override
    protected IManagerService service() {
        return managerService;
    }

	@Override
	public Manager fecthByMobile(Trace t, String mobile) {
		return service().fecthByMobile(t, mobile);
	}

	@Override
	public Manager fecthByLoginName(Trace t, String loginName) {
		return service().fecthByLoginName(t, loginName);
	}

	@Override
	public Manager loginValidate(Trace t, String loginName, String password) {
		return service().loginValidate(t, loginName, password);
	}

	@Override
	public boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd) {
		return service().updatePassword(t, id, uuid, oldPwd, newPwd);
	}

}
