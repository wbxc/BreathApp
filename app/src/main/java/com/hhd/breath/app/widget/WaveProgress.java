package com.hhd.breath.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.R;

/**
 * Created by familylove on 2015/12/3.
 */
public class WaveProgress extends View {

    private Paint mPaint ;
    private Paint mFlagPaint ;
    private Paint mShowPaint ;
    private float height ;
    private float width ;
    private int default_color ;
    private int flag_color ;
    private int show_color ;
    private int elementValue ;
    private int maxHeight ;
    private RectF  firstRectf ;
    private RectF  secondRectf ;
    private RectF  thirdRectf ;

    private int currentValue = 0 ;


    public WaveProgress(Context context) {
        super(context);
    }


    public WaveProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint() ;


        mFlagPaint = new Paint() ;
        mShowPaint = new Paint() ;
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveProgress) ;
        int textColor = mTypedArray.getColor(R.styleable.WaveProgress_textColor, 0xff00ff00) ;
        float textSize = mTypedArray.getDimension(R.styleable.WaveProgress_textSize,36) ;
        height = mTypedArray.getDimension(R.styleable.WaveProgress_layout_hight,100) ;
        width = mTypedArray.getDimension(R.styleable.WaveProgress_layout_width, 30) ;
        default_color = mTypedArray.getColor(R.styleable.WaveProgress_default_bg_color, 0Xffd5d5d5) ;
        flag_color = mTypedArray.getColor(R.styleable.WaveProgress_flag_bg_color, 0xff000000) ;
        show_color = mTypedArray.getColor(R.styleable.WaveProgress_show_bg_color,0xff2ea7e0) ;




        //first
        mShowPaint.setColor(default_color);

        //second
        mPaint.setColor(show_color);

        //third
        mFlagPaint.setColor(flag_color);

        mTypedArray.recycle();
    }

    public WaveProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setStyle(Paint.Style.FILL);
        int width1 = getMeasuredWidth() ;
        maxHeight = getMeasuredHeight()-80 ;
        elementValue = maxHeight / CommonValues.VALUE_MAX_PROGRESS ;


        firstRectf =  new RectF(width1/2-10, 40, width1/2+10, getMeasuredHeight()-40) ;
        canvas.drawRoundRect(firstRectf, 0, 0, mPaint);


        secondRectf = new RectF(width1 / 2 - 10, 40, width1 / 2 + 10, 40+currentValue*elementValue) ;
        canvas.drawRoundRect(secondRectf,0,0,mShowPaint);




        thirdRectf = new RectF(width1/2-10,40+CommonValues.VALUE_STANDARD*elementValue-5,
                    width1/2+10,40+CommonValues.VALUE_STANDARD*elementValue+5) ;
        canvas.drawRoundRect(thirdRectf, 5, 20, mFlagPaint) ;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec) ;
        int height = MeasureSpec.getSize(heightMeasureSpec) ;
        setMeasuredDimension(width, height);
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    public void refreshProgress(int value){

        if(value>CommonValues.VALUE_MAX_PROGRESS){
            this.currentValue = 0 ;
        }else{
            this.currentValue = CommonValues.VALUE_MAX_PROGRESS-value ;
        }
        invalidate();
    }
}
