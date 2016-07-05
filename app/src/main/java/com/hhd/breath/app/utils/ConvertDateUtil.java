package com.hhd.breath.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by familylove on 2016/5/18.
 */
public class ConvertDateUtil {

    public static String timestampToTime(String timestamp){

        long time = Long.parseLong(timestamp)*1000 ;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss") ;
        return sdf.format(new Date(time)) ;
    }
}
