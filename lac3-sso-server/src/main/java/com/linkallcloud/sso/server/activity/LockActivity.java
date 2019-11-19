package com.linkallcloud.sso.server.activity;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.activity.ILockActivity;
import com.linkallcloud.sso.domain.Lock;
import com.linkallcloud.sso.domain.LockHis;
import com.linkallcloud.sso.enums.LockStatus;
import com.linkallcloud.sso.server.dao.ILockDao;
import com.linkallcloud.sso.server.dao.ILockHisDao;

@Component
public class LockActivity extends BaseActivity<Lock, ILockDao> implements ILockActivity {

	@Autowired
	private ILockDao lockDao;

	@Autowired
	private ILockHisDao lockHisDao;

	@Override
	public ILockDao dao() {
		return lockDao;
	}

	@Override
	public Long insert(Trace t, Lock entity) {
		Long id = super.insert(t, entity);
		saveLockHis(t, entity);
		return id;
	}

	@Override
	public boolean update(Trace t, Lock entity) {
		boolean ret = super.update(t, entity);
		saveLockHis(t, entity);
		return ret;
	}

	@Override
	public boolean remove(Trace t, Lock entity) {
		boolean ret = super.delete(t, entity.getId(), entity.getUuid());
		
		entity.setStatus(LockStatus.UnLock.getCode());
		entity.setLockedTime(new Date());
		entity.setCount(0);
		entity.setErr(0);
		saveLockHis(t, entity);
		
		return ret;
	}

	private void saveLockHis(Trace t, Lock entity) {
		LockHis his = new LockHis(entity);
		lockHisDao.insert(t, his);
	}

}
