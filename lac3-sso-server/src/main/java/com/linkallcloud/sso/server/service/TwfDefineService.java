package com.linkallcloud.sso.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.sso.activity.ITwfDefineActivity;
import com.linkallcloud.sso.domain.TwfDefine;
import com.linkallcloud.sso.service.ITwfDefineService;

@Service
@Transactional(readOnly = true)
public class TwfDefineService extends BaseService<TwfDefine, ITwfDefineActivity> implements ITwfDefineService {

	@Autowired
	private ITwfDefineActivity twfDefineActivity;

	public TwfDefineService() {
		super();
	}

	@Override
	public ITwfDefineActivity activity() {
		return twfDefineActivity;
	}

	@Override
	public TwfDefine fetchByCode(Trace t, String code) {
		return activity().fetchByCode(t, code);
	}

}
