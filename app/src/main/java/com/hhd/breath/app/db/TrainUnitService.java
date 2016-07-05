package com.hhd.breath.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hhd.breath.app.model.RecordUnitData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by familylove on 2015/11/25.
 */
public class TrainUnitService {

    private static TrainUnitService instance = null ;
    private DbOpenHelper mDbOpenHelper = null;
    private Context mContext ;
    private TrainUnitService(Context mContext){
        this.mContext = mContext ;
    }

    public static TrainUnitService getInstance(Context mContext){

        if (instance == null){

            synchronized (TrainUnitService.class){
                if (instance==null){
                    instance = new TrainUnitService(mContext) ;
                }
            }
            if (instance.mDbOpenHelper == null){
                instance.mDbOpenHelper = new DbOpenHelper(mContext) ;
            }
            return instance ;
        }

        return instance ;
    }


/*    public static String TRAIN_RECORD_TIME_LONG = "train_record_time_long" ;
    public static String TRAIN_RECORD_GROUP_NUMBER = "train_record_group_number" ;
    public static String TRAIN_RECORD_SUGGESTION = "train_record_suggestion" ;*/



    public String insert(RecordUnitData mRecordUnitData){
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase() ;
        if (isExits(db,mRecordUnitData.getId())){
            return "aaa" ;
        }
        String insertSql = "insert into "+DBManger.TABLE_RECORD_DATA+" ("
                +DBManger.TRAIN_RECORD_ID+" ,"
                +DBManger.TRAIN_RECORD_FOREIGN_ID+","
                +DBManger.TRAIN_RECORD_STANDARD_RATE+","
                +DBManger.TRAIN_RECORD_LEVEL+","
                +DBManger.TRAIN_RECORD_TRAIN_TIME +","
                +DBManger.TRAIN_RECORD_TIME_LONG+" ,"
                +DBManger.TRAIN_RECORD_GROUP_NUMBER+" ,"
                +DBManger.TRAIN_RECORD_SUGGESTION +" ,"
                +DBManger.TRAIN_RECORD_TRAIN_NAME+","
                +DBManger.TRAIN_RECORD_TRAIN_PHONE+","
                +DBManger.TRAIN_RECORD_TRAIN_USERNAME+","
                +DBManger.TRAIN_RECORD_TRAIN_SEX+","
                +DBManger.TRAIN_RECORD_TRAIN_BIRTHDAY+","
                +DBManger.TRAIN_RECORD_TRAIN_SER_NUMBER
                +")"
                +" values ( '"+mRecordUnitData.getId()+"','"
                +mRecordUnitData.getForeignId()+"','"
                +mRecordUnitData.getStandardRate()+"','"
                +mRecordUnitData.getLevel()+"','"
                +mRecordUnitData.getTrainTime()+"','"
                +mRecordUnitData.getTimeLong()+"','"
                +mRecordUnitData.getGroupNumber()+"','"
                +mRecordUnitData.getTrainSuggestion()+"','"
                +mRecordUnitData.getTrainName()+"','"
                +mRecordUnitData.getPhone()+"','"
                +mRecordUnitData.getUserName()+"','"
                +mRecordUnitData.getSex()+"','"
                +mRecordUnitData.getBirthday()+"','"
                +mRecordUnitData.getSerNumber()
                +"')" ;

        try {
            db.beginTransaction();
            db.execSQL(insertSql);
            db.setTransactionSuccessful();
            return "bbb" ;

        }catch (Exception e){
            return e.getMessage() ;
        }finally {

            if (db!=null){
                db.endTransaction();
                db.close();
                db = null;
            }

        }
    }

    private boolean isExits(SQLiteDatabase db ,String train_record_id){

        String selectSql = "select * from "+DBManger.TABLE_RECORD_DATA+" where "+DBManger.TRAIN_RECORD_ID +" = "+"'"+train_record_id+"'" ;

        Cursor mCursor = null ;

        try {
            mCursor = db.rawQuery(selectSql,null) ;
            //mCursor.moveToFirst() ;
            if (!mCursor.isAfterLast()){
                return true ;
            }
        }catch (Exception e){
            return false ;
        }finally {

            if (mCursor!=null){
                mCursor.close();
                mCursor =null;
            }
        }
        return false ;
    }

