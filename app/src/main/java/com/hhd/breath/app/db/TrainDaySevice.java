package com.hhd.breath.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hhd.breath.app.model.RecordDayData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/17.
 */
public class TrainDaySevice {

    private static  TrainDaySevice instance = null;
    private DbOpenHelper mDbOpenHelper = null;

    public static TrainDaySevice getInstance(Context mContext){

        if (instance == null){

            synchronized (TrainDaySevice.class){
                if (instance == null){
                    instance = new TrainDaySevice() ;
                }
            }
            if (instance.mDbOpenHelper == null){
                instance.mDbOpenHelper = new DbOpenHelper(mContext) ;
            }

            return instance ;
        }
        return instance ;
    }

    //记录 天测试记录
/*    public static String TABLE_TRAIN_DAY = "train_day" ;

    public static String TRAIN_DAY_ID = "train_day_id" ;
    public static String TRAIN_DAY_RECORD_TIME = "train_day_record_time" ;
    public static String TRAIN_DAY_FOREIGN_ID = "train_day_foreign_id" ;
    public static String TRAIN_DAY_ACCOUNT = "train_day_account" ;*/


    public void insert(RecordDayData mRecordDayData){

        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase() ;
        if (isExits(db,mRecordDayData.getId())){
            return  ;
        }
        String insertSql = "insert into "+DBManger.TABLE_TRAIN_DAY+"("
                +DBManger.TRAIN_DAY_ID+","
                +DBManger.TRAIN_DAY_ACCOUNT+","
                +DBManger.TRAIN_DAY_FOREIGN_ID+","
                +DBManger.TRAIN_DAY_RECORD_TIME+")"
                +"values"+"('"+mRecordDayData.getId()+"','"
                +mRecordDayData.getAccount()+"','"
                +mRecordDayData.getForeignId()+"','"
                +mRecordDayData.getRecordTime()
                +"')" ;

        Log.e("TrainReportModel", insertSql) ;
        try{
            db.beginTransaction();
            db.execSQL(insertSql);
            db.setTransactionSuccessful();

        }catch (Exception e){


        }finally {
            if (db!=null){
                db.endTransaction();
                db.close();
                db = null;
            }
        }


    }

    public boolean  updateAccount(String train_day_id , String account){

        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase() ;

/*        UPDATE table_name
        SET column1 = value1, column2 = value2...., columnN = valueN
        WHERE [condition];*/

        String updateSql = "update "+DBManger.TABLE_TRAIN_DAY+" SET "+DBManger.TRAIN_DAY_ACCOUNT
                            +" = '"+account+"' where "+DBManger.TRAIN_DAY_ID+" = '"+train_day_id+"'" ;

        try {
            db.beginTransaction();
            db.execSQL(updateSql);
            db.setTransactionSuccessful();
            return true ;
        }catch (Exception e){

        }finally {
            db.endTransaction();
            if (db!=null){
                db.close();
                db = null ;
            }
        }

        return false ;
    }

