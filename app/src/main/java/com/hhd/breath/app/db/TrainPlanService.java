package com.hhd.breath.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hhd.breath.app.model.TrainPlan;
import com.hhd.breath.app.model.TrainPlanLog;
import com.hhd.breath.app.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/5.
 * <p>
 * 训练计划管理    训练计划创建
 */
public class TrainPlanService {

    private DbOpenHelper dbOpenHelper;
    private static TrainPlanService instance;
    private Context context;

    private TrainPlanService(Context context) {
        this.context = context;
        dbOpenHelper = new DbOpenHelper(context);
    }

    public static TrainPlanService getInstance(Context context) {
        if (instance == null)
            instance = new TrainPlanService(context);
        return instance;
    }

    /**
     * 创建 训练模式
     * @param trainPlan
     * @return
     */
    public boolean add(TrainPlan trainPlan) {

        String sql = "";
        boolean flag = false;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            if (!isExits(db, trainPlan.getName(), trainPlan.getUserId())) {
                db.insert(DBManger.TABLE_TRAIN_PLAN, null, trainPlan.trainPlanToContentValue(trainPlan));
                flag = true;
            } else {
                flag = false;
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            flag = false;
        } finally {
            db.endTransaction();
            if (db != null) {
                db.close();
                db = null;
            }
        }
        return flag;
    }

