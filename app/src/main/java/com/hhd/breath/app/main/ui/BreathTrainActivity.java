package com.hhd.breath.app.main.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.BreathApplication;
import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.R;
import com.hhd.breath.app.andengine.BreathAndEngine;
import com.hhd.breath.app.model.TrainPlan;
import com.hhd.breath.app.service.GlobalUsbService;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.utils.StringUtils;
import com.hhd.breath.app.view.ui.CreateTrainInstruction;
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
    private Context context;
    private boolean isHasPermission = false ;
    private byte[] readBuffer;
    private byte[] writeBuffer;
    private boolean READ_ENABLE_340 = false;
    private int actualNumBytes = 0;
    private Object ThreadLock = new Object();
    private BreathTrainHandler mHandler = null;
    private Dialog errorDialog = null;
    private TrainPlan trainPlan ;
    @Bind(R.id.tvLevelFlag)
    TextView tvLevelFlag ;
    @Bind(R.id.tvLevelValue)
    TextView tvLevelValue ;
    @Bind(R.id.layoutLevelContent)
    RelativeLayout layoutLevelContent ;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initData() {
        actionGroupNumber = ShareUtils.getActionGroup(BreathTrainActivity.this);

        trainCode = getIntent().getExtras().getString("trainCode");
        trainName = getIntent().getExtras().getString("trainName");
        level = getIntent().getExtras().getString("level");
        isHasPermission = getIntent().getExtras().getBoolean("isHasPermission") ;
        trainPlan = (TrainPlan) getIntent().getExtras().getSerializable("train_plan") ;
        int sum = Integer.parseInt(trainPlan.getInspirerTime()) +Integer.parseInt(trainPlan.getPersistentLevel()) ;
        timeLong = sum * Integer.parseInt(trainPlan.getGroupNumber());
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
        mBreathExplain = (TextView) findViewById(R.id.tvBreathExplain);
        imgConnectionTrainQi = (ImageView) findViewById(R.id.img_connect_train_qi);
    }

    @Override
    protected void initEvent() {

        backRe.setOnClickListener(this);
        start_connect.setOnClickListener(this);
        error_connect.setOnClickListener(this);
        layoutLevelContent.setOnClickListener(this);
        trainTimeLong.setText(getTimeLong(timeLong));
        trainGroupNumber.setText(trainPlan.getGroupNumber());
        mBreathExplain.setText(CommonValues.LEVEL_ONE_DS);
        imgConnectionTrainQi.setOnClickListener(this);
        mTextTrainName.setText(trainPlan.getName());
        int level = Integer.parseInt(trainPlan.getCurrentControl())+(Integer.parseInt(trainPlan.getCurrentStrength())-1)*3+(Integer.parseInt(trainPlan.getCurrentPersistent())-1)*6 ;

        tvLevelValue.setText(String.valueOf(level));
        if (level<=9){
            tvLevelFlag.setText("初级");
        }else if (level<=18){
            tvLevelFlag.setText("中级");
        }else {
            tvLevelFlag.setText("高级");
        }
        tvLevelValue.setText(String.valueOf(level));
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
            case R.id.layoutLevelContent:
                Intent intent = new Intent() ;
                intent.setClass(BreathTrainActivity.this, CreateTrainInstruction.class) ;
                Bundle bundle = new Bundle() ;
                bundle.putString("str_top_text",getResources().getString(R.string.string_look_introductions));
                bundle.putString("request_url","http://101.201.39.122/ftpuser01/app/djsm.html");
                intent.putExtras(bundle) ;
                startActivityForResult(intent,10);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startBreathAndEngine(){
        Intent intent = new Intent() ;
        Bundle bundle = new Bundle() ;
        bundle.putSerializable("train_plan",trainPlan);
        intent.putExtras(bundle) ;
        intent.setClass(BreathTrainActivity.this, BreathAndEngine.class) ;
        startActivity(intent);
        BreathTrainActivity.this.finish();
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
                case 15:
                    activity.function15();
                    break;
                case 16:
                    activity.function16();
                    break;
                case 24:
                    activity.function24();
                    break;
            }
        }
    }



    private void function24(){
        if (Global340Driver.getInstance(BreathTrainActivity.this).checkUsbStatus() == 1){
            stopTask();
            Global340Driver.getInstance(BreathTrainActivity.this).initDriver() ;
            Global340Driver.getInstance(BreathTrainActivity.this).initUart() ;
            Global340Driver.getInstance(BreathTrainActivity.this).setEnableRead(true); ;
            initUsbDevice();
        }
    }
    private void initUsbDevice(){

        Global340Driver.getInstance(BreathTrainActivity.this).setEnableRead(true);
        try {
            boolean flag = Global340Driver.getInstance(BreathTrainActivity.this).send("1");
            if (flag) {
                showProgressDialog(BreathTrainActivity.this,"") ;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        String result = Global340Driver.getInstance(BreathTrainActivity.this).readSerial() ;
                        hideProgress();
                        ShareUtils.setSerialNumber(BreathTrainActivity.this,result);

                        error_connect.setVisibility(View.GONE);
                        start_connect.setVisibility(View.VISIBLE);

                    }
                }, 500);
            } else {
                BreathApplication.toast(BreathTrainActivity.this, "设备获取序列号失败");
            }
        }catch (Exception e){
            BreathApplication.toast(BreathTrainActivity.this, "设备获取序列号失败");
        }
    }



    /**
     * USB插入
     * 判断usb是否是所需的USB
     */
    private void function15(){
        switch (Global340Driver.getInstance(BreathTrainActivity.this).checkUsbStatus()){
            case 2:
                Global340Driver.getInstance(BreathTrainActivity.this).getPermission();
                startTask();
                break;
            case 1:
                if (!StringUtils.isNotEmpty(ShareUtils.getSerialNumber(BreathTrainActivity.this))){
                    Global340Driver.getInstance(BreathTrainActivity.this).initDriver() ;
                    Global340Driver.getInstance(BreathTrainActivity.this).initUart() ;
                }else {
                    Global340Driver.getInstance(BreathTrainActivity.this).initDriver() ;
                }
                break;
            case 0:
                break;
        }


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
                mHandler.sendEmptyMessage(16) ;
            }
        }
    }

    private Timer mTimer = null ;
    private TimerTask mTimerTask = null;

    /**
     * 开启定时器
     * 检测USB是否获取权限
     */
    private void startTask(){

        if (mTimer == null){
            mTimer = new Timer() ;
        }

        if (mTimerTask == null){
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(24) ;
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
        mHandler.removeMessages(23);
        mHandler.removeMessages(15);
        mHandler.removeMessages(16);
    }
}
