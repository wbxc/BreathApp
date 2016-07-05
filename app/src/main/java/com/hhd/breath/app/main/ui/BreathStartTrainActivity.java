package com.hhd.breath.app.main.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.R;
import com.hhd.breath.app.db.TrainDaySevice;
import com.hhd.breath.app.db.TrainUnitService;
import com.hhd.breath.app.imp.ConnectInterface;
import com.hhd.breath.app.model.BreathTrainingResult;
import com.hhd.breath.app.model.RecordDayData;
import com.hhd.breath.app.model.RecordUnitData;
import com.hhd.breath.app.model.TrainingData;
import com.hhd.breath.app.net.HttpUtil;
import com.hhd.breath.app.net.ThreadPoolWrap;
import com.hhd.breath.app.net.UploadRecordData;
import com.hhd.breath.app.service.UploadDataService;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.utils.StringUtils;
import com.hhd.breath.app.utils.TimeUtils;
import com.hhd.breath.app.utils.UiUtils;
import com.hhd.breath.app.wchusbdriver.CH340AndroidDriver;
import com.hhd.breath.app.widget.CommonProgressBar;
import com.hhd.breath.app.widget.DynamicWave;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 开始训练
 * 训练界面
 */
public class BreathStartTrainActivity extends BaseActivity implements View.OnClickListener {

    private CommonProgressBar mTimeProgress;

    private RelativeLayout layoutTop;
    private TextView mTvXiqi;
    private TextView mTvPause;
    private TextView mTvHuqi;
    private FrameLayout mFragmentContent;
    private DynamicWave mDynamicWave;
    private TextView mTvAction;
    private TextView mTvActionInfo;

    private ProgressBar mProgressBar;
    private TextView mTvProgress;
    private LinearLayout layoutScale;
    private TextView mTvTimeLong;
    private TextView mGroupNumber;
    private RatingBar mRatingbar;
    private ImageView imgLayoutBack;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private int sumGroupNumber = 30;
    private int currentIndex = 0;
    private CH340AndroidDriver mCh340AndroidDriver;
    private boolean READ_ENABLE_340 = false;
    private boolean isConfiged_340 = false;
    byte[] writeBuffer;
    byte[] readBuffer;
    int actualNumBytes;
    private boolean isShowProgressFlag = false;
    private List<Integer> mIntegerValues;
    private List<Integer> mGroupValues;
    private int standValue = 15;

    private int errorValue = 6;
    private int finalRate = 0;
    private String trainName;
    private int mInspiratoryTime;
    private int mBreathTime;
    private int mPauseTime;
    private String timeLongString;
    private String level = "0";
    private String mRecordDayDataId;
    private int baudRate;
    private byte dataBit;
    private byte stopBit;
    private byte parity;
    private byte flowControl;
    private final Object ThreadLock = new Object();  // 线程锁
    private TimerTask mTimeCircleTimerTask;
    private Timer mCircleTimer;
    private int delayTime = 10;
    private int distanceValue = 0;
    private int mFragmentContentHeight = 0;
    private int timeProgress = 0;
    private List<TrainingData> mTrainingDatas;
    private int delayInspiratoryTime = 0;
    private int delayBreathTime = 0;
    private int delayPauseTime = 0;
    private PowerManager pm = null;
    private PowerManager.WakeLock mWakeLock = null;
    private String mTrainTime;

    private String phone;
    private String userName;
    private String sex;
    private String birthday;
    private String serNumber;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    functionAction();
                    break;
                case 2:
                    receiveData();
                    break;
                case 4:
                    receiveTimeData();
                    break;
                case 3:
                    //Toast.makeText(BreathStartTrainActivity.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
                    showDialog();
                    break;
                case 5:
                    Toast.makeText(BreathStartTrainActivity.this, "采集数据异常", Toast.LENGTH_SHORT).show();
                    break;
                case 12:
                    stopTimeAccount();
                    stopCircleTimeAccount();
                    if (mShowDialogEConnection != null) {

                        mShowDialogEConnection.show();
                    } else {
                        showDialogEConnection(BreathStartTrainActivity.this);
                    }
                    break;
                case 33:


                    Toast.makeText(BreathStartTrainActivity.this, "success  ", Toast.LENGTH_SHORT).show();
                    break;
                case 32:
                    Toast.makeText(BreathStartTrainActivity.this, (String) msg.obj + "", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath_start_train);
        initData();
        initView();
        initEvent();
        initCh340Android();
        startTimeAccount();
        startCircleTimeAccount();
        selectIndex(1);


