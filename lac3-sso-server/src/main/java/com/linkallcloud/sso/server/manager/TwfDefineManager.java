package com.linkallcloud.sso.server.manager;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.sso.domain.TwfDefine;
import com.linkallcloud.sso.manager.ITwfDefineManager;
import com.linkallcloud.sso.service.ITwfDefineService;

@Service(interfaceClass = ITwfDefineManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "流程活动")
public class TwfDefineManager extends BaseManager<TwfDefine, ITwfDefineService>
		implements ITwfDefineManager {

	@Autowired
	private ITwfDefineService twfDefineService;

	@Override
	protected ITwfDefineService service() {
		return twfDefineService;
	}

	@Override
	public TwfDefine fetchByCode(Trace t, String code) {
		return service().fetchByCode(t, code);
	}

}
