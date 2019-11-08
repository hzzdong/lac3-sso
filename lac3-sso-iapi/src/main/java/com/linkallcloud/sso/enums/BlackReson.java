package com.linkallcloud.sso.enums;

import com.linkallcloud.core.enums.EnumBase;

public enum BlackReson implements EnumBase<Integer> {

	BlackByLock(1, "自动加黑"), BlackByHand(2, "手工加黑"), UnBlackByHand(21, "手工除黑"), UnBlackBySelfAuth(22, "自助验证除黑");

	private Integer code;
	private String message;

	BlackReson(Integer code, String message) {
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

	public static BlackReson get(Integer code) {
		for (BlackReson ul : values()) {
			if (ul.getCode().equals(code)) {
				return ul;
			}
		}
		return null;
	}

}
