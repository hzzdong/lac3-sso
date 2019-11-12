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

	@Cacheable(value = "Lac-sso-SysSetup", key = "#code")
	@Override
	public SysSetup fetchByCode(Trace t, String code) {
		return activity().fetchByCode(t, code);
	}

	@Cacheable(value = "Lac-sso-SysSetup", key = "lock_account#error_times_count")
	@Override
	public SysSetup getErrorTimesCount4AccountLock(Trace t) {
		return fetchByCode(t, "lock_account#error_times_count");
	}

	@Cacheable(value = "Lac-sso-SysSetup", key = "lock_account#period")
	@Override
	public SysSetup getPeriod4AccountLock(Trace t) {
		return fetchByCode(t, "lock_account#period");
	}

	@Cacheable(value = "Lac-sso-SysSetup", key = "lock_account#lock_times_count")
	@Override
	public SysSetup getLockTimesCount4AccountLock(Trace t) {
		return fetchByCode(t, "lock_account#lock_times_count");
	}

	@Cacheable(value = "Lac-sso-SysSetup", key = "lock_ip#error_times_count")
	@Override
	public SysSetup getErrorTimesCount4IpLock(Trace t) {
		return fetchByCode(t, "lock_ip#error_times_count");
	}

	@Cacheable(value = "Lac-sso-SysSetup", key = "lock_ip#period")
	@Override
	public SysSetup getPeriod4IpLock(Trace t) {
		return fetchByCode(t, "lock_ip#period");
	}

	@Cacheable(value = "Lac-sso-SysSetup", key = "lock_ip#lock_times_count")
	@Override
	public SysSetup getLockTimesCount4IpLock(Trace t) {
		return fetchByCode(t, "lock_ip#lock_times_count");
	}

	@CacheEvict(value = "Lac-sso-SysSetup", key = "#entity.code")
	@Override
	@ServLog(db = true)
	@Transactional(readOnly = false)
	public SysSetup save(Trace t, SysSetup entity) {
		return super.save(t, entity);
	}

}
