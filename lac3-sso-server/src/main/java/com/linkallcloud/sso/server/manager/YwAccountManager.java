package com.linkallcloud.sso.server.manager;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.sso.domain.YwAccount;
import com.linkallcloud.sso.manager.IYwAccountManager;
import com.linkallcloud.sso.service.IYwAccountService;

@Service(interfaceClass = IYwAccountManager.class, version = "${dubbo.service.version}")
@Module(name = "运维账号")
public class YwAccountManager extends AccountManager<YwAccount, IYwAccountService> implements IYwAccountManager {

	@Autowired
	private IYwAccountService ywAccountService;

	@Override
	protected IYwAccountService service() {
		return ywAccountService;
	}

}
