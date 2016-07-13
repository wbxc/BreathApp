package com.hhd.breath.app.main.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.BreathApplication;
import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.R;
import com.hhd.breath.app.andengine.BreathAndEngine;
import com.hhd.breath.app.model.TrainPlan;
import com.hhd.breath.app.net.ThreadPoolWrap;
import com.hhd.breath.app.service.GlobalUsbService;
import com.hhd.breath.app.service.TransmitDataDriver;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.wchusbdriver.Global340Driver;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 训练计划详情
 */
public class BreathTrainActivity extends BaseActivity implements View.OnClickListener {


    private RelativeLayout backRe;
    private TextView topTextView;
    private static final String ACTION_USB_PERMISSION = "com.wch.wchusbdriver.USB_PERMISSION";
    private RelativeLayout error_connect;
    private RelativeLayout start_connect;
    private UsbBroadCaster mUsbBroadCaster;
    private boolean mConnectRight = false;
    private boolean mConnectError = false;
    private ImageView imgConnectionTrainQi;
    private TextView trainTimeLong;
    private TextView trainGroupNumber;
    private int timeLong;
    private int actionGroupNumber;
    private String trainName;
    private String level;
    private String trainCode;
    private TextView mTextTrainName;
    private TextView mBreathExplain;
    private PendingIntent mPendingIntent;
    private Context context;
    private Dialog countdown = null;
    private TextView mTvCountDown;
    private Timer mCountDownTimer;
    private TimerTask mCountDownTask;
    private int mCountSum = 5;
    private int mSendCount = 0;
    private boolean isHasPermission = false ;
    private byte[] readBuffer;
    private byte[] writeBuffer;
    private boolean READ_ENABLE_340 = false;
    private int actualNumBytes = 0;
    private Object ThreadLock = new Object();
    private boolean isReceiveData = false;
    //private TransmitDataDriver transmitDataDriver = null;
    private boolean isFlag = false ;
    //private ReadThreadData readThreadData = null;
    private BreathTrainHandler mHandler = null;
    private Dialog errorDialog = null;

