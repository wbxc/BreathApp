package com.hhd.breath.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;

import com.hhd.breath.app.R;

/**
 * Created by familylove on 2015/12/10.
 */
public class TextProgressBar extends ProgressBar {

    private Paint mLeftPain;
    private Paint mRightPain ;

    private float leftTextsize = 20f  ;
    private float rightTextSize = 20f ;
    private int leftTextColor = Color.WHITE ;
    private int rightTextColor = Color.WHITE ;

    private String leftText ="训练总进度" ;
    private String rightText = "01:30"  ;

    private int leftAlin ;
    private int rightAlin ;

    private Paint mainBgPaint ;
    private Paint mainSelectPain ;

    private  int mainBgColor ;
    private  int mainSelectColor ;




    public TextProgressBar(Context context) {
        super(context);
        initText();
    }

    public TextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.TextProgressBar) ;
        leftTextColor = mTypedArray.getColor(R.styleable.TextProgressBar_leftTextColor,Color.WHITE) ;
        leftTextsize = mTypedArray.getDimension(R.styleable.TextProgressBar_leftTextSize, 20f) ;

        rightTextColor = mTypedArray.getColor(R.styleable.TextProgressBar_rightTextColor, Color.WHITE) ;
        rightTextSize = mTypedArray.getDimension(R.styleable.TextProgressBar_rightTextSize, 20f) ;

        leftAlin = (int)mTypedArray.getDimension(R.styleable.TextProgressBar_leftTextAlin,20f) ;
        rightAlin = (int)mTypedArray.getDimension(R.styleable.TextProgressBar_rightTextAlin,20f) ;
        mainBgColor = (int)mTypedArray.getColor(R.styleable.TextProgressBar_main_BgColor, Color.WHITE) ;
        mainSelectColor = (int)mTypedArray.getColor(R.styleable.TextProgressBar_mainSelectColor, Color.WHITE) ;

        initText();
        mTypedArray.recycle();
    }

    public TextProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.TextProgressBar) ;
        leftTextColor = mTypedArray.getColor(R.styleable.TextProgressBar_leftTextColor,Color.WHITE) ;
        leftTextsize = mTypedArray.getDimension(R.styleable.TextProgressBar_leftTextSize, 20f) ;

        rightTextColor = mTypedArray.getColor(R.styleable.TextProgressBar_rightTextColor, Color.WHITE) ;
        rightTextSize = mTypedArray.getDimension(R.styleable.TextProgressBar_rightTextSize, 20f) ;

        leftAlin = (int)mTypedArray.getDimension(R.styleable.TextProgressBar_leftTextAlin,20f) ;
        rightAlin = (int)mTypedArray.getDimension(R.styleable.TextProgressBar_rightTextAlin,20f) ;

        mainBgColor = (int)mTypedArray.getColor(R.styleable.TextProgressBar_main_BgColor, Color.WHITE) ;
        mainSelectColor = (int)mTypedArray.getColor(R.styleable.TextProgressBar_mainSelectColor, Color.WHITE) ;

        initText();
        mTypedArray.recycle();


    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        int height = getMeasuredHeight() ;
        int width = getMeasuredWidth() ;






        if (!"".equals(leftText) && null !=leftText){
            Rect mRect = new Rect() ;
            mLeftPain.getTextBounds(leftText, 0, leftText.length(), mRect);
            Log.e("getTextBounds", mRect.centerY() + "getTextBounds");
            canvas.drawText(leftText, leftAlin, height / 2 - mRect.centerY(), mLeftPain);
        }


        if (!"".equals(rightText) && null !=rightText){

            Rect mRectRight = new Rect() ;
            mRightPain.getTextBounds(rightText, 0, rightText.length(), mRectRight);
            Log.e("getTextBounds", mRectRight.centerY() + "getTextBounds");
            canvas.drawText(rightText, width - rightAlin - (2 * mRectRight.centerX()), height / 2 - mRectRight.centerY(), mRightPain);
        }


    }

    public void setShowText(String leftText , String rightText){
        this.leftText = leftText ;
        this.rightText = rightText ;
        invalidate();
    }

    private void initText(){
        mLeftPain = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        mLeftPain.setColor(leftTextColor);
        mLeftPain.setTextSize(leftTextsize);
        mLeftPain.setStyle(Paint.Style.FILL);



        mRightPain = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        mRightPain.setTextSize(rightTextSize);
        mRightPain.setColor(rightTextColor);

        mainBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        mainBgPaint.setColor(mainBgColor);

        mainSelectPain = new Paint(Paint.ANTI_ALIAS_FLAG)  ;
        mainSelectPain.setColor(mainSelectColor);

    }
}
