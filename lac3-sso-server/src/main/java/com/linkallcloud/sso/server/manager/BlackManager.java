package com.linkallcloud.sso.server.manager;

import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.sso.domain.Black;
import com.linkallcloud.sso.exception.BlackListException;
import com.linkallcloud.sso.manager.IBlackManager;
import com.linkallcloud.sso.service.IBlackService;

@DubboService(interfaceClass = IBlackManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "黑名单")
public class BlackManager extends BaseManager<Black, IBlackService> implements IBlackManager {

	@Autowired
	private IBlackService blackService;

	@Override
	protected IBlackService service() {
		return blackService;
	}

	@Override
	public boolean blacks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av) {
		return service().blacks(t, uuidIds, remark, av);
	}

	@Override
	public boolean unBlacks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av) {
		return service().unBlacks(t, uuidIds, remark, av);
	}

	@Override
	public Black fetchBlack(Trace t, int type, String blackTarget) {
		return service().fetchBlack(t, type, blackTarget);
	}

	@Override
	public void check(Trace t, int appClazz, String blackTarget) throws BlackListException {
		service().check(t, appClazz, blackTarget);
	}

}
