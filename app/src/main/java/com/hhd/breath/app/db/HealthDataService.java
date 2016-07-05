package com.hhd.breath.app.db;

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


    public boolean add(HealthData healthData){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase() ;
        boolean flag = false ;
        try {
            db.beginTransaction();
            if (!isExists(db,healthData.getUserId(),healthData.getTime())){
                db.insert(DBManger.TABLE_HEALTH_DATA, null, healthData.convert(healthData)) ;
                flag = true ;
            }else {
                update(db,healthData);
                flag = true ;
            }
            db.setTransactionSuccessful();
        }catch (Exception e){

        }finally {
            if (db!=null){
                db.endTransaction();
                db.close();
                db =null ;
            }
        }
        return flag ;
    }

    public void update(SQLiteDatabase db,HealthData healthData){

/*        UPDATE table_name
        SET column1 = value1, column2 = value2...., columnN = valueN
        WHERE [condition];*/

        String sql = "update "+DBManger.TABLE_HEALTH_DATA +
                " set "+DBManger.HEALTH_DATA_COMP_VALUE+" =  ?,"+DBManger.HEALTH_DATA_SECOND_VALUE+" = ? ,"+DBManger.HEALTH_DATA_MAX_RATE+" = ?  " +
                "where "+DBManger.HEALTH_DATA_USER_ID+" = ? and "+DBManger.HEALTH_DATA_TIME+" = ?" ;
       db.execSQL(sql,new String[]{healthData.getCompValue(),healthData.getSecondValue(),healthData.getMaxRate(),healthData.getUserId(),healthData.getTime()});
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
            cursor.moveToFirst() ;
            while (cursor.moveToNext()){
                HealthData healthData = new HealthData() ;
                healthData.setUserId(cursor.getString(cursor.getColumnIndex(DBManger.HEALTH_DATA_USER_ID)));
                healthData.setTime(cursor.getString(cursor.getColumnIndex(DBManger.HEALTH_DATA_TIME)));
                healthData.setMaxRate(cursor.getString(cursor.getColumnIndex(DBManger.HEALTH_DATA_MAX_RATE)));
                healthData.setCompValue(cursor.getString(cursor.getColumnIndex(DBManger.HEALTH_DATA_COMP_VALUE)));
                healthData.setSecondValue(cursor.getString(cursor.getColumnIndex(DBManger.HEALTH_DATA_SECOND_VALUE)));
                healthDatas.add(healthData) ;
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

            Log.e("HealthData","11"+e.getMessage()) ;
        }finally {
            if (cursor!=null){
                cursor.close();
                cursor = null;
            }
        }
        return  true ;
    }
}
