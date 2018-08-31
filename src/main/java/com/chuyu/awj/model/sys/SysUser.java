package com.chuyu.awj.model.sys;

import java.io.Serializable;
import java.util.Date;

public class SysUser implements Serializable{
    /**
     * 用户账号
     */
    private String userCode;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户类型1：会员，2：商家
     */
    private Byte userType;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 支付密码
     */
    private String payPassword;

    /**
     * 是否激活
     */
    private Byte activity;

    /**
     * 用户状态0：可用，-1：不可用
     */
    private Byte status;

    /**
     * 创建者
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 用户账号
     * @return userCode 用户账号
     */
    public String getUserCode() {
        return userCode;
    }

    /**
     * 用户账号
     * @param userCode 用户账号
     */
    public void setUserCode(String userCode) {
        this.userCode = userCode == null ? null : userCode.trim();
    }

    /**
     * 用户名
     * @return userName 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 用户名
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 昵称
     * @return nickname 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 昵称
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    /**
     * 用户类型1：会员，2：商家
     * @return userType 用户类型1：会员，2：商家
     */
    public Byte getUserType() {
        return userType;
    }

    /**
     * 用户类型1：会员，2：商家
     * @param userType 用户类型1：会员，2：商家
     */
    public void setUserType(Byte userType) {
        this.userType = userType;
    }

    /**
     * 登录密码
     * @return password 登录密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 登录密码
     * @param password 登录密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 支付密码
     * @return payPassword 支付密码
     */
    public String getPayPassword() {
        return payPassword;
    }

    /**
     * 支付密码
     * @param payPassword 支付密码
     */
    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword == null ? null : payPassword.trim();
    }

    /**
     * 是否激活
     * @return activity 是否激活
     */
    public Byte getActivity() {
        return activity;
    }

    /**
     * 是否激活
     * @param activity 是否激活
     */
    public void setActivity(Byte activity) {
        this.activity = activity;
    }

    /**
     * 用户状态0：可用，-1：不可用
     * @return status 用户状态0：可用，-1：不可用
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 用户状态0：可用，-1：不可用
     * @param status 用户状态0：可用，-1：不可用
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 创建者
     * @return createUser 创建者
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * 创建者
     * @param createUser 创建者
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    /**
     * 创建时间
     * @return createTime 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 更新时间
     * @return updateTime 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 更新时间
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}