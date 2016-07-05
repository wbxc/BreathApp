package com.hhd.breath.app.model;

import android.content.ContentValues;

import com.hhd.breath.app.db.DBManger;

/**
 * Created by Administrator on 2016/6/14.
 */
public class HealthData {
    private String userId ;
    private String time ;
    private String maxRate ;
    private String secondValue ;
    private String compValue ;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(String maxRate) {
        this.maxRate = maxRate;
    }

    public String getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(String secondValue) {
        this.secondValue = secondValue;
    }

    public String getCompValue() {
        return compValue;
    }

    public void setCompValue(String compValue) {
        this.compValue = compValue;
    }


    public ContentValues convert(HealthData healthData){
        ContentValues contentValues = new ContentValues() ;
        contentValues.put(DBManger.HEALTH_DATA_USER_ID,healthData.getUserId());
        contentValues.put(DBManger.HEALTH_DATA_TIME,healthData.getTime());
        contentValues.put(DBManger.HEALTH_DATA_MAX_RATE,healthData.getMaxRate());
        contentValues.put(DBManger.HEALTH_DATA_SECOND_VALUE,healthData.getSecondValue());
        contentValues.put(DBManger.HEALTH_DATA_COMP_VALUE,healthData.getCompValue());
        return contentValues ;
    }
    @Override
    public String toString() {
        return "HealthData{" +
                "userId='" + userId + '\'' +
                ", time='" + time + '\'' +
                ", maxRate='" + maxRate + '\'' +
                ", secondValue='" + secondValue + '\'' +
                ", compValue='" + compValue + '\'' +
                '}';
    }
}
