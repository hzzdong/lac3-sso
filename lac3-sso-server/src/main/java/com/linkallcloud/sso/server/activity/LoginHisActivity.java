package com.linkallcloud.sso.server.activity;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.security.Securities;
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

}
