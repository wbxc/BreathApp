package com.hhd.breath.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.LinearSmoothScroller;

import com.hhd.breath.app.main.ui.BreathTrainActivity;
import com.hhd.breath.app.model.BreathHistoricalData;
import com.hhd.breath.app.utils.Utils;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by familylove on 2016/7/7.
 */
public class TrainHisService  {

    private DbOpenHelper dbOpenHelper ;
    private static TrainHisService instance ;

    private TrainHisService(Context context){
        dbOpenHelper = new DbOpenHelper(context) ;
    }

    public static TrainHisService getInstance(Context context){
        if (instance!=null)
            return instance ;
        else{
            instance = new TrainHisService(context) ;
            return  instance ;
        }
    }

    private boolean isExits(SQLiteDatabase db,String record_id, String user_id){
        boolean flag = false ;
        String sql = "select * from "+DBManger.TABLE_TRAIN_HIS+" where  "+DBManger.TRAIN_HIS_RECORD_ID+" = ? and "+ DBManger.TRAIN_HIS_USER_ID+" = ?" ;
        Cursor cursor =  null;
        try {
            cursor = db.rawQuery(sql,new String[]{record_id,user_id}) ;
            if (!cursor.isAfterLast())
                flag = true ;
            else
               flag = false ;
        }catch (Exception e){

        }finally {
            if (cursor!=null){
                cursor.close();
                cursor = null;
            }
        }
        return  flag ;
    }


    public void  addList(List<BreathHistoricalData> BreathHistoricalDatas){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase() ;
        try {
            db.beginTransaction();
            for (BreathHistoricalData data : BreathHistoricalDatas){

                if (!isExits(db,data.getRecord_id(),data.getUser_id())){
                    db.insert(DBManger.TABLE_TRAIN_HIS,null,data.toContentValues(data)) ;
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }catch (Exception e){

        }finally {
            if (db!=null){
                db.close();
                db = null ;
            }
        }

    }

    public List<BreathHistoricalData> getList(String user_id,String breath_type){

        SQLiteDatabase db = dbOpenHelper.getReadableDatabase() ;
        List<BreathHistoricalData> list = new ArrayList<BreathHistoricalData>() ;
        String sql = "" ;

        Cursor cursor = null;
        try {

            if (Utils.isNoEmpty(breath_type)){
                sql = "select * from "+DBManger.TABLE_TRAIN_HIS+"  where "+DBManger.TRAIN_HIS_USER_ID+" = ? and "+ DBManger.TRAIN_HIS_BREATH_TYPE+" = ? order by "+DBManger.TRAIN_HIS_TRAIN_TIME+" desc" ;
                cursor = db.rawQuery(sql,new String[]{user_id,breath_type}) ;
            }else {
                sql = "select * from "+DBManger.TABLE_TRAIN_HIS+"  where "+DBManger.TRAIN_HIS_USER_ID+" = ?  order by "+DBManger.TRAIN_HIS_TRAIN_TIME+" desc" ;
                cursor = db.rawQuery(sql,new String[]{user_id}) ;
            }
            if (cursor!=null && cursor.moveToFirst()){
                do {
                    BreathHistoricalData data = new BreathHistoricalData() ;
                    data.setBreath_type(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_BREATH_TYPE)));
                    data.setTrain_time(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_TRAIN_TIME)));
                    data.setRecord_id(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_RECORD_ID)));
                    data.setDifficulty(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_DIFFICULTY)));
                    list.add(data) ;
                }while (cursor.moveToNext()) ;
            }

        }catch (Exception e){

        }finally {
            if (cursor!=null)
                cursor.close();
            cursor = null;
            if (db!=null)
                db.close();
            db = null;
        }
        return list ;
    }

}
