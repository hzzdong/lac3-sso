package com.linkallcloud.sso.excel;

import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

public class BalckListPoiEntity implements Serializable {
    private static final long serialVersionUID = 9108664761642051340L;

    @Excel(name = "手机", width = 20)
    private String mobile;

    @Excel(name = "备注说明", width = 60)
    private String remark;

    public BalckListPoiEntity() {
        super();
    }

    public BalckListPoiEntity(String mobile, String remark) {
        super();
        this.mobile = mobile;
        this.remark = remark;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
