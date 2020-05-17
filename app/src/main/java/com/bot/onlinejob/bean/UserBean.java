package com.bot.onlinejob.bean;

public class UserBean {
    String userName;
    String email;
    String serviceType;
    String mobileNum;

    //constructor


    public UserBean(String userName, String email, String serviceType, String mobileNum) {
        this.userName = userName;
        this.email = email;
        this.serviceType = serviceType;
        this.mobileNum = mobileNum;
    }

    public UserBean() {

    }
    //accessors and mutator


    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
