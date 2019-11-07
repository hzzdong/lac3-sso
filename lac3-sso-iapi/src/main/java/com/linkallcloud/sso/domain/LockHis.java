package com.linkallcloud.sso.domain;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.annotation.ShowName;
import com.linkallcloud.sh.tuils.Dates;
import com.linkallcloud.sso.enums.LockReson;
import com.linkallcloud.sso.enums.LockType;

@ShowName(value = "锁/解锁历史", logFields = "id")
public class LockHis extends Domain {
	private static final long serialVersionUID = -2601776970647047357L;

	private Long hisId;// ID
	private String lockedTarget;// 锁定目标
	private int type;// 锁定类型
	private int count;// 锁定次数
	private int reason;// 锁定原因
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date lockedTime;// 锁定时间
	private String operator;// 操作员
	private String remark;// 描述

	public LockHis() {
		super();
	}

	public LockHis(Lock lock) {
		super();
		this.hisId = lock.getId();
		this.status = lock.getStatus();
		this.lockedTarget = lock.getLockedTarget();
		this.type = lock.getType();
		this.count = lock.getCount();
		this.reason = lock.getReason();
		this.lockedTime = lock.getLockedTime();
		this.operator = lock.getOperator();
		this.remark = lock.getRemark();
	}

	public Long getHisId() {
		return hisId;
	}

	public void setHisId(Long hisId) {
		this.hisId = hisId;
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
			return LockType.get(type).getMessage();
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
