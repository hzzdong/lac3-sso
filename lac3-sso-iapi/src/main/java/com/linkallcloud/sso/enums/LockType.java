package com.linkallcloud.sso.enums;

import com.linkallcloud.core.enums.EnumBase;

public enum LockType implements EnumBase<Integer> {

	AccountLock(1, "账号锁"), IpLock(2, "Ip锁");

	private Integer code;
	private String message;

	LockType(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public static LockType get(Integer code) {
		for (LockType ul : values()) {
			if (ul.getCode().equals(code)) {
				return ul;
			}
		}
		return null;
	}

}
