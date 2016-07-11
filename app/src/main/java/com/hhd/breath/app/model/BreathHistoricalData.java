package com.hhd.breath.app.model;

import android.app.admin.DeviceAdminInfo;
import android.content.ComponentName;
import android.content.ContentValues;

import com.hhd.breath.app.db.DBManger;

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
    private String file_id ;
    private String user_id ;
    private String train_group ;
    private String train_last ;
    private String suggestion ;
    private String devices_sn ;
    private String file_size ;
    private String file_type ;
    private String file_o_name ;
    private String file_n_name ;
    private String file_path ;
    private String file_md5 ;
    private String platform ;
    private String is_delete ;
    private String record_state ;
    private String file_upload_time ;

    public String getFile_upload_time() {
        return file_upload_time;
    }

    public void setFile_upload_time(String file_upload_time) {
        this.file_upload_time = file_upload_time;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getDevices_sn() {
        return devices_sn;
    }

    public void setDevices_sn(String devices_sn) {
        this.devices_sn = devices_sn;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getFile_o_name() {
        return file_o_name;
    }

    public void setFile_o_name(String file_o_name) {
        this.file_o_name = file_o_name;
    }

    public String getFile_n_name() {
        return file_n_name;
    }

    public void setFile_n_name(String file_n_name) {
        this.file_n_name = file_n_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_md5() {
        return file_md5;
    }

    public void setFile_md5(String file_md5) {
        this.file_md5 = file_md5;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getRecord_state() {
        return record_state;
    }

    public void setRecord_state(String record_state) {
        this.record_state = record_state;
    }

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


    public ContentValues toContentValues(BreathHistoricalData data){

        ContentValues contentValues = new ContentValues() ;
        contentValues.put(DBManger.TRAIN_HIS_RECORD_ID, data.getRecord_id());
        contentValues.put(DBManger.TRAIN_HIS_FILE_ID,data.getFile_id());
        contentValues.put(DBManger.TRAIN_HIS_USER_ID,data.getUser_id()) ;
        contentValues.put(DBManger.TRAIN_HIS_BREATH_TYPE,data.getBreath_type()) ;
        contentValues.put(DBManger.TRAIN_HIS_TRAIN_LAST,data.getTrain_last());
        contentValues.put(DBManger.TRAIN_HIS_TRAIN_TIME,data.getTrain_time());
        contentValues.put(DBManger.TRAIN_HIS_TRAIN_RESULT,data.getTrain_result());
        contentValues.put(DBManger.TRAIN_HIS_FILE_UPLOAD_TIME,data.getFile_upload_time());
        contentValues.put(DBManger.TRAIN_HIS_DIFFICULTY,data.getDifficulty());
        contentValues.put(DBManger.TRAIN_HIS_SUGGESTION,data.getSuggestion());
        contentValues.put(DBManger.TRAIN_HIS_DEVICE_SN,data.getDevices_sn());
        contentValues.put(DBManger.TRAIN_HIS_FILE_SIZE,data.getFile_size()) ;
        contentValues.put(DBManger.TRAIN_HIS_FILE_TYPE,data.getFile_type());
        contentValues.put(DBManger.TRAIN_HIS_FILE_O_NAME,data.getFile_o_name()) ;
        contentValues.put(DBManger.TRAIN_HIS_FILE_N_NAME,data.getFile_n_name());
        contentValues.put(DBManger.TRAIN_HIS_FILE_PATH,data.getFile_path());
        contentValues.put(DBManger.TRAIN_HIS_FILE_MD5,data.getFile_md5());
        contentValues.put(DBManger.TRAIN_HIS_PLATFORM,data.getPlatform()) ;
        contentValues.put(DBManger.TRAIN_HIS_RECORD_STATE,data.getRecord_state()) ;
        return contentValues ;
    }
}
