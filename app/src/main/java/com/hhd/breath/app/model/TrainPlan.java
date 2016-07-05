package com.hhd.breath.app.model;

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
