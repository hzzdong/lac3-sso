package com.linkallcloud.sso.server.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.activity.ITwfDefineActivity;
import com.linkallcloud.sso.domain.TwfDefine;
import com.linkallcloud.sso.server.dao.ITwfDefineDao;

@Component
public class TwfDefineActivity extends BaseActivity<TwfDefine, ITwfDefineDao> implements ITwfDefineActivity {

	@Autowired
	private ITwfDefineDao twfDefineDao;

	public TwfDefineActivity() {
		super();
	}

	@Override
	public ITwfDefineDao dao() {
		return twfDefineDao;
	}

	@Override
	public TwfDefine fetchByCode(Trace t, String code) {
		return dao().fetchByCode(t, code);
	}

}
