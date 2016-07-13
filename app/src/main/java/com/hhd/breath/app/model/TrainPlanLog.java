package com.hhd.breath.app.model;

/**
 * Created by familylove on 2016/7/13.
 *
 * 训练计划的辅助类
 *
 */
public class TrainPlanLog {

    private String name ;
    private String trainType ;
    private String userId ;



    private String days ;    // 训练的天数
    private String trainTimes ;   //训练的次数
    private String trainStartTime ; //训练模式开始的时间


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTrainTimes() {
        return trainTimes;
    }

    public void setTrainTimes(String trainTimes) {
        this.trainTimes = trainTimes;
    }

    public String getTrainStartTime() {
        return trainStartTime;
    }

    public void setTrainStartTime(String trainStartTime) {
        this.trainStartTime = trainStartTime;
    }
}
