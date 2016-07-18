package com.hhd.breath.app.model;

import android.content.ContentValues;

import com.hhd.breath.app.db.DBManger;

/**
 * Created by familylove on 2016/7/13.
 * 这个类是用于本地存储  每一条信息获取的额外的值训练参数记录值
 */
public class BreathHisLog {

    private String record_id ;   // 记录id值

    /**
     * 初始的难度系数
     */
    private String persistentLevel ;
    private String controlLevel ;
    private String strengthLevel ;

    /**
     * 当前的难度系数
     */
    private String currentPersistentLevel ;
    private String currentControlLevel ;
    private String currentStrengthLevel ;

    /**
     * 计划结果参数
     */
    private String trainStartTime ;    // 训练开启时间
    private String trainDays ;         // 训练的天数
    private String trainTimes ;        // 训练的次数
    private String trainAverTimes ;    // 训练的平均次数
    private String trainResult ;       // 训练的结果值
    private String trainSuccessTimes  ;  // 训练完成多少次可以晋级
    private String trainAverValue ;   // 平均分

    private String trainStageValue ; // 阶段考评值   50,100,30,200



    public ContentValues toContentValues(BreathHisLog breathHisLog){

        ContentValues contentValues = new ContentValues() ;
        contentValues.put(DBManger.HIS_LOG_RECORD_ID,breathHisLog.getRecord_id());
        contentValues.put(DBManger.HIS_LOG_INIT_CONTROL,breathHisLog.getControlLevel());
        contentValues.put(DBManger.HIS_LOG_INIT_STRENGTH,breathHisLog.getStrengthLevel()) ;
        contentValues.put(DBManger.HIS_LOG_INIT_PERSISTENT,breathHisLog.getPersistentLevel());
        contentValues.put(DBManger.HIS_LOG_CURRENT_CONTROL,breathHisLog.getCurrentControlLevel());
        contentValues.put(DBManger.HIS_LOG_CURRENT_STRENGTH,breathHisLog.getCurrentStrengthLevel());
        contentValues.put(DBManger.HIS_LOG_CURRENT_PERSISTENT,breathHisLog.getCurrentPersistentLevel());
        contentValues.put(DBManger.HIS_LOG_START_TRAIN_TIME,breathHisLog.getTrainStartTime());
        contentValues.put(DBManger.HIS_LOG_TRAIN_DAYS,breathHisLog.getTrainDays());
        contentValues.put(DBManger.HIS_LOG_TRAIN_TIMES,breathHisLog.getTrainTimes());
        contentValues.put(DBManger.HIS_LOG_TRAIN_AVER_TIMES,breathHisLog.getTrainAverTimes());
        contentValues.put(DBManger.HIS_LOG_TRAIN_RESULT,breathHisLog.getTrainResult());
        contentValues.put(DBManger.HIS_LOG_TRAIN_SUCCESS_TIMES,breathHisLog.getTrainSuccessTimes());
        contentValues.put(DBManger.HIS_LOG_TRAIN_AVER_VALUE,breathHisLog.getTrainAverValue());
        contentValues.put(DBManger.HIS_LOG_TRAIN_STATE_VALUE,breathHisLog.getTrainStageValue());

        return contentValues ;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getPersistentLevel() {
        return persistentLevel;
    }

    public void setPersistentLevel(String persistentLevel) {
        this.persistentLevel = persistentLevel;
    }

    public String getControlLevel() {
        return controlLevel;
    }

    public void setControlLevel(String controlLevel) {
        this.controlLevel = controlLevel;
    }

    public String getStrengthLevel() {
        return strengthLevel;
    }

    public void setStrengthLevel(String strengthLevel) {
        this.strengthLevel = strengthLevel;
    }

    public String getCurrentPersistentLevel() {
        return currentPersistentLevel;
    }

    public void setCurrentPersistentLevel(String currentPersistentLevel) {
        this.currentPersistentLevel = currentPersistentLevel;
    }

    public String getCurrentControlLevel() {
        return currentControlLevel;
    }

    public void setCurrentControlLevel(String currentControlLevel) {
        this.currentControlLevel = currentControlLevel;
    }

    public String getCurrentStrengthLevel() {
        return currentStrengthLevel;
    }

    public void setCurrentStrengthLevel(String currentStrengthLevel) {
        this.currentStrengthLevel = currentStrengthLevel;
    }

    public String getTrainStartTime() {
        return trainStartTime;
    }

    public void setTrainStartTime(String trainStartTime) {
        this.trainStartTime = trainStartTime;
    }

    public String getTrainDays() {
        return trainDays;
    }

    public void setTrainDays(String trainDays) {
        this.trainDays = trainDays;
    }

    public String getTrainTimes() {
        return trainTimes;
    }

    public void setTrainTimes(String trainTimes) {
        this.trainTimes = trainTimes;
    }

    public String getTrainAverTimes() {
        return trainAverTimes;
    }

    public void setTrainAverTimes(String trainAverTimes) {
        this.trainAverTimes = trainAverTimes;
    }

    public String getTrainResult() {
        return trainResult;
    }

    public void setTrainResult(String trainResult) {
        this.trainResult = trainResult;
    }

    public String getTrainSuccessTimes() {
        return trainSuccessTimes;
    }

    public void setTrainSuccessTimes(String trainSuccessTimes) {
        this.trainSuccessTimes = trainSuccessTimes;
    }

    public String getTrainAverValue() {
        return trainAverValue;
    }

    public void setTrainAverValue(String trainAverValue) {
        this.trainAverValue = trainAverValue;
    }

    public String getTrainStageValue() {
        return trainStageValue;
    }

    public void setTrainStageValue(String trainStageValue) {
        this.trainStageValue = trainStageValue;
    }


    @Override
    public String toString() {
        return "BreathHisLog{" +
                "record_id='" + record_id + '\'' +
                ", persistentLevel='" + persistentLevel + '\'' +
                ", controlLevel='" + controlLevel + '\'' +
                ", strengthLevel='" + strengthLevel + '\'' +
                ", currentPersistentLevel='" + currentPersistentLevel + '\'' +
                ", currentControlLevel='" + currentControlLevel + '\'' +
                ", currentStrengthLevel='" + currentStrengthLevel + '\'' +
                ", trainStartTime='" + trainStartTime + '\'' +
                ", trainDays='" + trainDays + '\'' +
                ", trainTimes='" + trainTimes + '\'' +
                ", trainAverTimes='" + trainAverTimes + '\'' +
                ", trainResult='" + trainResult + '\'' +
                ", trainSuccessTimes='" + trainSuccessTimes + '\'' +
                ", trainAverValue='" + trainAverValue + '\'' +
                ", trainStageValue='" + trainStageValue + '\'' +
                '}';
    }
}
