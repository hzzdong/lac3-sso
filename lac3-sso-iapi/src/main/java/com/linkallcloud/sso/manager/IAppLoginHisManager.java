package com.linkallcloud.sso.manager;

import java.util.List;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.sso.domain.AppLoginHis;

public interface IAppLoginHisManager extends IManager<AppLoginHis> {

	void loginSuccess(Trace t, AppLoginHis entity);

	List<Tree> findByTgt(Trace t, String tgt);

}
