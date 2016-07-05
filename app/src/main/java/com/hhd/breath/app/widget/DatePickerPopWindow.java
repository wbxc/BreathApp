package com.hhd.breath.app.widget;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.hhd.breath.app.wheelview.OnWheelScrollListener;
import com.hhd.breath.app.wheelview.WheelView;

import com.hhd.breath.app.R;
import com.hhd.breath.app.imp.PerfectInterface;
import com.hhd.breath.app.wheelview.adapter.NumericWheelAdapter;


public class DatePickerPopWindow extends PopupWindow{
	private Context context;
	private String startTime;
	private Date date;
	private int curYear,curMonth;
	private LayoutInflater mInflater;
	private View dateView;
	private WheelView yearView;
	private WheelView monthView;
	private WheelView dayView;
	private int[] timeInt;
	private int temp = 100 ;
	private PerfectInterface mInterface ;
	private Button queding ;
	private OnClickListener mOnClickListener ;
	public DatePickerPopWindow(Context context,String startTime,PerfectInterface mInterface,OnClickListener mOnClickListener){
		this.context=context;
		this.startTime=startTime;
		this.mInterface = mInterface ;
		this.mOnClickListener = mOnClickListener ;
		setStartTime();
		initWindow();
	}
	private void setStartTime() {
		// TODO Auto-generated method stub
		timeInt=new int[6];
		timeInt[0]=Integer.valueOf(startTime.substring(0, 4));
		timeInt[1]=Integer.valueOf(startTime.substring(4, 6));
		timeInt[2]=Integer.valueOf(startTime.substring(6, 8));
	}
	private void initWindow() {
		// TODO Auto-generated method stub
		mInflater=LayoutInflater.from(context);
		dateView=mInflater.inflate(R.layout.wheel_date_picker, null);
		yearView=(WheelView) dateView.findViewById(R.id.year);
		monthView=(WheelView) dateView.findViewById(R.id.month);
		dayView=(WheelView) dateView.findViewById(R.id.day);
		queding = (Button)dateView.findViewById(R.id.btn_ok) ;
		initWheel();
	}
	private void initWheel() {
		// TODO Auto-generated method stub
		Calendar calendar=Calendar.getInstance();
		curYear=calendar.get(Calendar.YEAR);
		curMonth=calendar.get(Calendar.MONTH)+1;
		
		NumericWheelAdapter numericWheelAdapter1=new NumericWheelAdapter(context,curYear-temp, curYear);
		numericWheelAdapter1.setLabel("年");
		yearView.setViewAdapter(numericWheelAdapter1);
		yearView.setCyclic(true);
		yearView.addScrollingListener(scrollListener);
		
		NumericWheelAdapter numericWheelAdapter2=new NumericWheelAdapter(context,1, 12, "%02d"); 
		numericWheelAdapter2.setLabel("月");
		monthView.setViewAdapter(numericWheelAdapter2);
		monthView.setCyclic(true);
		monthView.addScrollingListener(scrollListener);
		
		NumericWheelAdapter numericWheelAdapter3=new NumericWheelAdapter(context,1, getDay(curYear, curMonth), "%02d");
		numericWheelAdapter3.setLabel("日");
		dayView.setViewAdapter(numericWheelAdapter3);
		dayView.setCyclic(true);
		dayView.addScrollingListener(scrollListener);
		
		
		
		yearView.setCurrentItem(timeInt[0]-(curYear-temp));
		monthView.setCurrentItem(timeInt[1]-1);
		dayView.setCurrentItem(timeInt[2]-1);
		
		yearView.setVisibleItems(7);
		monthView.setVisibleItems(7);
		dayView.setVisibleItems(7);
		
		setContentView(dateView);
		setWidth(LayoutParams.FILL_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		ColorDrawable dw = new ColorDrawable(0xFFFFFFFF);
		setBackgroundDrawable(dw);
		setFocusable(true);
		queding.setOnClickListener(mOnClickListener) ;
	}
	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {

		}
		@Override
		public void onScrollingFinished(WheelView wheel) {
			int n_year = yearView.getCurrentItem() + (curYear-temp);
			int n_month = monthView.getCurrentItem() + 1;//
			initDay(n_year,n_month);
			String birthday=new StringBuilder().append((yearView.getCurrentItem()+(curYear-temp))).append("-").append((monthView.getCurrentItem() + 1) < 10 ? "0" + (monthView.getCurrentItem() + 1) : (monthView.getCurrentItem() + 1)).append("-").append(((dayView.getCurrentItem()+1) < 10) ? "0" + (dayView.getCurrentItem()+1) : (dayView.getCurrentItem()+1)).toString();
			//Toast.makeText(context, birthday, Toast.LENGTH_SHORT).show();
			mInterface.setDateValue(birthday) ;
		}
	};
	private void initDay(int arg1, int arg2) {
		NumericWheelAdapter numericWheelAdapter=new NumericWheelAdapter(context,1, getDay(arg1, arg2), "%02d");
		numericWheelAdapter.setLabel("日");
		dayView.setViewAdapter(numericWheelAdapter);
	}
	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}
}
