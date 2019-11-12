package com.linkallcloud.sso.server.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.activity.ISysSetupActivity;
import com.linkallcloud.sso.domain.SysSetup;
import com.linkallcloud.sso.exception.ArgException;
import com.linkallcloud.sso.server.dao.ISysSetupDao;

@Component
public class SysSetupActivity extends BaseActivity<SysSetup, ISysSetupDao> implements ISysSetupActivity {

	@Autowired
	private ISysSetupDao sysSetupDao;

	@Override
	public ISysSetupDao dao() {
		return sysSetupDao;
	}

	@Override
	protected void before(Trace t, boolean isNew, SysSetup entity) {
		super.before(t, isNew, entity);
		if (exist(t, entity, "code", entity.getCode())) {
			throw new ArgException(ArgException.ARG_CODE_ARG, "编号重复！");
		}
	}

	@Override
	public SysSetup fetchByCode(Trace t, String code) {
		return dao().fetchByCode(t, code);
	}

}
