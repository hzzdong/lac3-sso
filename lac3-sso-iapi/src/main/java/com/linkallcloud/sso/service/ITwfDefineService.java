package com.linkallcloud.sso.service;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.sso.domain.TwfDefine;

public interface ITwfDefineService extends IService<TwfDefine> {

	TwfDefine fetchByCode(Trace t, String code);

}
