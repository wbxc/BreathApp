package com.hhd.breath.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hhd.breath.app.model.TrainReportModel;

/**
 * Created by Administrator on 2015/12/16.
 */
public class ReportService {

    private  static ReportService instance = null;

    private DbOpenHelper mDbOpenHelper = null;
    private Context mContext ;

    public static  ReportService getInstance(Context mContext){

        if (instance == null){
            synchronized (ReportService.class){
                if (instance == null){
                    instance = new ReportService() ;
                }
            }
            if (instance.mDbOpenHelper == null){
                instance.mDbOpenHelper = new DbOpenHelper(mContext) ;
            }
        }
        return instance ;
    }

    public  synchronized boolean insert(TrainReportModel mTrainReportModel){

        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase() ;
        String sql = "insert into "+DBManger.TABLE_TRAIN_REPORT+"(" +
                DBManger.TRAIN_REPORT_ID +","+
                DBManger.TRAIN_REPORT_SUGGESTION+"," +
                DBManger.TRAIN_REPORT_TIME+"," +
                DBManger.TRAIN_REPORT_LEVEL+"," +
                DBManger.TRAIN_REPORT_GROUP+"," +
                DBManger.TRAIN_REPORT_RATE+","+
                DBManger.TRAIN_REPORT_TIME_LONG+")"+" values ('"
                +mTrainReportModel.getReportId()+"','"
                +mTrainReportModel.getReportSuggestion()+"','"
                +mTrainReportModel.getReportTime()+"','"
                +mTrainReportModel.getReportLevel()+"','"
                +mTrainReportModel.getReportGroup()+"','"
                +mTrainReportModel.getReportRate()+"','"
                +mTrainReportModel.getReportTimeLong()+"'"
                + ")" ;
        try {
            db.beginTransaction();
            db.execSQL(sql);
            db.setTransactionSuccessful();
            return true ;
        }catch (Exception e){

            return false ;
        }finally {

            db.endTransaction();
            if (db!=null){
                db.close();
                db = null ;
            }
        }
    }

    private boolean isExist(SQLiteDatabase db,String train_report_id){


        String sql = "select * from " + DBManger.TABLE_TRAIN_REPORT + " where " + DBManger.TRAIN_REPORT_ID
                + " ='" + train_report_id + "'";
        Cursor mCursor = null;
        try{
            mCursor = db.rawQuery(DBManger.TABLE_TRAIN_REPORT, null) ;
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


    public synchronized void delete(String train_report_id){


        String deleteSql = "delete from "+DBManger.TABLE_TRAIN_REPORT+" where "+DBManger.TRAIN_REPORT_ID+" = '"+train_report_id+"'" ;

        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase() ;
        if (isExist(db,train_report_id)){

            try {
                db.beginTransaction();
                db.execSQL(deleteSql);
                db.setTransactionSuccessful();
            }catch (Exception e){

            }finally {
                db.endTransaction();
                if (db!=null){
                    db.endTransaction();
                    db.close();
                    db = null;
                }
            }
        }
    }

    public TrainReportModel find(String train_report_id){


        String findSql = "select * from " + DBManger.TABLE_TRAIN_REPORT+" where "+DBManger.TRAIN_REPORT_ID+" = '"+train_report_id+"'" ;

        Log.e("TrainReportModel",findSql) ;
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase() ;
        Cursor mCursor = null ;
        try {
            //if (isExist(db,train_report_id)){

                mCursor = db.rawQuery(findSql,null) ;
                if (!mCursor.isAfterLast()){
                    mCursor.moveToFirst();


                    TrainReportModel mTrainRecordData = new TrainReportModel() ;
                    mTrainRecordData.setReportTime(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_REPORT_TIME)));
                    mTrainRecordData.setReportGroup(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_REPORT_GROUP)));
                    mTrainRecordData.setReportId(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_REPORT_ID)));
                    mTrainRecordData.setReportLevel(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_REPORT_LEVEL)));
                    mTrainRecordData.setReportRate(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_REPORT_RATE)));
                    mTrainRecordData.setReportSuggestion(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_REPORT_SUGGESTION)));
                    mTrainRecordData.setReportTimeLong(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_REPORT_TIME_LONG)));
                    return mTrainRecordData ;
                }

          //  }
        }catch (Exception e){
            Log.e("TrainReportModel",e.getMessage()+">>>>>") ;
        }finally {

            if (mCursor!=null){
                mCursor.close();
                mCursor = null;
            }
            if (db!=null){
                db.close();
                db = null ;
            }
        }

        return null ;
    }


    public synchronized void update(){

    }
}
