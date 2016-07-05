package com.hhd.breath.app.andengine;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by familylove on 2016/5/24.
 */
public class ScreenSizeHelper {

    public static float calculateScreenWidth(Activity context,float windowHeight){

        DisplayMetrics dm = new DisplayMetrics() ;
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int realHeight = dm.heightPixels ;
        final int realWidth = dm.widthPixels ;
        float ratio = (float) realWidth/realHeight ;
        return windowHeight*ratio ;
    }

    public static float getScreenHeight(Activity context){

        DisplayMetrics dm = new DisplayMetrics() ;
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);


        return (float) dm.heightPixels ;
    }
}
