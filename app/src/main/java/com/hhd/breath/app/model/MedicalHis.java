package com.hhd.breath.app.model;

import android.content.ContentValues;

import com.hhd.breath.app.db.DBManger;

/**
 * Created by Administrator on 2015/12/23.
 */
public class MedicalHis {

    private String id ;
    private String name ;
    /**
     * type 1表示选中 0表示没有选中
     */
    private int type ;
    private String userId ;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContentValues getContentValues(MedicalHis medicalHis){
        ContentValues m = new ContentValues() ;
        m.put(DBManger.MEDICAL_HIS_USER_ID,medicalHis.getUserId());
        m.put(DBManger.MEDICAL_HIS_ID,medicalHis.getId());
        m.put(DBManger.MEDICAL_HIS_NAME,medicalHis.getName());
        m.put(DBManger.MEDICAL_HIS_TYPE,String.valueOf(medicalHis.getType()));

        return m ;
    }

    @Override
    public String toString() {
        return "MedicalHis{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", userId='" + userId + '\'' +
                '}';
    }
}