        ViewTreeObserver vto = mFragmentContent.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mFragmentContent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mFragmentContentHeight = mFragmentContent.getHeight();

                distanceValue = (mFragmentContentHeight - UiUtils.dip2px(BreathStartTrainActivity.this, 130)) / (standValue * 2);
            }
        });

        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My_Tag");
        mTrainTime = String.valueOf(System.currentTimeMillis() / 1000);


        int intFlag = mCh340AndroidDriver.ResumeUsbList();

        if (2 == intFlag) {
            mCh340AndroidDriver.CloseDevice();
        } /*else if (0 == intFlag) {
            if (mCh340AndroidDriver.isConnected() && mCh340AndroidDriver.UartInit()) {
                mCh340AndroidDriver.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
            } else if (mCh340AndroidDriver.getmUsbDevice() != null) {
                mCh340AndroidDriver.OpenDevice(mCh340AndroidDriver.getmUsbDevice());
                mCh340AndroidDriver.UartInit();
                mCh340AndroidDriver.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
            }
        }*/
    }


    private void initData() {
        sumGroupNumber = getIntent().getExtras().getInt("sumGroupNumber");
        timeLongString = getIntent().getExtras().getString("sumTimeLong");
        trainName = getIntent().getExtras().getString("trainName");
        level = getIntent().getExtras().getString("level");
        serNumber = ShareUtils.getSerialNumber(BreathStartTrainActivity.this);

        mIntegerValues = new ArrayList<Integer>();
        mGroupValues = new ArrayList<Integer>();
        mTrainingDatas = new ArrayList<TrainingData>();

        if (StringUtils.isNotEmpty(ShareUtils.getUserPhone(BreathStartTrainActivity.this))) {
            phone = ShareUtils.getUserPhone(BreathStartTrainActivity.this);
        } else {
            phone = "null";
        }

        if (StringUtils.isNotEmpty(ShareUtils.getUserName(BreathStartTrainActivity.this))) {
            userName = ShareUtils.getUserName(BreathStartTrainActivity.this);
        } else {
            userName = "null";
        }

        if (StringUtils.isNotEmpty(ShareUtils.getUserSex(BreathStartTrainActivity.this))) {
            sex = ShareUtils.getUserSex(BreathStartTrainActivity.this);
        } else {
            sex = "null";
        }

        if (StringUtils.isNotEmpty(ShareUtils.getUserBirthday(BreathStartTrainActivity.this))) {
            birthday = ShareUtils.getUserBirthday(BreathStartTrainActivity.this);
        } else {
            birthday = "null";
        }

        mInspiratoryTime = ShareUtils.getInspirationTime(BreathStartTrainActivity.this);
        mBreathTime = ShareUtils.getBrathTime(BreathStartTrainActivity.this);
        mPauseTime = ShareUtils.getIntervalTime(BreathStartTrainActivity.this);

        delayBreathTime = mBreathTime * 1000 / 100;
        delayInspiratoryTime = mInspiratoryTime * 1000 / 100;
        delayPauseTime = mPauseTime * 1000 / 100;
        delayTime = delayInspiratoryTime;


        for (int i = 0; i < sumGroupNumber; i++) {
            TrainingData mTrainingData = new TrainingData();
            mTrainingData.setmHuqiFlag(false);
            mTrainingData.setmXiqiFlag(false);
            mTrainingData.setmPauseFlag(false);
            mTrainingData.setmHuxiValue(mBreathTime);
            mTrainingData.setmXiqiValue(mInspiratoryTime);
            mTrainingData.setmPauseTime(mPauseTime);
            mTrainingData.setStandardValue(CommonValues.STANDARD_VALUE);
            mTrainingDatas.add(mTrainingData);
        }
    }

    @Override
    protected void initView() {
        mTimeProgress = (CommonProgressBar) findViewById(R.id.roundProgressBar2);
        //layoutBack = (RelativeLayout)findViewById(R.id.layout_back) ;
        mTvXiqi = (TextView) findViewById(R.id.tv_xiqi);
        mTvHuqi = (TextView) findViewById(R.id.tv_huqi);
        mTvPause = (TextView) findViewById(R.id.tv_pause);
        mFragmentContent = (FrameLayout) findViewById(R.id.frag_content);
        mDynamicWave = (DynamicWave) findViewById(R.id.dyWave_layout);
        mTvAction = (TextView) findViewById(R.id.tv_action);
        mTvActionInfo = (TextView) findViewById(R.id.mtv_action_info);
        mProgressBar = (ProgressBar) findViewById(R.id.layout_progress);
        mTvProgress = (TextView) findViewById(R.id.tv_progress);

        mTvTimeLong = (TextView) findViewById(R.id.text_timeLong);
        mGroupNumber = (TextView) findViewById(R.id.tv_groupNumber);
        mRatingbar = (RatingBar) findViewById(R.id.ratingbar);
        layoutTop = (RelativeLayout) findViewById(R.id.layout_top);
        layoutScale = (LinearLayout) findViewById(R.id.layout_scale);
        imgLayoutBack = (ImageView) findViewById(R.id.img_layout_back);

    }

    @Override
    protected void initEvent() {
        mTimeProgress.setProgress(0);
        imgLayoutBack.setOnClickListener(this);
        layoutTop.setBackgroundColor(getResources().getColor(R.color.color_nobreath_bg));
        mFragmentContent.setBackgroundColor(getResources().getColor(R.color.color_nobreath_bg));

        mTvXiqi.setTextColor(getResources().getColor(R.color.common_top_color));
        mTvXiqi.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_bg));


        mTvPause.setTextColor(getResources().getColor(R.color.common_color_88_white));
        mTvPause.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_bg1));

        mTvHuqi.setTextColor(getResources().getColor(R.color.common_color_88_white));
        mTvHuqi.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_bg1));

        mTvTimeLong.setText(timeLongString);
        mGroupNumber.setText(sumGroupNumber + "");
        mRatingbar.setRating(Float.parseFloat(level));

        mProgressBar.setMax(sumGroupNumber);
        mProgressBar.setProgress(1);
        mTvAction.setText(getResources().getString(R.string.string_please_xiqi));
        mTvActionInfo.setText(CommonValues.xiqi);

        mDynamicWave.setVisibility(View.GONE);
        layoutScale.setVisibility(View.GONE);

        mTimeProgress.setTimeShow(mTrainingDatas.get(currentIndex).getmXiqiValue());
        if (sumGroupNumber < 9) {
            mTvProgress.setText("0" + String.valueOf(currentIndex + 1) + "/0" + String.valueOf(sumGroupNumber));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWakeLock.release();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mCh340AndroidDriver != null) {
            if (mCh340AndroidDriver.isConnected()) {
                mCh340AndroidDriver.CloseDevice();
            }
            mCh340AndroidDriver = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCh340AndroidDriver != null) {
            if (mCh340AndroidDriver.isConnected()) {
                mCh340AndroidDriver.CloseDevice();
            }
            mCh340AndroidDriver = null;
        }
        stopTimeAccount();
        startCircleTimeAccount();
        mHandler.removeMessages(1);
        mHandler.removeMessages(2);
        mHandler.removeMessages(3);
        mHandler.removeMessages(4);
        mHandler.removeMessages(5);
        mHandler.removeMessages(12);
    }

    private Dialog completeDialog = null;

    private void showDialog() {

        if (completeDialog == null) {
            completeDialog = new Dialog(this, R.style.common_dialog);
            //ViewGroup.LayoutParams mLayoutParams = new ViewGroup.LayoutParams() ;

            View mView = LayoutInflater.from(BreathStartTrainActivity.this).inflate(R.layout.complete_train_dialog, null);
            RelativeLayout mLayoutClose = (RelativeLayout) mView.findViewById(R.id.layout_dialog_close);
            mLayoutClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    completeDialog.cancel();
                    BreathStartTrainActivity.this.finish();
                }
            });
            RelativeLayout mLayoutCheck = (RelativeLayout) mView.findViewById(R.id.layout_check_report);

            mLayoutCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    completeDialog.cancel();
                    if (mCh340AndroidDriver!=null)
                        mCh340AndroidDriver.CloseDevice();
                    mCh340AndroidDriver = null;
                    BreathTrainingResult breathTrainingResult = uploadService();
                    Intent mIntent = new Intent();
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("breathTrainingData",breathTrainingResult);
                    mIntent.putExtras(mBundle) ;
                    mIntent.setClass(BreathStartTrainActivity.this, BreathReportActivity.class);
                    mIntent.putExtras(mBundle);
                    startActivity(mIntent);
                    BreathStartTrainActivity.this.finish();
                }
            });
            completeDialog.setContentView(mView);
            completeDialog.setCanceledOnTouchOutside(false);
        }
        completeDialog.show();
    }


    private void functionAction() {
        if (!mTrainingDatas.get(currentIndex).ismXiqiFlag()) {
            isShowProgressFlag = false;
            if (mTrainingDatas.get(currentIndex).getmXiqiValue() >= 0) {
                int xiqivalue = mTrainingDatas.get(currentIndex).getmXiqiValue();
                mTrainingDatas.get(currentIndex).setmXiqiValue(xiqivalue - 1);
                mTimeProgress.setTimeShow(xiqivalue);
                return;
            } else {
                selectIndex(2);
                mTrainingDatas.get(currentIndex).setmXiqiFlag(true);
                mTvAction.setText(getResources().getString(R.string.string_please_pause));
                mTvActionInfo.setText(CommonValues.pause);
                isShowProgressFlag = false;

                mTimeProgress.setTimeShow(mTrainingDatas.get(currentIndex).getmPauseTime());
                mFragmentContent.setBackgroundColor(getResources().getColor(R.color.color_nobreath_bg));
                layoutTop.setBackgroundColor(getResources().getColor(R.color.color_nobreath_bg));
                delayTime = delayPauseTime;
                timeProgress = 0;
                mTimeProgress.setProgress(timeProgress);
                stopCircleTimeAccount();
                startCircleTimeAccount();
            }
        }

        if (!mTrainingDatas.get(currentIndex).ismPauseFlag()) {

            if (mTrainingDatas.get(currentIndex).getmPauseTime() >= 0) {
                int intPause = mTrainingDatas.get(currentIndex).getmPauseTime();
                mTimeProgress.setTimeShow(intPause);
                mTrainingDatas.get(currentIndex).setmPauseTime(intPause - 1);
                return;
            } else {
                selectIndex(3);
                mTrainingDatas.get(currentIndex).setmPauseFlag(true);
                isShowProgressFlag = true;
                mTvAction.setText(getResources().getString(R.string.string_please_huqi));
                mTvActionInfo.setText(CommonValues.huqi);
                mTimeProgress.setTimeShow(mTrainingDatas.get(currentIndex).getmHuxiValue());
                mDynamicWave.setVisibility(View.VISIBLE);
                mFragmentContent.setBackgroundColor(getResources().getColor(R.color.color_breath_bg));
                layoutTop.setBackgroundColor(getResources().getColor(R.color.color_breath_bg));
                layoutScale.setVisibility(View.VISIBLE);
                delayTime = delayBreathTime;
                timeProgress = 0;
                mTimeProgress.setProgress(timeProgress);
                stopCircleTimeAccount();
                startCircleTimeAccount();

            }
        }
        if (!mTrainingDatas.get(currentIndex).ismHuqiFlag()) {

            if (mTrainingDatas.get(currentIndex).getmHuxiValue() >= 0) {

                int intHuqi = mTrainingDatas.get(currentIndex).getmHuxiValue();
                mTimeProgress.setTimeShow(intHuqi);
                mTrainingDatas.get(currentIndex).setmHuxiValue(intHuqi - 1);
                return;
            } else {
                mTrainingDatas.get(currentIndex).setmHuqiFlag(true);
                isShowProgressFlag = false;

                if (mIntegerValues != null && !mIntegerValues.isEmpty()) {
                    int value = 0;
                    for (int i = 0; i < mIntegerValues.size(); i++) {

                        if (isGoodValue(mIntegerValues.get(i)))
                            value++;
                    }
                    int rate = (int) ((float) (value * 100) / mIntegerValues.size());
                    mGroupValues.add(rate);
                }
                mIntegerValues.clear();

                if (currentIndex < mTrainingDatas.size() - 1) {
                    selectIndex(1);
                    mFragmentContent.setBackgroundColor(getResources().getColor(R.color.color_nobreath_bg));
                    layoutTop.setBackgroundColor(getResources().getColor(R.color.color_nobreath_bg));
                    layoutScale.setVisibility(View.GONE);


                    currentIndex = currentIndex + 1;
                    mTvAction.setText(getResources().getString(R.string.string_please_xiqi));
                    mTvActionInfo.setText(CommonValues.xiqi);
                    int temp = mTrainingDatas.get(currentIndex).getmXiqiValue();
                    mTimeProgress.setTimeShow(temp);
                    mTrainingDatas.get(currentIndex).setmXiqiValue(temp - 1);

                    mDynamicWave.setVisibility(View.GONE);
                    mDynamicWave.addProgress(0);
                    //  mDynamicWave.setProgress(0);
                    mProgressBar.setProgress(currentIndex + 1);
                    delayTime = delayInspiratoryTime;
                    timeProgress = 0;
                    mTimeProgress.setProgress(timeProgress);
                    stopCircleTimeAccount();


                    if (currentIndex < 9) {
                        if (sumGroupNumber < 9) {
                            mTvProgress.setText("0" + String.valueOf(currentIndex + 1) + "/0" + String.valueOf(sumGroupNumber));
                        } else {
                            mTvProgress.setText("0" + String.valueOf(currentIndex + 1) + "/" + String.valueOf(sumGroupNumber));
                        }
                    } else {
                        if (sumGroupNumber < 9) {
                            mTvProgress.setText(String.valueOf(currentIndex + 1) + "/0" + String.valueOf(sumGroupNumber));
                        } else {
                            mTvProgress.setText(String.valueOf(currentIndex + 1) + "/" + String.valueOf(sumGroupNumber));
                        }
                    }
                    timeProgress = 0;
                    startCircleTimeAccount();
                    mCurrent = 0;
                    return;
                } else {
                    stopTimeAccount();
                    stopCircleTimeAccount();
                    int value = 0;
                    for (int i = 0; i < mGroupValues.size(); i++) {
                        value += mGroupValues.get(i);
                    }
                    if (value == 0) {
                        mHandler.sendEmptyMessage(5);
                       showDialog();
                    } else {
                        finalRate = (int) value / mGroupValues.size();
                        showDialog();
                    }
                }
            }
        }
    }

    private BreathTrainingResult uploadService(){

        BreathTrainingResult breathTrainingResult = new BreathTrainingResult();
        breathTrainingResult.setUser_id(ShareUtils.getUserId(BreathStartTrainActivity.this));
        breathTrainingResult.setTrain_group(sumGroupNumber + "");
        breathTrainingResult.setBreath_type("2");
        breathTrainingResult.setBreath_name(trainName);
        breathTrainingResult.setTrain_last(timeLongString);
        breathTrainingResult.setTrain_result("12");  // 训练结果
        breathTrainingResult.setDifficulty(finalRate + "");
        breathTrainingResult.setTrain_time(mTrainTime);
        breathTrainingResult.setSuggestion(setSuggestion(finalRate));
        breathTrainingResult.setPlatform("1");
        breathTrainingResult.setDevice_sn(ShareUtils.getSerialNumber(BreathStartTrainActivity.this));
        String fileName = "file_" + String.valueOf(System.currentTimeMillis() / 1000);
        breathTrainingResult.setFname(fileName);  //  文件id  和文件名称 定义一致
        breathTrainingResult.setFile_id(fileName);
        breathTrainingResult.setTrain_last("16");
        breathTrainingResult.setDevice_sn("123456");
        return  breathTrainingResult ;
    }



    /**
     * 统计评分
     * @param finalRate
     * @return
     */
    private String setSuggestion(int finalRate) {

        if (finalRate <= 25) {
            return "1";
        } else if (finalRate > 25 && finalRate <= 50) {
            return "2";
        } else if (finalRate > 50 && finalRate <= 75) {
            return "3";
        } else if (finalRate > 75) {
            return "4";
        }
        return "0";
    }


    private void receiveTimeData() {
        timeProgress++;
        mTimeProgress.setProgress(timeProgress);
    }

    private void receiveData() {

        if (isShowProgressFlag) {
            if (actualNumBytes != 0x00) {

                try {
                    String result = new String(readBuffer, 0, actualNumBytes, "UTF-8");
                    int value = 0;
                    int value2 = 0;

                    if (result.length() >= 7) {

                        String result1 = result.substring(0, 3);
                        value = parseIntFromString(result1);  //*distanceValue ;
                        value2 = parseIntFromString(result.substring(4, 7));
                        mIntegerValues.add(value);
                        mIntegerValues.add(value2);
                        showDyWave(value, value2);
                    } else {
                        String result1 = result.substring(0, 3);
                        value = parseIntFromString(result1);  //*distanceValue ;
                        mIntegerValues.add(value);
                        if (value < 6) {
                            if (value > 2) {
                                value = 4;
                            } else {
                                value = 0;
                            }
                        }
                        int intValue = value * distanceValue;
                        mDynamicWave.addProgress(intValue);
                    }

                } catch (Exception e) {
                }
                actualNumBytes = 0;
            }
        }

    }


    // 开启读取数据线程
    private class ReadThread_340 extends Thread {

        private Handler mHandler;

        ReadThread_340(Handler h) {
            this.mHandler = h;
            this.setPriority(Thread.MIN_PRIORITY);
        }

        @Override
        public void run() {
            super.run();
            while (READ_ENABLE_340) {

                Message msg = mHandler.obtainMessage();
                msg.what = 2;

                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                }

                synchronized (ThreadLock) {
                    if (mCh340AndroidDriver != null) {
                        if (isReceiveData) {

                            if (mCh340AndroidDriver != null) {
                                actualNumBytes = mCh340AndroidDriver.ReadData(readBuffer, 64);
                            }
                            if (actualNumBytes > 0) {
                                mHandler.sendMessage(msg);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isReceiveData = false;
    private ReadThread_340 handlerThread340;


    private void initCh340Android() {

        readBuffer = new byte[512];
        writeBuffer = new byte[512];
        baudRate = 115200;
        dataBit = 8;
        stopBit = 1;
        parity = 0;
        flowControl = 0;
        //   getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mCh340AndroidDriver = new CH340AndroidDriver((UsbManager) getSystemService(Context.USB_SERVICE),
                this, CommonValues.ACTION_USB_PERMISSION);
        mCh340AndroidDriver.setConnectInterface(mConnectInterface);

        if (READ_ENABLE_340 == false) {
            READ_ENABLE_340 = true;
            handlerThread340 = new ReadThread_340(mHandler);   //开启线程
            handlerThread340.start();
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isReceiveData = true;
            }
        }, 5);
    }

    private ConnectInterface mConnectInterface = new ConnectInterface() {
        @Override
        public void errorConnect() {

        }

        @Override
        public void rightConnect(String data) {

            if (data.equals("error1")) {
                mHandler.sendEmptyMessage(12);
            }
        }
    };

    private Dialog mShowDialogEConnection = null;

    public void showDialogEConnection(Context mContext) {

        mShowDialogEConnection = new Dialog(mContext, R.style.common_dialog);

        View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_show_err_conection, null);

        RelativeLayout mRelativeLayout = (RelativeLayout) mView.findViewById(R.id.error_connect_exit);

        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mShowDialogEConnection.isShowing()) {
                    mShowDialogEConnection.dismiss();
                }
                BreathStartTrainActivity.this.finish();
            }
        });

        mShowDialogEConnection.setContentView(mView);
        mShowDialogEConnection.setCanceledOnTouchOutside(false);

        mShowDialogEConnection.show();
    }


    private Dialog mExitDialog = null;

    public void showExitDialog() {

        mExitDialog = new Dialog(BreathStartTrainActivity.this, R.style.common_dialog);
        View mView = LayoutInflater.from(BreathStartTrainActivity.this).inflate(R.layout.dialog_exit_train, null);

        Button okBtn = (Button) mView.findViewById(R.id.btn_train_ok);
        Button noBtn = (Button) mView.findViewById(R.id.btn_train_no);


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mExitDialog.isShowing()) {
                    mExitDialog.dismiss();
                    BreathStartTrainActivity.this.finish();
                }

            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExitDialog.isShowing()) {
                    mExitDialog.dismiss();

                }
                startTimeAccount();
                startCircleTimeAccount();
            }
        });
        mExitDialog.setCanceledOnTouchOutside(false);
        mExitDialog.setCancelable(false);
        mExitDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                }
                return false;
            }
        });
        mExitDialog.setContentView(mView);
        mExitDialog.show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            backFunction();
        }
        return false;
    }


    public static void actionStart(Activity mActivity, int sumGroupNumber, String sumTimeLong, String trainName, String level) {

        Intent mIntent = new Intent();
        Bundle mBundle = new Bundle();
        mBundle.putInt("sumGroupNumber", sumGroupNumber);
        mBundle.putString("sumTimeLong", sumTimeLong);
        mBundle.putString("trainName", trainName);
        mBundle.putString("level", level);
        mIntent.setClass(mActivity, BreathStartTrainActivity.class);
        mIntent.putExtras(mBundle);
        mActivity.startActivity(mIntent);
    }

    private void backFunction() {
        stopTimeAccount();
        stopCircleTimeAccount();

        if (mExitDialog != null) {
            if (!mExitDialog.isShowing()) {
                mExitDialog.show();
            }
        } else {
            showExitDialog();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
/*            case R.id.layout_back :
                backFunction();
                break;*/
            case R.id.img_layout_back:
                backFunction();
                break;
        }
    }

    private void startTimeAccount() {

        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Message mMessage = Message.obtain();
                    mMessage.what = 1;
                    mHandler.sendMessage(mMessage);
                }
            };
        }
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    private void stopTimeAccount() {

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void startCircleTimeAccount() {

        if (mCircleTimer == null) {
            mCircleTimer = new Timer();
        }
        if (mTimeCircleTimerTask == null) {
            mTimeCircleTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Message mMessage = Message.obtain();
                    mMessage.what = 4;
                    mHandler.sendMessage(mMessage);
                }
            };
        }
        mCircleTimer.schedule(mTimeCircleTimerTask, delayTime, delayTime);
    }

    private void stopCircleTimeAccount() {

        if (mTimeCircleTimerTask != null) {
            mTimeCircleTimerTask.cancel();
            mTimeCircleTimerTask = null;
        }

        if (mCircleTimer != null) {
            mCircleTimer.cancel();
            mCircleTimer = null;
        }
    }


    private boolean isGoodValue(int value) {

        if (value > ((standValue - errorValue)) && value < ((standValue + errorValue))) {
            return true;
        }
        return false;
    }

    private int parseIntFromString(String result) {

        int value = 0;
        value = Integer.parseInt(result.substring(0, 1)) * 100
                + Integer.parseInt(result.substring(1, 2)) * 10
                + Integer.parseInt(result.substring(2, 3));
        return value;
    }

    //1.吸气  2.暂停  3.呼气

    private void selectIndex(int index) {

        switch (index) {
            case 1:
                mTvXiqi.setTextColor(getResources().getColor(R.color.common_top_color));
                mTvXiqi.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_bg));
                mTvPause.setTextColor(getResources().getColor(R.color.common_color_88_white));
                mTvPause.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_bg1));

                mTvHuqi.setTextColor(getResources().getColor(R.color.common_color_88_white));
                mTvHuqi.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_bg1));

                break;
            case 2:
                mTvPause.setTextColor(getResources().getColor(R.color.common_top_color));
                mTvPause.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_bg));

                mTvHuqi.setTextColor(getResources().getColor(R.color.common_color_88_white));
                mTvHuqi.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_bg1));

                mTvXiqi.setTextColor(getResources().getColor(R.color.common_color_88_white));
                mTvXiqi.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_bg1));

                break;
            case 3:
                mTvHuqi.setTextColor(getResources().getColor(R.color.common_top_color));
                mTvHuqi.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_bg));

                mTvPause.setTextColor(getResources().getColor(R.color.common_color_88_white));
                mTvPause.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_bg1));

                mTvXiqi.setTextColor(getResources().getColor(R.color.common_color_88_white));
                mTvXiqi.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_bg1));
                break;
        }

    }

    private int mCurrent = 0;
    //private int mCount = 0 ;


    private void showDyWave(int value, int value2) {


        // mCount++ ;
        if (value < 6) {
            if (value > 2) {
                value = 4;
            } else {
                value = 0;
            }
        } else {
            value = (value + mCurrent) / 2;
            mCurrent = value;
        }

        //int intValue = value ; value*distanceValue
        mDynamicWave.addProgress(value * distanceValue);


        try {
            Thread.sleep(20);

            if (value2 < 6) {
                if (value2 > 2) {
                    value2 = 4;
                } else {
                    value2 = 0;
                }
            } else {
                value2 = (value2 + mCurrent) / 2;
                mCurrent = value2;
            }

            // int intValue2 = value2 * distanceValue ;
            //int intValue2 = value2 ;
            //value2*distanceValue
            mDynamicWave.addProgress(value2 * distanceValue);

        } catch (Exception e) {

        }
    }

}
