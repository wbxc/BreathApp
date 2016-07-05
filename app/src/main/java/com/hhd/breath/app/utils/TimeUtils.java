package com.hhd.breath.app.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/12/18.
 */
public class TimeUtils {

    private static  TimeUtils instance = null;
    private int[]  timeInt ;
    private DateFormat mDateFormat ;
    public  static  TimeUtils getInstance(){

        if (instance == null){

            synchronized (TimeUtils.class){
                if (instance == null){
                    instance = new TimeUtils() ;
                }
                instance.timeInt = new int[6] ;
                instance.mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss") ;
            }
            return instance ;
        }
        return instance ;
    }

    //2015.12.18

    public String getDay(){

        StringBuffer sb = new StringBuffer() ;
        Date mDate = new Date() ;
        String startTime = mDateFormat.format(mDate) ;
        timeInt[0]=Integer.valueOf(startTime.substring(0, 4));
        timeInt[1]=Integer.valueOf(startTime.substring(4, 6));
        timeInt[2]=Integer.valueOf(startTime.substring(6, 8));
        sb.append(String.valueOf(timeInt[0])).append(".");

        if (timeInt[1]<10){
            sb.append("0"+String.valueOf(timeInt[1])).append(".");
        }else{
            sb.append(String.valueOf(timeInt[1])).append(".");
        }
        if (timeInt[2]<10){
            sb.append("0"+String.valueOf(timeInt[2]));
        }else {
            sb.append(String.valueOf(timeInt[2]));
        }


        return sb.toString() ;
    }

    public String getForeignDay(){
        StringBuffer sb = new StringBuffer() ;
        Date mDate = new Date() ;
        String startTime = mDateFormat.format(mDate) ;
        timeInt[0]=Integer.valueOf(startTime.substring(0, 4));
        timeInt[1]=Integer.valueOf(startTime.substring(4, 6));
        timeInt[2]=Integer.valueOf(startTime.substring(6, 8));

        sb.append(String.valueOf(timeInt[0]));
        if (timeInt[1]<10){
            sb.append("0" + String.valueOf(timeInt[1]));
        }else {

            sb.append(String.valueOf(timeInt[1]));
        }
        if (timeInt[2]<10){

            sb.append("0"+String.valueOf(timeInt[2]));
        }else {
            sb.append(String.valueOf(timeInt[2]));
        }

        return sb.toString() ;
    }





    public  String getMinute(){

        StringBuffer sb = new StringBuffer() ;
        Date mDate = new Date() ;
        String startTime = mDateFormat.format(mDate) ;
        timeInt[0]=Integer.valueOf(startTime.substring(0, 4));
        timeInt[1]=Integer.valueOf(startTime.substring(4, 6));
        timeInt[2]=Integer.valueOf(startTime.substring(6, 8));
        timeInt[3] = Integer.valueOf(startTime.substring(8,10)) ;
        timeInt[4] = Integer.valueOf(startTime.substring(10,12)) ;

        sb.append(String.valueOf(timeInt[0])).append(".");

        if (timeInt[1]<10){

            sb.append("0"+String.valueOf(timeInt[1])).append(".");
        }else{
            sb.append(String.valueOf(timeInt[1])).append(".");
        }

        if (timeInt[2]<10) {

            sb.append("0"+String.valueOf(timeInt[2])).append("  ");
        }else{
            sb.append(String.valueOf(timeInt[2])).append("  ");
        }
        if (timeInt[3]<10) {
            sb.append("0"+String.valueOf(timeInt[3])).append(":");
        }else{
            sb.append(String.valueOf(timeInt[3])).append(":");
        }

        if (timeInt[4]<10) {
            sb.append("0"+String.valueOf(timeInt[4]));
        }else{
            sb.append(String.valueOf(timeInt[4]));
        }

        return sb.toString() ;

    }

    public  String getSecond(){

        StringBuffer sb = new StringBuffer() ;
        Date mDate = new Date() ;
        String startTime = mDateFormat.format(mDate) ;
        timeInt[0]=Integer.valueOf(startTime.substring(0, 4));
        timeInt[1]=Integer.valueOf(startTime.substring(4, 6));
        timeInt[2]=Integer.valueOf(startTime.substring(6, 8));
        timeInt[3] = Integer.valueOf(startTime.substring(8,10)) ;
        timeInt[4] = Integer.valueOf(startTime.substring(10,12)) ;
        timeInt[5] = Integer.valueOf(startTime.substring(12,14)) ;

        sb.append(String.valueOf(timeInt[0])).append(".");

        if (timeInt[1]<10){

            sb.append("0"+String.valueOf(timeInt[1])).append(".");
        }else{
            sb.append(String.valueOf(timeInt[1])).append(".");
        }

        if (timeInt[2]<10) {

            sb.append("0"+String.valueOf(timeInt[2])).append("  ");
        }else{
            sb.append(String.valueOf(timeInt[2])).append("  ");
        }
        if (timeInt[3]<10) {
            sb.append("0"+String.valueOf(timeInt[3])).append(":");
        }else{
            sb.append(String.valueOf(timeInt[3])).append(":");
        }

        if (timeInt[4]<10) {
            sb.append("0" + String.valueOf(timeInt[4])).append(":");
        }else{
            sb.append(String.valueOf(timeInt[4])).append(":");
        }

        if (timeInt[5]<10) {
            sb.append("0"+String.valueOf(timeInt[5]));
        }else{
            sb.append(String.valueOf(timeInt[5]));
        }

        return sb.toString() ;

    }

}
