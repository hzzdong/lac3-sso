package com.linkallcloud.sso.domain;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.annotation.ShowName;

/**
 * 流程定义
 *
 */
@ShowName(value = "流程定义", logFields = "id,name")
public class TwfDefine extends Domain {
	private static final long serialVersionUID = 7362236566282024618L;

	private String code;// 编码
	private String name;// 名称
	private String remark;// 备注

	private String fieldGroupsDefine;// 流程字段分组定义json

	/*
	 * 参见：com.linkallcloud.um.twf.TwfFileType
	 */
	private int fileType;// 归档类型

	/*
	 * 参见：com.linkallcloud.um.twf.TwfStatus
	 */
	private int supervise;// 督办功能

	/*
	 * 参见：com.linkallcloud.um.twf.TwfStatus
	 */
	private int report;// 上报功能

	/*
	 * 参见：com.linkallcloud.um.twf.TwfStatus
	 */
	private int allocate;// 下派功能

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public int getSupervise() {
		return supervise;
	}

	public void setSupervise(int supervise) {
		this.supervise = supervise;
	}

	public int getReport() {
		return report;
	}

	public void setReport(int report) {
		this.report = report;
	}

	public int getAllocate() {
		return allocate;
	}

	public void setAllocate(int allocate) {
		this.allocate = allocate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFieldGroupsDefine() {
		return fieldGroupsDefine;
	}

	public void setFieldGroupsDefine(String fieldGroupsDefine) {
		this.fieldGroupsDefine = fieldGroupsDefine;
	}

}