    public void updateTrainPlan(TrainPlan trainPlan) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues() ;
            contentValues.put(DBManger.TRAIN_PLAN_CONTROL_CURRENT_LEVEL,trainPlan.getCurrentControl());
            contentValues.put(DBManger.TRAIN_PLAN_CONTROL_LEVEL,trainPlan.getControlLevel());
            contentValues.put(DBManger.TRAIN_PLAN_STRENGTH_CURRENT_LEVEL,trainPlan.getCurrentStrength());
            contentValues.put(DBManger.TRAIN_PLAN_STRENGTH_LEVEL,trainPlan.getStrengthLevel());
            contentValues.put(DBManger.TRAIN_PLAN_PERSISTENT_CURRENT_LEVEL,trainPlan.getCurrentPersistent());
            contentValues.put(DBManger.TRAIN_PLAN_PERSISTENT_LEVEL,trainPlan.getPersistentLevel()) ;
            contentValues.put(DBManger.TRAIN_PLAN_SUM_TRAIN_TIMES,trainPlan.getSumTrainTimes()+1);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
            String dateFlag = sdf.format(new Date(System.currentTimeMillis())) ;
            if (!trainPlan.getTrainDayFlag().equals(dateFlag)){
                trainPlan.setTrainDayFlag(dateFlag);
                contentValues.put(DBManger.TRAIN_PLAN_SUM_TRAIN_TIMES,trainPlan.getSumTrainTimes()+1);
            }
            db.update(DBManger.TABLE_TRAIN_PLAN,contentValues,DBManger.TRAIN_PLAN_USER_ID+" = ? and "+DBManger.TRAIN_PLAN_NAME+" = ?",new String[]{trainPlan.getUserId(),trainPlan.getName()});
            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();
            if (db != null) {
                db.close();
                db = null;
            }
        }
    }


    /**
     * 累计训练时间
     * @param timeLast
     * @param trainPlan
     */
    public void countSumTime(String timeLast , TrainPlan trainPlan){

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase() ;

        try {

            db.beginTransaction();
            int sum = Integer.parseInt(trainPlan.getCumulativeTime())+Integer.parseInt(timeLast) ;
            ContentValues contentValues = new ContentValues() ;
            contentValues.put(DBManger.TRAIN_PLAN_CUM_TIME,String.valueOf(sum));
            db.update(DBManger.TABLE_TRAIN_PLAN,contentValues,DBManger.TRAIN_PLAN_NAME+" = ?  and "+DBManger.TRAIN_PLAN_USER_ID+" = ?",new String[]{trainPlan.getName(),trainPlan.getUserId()}) ;
            db.setTransactionSuccessful();
        }catch (Exception e){


        }finally {
            db.endTransaction();
            if (db!=null){
                db.close();
                db = null;
            }
        }

    }



    public int countTrainPlan(String userId){

        int result = 0;
        String sql = "select * from "+DBManger.TABLE_TRAIN_PLAN ;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase() ;
        Cursor cursor = db.rawQuery(sql,null) ;
        result = cursor.getCount() ;

        cursor.close();
        cursor = null;
        db.close();
        db = null;
        return result ;
    }

    private boolean isExits(SQLiteDatabase db, String name, String userId) {

        boolean flag = false;
        String sql = "select * from " + DBManger.TABLE_TRAIN_PLAN + " where " + DBManger.TRAIN_PLAN_NAME + " = ? and " + DBManger.TRAIN_PLAN_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{name, userId});
        if (!cursor.isAfterLast())
            flag = true;
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return flag;
    }


    public boolean exits(String userId, String trainType) {

        String sql = "select * from " + DBManger.TABLE_TRAIN_PLAN + " WHERE " + DBManger.TRAIN_PLAN_TYPE + " = ? AND " + DBManger.TRAIN_PLAN_USER_ID + " = ?";

        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = null;
        try {

            cursor = db.rawQuery(sql, new String[]{trainType, userId});
            if (!cursor.isAfterLast()) {
                return true;
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null)
                cursor.close();
            cursor = null;
            if (db != null)
                db.close();
            db = null;
        }
        return false;
    }

    public TrainPlan find() {

        String sql = "select * from " + DBManger.TABLE_TRAIN_PLAN;

        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = null;
        TrainPlan trainPlan = new TrainPlan();
        try {
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToNext()) {
                trainPlan.setName(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_NAME)));
                trainPlan.setControl(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLANE_CONTROL)));
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }

            if (db != null) {
                db.close();
                db = null;
            }
        }

        return trainPlan;
    }

    public TrainPlan findTrainPlan(String user_id ,String trainType){

        String sql = "select * from " + DBManger.TABLE_TRAIN_PLAN + " where " + DBManger.TRAIN_PLAN_USER_ID + " = ?  and "+DBManger.TRAIN_PLAN_TYPE+" = ?";
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = null;
        TrainPlan trainPlan = null ;
        try {
            cursor = db.rawQuery(sql,new String[]{user_id,trainType}) ;
            if (cursor!=null && cursor.moveToNext()){
                trainPlan = new TrainPlan() ;
                trainPlan.setName(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_NAME)));
                trainPlan.setControl(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLANE_CONTROL)));
                trainPlan.setCreateTime(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_CREATE_TIME)));
                trainPlan.setGroupNumber(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_GROUP_NUMBER)));
                trainPlan.setControlLevel(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_CONTROL_LEVEL)));
                trainPlan.setCumulativeTime(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_CUM_TIME)));
                trainPlan.setPersistent(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_PERSISTENT)));
                trainPlan.setPersistentLevel(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_PERSISTENT_LEVEL)));
                trainPlan.setInspirerTime(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_INSPIRER_TIME)));
                trainPlan.setStrength(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_STRENGTH)));
                trainPlan.setStrengthLevel(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_STRENGTH_LEVEL)));
                trainPlan.setTimes(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_TIMES)));
                trainPlan.setTrainType(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_TYPE)));
                trainPlan.setCurrentControl(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_CONTROL_CURRENT_LEVEL)));
                trainPlan.setCurrentStrength(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_STRENGTH_CURRENT_LEVEL)));
                trainPlan.setCurrentPersistent(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_PERSISTENT_CURRENT_LEVEL)));
                trainPlan.setUserId(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_USER_ID)));
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
        return trainPlan ;
    }


    /**
     *  获取所有的
     * @param userId
     * @return
     */
    public List<TrainPlan> getTrainPlans(String userId) {

        String sql = "select * from " + DBManger.TABLE_TRAIN_PLAN + " WHERE " + DBManger.TRAIN_PLAN_USER_ID + " = ?";
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = null;
        List<TrainPlan> trainPlans = new ArrayList<TrainPlan>();
        try {
            cursor = db.rawQuery(sql, new String[]{userId});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    TrainPlan trainPlan = new TrainPlan();
                    trainPlan.setName(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_NAME)));
                    trainPlan.setControl(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLANE_CONTROL)));
                    trainPlan.setCreateTime(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_CREATE_TIME)));
                    trainPlan.setGroupNumber(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_GROUP_NUMBER)));
                    trainPlan.setControlLevel(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_CONTROL_LEVEL)));
                    trainPlan.setCumulativeTime(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_CUM_TIME)));
                    trainPlan.setPersistent(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_PERSISTENT)));
                    trainPlan.setPersistentLevel(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_PERSISTENT_LEVEL)));
                    trainPlan.setInspirerTime(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_INSPIRER_TIME)));
                    trainPlan.setStrength(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_STRENGTH)));
                    trainPlan.setStrengthLevel(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_STRENGTH_LEVEL)));
                    trainPlan.setTimes(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_TIMES)));
                    trainPlan.setTrainType(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_TYPE)));
                    trainPlan.setCurrentControl(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_CONTROL_CURRENT_LEVEL)));
                    trainPlan.setCurrentStrength(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_STRENGTH_CURRENT_LEVEL)));
                    trainPlan.setCurrentPersistent(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_PERSISTENT_CURRENT_LEVEL)));
                    trainPlan.setUserId(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_HIS_USER_ID)));

                    trainPlans.add(trainPlan);
                }
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
            if (db != null) {
                db.close();
                db = null;
            }

        }
        return trainPlans;
    }


    /**
     * 增加训练类型的日志
     * @param trainPlanLog
     */
    public void addTrainLog(TrainPlanLog trainPlanLog){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase() ;
        try {
            db.beginTransaction();
            if (!isTrainPlanLogExists(db,trainPlanLog)){   // 初始化操作
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
                trainPlanLog.setTrainDayFlag(simpleDateFormat.format(new Date(System.currentTimeMillis())));

                trainPlanLog.setTrainTimes(1);
                trainPlanLog.setDays(1);
                db.insert(DBManger.TABLE_TRAIN_PLAN_LOG,null,trainPlanLog.toContentValues(trainPlanLog)) ;
            }else {   // 更新操作
                addTrainTimes(db,trainPlanLog);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
                String dateFlag = sdf.format(new Date(System.currentTimeMillis())) ;
                if (!trainPlanLog.getTrainDayFlag().equals(dateFlag)){
                    trainPlanLog.setTrainDayFlag(dateFlag);
                    addTrainDays(db,trainPlanLog);
                }
            }
            db.setTransactionSuccessful();
        }catch (Exception e){
        }finally {
            db.endTransaction();
            if (db!=null){
                db.close();
                db = null;
            }
        }
    }

    /**
     * 检测训练日志是否存在
     * @param db
     * @param trainPlanLog
     * @return
     */
    private boolean isTrainPlanLogExists(SQLiteDatabase db , TrainPlanLog trainPlanLog){
        Cursor cursor = null;
        try {
            String sql = "select * from  "+DBManger.TABLE_TRAIN_PLAN_LOG +" where "+DBManger.TRAIN_PLAN_LOG_NAME+" = ? and "+DBManger.TRAIN_PLAN_LOG_USER_ID+" = ?" ;
            cursor = db.rawQuery(sql,new String[]{trainPlanLog.getName(),trainPlanLog.getUserId()}) ;
            if (cursor!=null && cursor.getCount()>0){
                return true ;
            }
        }catch (Exception e){

        }finally {
            if (cursor!=null){
                cursor.close();
                cursor = null;
            }
        }
        return false ;
    }


    // 训练次数累计增加一个
    public void addTrainTimes(TrainPlanLog trainPlanLog){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase() ;
        db.beginTransaction();
        String updateSql = "update "+DBManger.TABLE_TRAIN_PLAN_LOG+
                            " SET "+DBManger.TRAIN_PLAN_LOG_TRAIN_TIMES +" = '"+(trainPlanLog.getTrainTimes()+1)+"'" +
                            " where "+DBManger.TRAIN_PLAN_LOG_NAME+" ='"+trainPlanLog.getName()+"' and "+DBManger.TRAIN_PLAN_LOG_TRAIN_TYPE+" = '"+trainPlanLog.getTrainType()+"'" ;
        db.execSQL(updateSql);
        db.setTransactionSuccessful();
        db.endTransaction();

        if (db!=null){
            db.close();
            db = null;
        }
    }


    // 训练次数累计增加一个
    private void addTrainTimes(SQLiteDatabase db,TrainPlanLog trainPlanLog){
        String updateSql = "update "+DBManger.TABLE_TRAIN_PLAN_LOG+
                " SET "+DBManger.TRAIN_PLAN_LOG_TRAIN_TIMES +" = '"+(trainPlanLog.getTrainTimes()+1)+"'" +
                " where "+DBManger.TRAIN_PLAN_LOG_NAME+" ='"+trainPlanLog.getName()+"' and "+DBManger.TRAIN_PLAN_LOG_TRAIN_TYPE+" = '"+trainPlanLog.getTrainType()+"'" ;
        db.execSQL(updateSql);
    }


    // 天累计
    private void addTrainDays(SQLiteDatabase db,TrainPlanLog trainPlanLog){
        db.beginTransaction();
        String update = "update "+DBManger.TABLE_TRAIN_PLAN_LOG+
                " SET "+DBManger.TRAIN_PLAN_LOG_DAYS+" = '"+(trainPlanLog.getDays()+1)+"'"+"," + DBManger.TRAIN_PLAN_LOG_DAY_FLAG+" = '"+trainPlanLog.getTrainDayFlag()+"'"+
                " where "+DBManger.TRAIN_PLAN_NAME+" = '"+trainPlanLog.getName()+"' and "+DBManger.TRAIN_PLAN_LOG_TRAIN_TYPE+" = '"+trainPlanLog.getTrainType()+"'" ;
        db.execSQL(update);
    }








    // 天累计
    public void addTrainDays(TrainPlanLog trainPlanLog){

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase() ;

        db.beginTransaction();
        String update = "update "+DBManger.TABLE_TRAIN_PLAN_LOG+
                         " SET "+DBManger.TRAIN_PLAN_LOG_DAYS+" = '"+(trainPlanLog.getDays()+1)+"'"+"," + DBManger.TRAIN_PLAN_LOG_DAY_FLAG+" = '"+trainPlanLog.getTrainDayFlag()+"'"+
                         " where "+DBManger.TRAIN_PLAN_NAME+" = '"+trainPlanLog.getName()+"' and "+DBManger.TRAIN_PLAN_LOG_TRAIN_TYPE+" = '"+trainPlanLog.getTrainType()+"'" ;
        db.execSQL(update);
        db.setTransactionSuccessful();
        db.endTransaction();
        if (db!=null){
            db.close();
            db = null;
        }
    }




    /**
     * 查询trainPlanLog
     * @param name
     * @param type
     * @return
     */
    public TrainPlanLog findTrainPlanLog(String name , String type){

        String sql = "select * from  "+DBManger.TABLE_TRAIN_PLAN_LOG+" where "+DBManger.TRAIN_PLAN_LOG_TRAIN_TYPE+" = ? and "+DBManger.TRAIN_PLAN_LOG_NAME+" = ?" ;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase() ;
        Cursor cursor = null;
        TrainPlanLog trainPlanLog = null ;

        try {
            cursor = db.rawQuery(sql , new String[]{type,name}) ;
            if (cursor!=null && cursor.moveToNext()){
                trainPlanLog = new TrainPlanLog() ;
                trainPlanLog.setName(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_LOG_NAME)));
                trainPlanLog.setDays(cursor.getInt(cursor.getColumnIndex(DBManger.TRAIN_PLAN_LOG_DAYS)));
                trainPlanLog.setTrainTimes(cursor.getInt(cursor.getColumnIndex(DBManger.TRAIN_PLAN_LOG_TRAIN_TIMES)));
                trainPlanLog.setTrainStartTime(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_LOG_START_TIME)));
                trainPlanLog.setTrainType(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_LOG_TRAIN_TYPE)));
                trainPlanLog.setUserId(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_LOG_USER_ID)));
                trainPlanLog.setTrainDayFlag(cursor.getString(cursor.getColumnIndex(DBManger.TRAIN_PLAN_LOG_DAY_FLAG)));

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
        return  trainPlanLog ;
    }






}
