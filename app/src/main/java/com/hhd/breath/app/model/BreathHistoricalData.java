package com.hhd.breath.app.model;

/**
 * Created by familylove on 2016/5/16.
 * 呼吸历史数据
 * 数据列表元素
 */
public class BreathHistoricalData {
    private String record_id ;
    private String breath_type ;
    private String breath_name ;
    private String train_time ;
    private String train_result ;
    private String difficulty ;



    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
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

    @Override
    public String toString() {
        return "BreathHistoricalData{" +
                "record_id='" + record_id + '\'' +
                ", breath_type='" + breath_type + '\'' +
                ", breath_name='" + breath_name + '\'' +
                ", train_time='" + train_time + '\'' +
                ", train_result='" + train_result + '\'' +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }
}
