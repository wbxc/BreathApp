package com.hhd.breath.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.hhd.breath.app.R;

/**
 * 仿iphone带进度的进度条，线程安全的View，可直接在线程中更新进度
 * @author xiaanming
 *
 */
public class RoundProgressBar extends View {
	/**
	 * 画笔对象的引用
	 */
	private Paint paint;
	
	/**
	 * 圆环的颜色
	 */
	private int roundColor;
	
	/**
	 * 圆环进度的颜色
	 */
	private int roundProgressColor;
	
	/**
	 * 中间进度百分比的字符串的颜色
	 */
	private int textColor;
	
	/**
	 * 中间进度百分比的字符串的字体
	 */
	private float textSize;
	
	/**
	 * 圆环的宽度
	 */
	private float roundWidth;
	
	/**
	 * 最大进度
	 */
	private int max;
	
	/**
	 * 当前进度
	 */
	private int progress;
	/**
	 * 是否显示中间的进度
	 */
	private boolean textIsDisplayable;
	
	/**
	 * 进度的风格，实心或者空心
	 */
	private int style;
	public static final int STROKE = 0;
	public static final int FILL = 1;
	private Paint mTextViewPaint ;
	private int timeShow = 0 ;
	private float rd = 0 ;
	private Paint circlePaint ;
	private int startAngle = -90 ;
	private static int COLOR1 = 0xFFFFFFFF ;
	private static int COLOR2 = 0xEEFFFFFF ;
	private static int COLOR3 = 0xDDFFFFFF ;
	private static int COLOR4 = 0xCCFFFFFF ;
	private ImageView mImagView ;


/*	RotateAnimation ra = new RotateAnimation(start, -degree,
			Animation.ABSOLUTE, 395f, Animation.ABSOLUTE, 235f);
	这样就可以了啊。395是横坐标，235是纵坐标
	*/

	public RoundProgressBar(Context context) {
		this(context, null);
	}

	public RoundProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mImagView = new ImageView(context) ;