    @Bind(R.id.levelControlRa)
    RatingBar  levelControlRa ;
    @Bind(R.id.levelStrengthRa)
    RatingBar levelStrengthRa ;
    @Bind(R.id.levelPresnsterthRa)
    RatingBar levelPresnsterthRa ;
    private TrainPlan trainPlan ;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_breath_train);
        ButterKnife.bind(this);
        context = BreathTrainActivity.this;
        mUsbBroadCaster = new UsbBroadCaster();
        mHandler = new BreathTrainHandler(BreathTrainActivity.this) ;
        initData();
        initView();
        initEvent();
        stopService(new Intent(BreathTrainActivity.this, GlobalUsbService.class)) ;
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        mIntentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mUsbBroadCaster, mIntentFilter);
        if (isHasPermission){
            error_connect.setVisibility(View.GONE);
            start_connect.setVisibility(View.VISIBLE);
        }else {
            start_connect.setVisibility(View.GONE);
            error_connect.setVisibility(View.VISIBLE);
        }
    }


    private void initData() {
        actionGroupNumber = ShareUtils.getActionGroup(BreathTrainActivity.this);
        int sum = ShareUtils.getBrathTime(BreathTrainActivity.this)
                + ShareUtils.getIntervalTime(BreathTrainActivity.this);
        timeLong = sum * ShareUtils.getActionGroup(BreathTrainActivity.this);
        trainCode = getIntent().getExtras().getString("trainCode");
        trainName = getIntent().getExtras().getString("trainName");
        level = getIntent().getExtras().getString("level");
        isHasPermission = getIntent().getExtras().getBoolean("isHasPermission") ;
        trainPlan = (TrainPlan) getIntent().getExtras().getSerializable("train_plan") ;
        readBuffer = new byte[512];
        writeBuffer = new byte[512];
    }

    @Override
    protected void initView() {
        topTextView = (TextView) findViewById(R.id.topText);
        topTextView.setText("训练计划");
        backRe = (RelativeLayout) findViewById(R.id.back_re);
        error_connect = (RelativeLayout) findViewById(R.id.error_connect);
        start_connect = (RelativeLayout) findViewById(R.id.start_train);
        trainTimeLong = (TextView) findViewById(R.id.time_long);
        trainGroupNumber = (TextView) findViewById(R.id.zushuo);
        mTextTrainName = (TextView) findViewById(R.id.train_name);
        mBreathExplain = (TextView) findViewById(R.id.text_breathExplain);
        imgConnectionTrainQi = (ImageView) findViewById(R.id.img_connect_train_qi);
    }

    @Override
    protected void initEvent() {

        backRe.setOnClickListener(this);
        start_connect.setOnClickListener(this);
        error_connect.setOnClickListener(this);
        trainTimeLong.setText(getTimeLong(timeLong));
        trainGroupNumber.setText(String.valueOf(ShareUtils.getActionGroup(BreathTrainActivity.this)));

        mBreathExplain.setText(CommonValues.LEVEL_ONE_DS);
        imgConnectionTrainQi.setOnClickListener(this);


        levelControlRa.setRating(Float.valueOf(trainPlan.getCurrentControl()));
        levelStrengthRa.setRating(Float.valueOf(trainPlan.getCurrentStrength()));
        levelPresnsterthRa.setRating(Float.valueOf(trainPlan.getCurrentPersistent()));
        mTextTrainName.setText(trainPlan.getName());

    }



    @Override
    protected void onResume() {
        super.onResume();
        CommonValues.select_activity = 0;
        mConnectError = false;
        mConnectRight = false;

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public static void actionStart(Activity mActivity, String trainName, String level, String trainCode, boolean isHasPermission, TrainPlan trainPlan) {
        Bundle mBundle = new Bundle();
        Intent mIntent = new Intent();
        mIntent.setClass(mActivity, BreathTrainActivity.class);
        mBundle.putString("trainName", trainName);  // 训练名称
        mBundle.putString("level", level);   // 训练级别
        mBundle.putString("trainCode", trainCode);   // 标示
        mBundle.putBoolean("isHasPermission",isHasPermission);
        mBundle.putSerializable("train_plan",trainPlan);

        mIntent.putExtras(mBundle);
        mActivity.startActivity(mIntent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_train:
                if (GlobalUsbService.isOpenBreath){
                    BreathApplication.toastTest(BreathTrainActivity.this, "已可以接收");
                    startBreathAndEngine() ;
                }else{
                    try {
                        if (Global340Driver.getInstance(BreathTrainActivity.this).send("2")){
                            GlobalUsbService.isOpenBreath = true ;
                            startBreathAndEngine() ;
                        }
                    }catch (Exception e){
                        BreathApplication.toastTest(BreathTrainActivity.this,e.getMessage());
                    }
                }
                break;
            case R.id.back_re:
                BreathTrainActivity.this.finish();
                break;
            case R.id.img_connect_train_qi:
                showDialog();
                break;
        }
    }


    private void startBreathAndEngine(){
        Intent intent = new Intent() ;
        Bundle bundle = new Bundle() ;
        bundle.putSerializable("train_plan",trainPlan);
       // BreathApplication.toast(BreathTrainActivity.this,trainPlan.toString());
        intent.putExtras(bundle) ;
        intent.setClass(BreathTrainActivity.this, BreathAndEngine.class) ;
        startActivity(intent);
        BreathTrainActivity.this.finish();
    }



    private void startTimeCountDown() {
        if (mCountDownTimer == null) {
            mCountDownTimer = new Timer();
        }
        if (mCountDownTask == null) {
            mCountDownTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(1);
                }
            };
        }
        mCountDownTimer.schedule(mCountDownTask, 0, 1000);
    }


    private void stopTimeCountDown() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        if (mCountDownTask != null) {
            mCountDownTask.cancel();
            mCountDownTask = null;
        }
    }


    private void showCountDown() {
        mCountSum = 5;
        if (countdown != null) {
            countdown.show();
        } else {
            countdown = new Dialog(BreathTrainActivity.this, R.style.common_dialog);
            View mView = LayoutInflater.from(BreathTrainActivity.this).inflate(R.layout.dialog_count_down, null);
            mTvCountDown = (TextView) mView.findViewById(R.id.tv_countdown);
            mTvCountDown.setText("4");
            countdown.setContentView(mView);
            countdown.setCanceledOnTouchOutside(false);
            countdown.setCancelable(false);
            countdown.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            countdown.show();
        }
        startTimeCountDown();
    }


    private static class BreathTrainHandler extends Handler{

        private WeakReference<BreathTrainActivity> reference ;
        private BreathTrainActivity activity ;
        public BreathTrainHandler(BreathTrainActivity context){
            reference = new WeakReference<BreathTrainActivity>(context) ;
            activity = reference.get() ;
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 22:
                    activity.function22();
                    break;
                case 23:
                    activity.function23();
                    break;
                case 15:
                    activity.function15();
                    break;
                case 16:
                    activity.function16();
                    break;
            }
        }
    }


    private void function22(){
        try {
            String result = new String(readBuffer, 0, actualNumBytes);
            actualNumBytes = 0;
            ShareUtils.setSerialNumber(BreathTrainActivity.this, result);
            isFlag = true  ;
        } catch (Exception e) {
        }
        //readThreadData.setReadThread(false);
    }

    private void function23(){

        //readThreadData.setReadThread(false);
        if (actualNumBytes != 0x00) {
            String result = new String(readBuffer, 0, actualNumBytes);
            actualNumBytes = 0;
        }
        if (isFlag){
            isFlag = false ;
            actualNumBytes = 0 ;
            showCountDown();
        }
    }

    /**
     * USB插入
     */
    private void function15(){
       /* if (transmitDataDriver == null)
            transmitDataDriver = TransmitDataDriver.getInstance(BreathTrainActivity.this) ;

        if (transmitDataDriver.checkUsbStatus() ==2){
            error_connect.setVisibility(View.GONE);
            start_connect.setVisibility(View.VISIBLE);
            if (errorDialog != null && errorDialog.isShowing()) {
                errorDialog.dismiss();
            }
            transmitDataDriver.getPermission();
        }*/
    }

    /**
     * USB拔出
     */
    private void function16(){
        start_connect.setVisibility(View.GONE);
        error_connect.setVisibility(View.VISIBLE);
        //ThreadPoolWrap.getThreadPool().removeTask(readThreadData);
    }




    /**
     * 内部广播类
     */
    private class UsbBroadCaster extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(intent.getAction())){
                mHandler.sendEmptyMessage(15) ;
            }else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(intent.getAction())){
                GlobalUsbService.isOpenBreath = false ;
                ShareUtils.setSerialNumber(BreathTrainActivity.this, "");
                Toast.makeText(BreathTrainActivity.this, "设备被拔出", Toast.LENGTH_SHORT).show();
                //ThreadPoolWrap.getThreadPool().removeTask(readThreadData);
                mHandler.sendEmptyMessage(16) ;
            }
        }
    }

    private Timer mTimer = null ;
    private TimerTask mTimerTask = null;

    private void startTask(){

        if (mTimer == null){
            mTimer = new Timer() ;
        }

        if (mTimerTask == null){
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(4) ;
                }
            } ;
        }

        mTimer.schedule(mTimerTask, 0, 100);

    }

    private void stopTask(){

        if (mTimer!=null){
            mTimer.cancel();
            mTimer =null;
        }

        if (mTimerTask!=null){
            mTimerTask.cancel() ;
            mTimerTask = null;
        }
    }


    private void showDialog() {
        if (errorDialog == null) {
            errorDialog = new Dialog(this, R.style.common_dialog);
            View mView = LayoutInflater.from(this).inflate(R.layout.alter_train_dialog, null);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    errorDialog.dismiss();
                }
            });
            errorDialog.setContentView(mView);
            errorDialog.setCanceledOnTouchOutside(true);
        }
        errorDialog.show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mUsbBroadCaster);
        startService(new Intent(BreathTrainActivity.this,GlobalUsbService.class)) ;
        mHandler.removeMessages(22);
        mHandler.removeMessages(23);
        mHandler.removeMessages(15);
        mHandler.removeMessages(16);

        //ThreadPoolWrap.getThreadPool().removeTask(readThreadData);
    }

}
