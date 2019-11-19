package com.linkallcloud.sso.server.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.SysSetup;
import com.linkallcloud.sso.dto.LockConfig;
import com.linkallcloud.sso.service.ISysSetupService;

@Component
public class LockConfigure {

	@Autowired
	private ISysSetupService sysSetupService;

	public LockConfig loadLockConfig(Trace t) {
		LockConfig conf = new LockConfig();

		SysSetup errSetup4Account = getErrorTimesCount4AccountLock(t);
		int accountErrCount = Integer.parseInt(errSetup4Account.getValue());
		conf.setAccountErrCount(accountErrCount);
		SysSetup periodSetup4Account = getPeriod4AccountLock(t);
		int accountLockPeriod = Integer.parseInt(periodSetup4Account.getValue());
		conf.setAccountLockPeriod(accountLockPeriod);
		SysSetup blackCountSetup4Account = getLockTimesCount4AccountLock(t);
		int accountLockCount = Integer.parseInt(blackCountSetup4Account.getValue());
		conf.setAccountLockCount(accountLockCount);

		SysSetup errSetup4Ip = getErrorTimesCount4IpLock(t);
		int ipErrCount = Integer.parseInt(errSetup4Ip.getValue());
		conf.setIpErrCount(ipErrCount);
		SysSetup periodSetup4Ip = getPeriod4IpLock(t);
		int ipLockPeriod = Integer.parseInt(periodSetup4Ip.getValue());
		conf.setIpLockPeriod(ipLockPeriod);
		SysSetup blackCountSetup4Ip = getLockTimesCount4IpLock(t);
		int ipLockCount = Integer.parseInt(blackCountSetup4Ip.getValue());
		conf.setIpLockCount(ipLockCount);

		return conf;
	}

	public SysSetup getErrorTimesCount4AccountLock(Trace t) {
		return sysSetupService.fetchByCode(t, "lock_account_error_times_count");
	}

	public SysSetup getPeriod4AccountLock(Trace t) {
		return sysSetupService.fetchByCode(t, "lock_account_period");
	}

	public SysSetup getLockTimesCount4AccountLock(Trace t) {
		return sysSetupService.fetchByCode(t, "black_account_lock_times_count");
	}

	public SysSetup getErrorTimesCount4IpLock(Trace t) {
		return sysSetupService.fetchByCode(t, "lock_ip_error_times_count");
	}

	public SysSetup getPeriod4IpLock(Trace t) {
		return sysSetupService.fetchByCode(t, "lock_ip_period");
	}

	public SysSetup getLockTimesCount4IpLock(Trace t) {
		return sysSetupService.fetchByCode(t, "black_ip_lock_times_count");
	}

}
