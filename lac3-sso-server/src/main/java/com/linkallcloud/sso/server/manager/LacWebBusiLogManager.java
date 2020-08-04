package com.linkallcloud.sso.server.manager;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.manager.LacLogManager;
import com.linkallcloud.sso.domain.LacWebLog;
import com.linkallcloud.sso.manager.ILacWebLogManager;
import com.linkallcloud.sso.service.ILacWebLogService;

@DubboService(interfaceClass = ILacWebLogManager.class, version = "${dubbo.service.version}")
@Module(name = "Web层日志")
public class LacWebBusiLogManager extends LacLogManager<LacWebLog, ILacWebLogService> implements ILacWebLogManager {

	@Autowired
	private ILacWebLogService lacWebLogService;

	@Override
	protected ILacWebLogService service() {
		return lacWebLogService;
	}

}
