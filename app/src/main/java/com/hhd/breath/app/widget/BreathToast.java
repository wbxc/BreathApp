package com.hhd.breath.app.widget;

import android.content.ContentValues;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.hhd.breath.app.R;

/**
 * Created by Administrator on 2016/6/17.
 */
public class BreathToast extends Toast {

    private static  BreathToast  instance = null ;

    public BreathToast(Context context) {
        super(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.layout_toast,null) ;
        this.setView(mView);
    }


    public static BreathToast getInstance(Context context){
        if (instance==null){
            instance = new BreathToast(context) ;
        }
        return instance ;
    }

    public void showToast(){

        setDuration(Toast.LENGTH_SHORT);
        setGravity(Gravity.CENTER,0,0);
        show();
    }

    public void hideToast(){

        this.cancel();
    }
}
