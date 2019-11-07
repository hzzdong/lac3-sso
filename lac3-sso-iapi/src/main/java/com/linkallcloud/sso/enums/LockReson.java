package com.linkallcloud.sso.enums;

import com.linkallcloud.core.enums.EnumBase;

public enum LockReson implements EnumBase<Integer> {

	LockByLoginFailure(1, "自动锁定"), LockByHand(2, "手工锁定"), UnLockByTime(21, "自动解锁(到期)"),
	UnLockByLoginSuccess(22, "自动解锁(登录成功)"), UnLockByHand(23, "手工解锁"), UnLockBySelfAuth(24, "自助验证解锁");

	private Integer code;
	private String message;

	LockReson(Integer code, String message) {
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

	public static LockReson get(Integer code) {
		for (LockReson ul : values()) {
			if (ul.getCode().equals(code)) {
				return ul;
			}
		}
		return null;
	}

}
