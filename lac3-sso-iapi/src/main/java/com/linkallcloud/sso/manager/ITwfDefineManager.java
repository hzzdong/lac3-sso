package com.linkallcloud.sso.manager;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.sso.domain.TwfDefine;

public interface ITwfDefineManager extends IManager<TwfDefine> {

    TwfDefine fetchByCode(Trace t, String code);

}
