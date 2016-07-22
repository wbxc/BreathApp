package com.hhd.breath.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hhd.breath.app.model.HealthData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/14.
 */
public class HealthDataService {

    private DbOpenHelper dbOpenHelper = null;
    private static HealthDataService instance = null;

    private HealthDataService(Context context) {
        dbOpenHelper = new DbOpenHelper(context) ;
    }

    public static HealthDataService getInstance(Context context){

        if (instance==null){
            instance = new HealthDataService(context) ;
        }
        return instance ;
    }


    /**
     * 检测评估
     * @param healthData
     * @return
     */
    public boolean add(HealthData healthData){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase() ;
        boolean flag = false ;
        try {
            db.beginTransaction();
            if (!isExists(db,healthData.getUserId(),healthData.getTime())){
                db.insert(DBManger.TABLE_HEALTH_DATA, null, healthData.convert(healthData)) ;
            }else {
                update(db,healthData);
            }
            flag = true ;
            db.setTransactionSuccessful();
        }catch (Exception e){
        }finally {
            db.endTransaction();
            if (db!=null){
                db.close();
                db =null ;
            }
        }
        return flag ;
    }

    /**
     * 评估数据的更新
     * @param db
     * @param healthData
     */
    private void update(SQLiteDatabase db,HealthData healthData){
        ContentValues contentValues = new ContentValues() ;
        contentValues.put(DBManger.HEALTH_DATA_COMP_VALUE,healthData.getCompValue());
        contentValues.put(DBManger.HEALTH_DATA_SECOND_VALUE,healthData.getSecondValue());
        contentValues.put(DBManger.HEALTH_DATA_MAX_RATE,healthData.getMaxRate());
        db.update(DBManger.TABLE_HEALTH_DATA,contentValues,DBManger.HEALTH_DATA_USER_ID+" = ? and "+DBManger.HEALTH_DATA_TIME+" = ?",new String[]{healthData.getUserId(),healthData.getTime()}) ;
    }




    public List<HealthData> findHealths(String userId){
        String sql = "select * from "+DBManger.TABLE_HEALTH_DATA+" where "+DBManger.HEALTH_DATA_USER_ID+" = ?" ;
        SQLiteDatabase db = null ;
        Cursor cursor = null ;
        List<HealthData> healthDatas = null ;
        try {
            healthDatas = new ArrayList<HealthData>() ;
            db = dbOpenHelper.getReadableDatabase() ;
            cursor = db.rawQuery(sql, new String[]{userId}) ;
            if (cursor!=null){
                while (cursor.moveToNext()){
                    HealthData healthData = new HealthData() ;
                    healthData.setUserId(cursor.getString(cursor.getColumnIndex(DBManger.HEALTH_DATA_USER_ID)));
                    healthData.setTime(cursor.getString(cursor.getColumnIndex(DBManger.HEALTH_DATA_TIME)));
                    healthData.setMaxRate(cursor.getString(cursor.getColumnIndex(DBManger.HEALTH_DATA_MAX_RATE)));
                    healthData.setCompValue(cursor.getString(cursor.getColumnIndex(DBManger.HEALTH_DATA_COMP_VALUE)));
                    healthData.setSecondValue(cursor.getString(cursor.getColumnIndex(DBManger.HEALTH_DATA_SECOND_VALUE)));
                    healthDatas.add(healthData) ;
                }
            }


        }catch (Exception e){


        }finally {

            if (cursor!=null){
                cursor.close();
                cursor = null;
            }
            if (db!=null){
                db.close();
                db = null;
            }
        }
        return  healthDatas ;
    }






    public boolean isExists(SQLiteDatabase db,String userId , String time){

        String sql = "select * from "+DBManger.TABLE_HEALTH_DATA+" where "+DBManger.HEALTH_DATA_USER_ID+" = ? and "+DBManger.HEALTH_DATA_TIME+" = ?" ;
        Cursor cursor = null;
        try {
             cursor = db.rawQuery(sql, new String[]{userId, time}) ;

            if (!cursor.isAfterLast()){
                return true ;
            }else {
                return false ;
            }
        }catch (Exception e){

        }finally {
            if (cursor!=null){
                cursor.close();
                cursor = null;
            }
        }
        return  true ;
    }
}
