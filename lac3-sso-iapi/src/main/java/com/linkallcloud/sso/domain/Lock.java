package com.linkallcloud.sso.domain;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.annotation.ShowName;
import com.linkallcloud.sh.tuils.Dates;
import com.linkallcloud.sso.enums.LockBlackType;
import com.linkallcloud.sso.enums.LockReson;

@ShowName(value = "锁", logFields = "id,lockedTarget")
public class Lock extends Domain {
	private static final long serialVersionUID = -8765203997616550354L;

	private String lockedTarget;// 锁定目标
	private int type;// 锁定类型
	private int count;// 锁定次数
	private int err;// 错误次数
	private int reason;// 锁定原因
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date lockedTime;// 锁定时间
	private String operator;// 操作员
	private String remark;// 描述

	public Lock() {
		super();
		this.lockedTime = new Date();
	}

	public Lock(int type, String lockedTarget, int status, int count, int err, int reason, String operator,
			String remark) {
		super();
		this.status = status;
		this.lockedTime = new Date();
		this.type = type;
		this.lockedTarget = lockedTarget;
		this.count = count;
		this.err = err;
		this.reason = reason;
		this.operator = operator;
		this.remark = remark;
	}

	/**
	 * 锁定周期是否结束
	 * 
	 * @param period 单位：分钟
	 * @return
	 */
	public boolean isPeriodOver(int period) {
		return Dates.addMinute(lockedTime, period).before(new Date());
	}

	public String getLockedTarget() {
		return lockedTarget;
	}

	public void setLockedTarget(String lockedTarget) {
		this.lockedTarget = lockedTarget;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getErr() {
		return err;
	}

	public void setErr(int err) {
		this.err = err;
	}

	public int getReason() {
		return reason;
	}

	public String getReasonStr() {
		if (reason < 1) {
			return "";
		} else {
			return LockReson.get(reason).getMessage();
		}
	}

	public void setReason(int reason) {
		this.reason = reason;
	}

	public Date getLockedTime() {
		return lockedTime;
	}

	public String getLockedTimeStr() {
		if (lockedTime == null) {
			return "";
		} else {
			return Dates.formatDate(lockedTime, "yyyy-MM-dd HH:mm:ss");
		}
	}

	public void setLockedTime(Date lockedTime) {
		this.lockedTime = lockedTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}
