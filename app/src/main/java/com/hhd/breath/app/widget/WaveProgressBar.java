package com.hhd.breath.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.hhd.breath.app.utils.Utils;

/**
 * Created by Administrator on 2016/1/12.
 */
public class WaveProgressBar extends ProgressBar {
    public WaveProgressBar(Context context) {
        super(context);
    }

    public WaveProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WaveProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void  addProgress(int progress){


        Utils.write2(progress + "\n");
        if (progress>50){
            progress = 50 ;
        }
        if (progress<0){
            progress = 0 ;
        }
        this.setProgress(progress*2);

    }

}
