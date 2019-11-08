package com.linkallcloud.sso.enums;

import com.linkallcloud.core.enums.EnumBase;

public enum LockBlackType implements EnumBase<Integer> {

	Account(1, "账号"), Ip(2, "Ip");

	private Integer code;
	private String message;

	LockBlackType(Integer code, String message) {
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

	public static LockBlackType get(Integer code) {
		for (LockBlackType ul : values()) {
			if (ul.getCode().equals(code)) {
				return ul;
			}
		}
		return null;
	}

}
