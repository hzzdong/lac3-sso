package com.linkallcloud.sso.activity;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.Lock;

public interface ILockActivity extends IActivity<Lock> {

	boolean remove(Trace t, Lock entity);

}
