package com.hhd.breath.app.model;

/**
 * Created by familylove on 2015/12/14.
 */
public class RecordUnitData {

    private String id  ;     // 测试id 唯一标示
    private String trainTime ;   // 测试时间
    private String level ;     // 测试类别
    private String standardRate ;  //达标率
    private String foreignId ;  // 外键
    private String timeLong ;
    private String groupNumber ;
    private String trainSuggestion ;
    private String trainName ;

    private String phone ;
    private String userName ;
    private String sex ;
    private String birthday ;
    private String serNumber ;
    private String sign ;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSerNumber() {
        return serNumber;
    }

    public void setSerNumber(String serNumber) {
        this.serNumber = serNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTimeLong() {
        return timeLong;
    }

    public void setTimeLong(String timeLong) {
        this.timeLong = timeLong;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getTrainSuggestion() {
        return trainSuggestion;
    }

    public void setTrainSuggestion(String trainSuggestion) {
        this.trainSuggestion = trainSuggestion;
    }

    public String getForeignId() {
        return foreignId;
    }

    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrainTime() {
        return trainTime;
    }

    public void setTrainTime(String trainTime) {
        this.trainTime = trainTime;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStandardRate() {
        return standardRate;
    }

    public void setStandardRate(String standardRate) {
        this.standardRate = standardRate;
    }
}
