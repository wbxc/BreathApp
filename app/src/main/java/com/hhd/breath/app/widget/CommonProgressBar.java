package com.hhd.breath.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.hhd.breath.app.R;

/**
 * Created by Administrator on 2016/1/7.
 */
public class CommonProgressBar extends RelativeLayout {

    private  RoundNoProgressBar mRoundNoProgressBar ;
    private LocalSetImgView mImageView ;
    private int width ;
    private int height ;
    private int xPointer = 0 ;
    private int yPointer = 0 ;
    private int max = 100 ;

    public CommonProgressBar(Context context) {
        super(context);
    }

    public CommonProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        View mView = LayoutInflater.from(context).inflate(R.layout.layout_round_bar, null) ;
        mRoundNoProgressBar = (RoundNoProgressBar)mView.findViewById(R.id.roundProgressBar2) ;
        mImageView = (LocalSetImgView)mView.findViewById(R.id.img_common_dian) ;
        addView(mView);

    }

    public CommonProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View mView = LayoutInflater.from(context).inflate(R.layout.layout_round_bar, null) ;
        mRoundNoProgressBar = (RoundNoProgressBar)mView.findViewById(R.id.roundProgressBar2) ;
        mImageView = (LocalSetImgView)mView.findViewById(R.id.img_common_dian) ;
        addView(mView);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec) ;
        xPointer = width /2 ;
        yPointer = height /2 ;
    }


    public void setProgress(int startAngle , int progress){

        int width1 = (int) (xPointer + (xPointer-23) * Math.cos(( startAngle+360 * progress / 100) * 3.14 / 180)) ;
        int height1 = (int) (yPointer + (xPointer-23) * Math.sin(( startAngle+360 * progress / 100) * 3.14 / 180)) ;

        mImageView.setImgLocal(width1, height1, 49, 48);
       // mImageView.setLayoutParams(layoutParams);

        mRoundNoProgressBar.setProgress(progress);
        this.postInvalidate();
    }

    public void setProgress(int progress){

        if(progress < 0){
            throw new IllegalArgumentException("progress not less than 0");
        }
        if(progress > max){
            progress = max;
        }
        if(progress <= max){
            postInvalidate();
            int width1 = (int) (xPointer + (xPointer-23) * Math.cos(( -90+360 * progress / 100) * 3.14 / 180)) ;
            int height1 = (int) (yPointer + (xPointer-23) * Math.sin(( -90+360 * progress / 100) * 3.14 / 180)) ;

            mImageView.setImgLocal(width1, height1, 49, 48);
            // mImageView.setLayoutParams(layoutParams);
            mRoundNoProgressBar.setProgress(progress);
            this.postInvalidate();
        }
    }



    public void setTimeShow(int time){
        mRoundNoProgressBar.setTimeShow(time);
    }

}
