package com.linkallcloud.sso.dto;

public class LockConfig {

	private int accountErrCount;
	private int accountLockPeriod;
	private int accountLockCount;

	private int ipErrCount;
	private int ipLockPeriod;
	private int ipLockCount;

	public LockConfig() {
		super();
	}

	public int getAccountErrCount() {
		return accountErrCount;
	}

	public void setAccountErrCount(int accountErrCount) {
		this.accountErrCount = accountErrCount;
	}

	public int getAccountLockPeriod() {
		return accountLockPeriod;
	}

	public void setAccountLockPeriod(int accountLockPeriod) {
		this.accountLockPeriod = accountLockPeriod;
	}

	public int getAccountLockCount() {
		return accountLockCount;
	}

	public void setAccountLockCount(int accountLockCount) {
		this.accountLockCount = accountLockCount;
	}

	public int getIpErrCount() {
		return ipErrCount;
	}

	public void setIpErrCount(int ipErrCount) {
		this.ipErrCount = ipErrCount;
	}

	public int getIpLockPeriod() {
		return ipLockPeriod;
	}

	public void setIpLockPeriod(int ipLockPeriod) {
		this.ipLockPeriod = ipLockPeriod;
	}

	public int getIpLockCount() {
		return ipLockCount;
	}

	public void setIpLockCount(int ipLockCount) {
		this.ipLockCount = ipLockCount;
	}

}
