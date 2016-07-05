package com.hhd.breath.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/1/8.
 */
public class LocalSetImgView extends ImageView {

    public LocalSetImgView(Context context) {
        super(context);
    }

    public LocalSetImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LocalSetImgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    public void setImgLocal(int xCenter,int yCenter,int imgWidth,int imgHeight){

        this.setFrame(xCenter-imgWidth/2,yCenter-imgHeight/2,xCenter+imgWidth/2,yCenter+imgHeight/2) ;
    }
}
