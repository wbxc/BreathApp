package com.hhd.breath.app.model;

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
}
