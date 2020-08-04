package com.linkallcloud.sso.server.manager;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.sso.domain.AppLoginHis;
import com.linkallcloud.sso.manager.IAppLoginHisManager;
import com.linkallcloud.sso.service.IAppLoginHisService;

@DubboService(interfaceClass = IAppLoginHisManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "应用登录日志")
public class AppLoginHisManager extends BaseManager<AppLoginHis, IAppLoginHisService> implements IAppLoginHisManager {

	@Autowired
	private IAppLoginHisService appLoginHisService;

	@Override
	protected IAppLoginHisService service() {
		return appLoginHisService;
	}

	@Override
	public void loginSuccess(Trace t, AppLoginHis entity) {
		try {
			service().insert(t, entity);
		} catch (Throwable e) {
			log.errorf("记录应用登录成功日志失败：%s", entity.getLoginname());
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public List<Tree> findByTgt(Trace t, String tgt) {
		return service().findByTgt(t, tgt);
	}

}
