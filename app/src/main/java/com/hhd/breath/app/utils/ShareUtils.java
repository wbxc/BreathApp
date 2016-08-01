package com.hhd.breath.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by familylove on 2015/11/23.
 */
public class ShareUtils {

    private static  String  BASE_NAME = "breath_time_value" ;
    private static  String INSPIRATION_TIME = "inspiration_time" ;
    private static  String BREATH_TIME = "breath_time" ;
    private static  String  BASE_USER_INFO = "base_user_info" ;

    private static String IS_FIRST_LAUNCH = "is_launch_first" ;






    public static void setIsLaunchFirst(Context mContext ,boolean value){
        SharedPreferences  spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putBoolean(IS_FIRST_LAUNCH,value) ;
        mEditor.commit() ;
    }

    public static  boolean getLaunchFirst(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        return spf.getBoolean(IS_FIRST_LAUNCH,false) ;
    }




    public static void setInspirationTime(Context mContext ,int value){
        SharedPreferences  spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putInt(INSPIRATION_TIME,value) ;
        mEditor.commit() ;
    }
    public static  int getInspirationTime(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        return spf.getInt(INSPIRATION_TIME,2) ;
    }


    public static void setBrathTime(Context mContext ,int value){
        SharedPreferences  spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putInt(BREATH_TIME, value) ;
        mEditor.commit() ;
    }
    public static  int getBrathTime(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        return spf.getInt(BREATH_TIME,3) ;
    }


    //  间隔时间
    private static  String  INTERVALTIME = "interval_time" ;

    public static void setIntervalTime(Context mContext ,int value){
        SharedPreferences  spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putInt(INTERVALTIME,value) ;
        mEditor.commit() ;
    }
    public static  int getIntervalTime(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        return spf.getInt(INTERVALTIME,3) ;
    }

    private static String CYCLE = "cycle" ;

    public static void setCycle(Context mContext ,int value){
        SharedPreferences  spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putInt(CYCLE,value) ;
        mEditor.commit() ;
    }
    public static  int getCycle(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        return spf.getInt(CYCLE,-1) ;
    }

    private static String CYCLE_NAME = "cycle_name" ;

    public static void setCycleName(Context mContext ,String value){
        SharedPreferences  spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putString(CYCLE_NAME,value) ;
        mEditor.commit() ;
    }
    public static  String getCycleName(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        return spf.getString(CYCLE_NAME,"只响一次") ;
    }


    private static String RING = "ring" ;

    public static void setRing(Context mContext ,int value){
        SharedPreferences  spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putInt(RING,value) ;
        mEditor.commit() ;
    }
    public static  int getRing(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        return spf.getInt(RING,0) ;
    }

    private static String TIME_MAP_1 = "time_map_1" ;
    private static String TIME_MAP_2 = "time_map_2" ;



    public static void setTime1(Context mContext ,int value){

        SharedPreferences  spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putInt(TIME_MAP_1,value) ;
        mEditor.commit() ;

    }

    public static int getTime1(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        return spf.getInt(TIME_MAP_1,0) ;
    }

    public static void setTime2(Context mContext ,int value){

        SharedPreferences  spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putInt(TIME_MAP_2,value) ;
        mEditor.commit() ;

    }

    public static int getTime2(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        return spf.getInt(TIME_MAP_2,0) ;
    }





    //  间隔时间
    private static  String  MUSIC_SWITCH = "music_switch" ;

    public static void setMusicSwitch(Context mContext ,int value){
        SharedPreferences  spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putInt(MUSIC_SWITCH,value) ;
        mEditor.commit() ;
    }
    public static  int getMusicSwitch(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        return spf.getInt(MUSIC_SWITCH,1) ;
    }





    private static String ACTION_GROUP = "action_group" ;

