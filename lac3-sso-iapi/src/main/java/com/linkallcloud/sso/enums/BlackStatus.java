package com.linkallcloud.sso.enums;

import com.linkallcloud.core.enums.EnumBase;

public enum BlackStatus implements EnumBase<Integer> {

	Black(0, "已加黑"), UnBlack(1, "已除黑");

	private Integer code;
	private String message;

	BlackStatus(Integer code, String message) {
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

	public static BlackStatus get(Integer code) {
		for (BlackStatus ul : values()) {
			if (ul.getCode().equals(code)) {
				return ul;
			}
		}
		return null;
	}

}