		mImagView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_common_dian));
		paint = new Paint();
		mTextViewPaint = new Paint() ;
		mTextViewPaint.setAntiAlias(true);
		circlePaint = new Paint() ;
		circlePaint.setAntiAlias(true);
		circlePaint.setColor(Color.WHITE);
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.RoundProgressBar);
		//获取自定义属性和默认值
		roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
		roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
		textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColorR, Color.GREEN);
		textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSizeR, 300f);
		roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
		max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
		textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
		style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);
		mTypedArray.recycle();
	}
	

	private  int centre ;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		/**
		 * 画最外层的大圆环
		 */
		centre = getWidth()/2; //获取圆心的x坐标
		int radius = (int) (centre-20 - roundWidth/2); //圆环的半径
		rd = radius ;


		paint.setColor(roundColor); //设置圆环的颜色
		paint.setStyle(Paint.Style.STROKE); //设置空心
		paint.setTypeface(Typeface.SANS_SERIF) ;
		paint.setStrokeWidth(roundWidth); //设置圆环的宽度
		paint.setAntiAlias(true);  //消除锯齿 
		canvas.drawCircle(centre, centre, radius, paint); //画出圆环
		

		/**
		 * 画进度百分比
		 */
		paint.setStrokeWidth(0); 
		paint.setColor(textColor);
		paint.setTextSize(textSize);

		mTextViewPaint.setTextSize(60f);
		mTextViewPaint.setColor(textColor);


		//paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
		int percent = (int)(((float)progress / (float)max) * 100);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0

		/*float textWidth = paint.measureText(percent+"");   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间

		if(textIsDisplayable && percent != 0 && style == STROKE){
			canvas.drawText(percent+"", centre - textWidth / 2, centre + textSize/2, paint); //画出进度百分比
		}*/

		Rect mRect = new Rect() ;
		String timeValue = timeShow+"" ;
		paint.getTextBounds(timeValue,0,timeValue.length(),mRect);



		//float textWidth = paint.measureText();   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间

		/*if(textIsDisplayable && percent != 0 && style == STROKE){
			canvas.drawText(percent+"", centre - textWidth / 2, centre + textSize/2, paint); //画出进度百分比
		}*/
		canvas.drawText(timeValue, centre - mRect.centerX(), centre - mRect.centerY(), paint); //画出进度百分比




		float textS = mTextViewPaint.measureText("s") ;

		canvas.drawText("s", centre +mRect.centerX() + textS/2, centre - mRect.centerY() +textS/4, mTextViewPaint); //画出进度百分比

		
		/**
		 * 画圆弧 ，画圆环的进度
		 */
		//设置进度是实心还是空心
		paint.setStrokeWidth(roundWidth); //设置圆环的宽度
		paint.setColor(roundProgressColor);  //设置进度的颜色
		RectF oval = new RectF(centre - radius, centre - radius, centre
				+ radius, centre + radius);  //用于定义的圆弧的形状和大小的界限
		
		switch (style) {
		case STROKE:{
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(oval, startAngle, 360 * progress / max, false, paint);  //根据进度画圆弧
			break;
		}
		case FILL:{
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			if(progress !=0)
				canvas.drawArc(oval, 0, 360 * progress / max, true, paint);  //根据进度画圆弧
			break;
		 }
		}

		Shader mShader = new RadialGradient((float) (centre + rd * Math.cos((startAngle+360 * progress / max) * 3.14 / 180)),
				(float) (centre + rd * Math.sin((startAngle+360 * progress / max) * 3.14 / 180)),
				(roundWidth+15)/2,new int[]{Color.WHITE,COLOR1,COLOR2,COLOR3,COLOR4},null,Shader.TileMode.REPEAT)  ;


		circlePaint.setShader(mShader) ;


		canvas.drawCircle(
				(float) (centre + rd * Math.cos((startAngle+360 * progress / max) * 3.14 / 180)),
				(float) (centre + rd * Math.sin((startAngle+360 * progress / max) * 3.14 / 180)),
				(roundWidth+15)/2, circlePaint);// 小圆
	}

	/**
	 * 进度标注点的动画
	 *
	 * @param fromDegrees
	 * @param toDegrees
	 * @return
	 */

	/*	canvas.drawCircle(
			(float) (centre + rd * Math.cos((startAngle+360 * progress / max) * 3.14 / 180)),
			(float) (centre + rd * Math.sin((startAngle+360 * progress / max) * 3.14 / 180)),
			(roundWidth+15)/2, circlePaint);// 小圆*/
	private Animation pointRotationAnima(float fromDegrees, float toDegrees) {
		//int initDegress = 306;// 进度点起始位置(图片偏移约54度)

		RotateAnimation animation = new RotateAnimation(fromDegrees,
				toDegrees, Animation.RELATIVE_TO_SELF, (float)(centre + rd * Math.cos((startAngle+360 * progress / max) * 3.14 / 180)),
				Animation.RELATIVE_TO_SELF, (float) (centre + rd * Math.sin((startAngle+360 * progress / max) * 3.14 / 180)));
		animation.setDuration(1);// 设置动画执行时间
		animation.setRepeatCount(1);// 设置重复执行次数
		animation.setFillAfter(true);// 设置动画结束后是否停留在结束位置
		tempProgress = toDegrees ;
		return animation;
	}
	
	
	public synchronized int getMax() {
		return max;
	}

	/**
	 * 设置进度的最大值
	 * @param max
	 */
	public synchronized void setMax(int max) {
		if(max < 0){
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}


	/**
	 * 获取进度.需要同步
	 * @return
	 */
	public synchronized int getProgress() {
		return progress;
	}

	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
	 * 刷新界面调用postInvalidate()能在非UI线程刷新
	 * @param progress
	 */

	private float tempProgress = -90 ;

	public synchronized void setProgress(int progress,View view ) {
		if(progress < 0){
			throw new IllegalArgumentException("progress not less than 0");
		}
		if(progress > max){
			progress = max;
		}
		if(progress <= max){
			this.progress = progress;
			//postInvalidate();
		}

		view.setAnimation(pointRotationAnima(tempProgress, startAngle+((float) 360 / max) * progress));
		this.postInvalidate();
		
	}
	public synchronized void setProgress(int progress) {
		if(progress < 0){
			throw new IllegalArgumentException("progress not less than 0");
		}
		if(progress > max){
			progress = max;
		}
		if(progress <= max){
			this.progress = progress;
			postInvalidate();
		}

	}

	public synchronized void setTimeShow(int timeShow){
		this.timeShow = timeShow ;
		invalidate();
	}
	
	
	public int getCricleColor() {
		return roundColor;
	}

	public void setCricleColor(int cricleColor) {
		this.roundColor = cricleColor;
	}

	public int getCricleProgressColor() {
		return roundProgressColor;
	}

	public void setCricleProgressColor(int cricleProgressColor) {
		this.roundProgressColor = cricleProgressColor;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	public float getRoundWidth() {
		return roundWidth;
	}

	public void setRoundWidth(float roundWidth) {
		this.roundWidth = roundWidth;
	}



}
