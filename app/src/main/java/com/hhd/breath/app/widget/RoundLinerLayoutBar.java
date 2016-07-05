package com.hhd.breath.app.widget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hhd.breath.app.R;

/**
 * Created by Administrator on 2016/1/7.
 */
public class RoundLinerLayoutBar extends LinearLayout {


    public RoundLinerLayoutBar(Context context) {
        super(context);
    }

    public RoundLinerLayoutBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundLinerLayoutBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ImageView iv = new ImageView(context);
        iv.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.mipmap.icon_common_dian));

        addView(iv);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



    }

}
