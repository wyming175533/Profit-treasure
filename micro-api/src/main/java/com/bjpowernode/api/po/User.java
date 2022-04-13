package com.bjpowernode.api.po;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private static final long serialVersionUID = -9080447220354860689L;
    private Integer id;

    private String phone;

    private String loginPassword;

    private String name;

    private String idCard;

    private Date addTime;

    private Date lastLoginTime;

    private String headerImage;

    private String login_ip;

    public String getLogin_ip() {
        return login_ip;
    }

    public void setLogin_ip(String login_ip) {
        this.login_ip = login_ip;
    }

    public String getLogin_device() {
        return login_device;
    }

    public void setLogin_device(String login_device) {
        this.login_device = login_device;
    }

    private String login_device;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", loginPassword='" + loginPassword + '\'' +
                ", name='" + name + '\'' +
                ", idCard='" + idCard + '\'' +
                ", addTime=" + addTime +
                ", lastLoginTime=" + lastLoginTime +
                ", headerImage='" + headerImage + '\'' +
                ", login_ip='" + login_ip + '\'' +
                ", login_device='" + login_device + '\'' +
                '}';
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }
}