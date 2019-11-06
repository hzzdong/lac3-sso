package com.linkallcloud.sso.domain;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.annotation.ShowName;

/**
 * @author 孙宇冰
 * @version V1.0
 * @Package com.linkallcloud.vsms.domain
 * @date 2019/9/3 14:32
 */
@ShowName(value = "黑名单", logFields = "id,mobile")
public class BlackList extends Domain {

    private static final long serialVersionUID = -446465318078095913L;
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 备注
     */
    private String remark;

    public BlackList() {
        super();
    }

    public BlackList(String mobile, String remark) {
        this();
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
