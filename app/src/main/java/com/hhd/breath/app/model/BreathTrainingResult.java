package com.hhd.breath.app.model;

import java.io.Serializable;

/**
 * Created by familylove on 2016/5/16.
 * 训练结果  用于数据上传
 */
public class BreathTrainingResult  implements Serializable {

    private String user_id ;
    private String breath_type ;
    private String breath_name ;
    private String train_group ;
    private String train_last ;
    private String train_result ;
    private String difficulty ;
    private String suggestion ;
    private String platform ;
    private String device_sn ;
    private String file_id ;
    private String fname ;
    private String uploadFlag ;
    private String train_time ;

    public String getTrain_time() {
        return train_time;
    }

    public void setTrain_time(String train_time) {
        this.train_time = train_time;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }


    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getUploadFlag() {
        return uploadFlag;
    }

    public void setUploadFlag(String uploadFlag) {
        this.uploadFlag = uploadFlag;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBreath_type() {
        return breath_type;
    }

    public void setBreath_type(String breath_type) {
        this.breath_type = breath_type;
    }

    public String getBreath_name() {
        return breath_name;
    }

    public void setBreath_name(String breath_name) {
        this.breath_name = breath_name;
    }

    public String getTrain_group() {
        return train_group;
    }

    public void setTrain_group(String train_group) {
        this.train_group = train_group;
    }

    public String getTrain_last() {
        return train_last;
    }

    public void setTrain_last(String train_last) {
        this.train_last = train_last;
    }

    public String getTrain_result() {
        return train_result;
    }

    public void setTrain_result(String train_result) {
        this.train_result = train_result;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDevice_sn() {
        return device_sn;
    }

    public void setDevice_sn(String device_sn) {
        this.device_sn = device_sn;
    }

    @Override
    public String toString() {
        return "BreathTrainingResult{" +
                "user_id='" + user_id + '\'' +
                ", breath_type='" + breath_type + '\'' +
                ", breath_name='" + breath_name + '\'' +
                ", train_group='" + train_group + '\'' +
                ", train_last='" + train_last + '\'' +
                ", train_result='" + train_result + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", suggestion='" + suggestion + '\'' +
                ", platform='" + platform + '\'' +
                ", device_sn='" + device_sn + '\'' +
                ", file_id='" + file_id + '\'' +
                ", fname='" + fname + '\'' +
                ", uploadFlag='" + uploadFlag + '\'' +
                ", train_time='" + train_time + '\'' +
                '}';
    }
}
