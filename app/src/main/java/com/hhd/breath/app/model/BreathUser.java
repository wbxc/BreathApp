package com.hhd.breath.app.model;

/**
 * Created by familylove on 2016/4/22.
 */
public class BreathUser {

    private String userId ;
    private String userPhone ;
    private String fullName ;
    private String userBirthday ;
    private String userDisease ;
    private String userAvatar ;

    public BreathUser(){

    }
    public BreathUser(String userId, String userPhone, String fullName, String userBirthday, String userDisease) {
        this.userId = userId;
        this.userPhone = userPhone;
        this.fullName = fullName;
        this.userBirthday = userBirthday;
        this.userDisease = userDisease;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getUserDisease() {
        return userDisease;
    }

    public void setUserDisease(String userDisease) {
        this.userDisease = userDisease;
    }
}
