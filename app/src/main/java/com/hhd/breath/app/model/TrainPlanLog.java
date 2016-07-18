package com.hhd.breath.app.model;

import android.content.ContentValues;

import com.hhd.breath.app.db.DBManger;

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



    private Integer days ;    // 训练的天数
    private Integer trainTimes ;   //训练的次数
    private String trainStartTime ; //训练模式开始的时间
    private String trainDayFlag  ; // 记录最后一次写进的日期2016-07-14

    public String getTrainDayFlag() {
        return trainDayFlag;
    }

    public void setTrainDayFlag(String trainDayFlag) {
        this.trainDayFlag = trainDayFlag;
    }

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

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getTrainTimes() {
        return trainTimes;
    }

    public void setTrainTimes(Integer trainTimes) {
        this.trainTimes = trainTimes;
    }

    public String getTrainStartTime() {
        return trainStartTime;
    }

    public void setTrainStartTime(String trainStartTime) {
        this.trainStartTime = trainStartTime;
    }


    public ContentValues toContentValues(TrainPlanLog trainPlanLog){

        ContentValues contentValues = new ContentValues() ;
        contentValues.put(DBManger.TRAIN_PLAN_LOG_NAME,trainPlanLog.getName());
        contentValues.put(DBManger.TRAIN_PLAN_LOG_TRAIN_TYPE,trainPlanLog.getTrainType());
        contentValues.put(DBManger.TRAIN_PLAN_LOG_USER_ID,trainPlanLog.getUserId());
        contentValues.put(DBManger.TRAIN_PLAN_LOG_DAYS,trainPlanLog.getDays()) ;
        contentValues.put(DBManger.TRAIN_PLAN_LOG_TRAIN_TIMES,trainPlanLog.getTrainTimes());
        contentValues.put(DBManger.TRAIN_PLAN_LOG_START_TIME,trainPlanLog.getTrainStartTime());
        contentValues.put(DBManger.TRAIN_PLAN_LOG_DAY_FLAG,trainPlanLog.getTrainDayFlag());
        return contentValues ;
    }


    @Override
    public String toString() {
        return "TrainPlanLog{" +
                "name='" + name + '\'' +
                ", trainType='" + trainType + '\'' +
                ", userId='" + userId + '\'' +
                ", days=" + days +
                ", trainTimes=" + trainTimes +
                ", trainStartTime='" + trainStartTime + '\'' +
                ", trainDayFlag='" + trainDayFlag + '\'' +
                '}';
    }
}
