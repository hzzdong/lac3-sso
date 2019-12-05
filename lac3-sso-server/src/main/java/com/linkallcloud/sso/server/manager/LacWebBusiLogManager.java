package com.linkallcloud.sso.server.manager;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.sso.domain.LacWebLog;
import com.linkallcloud.sso.manager.ILacWebLogManager;
import com.linkallcloud.sso.service.ILacWebLogService;

@Service(interfaceClass = ILacWebLogManager.class, version = "${dubbo.service.version}")
@Module(name = "Web层日志")
public class LacWebBusiLogManager extends BaseManager<LacWebLog, ILacWebLogService> implements ILacWebLogManager {

	@Autowired
	private ILacWebLogService lacWebLogService;

	@Override
	protected ILacWebLogService service() {
		return lacWebLogService;
	}

}
