package com.hhd.breath.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.LinearSmoothScroller;
import android.util.Log;

import com.hhd.breath.app.main.ui.BreathTrainActivity;
import com.hhd.breath.app.model.BreathDetailReport;
import com.hhd.breath.app.model.BreathHisLog;
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

    /**
     * 增加本地记录
     * @param data
     */
    public boolean addBreathDetialReport(BreathDetailReport data){

        boolean flag = false ;

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase() ;

        try {

            db.beginTransaction();
            db.insert(DBManger.TABLE_TRAIN_HIS,null,data.toContentValues(data)) ;
            db.setTransactionSuccessful();
            flag = true ;

        }catch (Exception e){

            flag = false ;
        }finally {
            db.endTransaction();
            if (db!=null){
                db.close();
                db = null ;
            }
        }

        return flag ;

    }


    /**
     * 返回最近五条记录
     * @return
     */
    public List<BreathDetailReport> findFiveBreathDetailReports(String breath_type,String user_id){

        //select * from users order by id limit 10 offset 0;//offset代表从第几条记录“之后“开始查询，limit表明查询多少条结果
        /*sqlitecmd.CommandText = string.Format("select * from GuestInfo order by GuestId limit {0} offset {0}*{1}", size, index-1);//size:每页显示条数，index页码*/


        //ASC

        List<BreathDetailReport> breathDetailReports = new ArrayList<BreathDetailReport>() ;
        String sql = "select * from "+DBManger.TABLE_TRAIN_HIS+" where "+DBManger.TRAIN_HIS_BREATH_TYPE+" = ? and "+DBManger.TRAIN_HIS_USER_ID+" = ?"+" order by "+DBManger.TRAIN_HIS_RECORD_ID+" DESC  limit 5 offset 0" ;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase() ;
        Cursor cursor = null;
        try {
            cursor  = db.rawQuery(sql,new String[]{breath_type,user_id}) ;
            if (cursor!=null){
                while (cursor.moveToNext()){
                    BreathDetailReport breathDetailReport = new BreathDetailReport() ;
                    breathDetailReport.setRecord_id(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_RECORD_ID)));
                    breathDetailReport.setBreath_type(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_BREATH_TYPE)));
                    breathDetailReport.setDevice_sn(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_DEVICE_SN)));
                    breathDetailReport.setDifficulty(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_DIFFICULTY)));
                    breathDetailReport.setFile_id(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_FILE_ID)));
                    breathDetailReport.setFile_md5(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_FILE_MD5)));
                    breathDetailReport.setFile_n_name(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_FILE_N_NAME)));
                    breathDetailReport.setFile_path(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_FILE_PATH)));
                    breathDetailReport.setFile_o_name(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_FILE_O_NAME)));
                    breathDetailReport.setFile_size(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_FILE_SIZE)));
                    breathDetailReport.setFile_type(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_FILE_TYPE)));
                    breathDetailReport.setFile_upload_time(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_FILE_UPLOAD_TIME)));
                    breathDetailReport.setIs_delete(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_IS_DELETE)));
                    breathDetailReport.setTrain_time(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_TRAIN_TIME)));
                    breathDetailReport.setPlatform(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_PLATFORM)));
                    breathDetailReport.setTrain_group(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_TRAIN_GROUP)));
                    breathDetailReport.setTrain_last(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_TRAIN_LAST)));
                    breathDetailReport.setTrain_result(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_TRAIN_RESULT)));
                    breathDetailReport.setUser_id(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_USER_ID)));

                    breathDetailReports.add(breathDetailReport) ;
                }
            }
        }catch (Exception e){

            Log.e("trainPlanLog",e.getMessage()) ;

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
        return  breathDetailReports ;
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

    /**
     * 本地增加测试记录
     * @param breathHisLog
     */
    public void addBreathHisLog(BreathHisLog breathHisLog){

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase() ;
        db.beginTransaction();
        db.insert(DBManger.TABLE_HIS_LOG,null,breathHisLog.toContentValues(breathHisLog)) ;
        db.setTransactionSuccessful();
        db.endTransaction();

        if (db!=null){
            db.close();
            db = null ;
        }
    }


    /**
     * 本地查询
     * @param record_id
     * @return
     */
    public BreathHisLog findBreathHisLog(String record_id){


        String sql = "select * from "+DBManger.TABLE_HIS_LOG +" where "+DBManger.HIS_LOG_RECORD_ID+" = ?" ;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase() ;
        Cursor cursor = null;
        BreathHisLog breathHisLog = null;

        try {
            cursor = db.rawQuery(sql,new String[]{record_id}) ;

            if (cursor!=null && cursor.moveToNext()){
                breathHisLog = new BreathHisLog() ;
                breathHisLog.setRecord_id(cursor.getString(cursor.getColumnIndex(DBManger.HIS_LOG_RECORD_ID)));
                breathHisLog.setControlLevel(cursor.getString(cursor.getColumnIndex(DBManger.HIS_LOG_INIT_CONTROL)));
                breathHisLog.setStrengthLevel(cursor.getString(cursor.getColumnIndex(DBManger.HIS_LOG_INIT_STRENGTH)));
                breathHisLog.setPersistentLevel(cursor.getString(cursor.getColumnIndex(DBManger.HIS_LOG_INIT_PERSISTENT)));


                breathHisLog.setCurrentControlLevel(cursor.getString(cursor.getColumnIndex(DBManger.HIS_LOG_INIT_CONTROL)));
                breathHisLog.setCurrentStrengthLevel(cursor.getString(cursor.getColumnIndex(DBManger.HIS_LOG_CURRENT_STRENGTH)));
                breathHisLog.setCurrentPersistentLevel(cursor.getString(cursor.getColumnIndex(DBManger.HIS_LOG_CURRENT_PERSISTENT)));

                breathHisLog.setTrainStartTime(cursor.getString(cursor.getColumnIndex(DBManger.HIS_LOG_START_TRAIN_TIME)));
                breathHisLog.setTrainDays(cursor.getString(cursor.getColumnIndex(DBManger.HIS_LOG_TRAIN_DAYS)));
                breathHisLog.setTrainAverTimes(cursor.getString(cursor.getColumnIndex(DBManger.HIS_LOG_TRAIN_AVER_TIMES)));
                breathHisLog.setTrainTimes(cursor.getString(cursor.getColumnIndex(DBManger.HIS_LOG_TRAIN_TIMES)));
                breathHisLog.setTrainAverValue(cursor.getString(cursor.getColumnIndex(DBManger.HIS_LOG_TRAIN_AVER_VALUE)));
                breathHisLog.setTrainStageValue(cursor.getString(cursor.getColumnIndex(DBManger.HIS_LOG_TRAIN_STATE_VALUE)));
                breathHisLog.setTrainSuccessTimes(cursor.getString(cursor.getColumnIndex(DBManger.HIS_LOG_TRAIN_SUCCESS_TIMES)));
                breathHisLog.setTrainResult(cursor.getString(cursor.getColumnIndex(DBManger.HIS_LOG_TRAIN_RESULT)));
            }
        }catch (Exception e){

        }finally {
            if (cursor!=null){
                cursor.close();
                cursor = null ;
            }
            if (db!=null){
                db.close();
                db = null;
            }
        }
        return breathHisLog ;
    }
}
