package com.hhd.breath.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/11/23.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "breath.db" ;
    private static int DB_VERSION = 5 ;


    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public DbOpenHelper(Context context, String name, int version) {
        super(context, DbOpenHelper.DB_NAME, null, DbOpenHelper.DB_VERSION);
    }

    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DBManger.CREATE_RECORD_TABLE);
        db.execSQL(DBManger.CREATE_REPORT_TABLE);
        db.execSQL(DBManger.CREATE_TRAIN_DAY_TABLE);
        db.execSQL(DBManger.CREATE_MEDICAL_CASEBOOK);
        db.execSQL(DBManger.CREATE_HEALTH_DATA);
        db.execSQL(DBManger.CREATE_TRAIN_PLAN);
        db.execSQL(DBManger.CREATE_TRAIN_HIS);
        db.execSQL(DBManger.CREATE_TABLE_HIS_LOG);
        db.execSQL(DBManger.CREATE_TABLE_PLAN_LOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion>oldVersion){
            db.execSQL(DBManger.CREATE_MEDICAL_CASEBOOK);
            db.execSQL(DBManger.CREATE_HEALTH_DATA);
            db.execSQL(DBManger.CREATE_TABLE_HIS_LOG);
            db.execSQL(DBManger.CREATE_TABLE_PLAN_LOG);
        }


    }
}
