package com.hhd.breath.app.view.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.R;
import com.hhd.breath.app.arm.AlarmManagerUtil;
import com.hhd.breath.app.arm.view.SelectRemindCyclePopup;
import com.hhd.breath.app.arm.view.SelectRemindWayPopup;
import com.hhd.breath.app.utils.ShareUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArmClockActivity extends BaseActivity implements View.OnClickListener {

    TimePickerView timePickerView ;
    @Bind(R.id.layoutRepeat)
    RelativeLayout layoutRepeat ;
    @Bind(R.id.layoutBellWay)
    RelativeLayout layoutBellWay ;
    @Bind(R.id.layoutSetTime)
    RelativeLayout layoutSetTime ;
    @Bind(R.id.topText)
    TextView topText ;
    @Bind(R.id.back_re)
    RelativeLayout layoutBack ;
    @Bind(R.id.layout_right)
    RelativeLayout layout_right ;
    @Bind(R.id.tvRight)
    TextView tvRight ;
    @Bind(R.id.tvSetTime)
    TextView tvSetTime ;
    private String time ;
    @Bind(R.id.allLayout)
    RelativeLayout allLayout ;
    @Bind(R.id.tvBellWay)
    TextView tvBellWay ;
    @Bind(R.id.tvRepeat)
    TextView tvRepeat ;
    private int ring = 0 ;
    private int cycle  ;

    @Bind(R.id.layoutSwitch)
    RelativeLayout layoutSwitch ;
    @Bind(R.id.imgSwitchClock)
    ImageView imgSwitchClock ;
    private String cycleName ;
    private int time0;
    private int time1;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arm_clock);
        ButterKnife.bind(this);
        initView();
        initEvent();
        timePickerView = new TimePickerView(this,TimePickerView.Type.HOURS_MINS) ;
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(false);
        timePickerView.setCancelable(true) ;

        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener(){
            @Override
            public void onTimeSelect(Date date) {
                time = getTime(date) ;
                ShareUtils.setTimeName(ArmClockActivity.this,time+" 提醒");
                tvSetTime.setText(time+" 提醒");
            }
        });

        ring = ShareUtils.getRing(ArmClockActivity.this) ;
        cycleName = ShareUtils.getCycleName(ArmClockActivity.this) ;
        switch (ring) {
            // 震动
            case 0:
                tvBellWay.setText("震动");
                break;
            // 铃声
            case 1:
                tvBellWay.setText("铃声");
                break;
            default:
                break;
        }

        tvRepeat.setText(cycleName);
        time0 = ShareUtils.getTime1(ArmClockActivity.this) ;
        time1 = ShareUtils.getTime2(ArmClockActivity.this) ;
        tvSetTime.setText(ShareUtils.getTimeName(ArmClockActivity.this));
    }

    @Override
    protected void initView() {
        super.initView();
        layout_right.setVisibility(View.VISIBLE);
        tvRight.setText("保存");
        topText.setText("修改训练提醒");
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        layoutBellWay.setOnClickListener(this);
        layoutRepeat.setOnClickListener(this);
        layoutSetTime.setOnClickListener(this);
        layoutBack.setOnClickListener(this);
        layout_right.setOnClickListener(this);
        layoutSwitch.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.layoutSwitch:

                break;
            case R.id.layoutRepeat:
                selectRemindCycle() ;
                break;
            case R.id.layoutBellWay:
                selectRingWay() ;
                break;
            case R.id.layoutSetTime:
                if (!timePickerView.isShowing()){
                    timePickerView.show();
                }
                break;
            case R.id.back_re:
                setResult(11);
                ArmClockActivity.this.finish();
                break;
            case R.id.layout_right:
                setClock();
                setResult(12);
                ArmClockActivity.this.finish();
                break;

        }
    }

    private void setClock() {
        if (time != null && time.length() > 0) {
            String[] times = time.split(":");


            time0 = Integer.parseInt(times[0]) ;
            time1 = Integer.parseInt(times[1]) ;


            if (cycle == 0) {//是每天的闹钟
                AlarmManagerUtil.setAlarm(this, 0,time0 , time1, 0, 0, "呼吸训练时间到了", ring);
            } if(cycle == -1){//是只响一次的闹钟
                AlarmManagerUtil.setAlarm(this, 1, time0 , time1, 0, 0, "呼吸训练时间到了", ring);
            }else {//多选，周几的闹钟
                String weeksStr = parseRepeat(cycle, 1);
                String[] weeks = weeksStr.split(",");
                for (int i = 0; i < weeks.length; i++) {
                    AlarmManagerUtil.setAlarm(this, 2, time0 , time1, i, Integer.parseInt(weeks[i]), "呼吸训练时间到了", ring);
                }
            }
            ShareUtils.setTime1(ArmClockActivity.this,time0);
            ShareUtils.setTime2(ArmClockActivity.this,time1);

            Toast.makeText(this, "闹钟设置成功", Toast.LENGTH_LONG).show();
        }
    }



    public void selectRingWay() {
        SelectRemindWayPopup fp = new SelectRemindWayPopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindWayPopupListener(new SelectRemindWayPopup
                .SelectRemindWayPopupOnClickListener() {

            @Override
            public void obtainMessage(int flag) {
                switch (flag) {
                    // 震动
                    case 0:
                        tvBellWay.setText("震动");
                        ring = 0;
                        break;
                    // 铃声
                    case 1:
                        tvBellWay.setText("铃声");
                        ring = 1;
                        break;
                    default:
                        break;
                }
                ShareUtils.setRing(ArmClockActivity.this,ring);
            }
        });
    }
    public void selectRemindCycle() {
        final SelectRemindCyclePopup fp = new SelectRemindCyclePopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindCyclePopupListener(new SelectRemindCyclePopup
                .SelectRemindCyclePopupOnClickListener() {

            @Override
            public void obtainMessage(int flag, String ret) {
                switch (flag) {
                    // 星期一
                    case 0:

                        break;
                    // 星期二
                    case 1:

                        break;
                    // 星期三
                    case 2:

                        break;
                    // 星期四
                    case 3:

                        break;
                    // 星期五
                    case 4:

                        break;
                    // 星期六
                    case 5:

                        break;
                    // 星期日
                    case 6:

                        break;
                    // 确定
                    case 7:
                        int repeat = Integer.valueOf(ret);
                        cycleName = parseRepeat(repeat,0) ;

                        //tvRepeat.setText(parseRepeat(repeat, 0));
                        cycle = repeat;
                        fp.dismiss();
                        break;
                    case 8:
                        //tvRepeat.setText("每天");
                        cycleName = "每天" ;
                        cycle = 0;
                        fp.dismiss();
                        break;
                    case 9:
                        cycleName = "只响一次" ;
                        //tvRepeat.setText("只响一次");
                        cycle = -1;
                        fp.dismiss();
                        break;
                    default:
                        break;
                }

                tvRepeat.setText(cycleName);
            }
        });
    }


    /**
     * @param repeat 解析二进制闹钟周期
     * @param flag   flag=0返回带有汉字的周一，周二cycle等，flag=1,返回weeks(1,2,3)
     * @return
     */
    public static String parseRepeat(int repeat, int flag) {
        String cycle = "";
        String weeks = "";
        if (repeat == 0) {
            repeat = 127;
        }
        if (repeat % 2 == 1) {
            cycle = "周一";
            weeks = "1";
        }
        if (repeat % 4 >= 2) {
            if ("".equals(cycle)) {
                cycle = "周二";
                weeks = "2";
            } else {
                cycle = cycle + "," + "周二";
                weeks = weeks + "," + "2";
            }
        }
        if (repeat % 8 >= 4) {
            if ("".equals(cycle)) {
                cycle = "周三";
                weeks = "3";
            } else {
                cycle = cycle + "," + "周三";
                weeks = weeks + "," + "3";
            }
        }
        if (repeat % 16 >= 8) {
            if ("".equals(cycle)) {
                cycle = "周四";
                weeks = "4";
            } else {
                cycle = cycle + "," + "周四";
                weeks = weeks + "," + "4";
            }
        }
        if (repeat % 32 >= 16) {
            if ("".equals(cycle)) {
                cycle = "周五";
                weeks = "5";
            } else {
                cycle = cycle + "," + "周五";
                weeks = weeks + "," + "5";
            }
        }
        if (repeat % 64 >= 32) {
            if ("".equals(cycle)) {
                cycle = "周六";
                weeks = "6";
            } else {
                cycle = cycle + "," + "周六";
                weeks = weeks + "," + "6";
            }
        }
        if (repeat / 64 == 1) {
            if ("".equals(cycle)) {
                cycle = "周日";
                weeks = "7";
            } else {
                cycle = cycle + "," + "周日";
                weeks = weeks + "," + "7";
            }
        }

        return flag == 0 ? cycle : weeks;
    }

    private String getTime(Date date){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm") ;
        return format.format(date) ;
    }
}