    public RecordUnitData find(String record_unit_id){


        String selectSql = "select * from "+DBManger.TABLE_RECORD_DATA+" where "+DBManger.TRAIN_RECORD_ID +" = "+"'"+record_unit_id+"'" ;



        RecordUnitData mRecordUnitData = null;
        SQLiteDatabase db = null ;
        Cursor mCursor = null ;

        db = mDbOpenHelper.getReadableDatabase() ;
        try {
            mCursor = db.rawQuery(selectSql,null) ;
            if (!mCursor.isAfterLast()){
                mCursor.moveToFirst();
                mRecordUnitData = new RecordUnitData() ;
                mRecordUnitData.setTrainTime(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_TRAIN_TIME)));
                mRecordUnitData.setStandardRate(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_STANDARD_RATE)));
                mRecordUnitData.setLevel(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_LEVEL)));
                mRecordUnitData.setId(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_ID)));
                mRecordUnitData.setForeignId(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_FOREIGN_ID)));

                mRecordUnitData.setGroupNumber(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_GROUP_NUMBER)));
                mRecordUnitData.setTimeLong(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_TIME_LONG)));
                mRecordUnitData.setTrainSuggestion(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_SUGGESTION)));
                mRecordUnitData.setTrainName(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_TRAIN_NAME)));

                mRecordUnitData.setBirthday(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_TRAIN_BIRTHDAY)));
                mRecordUnitData.setSex(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_TRAIN_SEX)));
                mRecordUnitData.setPhone(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_TRAIN_PHONE)));
                mRecordUnitData.setUserName(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_TRAIN_USERNAME)));
                mRecordUnitData.setSerNumber(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_TRAIN_SER_NUMBER)));
                return mRecordUnitData ;
            }
        }catch (Exception e){



        }finally {

            if (mCursor!=null){
                mCursor.close();
                mCursor =null;
            }
            if (db!=null){
                db.close();
                db = null;
            }
        }
        return mRecordUnitData;
    }


    /**
     * 获取没有上传的数据
     * 对数据进行后台更新
     * @return
     */

    /**
     *  *user_id
     *file_id
     *breath_type
     *train_group
     *train_time
     *train_last
     *train_result
     *difficulty
     *suggesti
     * @return
     */

    public List<RecordUnitData> findUnUploads(){

        String sql = "select * from "+DBManger.TABLE_RECORD_DATA+" where "+DBManger.TRAIN_RECORD_TRAIN_UPLOAD_FLAG+"='1'" ;
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase() ;
        Cursor mCursor = null ;

        try {
            mCursor = db.rawQuery(sql,null) ;
            if (mCursor!=null){
                List<RecordUnitData> recordUnitDatas = new ArrayList<RecordUnitData>() ;
                mCursor.moveToFirst() ;
                while (mCursor.moveToNext()){
                    RecordUnitData recordUnitData = new RecordUnitData() ;
                    recordUnitData.setGroupNumber(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_GROUP_NUMBER)));
                    recordUnitData.setSerNumber(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_TRAIN_SER_NUMBER)));
                    recordUnitData.setStandardRate(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_STANDARD_RATE)));
                    recordUnitData.setSign(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_TRAIN_UPLOAD_FLAG))); //标记
                    recordUnitData.setTrainName(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_TRAIN_TIME))); //训练时间
                    recordUnitDatas.add(recordUnitData);
                }
                return recordUnitDatas ;
            }
        }catch (Exception e){
        }finally {
            if (mCursor!=null)
                mCursor.close();
            mCursor = null;
            if (db!=null){
                db.close();
                db = null;
            }
        }
        return null ;
    }



    public List<RecordUnitData> finaAll(){

        String sql = "select * from "+DBManger.TABLE_RECORD_DATA ;
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase() ;
        Cursor mCursor = null ;
        try {
            mCursor = db.rawQuery(sql,null) ;
            if (mCursor!=null){
                List<RecordUnitData> mRecordUnitDatas  = new ArrayList<RecordUnitData>() ;
                mCursor.moveToFirst() ;
                while (mCursor.moveToNext()){
                    RecordUnitData mRecordUnitData = new RecordUnitData() ;
                    mRecordUnitData.setId(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_ID)));
                    mRecordUnitDatas.add(mRecordUnitData) ;
                }
                return mRecordUnitDatas ;
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

        return null ;
    }

    public List<RecordUnitData> finaAllByForginId(String forginId){

        String sql = "select * from "+DBManger.TABLE_RECORD_DATA+" where "+DBManger.TRAIN_RECORD_FOREIGN_ID+" = "+forginId +" order by "+DBManger.TRAIN_RECORD_TRAIN_TIME+" desc" ;
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase() ;

        Cursor mCursor = null ;
        try {
            mCursor = db.rawQuery(sql,null) ;
            if (mCursor!=null){
                List<RecordUnitData> mRecordUnitDatas  = new ArrayList<RecordUnitData>() ;
                while (mCursor.moveToNext()){
                    RecordUnitData mRecordUnitData = new RecordUnitData() ;
                    mRecordUnitData.setId(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_ID)));
                    mRecordUnitData.setGroupNumber(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_GROUP_NUMBER)));
                    mRecordUnitData.setTrainTime(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_TRAIN_TIME)));
                    mRecordUnitData.setTrainName(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_TRAIN_NAME)));
                    mRecordUnitData.setStandardRate(mCursor.getString(mCursor.getColumnIndex(DBManger.TRAIN_RECORD_STANDARD_RATE)));
                    mRecordUnitDatas.add(mRecordUnitData) ;
                }
                return mRecordUnitDatas ;
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

        return null ;
    }



}
