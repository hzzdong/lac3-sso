package com.linkallcloud.sso.server.activity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.activity.IAppLoginHisActivity;
import com.linkallcloud.sso.domain.AppLoginHis;
import com.linkallcloud.sso.server.dao.IAppLoginHisDao;

@Component
public class AppLoginHisActivity extends BaseActivity<AppLoginHis, IAppLoginHisDao> implements IAppLoginHisActivity {

	@Autowired
	private IAppLoginHisDao appLoginHisDao;

	@Override
	public IAppLoginHisDao dao() {
		return appLoginHisDao;
	}

	@Override
	public List<AppLoginHis> findEntitiesByTgt(Trace t, String tgt) {
		return dao().findEntitiesByTgt(t, tgt);
	}

}
