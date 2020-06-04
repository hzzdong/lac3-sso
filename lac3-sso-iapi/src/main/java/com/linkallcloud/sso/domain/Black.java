package com.linkallcloud.sso.domain;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.annotation.ShowName;
import com.linkallcloud.sh.tuils.Dates;
import com.linkallcloud.sso.enums.BlackReson;
import com.linkallcloud.sso.enums.LockBlackType;

@ShowName(value = "黑名单", logFields = "id,blackTarget")
public class Black extends Domain {
	private static final long serialVersionUID = 5750266573536145328L;

	private String blackTarget;// 加黑目标
	private int appClazz;// 加黑应用类别，0：运维，1：客户
	private int type;// 加黑类型LockBlackType
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date blackTime;// 加黑时间
	private int reason;// 加黑原因
	private String operator;// 操作者
	private String remark;// 描述

	public Black() {
		super();
	}

	public Black(int type, int appClazz, String blackTarget, int status, int reason, String operator, String remark) {
		super();
		this.status = status;
		this.type = type;
		this.appClazz = appClazz;
		this.blackTarget = blackTarget;
		this.blackTime = new Date();
		this.reason = reason;
		this.operator = operator;
		this.remark = remark;
	}

	public String getBlackTarget() {
		return blackTarget;
	}

	public void setBlackTarget(String blackTarget) {
		this.blackTarget = blackTarget;
	}

	public int getType() {
		return type;
	}

	public String getTypeStr() {
		if (type < 1) {
			return "";
		} else {
			return LockBlackType.get(type).getMessage();
		}
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAppClazz() {
		return appClazz;
	}

	public void setAppClazz(int appClazz) {
		this.appClazz = appClazz;
	}

	public Date getBlackTime() {
		return blackTime;
	}

	public String getBlackTimeStr() {
		if (blackTime == null) {
			return "";
		} else {
			return Dates.formatDate(blackTime, "yyyy-MM-dd HH:mm:ss");
		}
	}

	public void setBlackTime(Date blackTime) {
		this.blackTime = blackTime;
	}

	public int getReason() {
		return reason;
	}

	public String getReasonStr() {
		if (reason < 1) {
			return "";
		} else {
			return BlackReson.get(reason).getMessage();
		}
	}

	public void setReason(int reason) {
		this.reason = reason;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
