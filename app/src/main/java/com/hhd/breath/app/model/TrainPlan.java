package com.hhd.breath.app.model;

import android.content.ContentValues;

import com.hhd.breath.app.db.DBManger;

/**
 * Created by Administrator on 2016/6/30.
 */
public class TrainPlan {

    private String name ;   // 训练名称
    private String persistent ;
    private String control ;
    private String strength ;
    private String groupNumber  ;
    private String persistentLevel ;
    private String controlLevel ;
    private String strengthLevel ;
    private String breathTime ;
    private String stopTime ;
    private String createTime ;  //创建时间
    private String userId ;


    private String times ;

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getBreathTime() {
        return breathTime;
    }

    public void setBreathTime(String breathTime) {
        this.breathTime = breathTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getPersistent() {
        return persistent;
    }

    public void setPersistent(String persistent) {
        this.persistent = persistent;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     *
     * @param trainPlan
     * @return ContentValues
     */
    public ContentValues trainPlanToContentValue(TrainPlan trainPlan){

        ContentValues contentValues = new ContentValues() ;
        contentValues.put(DBManger.TRAIN_PLAN_NAME,trainPlan.getName());
        contentValues.put(DBManger.TRAIN_PLAN_USER_ID,trainPlan.getUserId());
        contentValues.put(DBManger.TRAIN_PLANE_CONTROL,trainPlan.getControl());
        contentValues.put(DBManger.TRAIN_PLAN_CONTROL_LEVEL,trainPlan.getControlLevel());
        contentValues.put(DBManger.TRAIN_PLAN_STRENGTH,trainPlan.getStrength());
        contentValues.put(DBManger.TRAIN_PLAN_STRENGTH_LEVEL,trainPlan.getStrengthLevel());
        contentValues.put(DBManger.TRAIN_PLAN_PERSISTENT,trainPlan.getPersistent()) ;
        contentValues.put(DBManger.TRAIN_PLAN_PERSISTENT_LEVEL,trainPlan.getStrengthLevel());
        contentValues.put(DBManger.TRAIN_PLAN_GROUP_NUMBER,trainPlan.getGroupNumber());
        contentValues.put(DBManger.TRAIN_BREATH_TIME,trainPlan.getBreathTime());
        contentValues.put(DBManger.TRAIN_STOP_TIME,trainPlan.getStopTime());
        contentValues.put(DBManger.TRAIN_CREATE_TIME,trainPlan.getCreateTime());


        return contentValues ;
    }
    @Override
    public String toString() {
        return "TrainPlan{" +
                "name='" + name + '\'' +
                ", persistent='" + persistent + '\'' +
                ", control='" + control + '\'' +
                ", strength='" + strength + '\'' +
                ", persistentLevel='" + persistentLevel + '\'' +
                ", controlLevel='" + controlLevel + '\'' +
                ", strengthLevel='" + strengthLevel + '\'' +
                '}';
    }
}
