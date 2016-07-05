package com.hhd.breath.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.hhd.breath.app.model.MedicalHis;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/16.
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

    public List<MedicalHis> getMedicalHis(){

        String sql = "select * from "+DBManger.TABLE_MEDICAL_HIS ;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase() ;
        Cursor cursor = null ;

        try {
            cursor = db.rawQuery(sql,null) ;
            if (cursor!=null){
                List<MedicalHis> medicalHises = new ArrayList<MedicalHis>() ;
                while (cursor.moveToNext()){
                    MedicalHis medicalHis = new MedicalHis() ;
                    medicalHis.setType(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManger.MEDICAL_HIS_TYPE))));
                    medicalHis.setName(cursor.getString(cursor.getColumnIndex(DBManger.MEDICAL_HIS_NAME)));
                    medicalHis.setId(cursor.getString(cursor.getColumnIndex(DBManger.MEDICAL_HIS_ID)));
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
     *
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

    public void insert(MedicalHis medicalHis){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase() ;
        //SQLiteStatement statement = db.compileStatement(inserSql) ;


        try {
            db.beginTransaction();
            db.insert(DBManger.TABLE_MEDICAL_HIS, null, medicalHis.getContentValues(medicalHis)) ;
            db.setTransactionSuccessful();

        }catch (Exception e){
            Log.e("Exception_Exception",e.getMessage()) ;
        }finally {
            //statement.close();
            db.endTransaction();
            if (db!=null){
                db.close();
                db=null;
            }
        }

    }

    public boolean inserts(List<MedicalHis> medicalHises){


        String inserSql = "insert into "+DBManger.TABLE_MEDICAL_HIS+
                         "("+DBManger.MEDICAL_HIS_ID+", "+DBManger.MEDICAL_HIS_NAME+" ,"+DBManger.MEDICAL_HIS_TYPE+") VALUES(?,?,?)" ;

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase() ;
        SQLiteStatement statement = db.compileStatement(inserSql) ;


        try {
            db.beginTransaction();

            for (MedicalHis medicalHis : medicalHises){

                statement.bindString(1,medicalHis.getId());
                statement.bindString(2,medicalHis.getName());
                statement.bindString(3,String.valueOf(medicalHis.getType()));
                statement.executeInsert() ;
                //db.insert(DBManger.TABLE_MEDICAL_HIS,null,medicalHis.getContentValues(medicalHis)) ;
            }
            db.setTransactionSuccessful();

            return true ;

        }catch (Exception e){

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

    public boolean isNoHasData(){

        String sql = "select * from "+DBManger.TABLE_MEDICAL_HIS ;

        SQLiteDatabase db = dbOpenHelper.getReadableDatabase() ;

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(sql,null) ;
            if (cursor!=null){
                if (!cursor.isAfterLast()){
                    return false ;
                }

                return true ;
            }else {
                return  true ;
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
