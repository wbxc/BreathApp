package com.hhd.breath.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.hhd.breath.app.model.MedicalHis;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2016/2/16.
 *
 * 本地存储用户的病例
 *
 */
public class CaseBookService {

    private  DbOpenHelper dbOpenHelper = null ;
    private Context mContext ;
    private static CaseBookService instance = null;


    private CaseBookService(Context mContext){
        this.mContext = mContext ;
    }

    public static CaseBookService getInstance(Context context){


        if (instance == null){
            synchronized (CaseBookService.class){
                if (instance == null){
                    instance = new CaseBookService(context) ;
                }
                if (instance.dbOpenHelper==null){
                    instance.dbOpenHelper = new DbOpenHelper(context) ;
                }
                return instance ;
            }
        }
        return instance ;
    }

    /**
     * 获取所有的曲线
     * @param user_id
     * @return
     */
    public List<MedicalHis> getMedicalHis(String user_id){

        String sql = "select * from "+DBManger.TABLE_MEDICAL_HIS+" where "+DBManger.MEDICAL_HIS_USER_ID+" = ?" ;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase() ;
        Cursor cursor = null ;
        try {
            cursor = db.rawQuery(sql,new String[]{user_id}) ;
            if (cursor!=null){
                List<MedicalHis> medicalHises = new ArrayList<MedicalHis>() ;
                while (cursor.moveToNext()){
                    MedicalHis medicalHis = new MedicalHis() ;
                    medicalHis.setType(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManger.MEDICAL_HIS_TYPE))));
                    medicalHis.setName(cursor.getString(cursor.getColumnIndex(DBManger.MEDICAL_HIS_NAME)));
                    medicalHis.setId(cursor.getString(cursor.getColumnIndex(DBManger.MEDICAL_HIS_ID)));
                    medicalHis.setUserId(cursor.getString(cursor.getColumnIndex(DBManger.MEDICAL_HIS_USER_ID)));
                    medicalHises.add(medicalHis) ;
                }
                return medicalHises ;
            }
        }catch (Exception e){

        }finally {
            if (cursor!=null){
                cursor.close();
                cursor =null;
            }
            if (db!=null){
                db.close();
                db=null;
            }
        }
        return null;
    }

    /**
     * 判断 MEDICAL_HIS 是否有数据
     * @param user_id
     * @return
     */
    public boolean isHasMedicals(String user_id){
        String sql = "select * from "+DBManger.TABLE_MEDICAL_HIS+" where "+DBManger.MEDICAL_HIS_USER_ID+" = ?" ;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase() ;
        Cursor cursor =null;
        try {
            cursor = db.rawQuery(sql,new String[]{user_id}) ;
            if (cursor!=null&&cursor.getCount()>0){
                return  true ;
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
        return false ;
    }

    /**
     * @param medical_id
     * @param medical_type
     * @return true更新成功  false更新失败
     */
    public boolean update(String medical_id,String medical_type){

        String sql = "update "+DBManger.TABLE_MEDICAL_HIS+" SET "+
                     DBManger.MEDICAL_HIS_TYPE+" = '"+medical_type+"' where "+
                     DBManger.MEDICAL_HIS_ID+" = '"+medical_id+"'" ;


        SQLiteDatabase db = dbOpenHelper.getWritableDatabase() ;

        try {
            db.beginTransaction();
            db.execSQL(sql);
            db.setTransactionSuccessful();
            return true ;
        }catch (Exception e){


        }finally {
            db.endTransaction();
            if (db!=null){
                db.close();
                db =null;
            }

        }
        return false ;
    }

    /**
     * 更新
     * @param medicalHises
     */
    public void  updateMedicalHis(List<MedicalHis> medicalHises){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase() ;

        try {
            db.beginTransaction();
            for (MedicalHis medicalHis : medicalHises){
                ContentValues contentValues = new ContentValues() ;
                contentValues.put(DBManger.MEDICAL_HIS_TYPE,String.valueOf(medicalHis.getType()));
                db.update(DBManger.TABLE_MEDICAL_HIS,contentValues,DBManger.MEDICAL_HIS_ID+" = ? and "+DBManger.MEDICAL_HIS_USER_ID+" = ?",new String[]{medicalHis.getId(),medicalHis.getUserId()}) ;
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
     * 批量插入
     * @param medicalHises
     * @return
     */
    public boolean inserts(List<MedicalHis> medicalHises){


        String inserSql = "insert into "+DBManger.TABLE_MEDICAL_HIS+ "("+DBManger.MEDICAL_HIS_USER_ID+","+DBManger.MEDICAL_HIS_ID+", "+DBManger.MEDICAL_HIS_NAME+" ,"+DBManger.MEDICAL_HIS_TYPE+") VALUES(?,?,?,?)" ;

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase() ;
        SQLiteStatement statement = db.compileStatement(inserSql) ;

        try {
            db.beginTransaction();
            for (MedicalHis medicalHis : medicalHises){
                statement.bindString(1,medicalHis.getUserId());
                statement.bindString(2,medicalHis.getId());
                statement.bindString(3,medicalHis.getName());
                statement.bindString(4,String.valueOf(medicalHis.getType()));
                statement.executeInsert() ;
            }
            db.setTransactionSuccessful();
            return true ;

        }catch (Exception e){

            Log.e("medicalHises",e.getMessage()) ;
        }finally {
            statement.close();
            db.endTransaction();
            if (db!=null){
                db.close();
                db=null;
            }
        }
        return false ;
    }

    /**
     * 判断是否有呼吸病史
     * @param userId
     * @return
     */
    public boolean isHasMedical(String userId){
        String sql = "select * from "+DBManger.TABLE_MEDICAL_HIS +" where "+DBManger.MEDICAL_HIS_USER_ID+" = ? and "+DBManger.MEDICAL_HIS_TYPE+" = 1" ;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase() ;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql,new String[]{userId}) ;
            if (cursor!=null && cursor.getCount()>0){
                return true ;
            }else {
                return  false ;
            }

        }catch (Exception e){
            return false ;
        }finally {
            if (cursor!=null){
                cursor.close();
                cursor =null;
            }
            if (db!=null){
                db.close();
                db=null;
            }
        }
    }

    public void clearAllData(){

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase() ;
        try {
            db.beginTransaction();
            db.delete(DBManger.TABLE_MEDICAL_HIS,null,null) ;
            db.setTransactionSuccessful();
        }catch (Exception e){

        }finally {
            db.endTransaction(); ;
            if (db!=null){
                db.close();
                db =null;
            }
        }
    }
}
