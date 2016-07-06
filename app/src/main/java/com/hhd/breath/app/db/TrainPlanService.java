package com.hhd.breath.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hhd.breath.app.model.TrainPlan;

/**
 * Created by Administrator on 2016/7/5.
 */
public class TrainPlanService {

    private DbOpenHelper dbOpenHelper ;
    private static TrainPlanService instance ;
    private Context context ;

    private TrainPlanService(Context context){
        this.context = context ;
        dbOpenHelper = new DbOpenHelper(context) ;
    }
    public static TrainPlanService getInstance(Context context){
        if (instance==null)
            instance = new TrainPlanService(context) ;
        return instance ;
    }

    public boolean add(TrainPlan trainPlan){

        String sql = "" ;
        boolean flag = false ;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase() ;
        try {
            db.beginTransaction();
            if (!isExits(db,trainPlan.getName(),trainPlan.getUserId())){
                db.insert(DBManger.TABLE_TRAIN_PLAN,null,trainPlan.trainPlanToContentValue(trainPlan)) ;
            }else {
                db.update(DBManger.TABLE_TRAIN_PLAN,trainPlan.trainPlanToContentValue(trainPlan),null,null) ;
            }
            db.setTransactionSuccessful();

            flag = true ;
        }catch (Exception e){

            flag = false ;

        }finally {
            db.endTransaction();
            if (db!=null){
                db.close();
                db = null;
            }
        }
        return flag ;
    }

    private boolean isExits(SQLiteDatabase db , String name ,String userId){

        boolean flag = false ;
        String sql = "select * from "+DBManger.TABLE_TRAIN_PLAN+" where "+DBManger.TRAIN_PLAN_NAME +" = ? and "+DBManger.TRAIN_PLAN_USER_ID+" = ?" ;
        Cursor cursor = db.rawQuery(sql,new String[]{name,userId}) ;
        if (!cursor.isAfterLast())
            flag = true ;

        if (cursor!=null){
            cursor.close();
            cursor = null;
        }
        return flag ;
    }

    public int delete(String name,String userId){
        int flag = 2 ;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase() ;

        try {
            db.beginTransaction();
            String whereSql = DBManger.TRAIN_PLAN_NAME+" = ? and "+DBManger.TRAIN_PLAN_USER_ID+" = ?" ;
            flag = db.delete(DBManger.TABLE_TRAIN_PLAN,whereSql,new String[]{name,userId}) ;
            db.setTransactionSuccessful();
        }catch (Exception e){

        }finally {

            db.endTransaction();
            if (db!=null){
                db.close();
                db = null ;
            }
        }
        return flag ;
    }
}
