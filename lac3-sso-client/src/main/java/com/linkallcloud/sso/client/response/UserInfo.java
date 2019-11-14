/**
 * Copyright (c) 2011 www.public.zj.cn
 *
 * cn.zj.pubinfo.sso.response.UserInfo.java 
 *
 * 2011-6-4
 * 
 */
package com.linkallcloud.sso.client.response;

import java.io.Serializable;
import java.util.Date;

/**
 * 2011-6-4
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 4881632854266480839L;

    private String loginname;// 登录名
    private String name;// 名称
    private String phone;// 电话号码
    private String mobile;// 手机号码
    private String email;// 邮箱
    private String company;// 公司
    private String department;// 部门
    private Date birthday;// 生日
    private String addr;// 地址
    private String desc;// 备注
    private int state;

    /**
     * 
     */
    public UserInfo() {
        super();
    }

    /**
     * @return the loginname
     */
    public String getLoginname() {
        return loginname;
    }

    /**
     * @param loginname
     *            the loginname to set
     */
    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     *            the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     *            the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company
     *            the company to set
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department
     *            the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the birthday
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * @param birthday
     *            the birthday to set
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * @return the addr
     */
    public String getAddr() {
        return addr;
    }

    /**
     * @param addr
     *            the addr to set
     */
    public void setAddr(String addr) {
        this.addr = addr;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc
     *            the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return the state
     */
    public int getState() {
        return state;
    }

    /**
     * @param state
     *            the state to set
     */
    public void setState(int state) {
        this.state = state;
    }

}
