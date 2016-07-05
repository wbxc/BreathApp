package com.hhd.breath.app.model;

import java.util.List;

/**
 * Created by familylove on 2015/12/14.
 */
public class RecordDayData {

    private String id ;           // 记录id
    private String recordTime ; //记录时间
    private String  foreignId ;  // 外键id
    private String account   ;   //这一天测试的次数

    private List<RecordUnitData> mRecordUnitDatas ;  //记录单测量

    public List<RecordUnitData> getmRecordUnitDatas() {
        return mRecordUnitDatas;
    }

    public void setmRecordUnitDatas(List<RecordUnitData> mRecordUnitDatas) {
        this.mRecordUnitDatas = mRecordUnitDatas;
    }

    public String getId() {
        return id;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public void setId(String id) {
        this.id = id;

    }



    public String getForeignId() {
        return foreignId;
    }

    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
