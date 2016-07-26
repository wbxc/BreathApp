package com.hhd.breath.app.model;

import android.content.ContentValues;
import android.text.TextUtils;

import com.hhd.breath.app.db.DBManger;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/30.
 */
public class TrainPlan  implements Serializable{

    private String name ;   // 训练名称
    private String persistent ;  // 表示太阳的个数
    private String control ;   // 表示星星的个数
    private String strength ;  // 表示月亮的个数
    private String groupNumber  ;


    private String persistentLevel ;
    private String controlLevel ;
    private String strengthLevel ;

    private String currentPersistent;
    private String currentControl;
    private String currentStrength;
    private String times ;  //  训练多少次成功后过关

    private String sumTrainTimes ; // 训练的次数
    private String sumTrainDays ;  // 训练的总天数
    private String trainDayFlag ;  // 训练的表示




    private String breathTime ;    //呼气时间
    private String inspirerTime;   // 吸气时间
    private String createTime ;  //创建时间
    private String userId ;
    private String trainType ;   // 训练的类型  区分循环渐进  还是 自定义
    private String cumulativeTime ;  // 累计时间
    private String trainPlanTypeName ;


    public String getSumTrainTimes() {
        return sumTrainTimes;
    }

    public void setSumTrainTimes(String sumTrainTimes) {
        this.sumTrainTimes = sumTrainTimes;
    }

    public String getSumTrainDays() {
        return sumTrainDays;
    }

    public void setSumTrainDays(String sumTrainDays) {
        this.sumTrainDays = sumTrainDays;
    }

    public String getTrainDayFlag() {
        return trainDayFlag;
    }

    public void setTrainDayFlag(String trainDayFlag) {
        this.trainDayFlag = trainDayFlag;
    }

    public String getCurrentPersistent() {
        return currentPersistent;
    }

    public void setCurrentPersistent(String currentPersistent) {
        this.currentPersistent = currentPersistent;
    }

    public String getCurrentControl() {
        return currentControl;
    }

    public void setCurrentControl(String currentControl) {
        this.currentControl = currentControl;
    }

    public String getCurrentStrength() {
        return currentStrength;
    }

    public void setCurrentStrength(String currentStrength) {
        this.currentStrength = currentStrength;
    }

    public String getTrainPlanTypeName() {
        return trainPlanTypeName;
    }

    public void setTrainPlanTypeName(String trainPlanTypeName) {
        this.trainPlanTypeName = trainPlanTypeName;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public String getCumulativeTime() {
        return cumulativeTime;
    }

    public void setCumulativeTime(String cumulativeTime) {
        this.cumulativeTime = cumulativeTime;
    }



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

    public String getInspirerTime() {
        return inspirerTime;
    }

    public void setInspirerTime(String inspirerTime) {
        this.inspirerTime = inspirerTime;
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
        contentValues.put(DBManger.TRAIN_PLAN_PERSISTENT_LEVEL,trainPlan.getPersistentLevel());
        contentValues.put(DBManger.TRAIN_PLAN_GROUP_NUMBER,trainPlan.getGroupNumber());
        contentValues.put(DBManger.TRAIN_INSPIRER_TIME,trainPlan.getInspirerTime());
        contentValues.put(DBManger.TRAIN_CREATE_TIME,trainPlan.getCreateTime());
        contentValues.put(DBManger.TRAIN_PLAN_TYPE,trainPlan.getTrainType()) ;
        contentValues.put(DBManger.TRAIN_PLAN_TIMES,trainPlan.getTimes());
        contentValues.put(DBManger.TRAIN_PLAN_CUM_TIME,trainPlan.getCumulativeTime());
        contentValues.put(DBManger.TRAIN_PLAN_CONTROL_CURRENT_LEVEL,trainPlan.getCurrentControl());
        contentValues.put(DBManger.TRAIN_PLAN_STRENGTH_CURRENT_LEVEL,trainPlan.getCurrentStrength());
        contentValues.put(DBManger.TRAIN_PLAN_PERSISTENT_CURRENT_LEVEL,trainPlan.getCurrentPersistent()) ;
/*        contentValues.put(DBManger.TRAIN_PLAN_SUM_TRAIN_DAYS,trainPlan.getSumTrainDays());
        contentValues.put(DBManger.TRAIN_PLAN_SUM_TRAIN_TIMES,trainPlan.getSumTrainTimes());
        contentValues.put(DBManger.TRAIN_PLAN_TRAIN_DAY_FLAG,trainPlan.gettra);*/
        return contentValues ;
    }


    public boolean isNotEmpty(TrainPlan trainPlan){

        if (TextUtils.isEmpty(trainPlan.getName()) ||
                TextUtils.isEmpty(trainPlan.getInspirerTime())||
                TextUtils.isEmpty(trainPlan.getGroupNumber()) ||
                TextUtils.isEmpty(trainPlan.getTimes()) ||
                TextUtils.isEmpty(trainPlan.getControl())||
                TextUtils.isEmpty(trainPlan.getStrength())||
                TextUtils.isEmpty(trainPlan.getPersistent())){
            return  false ;
        }else {
            return true ;
        }
    }

    @Override
    public String toString() {
        return "TrainPlan{" +
                "name='" + name + '\'' +
                ", persistent='" + persistent + '\'' +
                ", control='" + control + '\'' +
                ", strength='" + strength + '\'' +
                ", groupNumber='" + groupNumber + '\'' +
                ", persistentLevel='" + persistentLevel + '\'' +
                ", controlLevel='" + controlLevel + '\'' +
                ", strengthLevel='" + strengthLevel + '\'' +
                ", currentPersistent='" + currentPersistent + '\'' +
                ", currentControl='" + currentControl + '\'' +
                ", currentStrength='" + currentStrength + '\'' +
                ", breathTime='" + breathTime + '\'' +
                ", inspirerTime='" + inspirerTime + '\'' +
                ", createTime='" + createTime + '\'' +
                ", userId='" + userId + '\'' +
                ", trainType='" + trainType + '\'' +
                ", cumulativeTime='" + cumulativeTime + '\'' +
                ", trainPlanTypeName='" + trainPlanTypeName + '\'' +
                ", times='" + times + '\'' +
                '}';
    }
}
