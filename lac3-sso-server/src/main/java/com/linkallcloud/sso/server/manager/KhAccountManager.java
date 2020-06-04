package com.linkallcloud.sso.server.manager;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.sso.domain.KhAccount;
import com.linkallcloud.sso.manager.IKhAccountManager;
import com.linkallcloud.sso.service.IKhAccountService;

@Service(interfaceClass = IKhAccountManager.class, version = "${dubbo.service.version}")
@Module(name = "客户账号")
public class KhAccountManager extends AccountManager<KhAccount, IKhAccountService> implements IKhAccountManager {

	@Autowired
	private IKhAccountService khAccountService;

	@Override
	protected IKhAccountService service() {
		return khAccountService;
	}

}
