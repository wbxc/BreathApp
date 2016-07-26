package com.hhd.breath.app.main.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.BreathApplication;
import com.hhd.breath.app.R;
import com.hhd.breath.app.model.SysDataModel;
import com.hhd.breath.app.service.GlobalUsbService;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.utils.Utils;
import com.hhd.breath.app.view.ui.ArmClockActivity;
import com.hhd.breath.app.wchusbdriver.Global340Driver;
import com.hhd.breath.app.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SystemSettingActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mLayoutInflater;
    private int musicSwitch = 1;
    private TextView topTextView;
    private RelativeLayout backRe;
    private RelativeLayout mLayoutSerial;
    private final Handler handler = new Handler();
    private List<SysDataModel> armClockModels;
    private List<String> armClockStrings;
    private Dialog armClockDialog = null;

    @Bind(R.id.tvMusicSwitch)
    TextView tvMusicSwitch ;
    @Bind(R.id.layoutMusicSwitch)
    RelativeLayout layoutMusicSwitch ;
    @Bind(R.id.layoutAlarmClock)
    RelativeLayout layoutAlarmClock ;
    @Bind(R.id.tvAlarmClock)
    TextView tvAlarmClock ;
    private String musicName = "";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        ButterKnife.bind(this);
        mLayoutInflater = LayoutInflater.from(SystemSettingActivity.this);
        Global340Driver.getInstance(SystemSettingActivity.this).setEnableRead(false);
        initData();
        initView();
        initEvent();
        try {
            boolean flag = Global340Driver.getInstance(SystemSettingActivity.this).send("3");
            if (flag) {
                GlobalUsbService.isOpenBreath = false;
            }
            Global340Driver.getInstance(SystemSettingActivity.this).setEnableRead(true);

        } catch (Exception e) {

        }
    }

    @Override
    protected void initView() {
        topTextView = (TextView) findViewById(R.id.topText);
        backRe = (RelativeLayout) findViewById(R.id.back_re);
        mLayoutSerial = (RelativeLayout) findViewById(R.id.layout_serial);
    }

    @Override
    protected void initEvent() {
        topTextView.setText(getResources().getString(R.string.string_system_setting_top));
        backRe.setOnClickListener(this);
        mLayoutSerial.setOnClickListener(this);
        layoutMusicSwitch.setOnClickListener(this);
        layoutAlarmClock.setOnClickListener(this);
        musicSwitch = ShareUtils.getMusicSwitch(SystemSettingActivity.this) ;
        musicName = musicSwitch ==1?"打开":"关闭" ;
        tvMusicSwitch.setText(musicName);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.back_re:
                SystemSettingActivity.this.finish();
                break;
            case R.id.layoutMusicSwitch:
                if (armClockDialog == null) {
                    armClockDialog = showDialogPopuWindow(musicSwitch, armClockStrings,
                            armClockModels, armClockOnclickListener, armClockWheelOnclick);
                }
                armClockDialog.show();
                break;
            case R.id.layout_serial:
                if (!isNotEmpty(ShareUtils.getSerialNumber(SystemSettingActivity.this))) {
                    getSerial();
                } else {
                    BreathSerialActivity.actionStart(SystemSettingActivity.this);
                }
                break;
            case R.id.layoutAlarmClock:
                Intent intent = new Intent() ;
                intent.setClass(SystemSettingActivity.this, ArmClockActivity.class) ;
                startActivityForResult(intent,10);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String result = "";

    private void getSerial() {
        try {
            Global340Driver.getInstance(SystemSettingActivity.this).setEnableRead(true);
            boolean flag1 = Global340Driver.getInstance(SystemSettingActivity.this).send("4");
            if (flag1) {
                showProgressDialog("");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        result = Global340Driver.getInstance(SystemSettingActivity.this).readSerial();
                        hideProgress();
                        if (!isNotEmpty(result)) {
                            try {
                                Thread.sleep(100);
                                result = Global340Driver.getInstance(SystemSettingActivity.this).readSerial();
                            } catch (Exception e) {

                            }
                        }
                        if (Utils.isNoEmpty(result)) {
                            ShareUtils.setSerialNumber(SystemSettingActivity.this, result);
                        }
                        BreathSerialActivity.actionStart(SystemSettingActivity.this);
                    }
                }, 1000);
            } else {
                BreathApplication.toast(SystemSettingActivity.this, getResources().getString(R.string.string_health_error_serial));
            }
            Global340Driver.getInstance(SystemSettingActivity.this).setEnableRead(false);

        } catch (Exception e) {
            Global340Driver.getInstance(SystemSettingActivity.this).setEnableRead(false);
            BreathApplication.toast(SystemSettingActivity.this, getString(R.string.string_health_no_connect));
            Global340Driver.getInstance(SystemSettingActivity.this).read();
        }
    }


    private WheelView.OnWheelViewListener armClockWheelOnclick = new WheelView.OnWheelViewListener() {

        @Override
        public void onSelected(int selectedIndex, String item) {
            super.onSelected(selectedIndex, item);

            musicSwitch = Integer.parseInt(armClockModels.get(selectedIndex).getValue());
            musicName = armClockModels.get(selectedIndex).getName()  ;
            tvMusicSwitch.setText(musicName);
        }
    };


    private View.OnClickListener armClockOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (armClockDialog.isShowing()) {
                armClockDialog.dismiss();
            }
            tvMusicSwitch.setText(musicName);
            ShareUtils.setMusicSwitch(SystemSettingActivity.this, musicSwitch);
        }
    };





    private void initData() {
        musicSwitch = ShareUtils.getMusicSwitch(SystemSettingActivity.this);
        armClockModels = new ArrayList<SysDataModel>();
        armClockStrings = new ArrayList<String>();

        SysDataModel mSysDataModel = new SysDataModel();
        mSysDataModel.setId(String.valueOf(0));
        mSysDataModel.setName("关闭");
        mSysDataModel.setValue("0");
        armClockStrings.add("关闭");
        armClockModels.add(mSysDataModel);

        SysDataModel mSysDataModel1 = new SysDataModel();
        mSysDataModel1.setId(String.valueOf(1));
        mSysDataModel1.setName("打开");
        mSysDataModel1.setValue("1");
        armClockStrings.add("打开");
        armClockModels.add(mSysDataModel1);
    }


    private Dialog showDialogPopuWindow(int index, List<String> mStrings,
                                        List<SysDataModel> mSysDataModels,
                                        View.OnClickListener mOnClickListener, WheelView.OnWheelViewListener mOnWheelViewListener) {
        Dialog mDialog = null;
        if (mDialog == null) {
            View mInspirationView = mLayoutInflater.inflate(R.layout.popu_system_dialog, null);
            WheelView mWheelView = (WheelView) mInspirationView.findViewById(R.id.wheel_view_wv);
            Button mButton = (Button) mInspirationView.findViewById(R.id.queding);
            mButton.setOnClickListener(mOnClickListener);
            mWheelView.setOffset(2);
            mWheelView.setItems(mStrings);
            mWheelView.setSeletion(index - 1);
            mWheelView.setOnWheelViewListener(mOnWheelViewListener);
            mDialog = new Dialog(SystemSettingActivity.this, R.style.common_dialog);
            mDialog.setCancelable(true);
            ViewGroup.LayoutParams mParmas = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mDialog.setContentView(mInspirationView, mParmas);
        }
        return mDialog;
    }
}
