package com.linkallcloud.sso.oapi.enums;

import com.linkallcloud.core.enums.EnumBase;

public enum MappingType implements EnumBase<Integer> {

	Unified(1, "统一账号"), Mapping(2, "账号映射");

	private Integer code;
	private String message;

	MappingType(Integer code, String message) {
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

	public static MappingType get(Integer code) {
		for (MappingType ul : values()) {
			if (ul.getCode().equals(code)) {
				return ul;
			}
		}
		return null;
	}

}
