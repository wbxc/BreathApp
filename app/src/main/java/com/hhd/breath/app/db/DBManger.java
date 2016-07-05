package com.hhd.breath.app.db;

/**
 * Created by Administrator on 2015/12/16.
 */
public class DBManger {


    //训练报告
    public static  String TABLE_TRAIN_REPORT = "train_report" ;

    public  static String TRAIN_REPORT_RATE = "train_report_rate" ;
    public  static String TRAIN_REPORT_TIME_LONG = "train_report_time_long" ;
    public  static String TRAIN_REPORT_GROUP = "train_report_group" ;
    public  static String TRAIN_REPORT_LEVEL = "train_report_level" ;
    public  static String TRAIN_REPORT_TIME = "train_report_time" ;
    public  static String TRAIN_REPORT_SUGGESTION = "train_report_suggestion" ;
    public  static String TRAIN_REPORT_ID = "train_report_id" ;


    /**
     * 记录报告
     *
     */
    public static String CREATE_REPORT_TABLE = "create table if not exists "
            + TABLE_TRAIN_REPORT
            +"("+"id integer primary key AUTOINCREMENT,"
            +TRAIN_REPORT_RATE+" VARCHAR(20),"
            +TRAIN_REPORT_TIME_LONG+" VARCHAR(20),"
            +TRAIN_REPORT_GROUP+" VARCHAR(20),"
            +TRAIN_REPORT_LEVEL+" VARCHAR(10),"
            +TRAIN_REPORT_TIME +" VARCHAR(20),"
            +TRAIN_REPORT_SUGGESTION+" VARCHAR(20),"
            +TRAIN_REPORT_ID+" VARCHAR(25)"
            +")" ;


    //记录 天测试记录
    public static String TABLE_TRAIN_DAY = "train_day" ;

    public static String TRAIN_DAY_ID = "train_day_id" ;
    public static String TRAIN_DAY_RECORD_TIME = "train_day_record_time" ;
    public static String TRAIN_DAY_FOREIGN_ID = "train_day_foreign_id" ;
    public static String TRAIN_DAY_ACCOUNT = "train_day_account" ;

    public static String CREATE_TRAIN_DAY_TABLE = "create table if not exists "
            +TABLE_TRAIN_DAY+" ("
            +"id integer primary key AUTOINCREMENT,"
            +TRAIN_DAY_RECORD_TIME+" VARCHAR(20),"
            +TRAIN_DAY_ID+" VARCHAR(20),"
            +TRAIN_DAY_FOREIGN_ID+" VARCHAR(20),"
            +TRAIN_DAY_ACCOUNT+" VARCHAR(20)"
            +")";


    //记录一天中测试的每一条记录

    public static String TABLE_RECORD_DATA = "train_record_data" ;
    public static String TRAIN_RECORD_ID = "train_record_id" ;
    public static String TRAIN_RECORD_TRAIN_TIME = "train_record_train_time" ;
    public static String TRAIN_RECORD_LEVEL = "train_record_level" ;
    public static String TRAIN_RECORD_STANDARD_RATE = "train_record_standard_rate" ;
    public static String TRAIN_RECORD_FOREIGN_ID = "train_record_foreign_id" ;





    public static String TRAIN_RECORD_TIME_LONG = "train_record_time_long" ;
    public static String TRAIN_RECORD_GROUP_NUMBER = "train_record_group_number" ;
    public static String TRAIN_RECORD_SUGGESTION = "train_record_suggestion" ;
    public static String TRAIN_RECORD_TRAIN_NAME = "train_record_train_name" ;




    public static String TRAIN_RECORD_TRAIN_PHONE = "train_record_train_phone" ;
    public static String TRAIN_RECORD_TRAIN_SEX = "train_record_train_sex" ;
    public static String TRAIN_RECORD_TRAIN_USERNAME = "train_record_train_username" ;
    public static String TRAIN_RECORD_TRAIN_BIRTHDAY = "train_record_train_birthday" ;
    public static String TRAIN_RECORD_TRAIN_SER_NUMBER = "train_record_train_ser_number" ;
    public static String TRAIN_RECORD_TRAIN_UPLOAD_FLAG = "train_record_upload_flag" ;