    public static void setActionGroup(Context mContext ,int value){
        SharedPreferences  spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putInt(ACTION_GROUP,value) ;
        mEditor.commit() ;
    }
    public static  int getActionGroup(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_NAME,Context.MODE_PRIVATE) ;
        return spf.getInt(ACTION_GROUP,10) ;
    }

    private static String USER_ID = "breath_user_id" ;

    public static  void setUserId(Context mContext,String userId){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_USER_INFO,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putString(USER_ID, userId) ;
        mEditor.commit() ;
    }

    public static String getUserId(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_USER_INFO,Context.MODE_PRIVATE) ;
        return spf.getString(USER_ID, "") ;
    }

    private static String USER_STATUS = "breath_user_status" ;

    public static  void setUserStatus(Context mContext,String userStatus){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_USER_INFO,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putString(USER_STATUS, userStatus) ;
        mEditor.commit() ;
    }

    public static String getUserStatus(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_USER_INFO,Context.MODE_PRIVATE) ;
        return spf.getString(USER_STATUS, "") ;
    }




    private static String USER_NAME = "breath_username" ;

    public static  void setUserName(Context mContext,String userName){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_USER_INFO,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putString(USER_NAME, userName) ;
        mEditor.commit() ;
    }
    public static String getUserName(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_USER_INFO,Context.MODE_PRIVATE) ;

        return spf.getString(USER_NAME, "") ;
    }

    private static String USER_BIRTHDAY = "breath_user_birthday" ;

    public static  void setUserBirthday(Context mContext,String userName){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_USER_INFO,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putString(USER_BIRTHDAY,userName) ;
        mEditor.commit() ;
    }
    public static String getUserBirthday(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_USER_INFO,Context.MODE_PRIVATE) ;
        return spf.getString(USER_BIRTHDAY,"") ;
    }

    //user_image

    private static String USER_IMAGE = "breath_user_image" ;

    public static  void setUserImage(Context mContext,String userName){
        SharedPreferences spf = mContext.getSharedPreferences(USER_IMAGE,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putString(USER_BIRTHDAY,userName) ;
        mEditor.commit() ;
    }
    public static String getUserImage(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(USER_IMAGE,Context.MODE_PRIVATE) ;
        return spf.getString(USER_BIRTHDAY,"") ;
    }



    private static String USER_SEX = "breath_user_sex" ;

    public static  void setUserSex(Context mContext,String userName){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_USER_INFO,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putString(USER_SEX,userName) ;
        mEditor.commit() ;
    }
    public static String getUserSex(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_USER_INFO,Context.MODE_PRIVATE) ;
        return spf.getString(USER_SEX,"") ;
    }

    private static String APP_LAUNCH = "app_launch" ;

    public static  void setFirstLaunch(Context mContext,boolean applaunch){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_USER_INFO,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putBoolean(APP_LAUNCH, applaunch) ;
        mEditor.commit() ;
    }


    public static boolean getFirstLaunch(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_USER_INFO,Context.MODE_PRIVATE) ;
        return spf.getBoolean(APP_LAUNCH, false) ;
    }

    private static String USER_PHONE = "user_phone" ;

    public static  void setUserPhone(Context mContext,String applaunch){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_USER_INFO,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putString(USER_PHONE, applaunch) ;
        mEditor.commit() ;
    }


    public static String getUserPhone(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_USER_INFO,Context.MODE_PRIVATE) ;
        return spf.getString(USER_PHONE, "") ;
    }

    private static String USER_PASSWORD = "user_password" ;

    public static  void setUserPassword(Context mContext,String applaunch){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_USER_INFO,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putString(USER_PASSWORD, applaunch) ;
        mEditor.commit() ;
    }


    public static String getUserPassword(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_USER_INFO,Context.MODE_PRIVATE) ;
        return spf.getString(USER_PASSWORD, "") ;
    }

    public static void clearUserInfo(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(BASE_USER_INFO,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor editor = spf.edit() ;
        editor.clear() ;
        editor.putString(USER_NAME, "") ;
        editor.putString(USER_BIRTHDAY,"") ;
        editor.putString(USER_PASSWORD,"") ;
        editor.putString(USER_PHONE,"") ;
        editor.putString(USER_SEX,"") ;
        editor.putString(USER_ID,"") ;
        editor.commit() ;
    }


    // 序列号

    protected  final static String  base_serial = "base_serial" ;
    public static void setSerialNumber(Context mContext,String serial){

        SharedPreferences spf = mContext.getSharedPreferences(base_serial,Context.MODE_PRIVATE) ;
        SharedPreferences.Editor mEditor = spf.edit() ;
        mEditor.putString(base_serial,serial) ;
        mEditor.commit() ;
    }
    public static String getSerialNumber(Context mContext){
        SharedPreferences spf = mContext.getSharedPreferences(base_serial,Context.MODE_PRIVATE) ;
        return  spf.getString(base_serial,"") ;
    }
}
