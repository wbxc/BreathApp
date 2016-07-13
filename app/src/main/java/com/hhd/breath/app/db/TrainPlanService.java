package com.hhd.breath.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hhd.breath.app.model.TrainPlan;

import java.util.ArrayList;
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

    public boolean add(TrainPlan trainPlan) {

        String sql = "";
        boolean flag = false;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            if (!isExits(db, trainPlan.getName(), trainPlan.getUserId())) {
                db.insert(DBManger.TABLE_TRAIN_PLAN, null, trainPlan.trainPlanToContentValue(trainPlan));
                flag = true;
            }/*else {
                db.update(DBManger.TABLE_TRAIN_PLAN,trainPlan.trainPlanToContentValue(trainPlan),null,null) ;
            }*/ else {
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

    public int delete(String name, String userId) {
        int flag = 2;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        try {
            db.beginTransaction();
            String whereSql = DBManger.TRAIN_PLAN_NAME + " = ? and " + DBManger.TRAIN_PLAN_USER_ID + " = ?";
            flag = db.delete(DBManger.TABLE_TRAIN_PLAN, whereSql, new String[]{name, userId});
            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {

            db.endTransaction();
            if (db != null) {
                db.close();
                db = null;
            }
        }
        return flag;
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
}
