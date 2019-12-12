package com.linkallcloud.sso.server.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.activity.ILoginHisActivity;
import com.linkallcloud.sso.domain.LoginHis;
import com.linkallcloud.sso.server.dao.ILoginHisDao;

@Component
public class LoginHisActivity extends BaseActivity<LoginHis, ILoginHisDao> implements ILoginHisActivity {

	@Autowired
	private ILoginHisDao loginHisDao;

	@Override
	public ILoginHisDao dao() {
		return loginHisDao;
	}

	@Override
	public void logout(Trace t, String md5Tgt) {
		LoginHis entity = dao().fetchByTgt(t, md5Tgt);
		if (entity != null) {
			dao().logout(t, entity.getId());
		}
	}

	@Override
	public void logout(Trace t, Long id) {
		dao().logout(t, id);
	}

	@Override
	public LoginHis fetchByTgt(Trace t, String md5Tgt) {
		return dao().fetchByTgt(t, md5Tgt);
	}

}