    /**
     * 测试记录
     * 一条详细的记录
     */
    public static String CREATE_RECORD_TABLE = "create table if not exists "
            +TABLE_RECORD_DATA+" ("
            +"id integer primary key AUTOINCREMENT ,"
            +TRAIN_RECORD_ID+" VARCHAR(20),"
            +TRAIN_RECORD_TRAIN_TIME+" VARCHAR(20) ,"
            +TRAIN_RECORD_LEVEL+" VARCHAR(20),"
            +TRAIN_RECORD_STANDARD_RATE+" VARCHAR(10),"
            +TRAIN_RECORD_FOREIGN_ID+" VARCHAR(20),"
            +TRAIN_RECORD_TIME_LONG+" VARCHAR(20),"
            +TRAIN_RECORD_GROUP_NUMBER+" VARCHAR(20),"
            +TRAIN_RECORD_SUGGESTION+" VARCHAR(20),"
            +TRAIN_RECORD_TRAIN_NAME+" VARCHAR(100),"
            +TRAIN_RECORD_TRAIN_PHONE+" VARCHAR(30),"
            +TRAIN_RECORD_TRAIN_USERNAME+" VARCHAR(100),"
            +TRAIN_RECORD_TRAIN_SEX+"  VARCHAR(10),"
            +TRAIN_RECORD_TRAIN_BIRTHDAY+" VARCHAR(20),"
            +TRAIN_RECORD_TRAIN_SER_NUMBER+" VARCHAR(20),"
            +TRAIN_RECORD_TRAIN_UPLOAD_FLAG+" VARCHAR(5)"
            +")";


    public static String MEDICAL_HIS_ID = "medical_his_id" ;
    public static String MEDICAL_HIS_NAME = "medical_his_name" ;
    public static String MEDICAL_HIS_TYPE = "medical_his_type" ;
    public static String TABLE_MEDICAL_HIS = "medical_his" ;

    public static String CREATE_MEDICAL_CASEBOOK =
            "create table if not exists "+TABLE_MEDICAL_HIS+" ( id integer primary key AUTOINCREMENT ," +
            MEDICAL_HIS_ID+" VARCHAR(10) ,"+
            MEDICAL_HIS_NAME+" VARCHAR(100) ,"+
            MEDICAL_HIS_TYPE+" VARCHAR(10)"+")" ;

    public static String TABLE_HEALTH_DATA = "health_data" ;
    public static String HEALTH_DATA_TIME = "data_time" ;
    public static String HEALTH_DATA_ID = "data_id";
    public static String HEALTH_DATA_USER_ID = "data_user_id" ;
    public static String HEALTH_DATA_MAX_RATE = "data_max_rate" ;
    public static String HEALTH_DATA_SECOND_VALUE = "data_second_value" ;
    public static String HEALTH_DATA_COMP_VALUE = "data_comp_value" ;

    public static String CREATE_HEALTH_DATA =
            "create table if not exists "+TABLE_HEALTH_DATA+"" +
                    "(id integer primary key autoincrement ," +
                    HEALTH_DATA_TIME+" VARCHAR(30)," +
                    HEALTH_DATA_USER_ID+" VARCHAR(30)," +
                    HEALTH_DATA_MAX_RATE+" VARCHAR(30)," +
                    HEALTH_DATA_SECOND_VALUE+" VARCHAR(30)," +
                    HEALTH_DATA_COMP_VALUE+" VARCHAR(30))" ;


    public static String TRAIN_PLAN_NAME = "name" ;
    public static String TRAIN_PLAN_PERSISTENT = "persistent" ;
    public static String TRAIN_PLANE_CONTROL = "control" ;
    public static String TRAIN_PLAN_STRENGTH = "strength" ;
    public static String TRAIN_PLAN_GROUP_NUMBER ="groupNumber" ;
    public static String TRAIN_PLAN_PERSISTENT_LEVEL="persistentLevel" ;
    public static String TRAIN_PLAN_CONTROL_LEVEL = "controlLevel" ;
    public static String TRAIN_PLAN_STRENGTH_LEVEL = "strengthLevel" ;
    public static String TRAIN_BREATH_TIME = "breathTime" ;
    public static String TRAIN_STOP_TIME = "stopTime" ;
    public static String TRAIN_CREATE_TIME = "createTime" ;
    public static String TABLE_TRAIN_PLAN = "trainPlan" ;


    public static String  CREATE_TRAIN_PLAN = "CREATE TABLE IF NOT EXITS "+TABLE_TRAIN_PLAN+" (" +
                "id integer primary key autoincrement , "+
            TRAIN_PLAN_NAME+" VARCHAR(50),"+
            TRAIN_PLAN_PERSISTENT+" VARCHAR(20),"+
            TRAIN_PLANE_CONTROL+" VARCHAR(20),"+
            TRAIN_PLAN_STRENGTH+" VARCHAR(20),"+
            TRAIN_PLAN_GROUP_NUMBER+" VARCHAR(20),"+
            TRAIN_PLAN_PERSISTENT_LEVEL+" VARCHAR(20) ,"+
            TRAIN_PLAN_CONTROL_LEVEL+" VARCHAR(20),"+
            TRAIN_PLAN_STRENGTH_LEVEL+" VARCHAR(20),"+
            TRAIN_BREATH_TIME+" VARCHAR(20),"+
            TRAIN_STOP_TIME+" VARCHAR(20),"+
            TRAIN_CREATE_TIME+" VARCHAR(20)"+
            ")"  ;

}
