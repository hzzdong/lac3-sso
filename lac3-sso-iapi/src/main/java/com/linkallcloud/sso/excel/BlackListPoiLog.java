package com.linkallcloud.sso.excel;

import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

public class BlackListPoiLog implements Serializable {
    private static final long serialVersionUID = -8808115066424911923L;

    @Excel(name = "行号", width = 30)
    private int rowNum;

    @Excel(name = "手机", width = 60)
    private String mobile;

    @Excel(name = "错误信息", width = 200)
    private String error;

    public BlackListPoiLog() {
        super();
    }

    public BlackListPoiLog(int rowNum, String mobile, String error) {
        super();
        this.rowNum = rowNum;
        this.mobile = mobile;
        this.error = error;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
