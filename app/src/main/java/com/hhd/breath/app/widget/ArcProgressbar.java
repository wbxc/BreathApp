package com.hhd.breath.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.hhd.breath.app.R;

public class ArcProgressbar extends View {

    private int bgStrokeWidth = 15;
    private int barStrokeWidth = 15;
    private int first_color;
    private int second_color;
    private int third_color;

    private int progress = 0;
    private int angleOfMoveCircle = 140;// 移动小园的起始角度。
    private int startAngle = 140;
    private int endAngle = 260;
    private Paint mPaintBar = null;
    private Paint mPaintSmallBg = null;
    private Paint mPaintBg = null;
    private Paint mPaintCircle = null;
    private RectF rectBg = null;
    /**
     * 直徑。
     */
    private int diameter = 280;
    private boolean showSmallBg = true;// 是否显示小背景。
    private boolean showMoveCircle = false;// 是否显示移动的小园。
    private float textSizeLarge ;
    private float textSizeSmall ;

    private String accountTime  = "";


    public ArcProgressbar(Context context) {
        super(context);
    }
 
    public ArcProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcProgressbar) ;
        first_color = mTypedArray.getColor(R.styleable.ArcProgressbar_first_color,context.getResources().getColor(R.color.common_cir_bg_color)) ;
        second_color = mTypedArray.getColor(R.styleable.ArcProgressbar_second_color,Color.RED) ;
        third_color = mTypedArray.getColor(R.styleable.ArcProgressbar_third_color,context.getResources().getColor(R.color.common_cir_bg_color)) ;

        textSizeLarge = mTypedArray.getDimension(R.styleable.ArcProgressbar_textSizeLarge, 300f);
        textSizeSmall = mTypedArray.getDimension(R.styleable.ArcProgressbar_textSizeSmall, 30f);
        diameter = (int)mTypedArray.getDimension(R.styleable.ArcProgressbar_round_cir,280) ;
        mTypedArray.recycle();
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init(canvas);
    }
 
    private void init(Canvas canvas) {
        // 画弧形的矩阵区域。
        rectBg = new RectF(getMeasuredWidth()/2-diameter/2, getMeasuredHeight()/2-diameter/2,
                getMeasuredWidth()/2+diameter/2, getMeasuredHeight()/2+diameter/2);
 
        // 计算弧形的圆心和半径。
        int cx1 = getMeasuredWidth() / 2;
        int cy1 = getMeasuredHeight() / 2;
        int arcRadius = diameter / 2;





        // ProgressBar结尾和开始画2个圆，实现ProgressBar的圆角。
        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setColor(first_color);



        canvas.drawCircle(
                (float) (cx1 + arcRadius * Math.cos(startAngle * 3.14 / 180)),
                (float) (cy1 + arcRadius * Math.sin(startAngle * 3.14 / 180)),
                bgStrokeWidth / 2, mPaintCircle);// 小圆
 
        canvas.drawCircle(
                (float) (cx1 + arcRadius
                        * Math.cos((180 - startAngle) * 3.14 / 180)),
                (float) (cy1 + arcRadius
                        * Math.sin((180 - startAngle) * 3.14 / 180)),
                bgStrokeWidth / 2, mPaintCircle);// 小圆
 
        // 弧形背景。
        mPaintBg = new Paint();
        mPaintBg.setAntiAlias(true);
        mPaintBg.setStyle(Paint.Style.STROKE);
        mPaintBg.setStrokeWidth(bgStrokeWidth);
        mPaintBg.setColor(first_color);
        canvas.drawArc(rectBg, startAngle, endAngle, false, mPaintBg);
 
        // 弧形小背景。
        if (showSmallBg) {
            mPaintSmallBg = new Paint();
            mPaintSmallBg.setAntiAlias(true);
            mPaintSmallBg.setStyle(Paint.Style.STROKE);
            mPaintSmallBg.setStrokeWidth(barStrokeWidth);
            mPaintSmallBg.setColor(third_color);
            canvas.drawArc(rectBg, startAngle, endAngle, false, mPaintSmallBg);
        }

        if (progress!=0){

            // 弧形ProgressBar。
            mPaintBar = new Paint();
            mPaintBar.setAntiAlias(true);
            mPaintBar.setStyle(Paint.Style.STROKE);
            mPaintBar.setStrokeWidth(barStrokeWidth);
            mPaintBar.setColor(second_color);
            canvas.drawArc(rectBg, startAngle, progress, false, mPaintBar);


            // ProgressBar结尾和开始画2个圆，实现ProgressBar的圆角。
            Paint mPaintCircle1 = new Paint();
            mPaintCircle1.setAntiAlias(true);
            mPaintCircle1.setColor(second_color);


            canvas.drawCircle(
                    (float) (cx1 + arcRadius * Math.cos(startAngle * 3.14 / 180)),
                    (float) (cy1 + arcRadius * Math.sin(startAngle * 3.14 / 180)),
                    bgStrokeWidth / 2, mPaintCircle1);// 小圆


            // 随ProgressBar移动的圆。
            if (showMoveCircle) {
                mPaintCircle.setColor(second_color);
                canvas.drawCircle(
                        (float) (cx1 + arcRadius
                                * Math.cos(angleOfMoveCircle * 3.14 / 180)),
                        (float) (cy1 + arcRadius
                                * Math.sin(angleOfMoveCircle * 3.14 / 180)),
                        bgStrokeWidth / 2, mPaintCircle);// 小圆
            }
        }


        Paint textPain = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        textPain.setTextSize(180f);
        textPain.setColor(Color.WHITE);
        Rect mRect = new Rect() ;

        textPain.getTextBounds(accountTime, 0, accountTime.length(), mRect);
        canvas.drawText(accountTime, cx1 - mRect.centerX(), cy1 - mRect.centerY(), textPain);


        Paint text1Pain = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        text1Pain.setTextSize(50f);
        text1Pain.setColor(Color.WHITE);
        Rect m1Rect = new Rect() ;
        String ss1 = "s" ;
        text1Pain.getTextBounds(ss1, 0, ss1.length(), m1Rect);
        canvas.drawText(ss1, cx1 + 51 + m1Rect.centerX(), cy1 - (-64) - m1Rect.centerY(), text1Pain);
    }



    public void setShowAccount(String accountTime){

        this.accountTime = accountTime ;

        invalidate();
    }
    /**
     * 
     * @param _progress
     */
    public void addProgress(int _progress) {
        progress = _progress;

        angleOfMoveCircle =startAngle+ _progress;
        if (progress > endAngle) {
            progress = 0;
            angleOfMoveCircle = startAngle;
        }
        invalidate();
    }
 
    /**
     * 设置弧形背景的画笔宽度。
     */
    public void setBgStrokeWidth(int bgStrokeWidth) {
        this.bgStrokeWidth = bgStrokeWidth;
    }
 
    /**
     * 设置弧形ProgressBar的画笔宽度。
     */
    public void setBarStrokeWidth(int barStrokeWidth) {
        this.barStrokeWidth = barStrokeWidth;
    }
 
    /**
     * 设置弧形背景的颜色。
     */
    public void setBgColor(int bgColor) {
        this.first_color = bgColor;
    }
 
    /**
     * 设置弧形ProgressBar的颜色。
     */
    public void setBarColor(int barColor) {
        this.second_color = barColor;
    }
 
    /**
     * 设置弧形小背景的颜色。
     */
    public void setSmallBgColor(int smallBgColor) {
        this.third_color = smallBgColor;
    }
 
    /**
     * 设置弧形的直径。
     */
    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }
 
    /**
     * 是否显示小背景。
     */
    public void setShowSmallBg(boolean showSmallBg) {
        this.showSmallBg = showSmallBg;
    }
 
    /**
     * 是否显示移动的小圆。
     */
    public void setShowMoveCircle(boolean showMoveCircle) {
        this.showMoveCircle = showMoveCircle;
    }
 

 
}