    public String getAccount(String train_day_id){

        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase() ;

        if (isExits(db,train_day_id)){

            String selectSql = "select * from "+DBManger.TABLE_TRAIN_DAY+" where "+DBManger.TRAIN_DAY_ID+" = "+"'"+train_day_id+"'" ;

            Cursor mCursor = null ;

            try {
                mCursor = db.rawQuery(selectSql,null) ;
                if (!mCursor.isAfterLast()){
                    mCursor.moveToFirst() ;

                    return  mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_DAY_ACCOUNT)) ;
                }
            }catch (Exception e){

            }finally {

                if (mCursor!=null){
                    mCursor.close();
                    mCursor = null;
                }
                if (db!=null){
                    db.close();
                    db = null;
                }
            }
        }else{
            if (db!=null){
                db.close();
                db = null;
            }
        }
        return "-1";
    }





    private boolean isExits(SQLiteDatabase db , String train_day_id){

        String selectSql = "select * from "+DBManger.TABLE_TRAIN_DAY+" where "+DBManger.TRAIN_DAY_ID+" = "+"'"+train_day_id+"'" ;

        Cursor mCursor = null ;

        try {
            mCursor = db.rawQuery(selectSql,null) ;
            if (!mCursor.isAfterLast()){
                return true ;
            }
        }catch (Exception e){

        }finally {

            if (mCursor!=null){
                mCursor.close();
                mCursor = null;
            }
        }
        return false ;
    }

    public RecordDayData find(String train_day_id){

        SQLiteDatabase db  = mDbOpenHelper.getReadableDatabase() ;

        String selectSql = "select * from "+DBManger.TABLE_TRAIN_DAY+" where "+DBManger.TRAIN_DAY_ID+" = "+"'"+train_day_id+"'" ;

        Cursor mCursor = null ;

        try {
            mCursor = db.rawQuery(selectSql,null) ;
            if (!mCursor.isAfterLast()){
                mCursor.moveToFirst() ;


                RecordDayData mRecordDayData = new RecordDayData() ;
                mRecordDayData.setRecordTime(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_DAY_RECORD_TIME)));
                mRecordDayData.setId(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_DAY_ID)));
                mRecordDayData.setForeignId(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_DAY_FOREIGN_ID)));

                mRecordDayData.setAccount(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_DAY_ACCOUNT)));
                return mRecordDayData ;

            }
        }catch (Exception e){

        }finally {

            if (mCursor!=null){
                mCursor.close();
                mCursor = null;
            }
            if (db !=null){
                db.close();
                db = null;
            }
        }

        return null;
    }

   // select * from users order by id limit 10 offset 0;//offset代表从第几条记录“之后“开始查询，limit表明查询多少条结果

    public List<RecordDayData> findLimitAll(int size , int sumIndex){

        //跳过sumIndex行，取size行
        String sql = "select * from "+DBManger.TABLE_TRAIN_DAY +" order by "+DBManger.TRAIN_DAY_ID+" desc"+" limit "+sumIndex+" , "+size  ;
        SQLiteDatabase db = null;
        db = mDbOpenHelper.getReadableDatabase() ;
        List<RecordDayData> mRecordDayDatas = new ArrayList<RecordDayData>() ;
        Cursor mCursor = null;
        try{
            mCursor = db.rawQuery(sql,null) ;
            if (mCursor!=null){
                while (mCursor.moveToNext()){
                    RecordDayData mRecordDayData = new RecordDayData() ;
                    mRecordDayData.setAccount(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_DAY_ACCOUNT)));
                    mRecordDayData.setId(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_DAY_ID))) ;
                    mRecordDayData.setForeignId(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_DAY_FOREIGN_ID)));
                    mRecordDayData.setRecordTime(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_DAY_RECORD_TIME)));
                    mRecordDayDatas.add(mRecordDayData) ;
                }
                return mRecordDayDatas ;
            }

        }catch (Exception e){

        }finally {

            if (mCursor!=null){
                mCursor.close();
                mCursor = null;
            }
            if (db!=null){
                db.close();
                db = null;
            }
        }
        return null;
    }




    public List<RecordDayData> findAll(){

        String sql = "select * from "+DBManger.TABLE_TRAIN_DAY +" order by "+DBManger.TRAIN_DAY_RECORD_TIME+" desc"  ;
        SQLiteDatabase db = null;
        db = mDbOpenHelper.getReadableDatabase() ;
        List<RecordDayData> mRecordDayDatas = new ArrayList<RecordDayData>() ;
        Cursor mCursor = null;
        try{
            mCursor = db.rawQuery(sql,null) ;
            if (mCursor!=null){
                while (mCursor.moveToNext()){
                    RecordDayData mRecordDayData = new RecordDayData() ;
                    mRecordDayData.setAccount(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_DAY_ACCOUNT)));
                    mRecordDayData.setId(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_DAY_ID))) ;
                    mRecordDayData.setForeignId(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_DAY_FOREIGN_ID)));
                    mRecordDayData.setRecordTime(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_DAY_RECORD_TIME)));
                    mRecordDayDatas.add(mRecordDayData) ;
                }
                return mRecordDayDatas ;
            }

        }catch (Exception e){

        }finally {

            if (mCursor!=null){
                mCursor.close();
                mCursor = null;
            }
            if (db!=null){
                db.close();
                db = null;
            }
        }
        return null;
    }



}
