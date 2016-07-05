package com.hhd.breath.app.model;

/**
 * Created by familylove on 2016/5/16.
 * 详细的呼吸训练报告
 */
public class BreathDetailReport {

    private String record_id ;
    private String user_id ;
    private String breath_type ;
    private String train_group ;
    private String train_time ;
    private String train_result ;
    private String difficulty ;
    private String suggestion ;
    private String device_sn ;
    private String train_last ;

    public String getTrain_last() {
        return train_last;
    }

    public void setTrain_last(String train_last) {
        this.train_last = train_last;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
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

    public String getTrain_group() {
        return train_group;
    }

    public void setTrain_group(String train_group) {
        this.train_group = train_group;
    }

    public String getTrain_time() {
        return train_time;
    }

    public void setTrain_time(String train_time) {
        this.train_time = train_time;
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

    public String getDevice_sn() {
        return device_sn;
    }

    public void setDevice_sn(String device_sn) {
        this.device_sn = device_sn;
    }
}
