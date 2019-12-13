package com.linkallcloud.sso.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.busilog.annotation.ServLog;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.sso.activity.ISysSetupActivity;
import com.linkallcloud.sso.domain.SysSetup;
import com.linkallcloud.sso.service.ISysSetupService;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysSetupService extends BaseService<SysSetup, ISysSetupActivity> implements ISysSetupService {

	@Autowired
	private ISysSetupActivity sysSetupActivity;

	@Override
	public ISysSetupActivity activity() {
		return sysSetupActivity;
	}

	//@Cacheable(value = "SysSetup", key = "#code")
	@Override
	public SysSetup fetchByCode(Trace t, String code) {
		return activity().fetchByCode(t, code);
	}

//	//@Cacheable(value = "SysSetup", key = "lock_account_error_times_count")
//	@Override
//	public SysSetup getErrorTimesCount4AccountLock(Trace t) {
//		return fetchByCode(t, "lock_account_error_times_count");
//	}
//
//	@Cacheable(value = "SysSetup", key = "lock_account_period")
//	@Override
//	public SysSetup getPeriod4AccountLock(Trace t) {
//		return fetchByCode(t, "lock_account_period");
//	}
//
//	@Cacheable(value = "SysSetup", key = "black_account_lock_times_count")
//	@Override
//	public SysSetup getLockTimesCount4AccountLock(Trace t) {
//		return fetchByCode(t, "black_account_lock_times_count");
//	}
//
//	@Cacheable(value = "SysSetup", key = "lock_ip_error_times_count")
//	@Override
//	public SysSetup getErrorTimesCount4IpLock(Trace t) {
//		return fetchByCode(t, "lock_ip_error_times_count");
//	}
//
//	@Cacheable(value = "SysSetup", key = "lock_ip_period")
//	@Override
//	public SysSetup getPeriod4IpLock(Trace t) {
//		return fetchByCode(t, "lock_ip_period");
//	}
//
//	@Cacheable(value = "SysSetup", key = "black_ip_lock_times_count")
//	@Override
//	public SysSetup getLockTimesCount4IpLock(Trace t) {
//		return fetchByCode(t, "black_ip_lock_times_count");
//	}

	//@CacheEvict(value = "SysSetup", key = "#entity.code")
	@Override
	@ServLog(db = true)
	@Transactional(readOnly = false)
	public SysSetup save(Trace t, SysSetup entity) {
		return super.save(t, entity);
	}

}
