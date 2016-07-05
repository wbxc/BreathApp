package com.hhd.breath.app.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.BreathApplication;
import com.hhd.breath.app.R;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.utils.StringUtils;
import com.hhd.breath.app.utils.Utils;
import com.hhd.breath.app.wchusbdriver.CH340AndroidDriver;
import com.hhd.breath.app.wchusbdriver.Global340Driver;

public class BreathSerialActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mLayoutBack;
    private TextView mTvTop;
    private TextView mTvSerial;
    private final Handler handler = new Handler() ;
    private String result = "" ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath_serial);
        initView();
        initEvent();

        if (!isNotEmpty(ShareUtils.getSerialNumber(BreathSerialActivity.this))){
            Global340Driver.getInstance(BreathSerialActivity.this).setEnableRead(true);
            try {
                boolean flag1 = Global340Driver.getInstance(BreathSerialActivity.this).send("4") ;
                if (flag1){
                    showProgressDialog("");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            result = Global340Driver.getInstance(BreathSerialActivity.this).readSerial();

                            hideProgress();
                            if (!isNotEmpty(result)) {

                                try {
                                    Thread.sleep(100);
                                    result = Global340Driver.getInstance(BreathSerialActivity.this).readSerial();
                                }catch (Exception e){

                                }
                            }
                            if (Utils.isNoEmpty(result)){
                                ShareUtils.setSerialNumber(BreathSerialActivity.this, result);
                                mTvSerial.setText(result);
                            }
                        }
                    }, 1000) ;
                }else {
                    BreathApplication.toast(BreathSerialActivity.this, getResources().getString(R.string.string_health_error_serial));
                }
                Global340Driver.getInstance(BreathSerialActivity.this).setEnableRead(false);
            }catch (Exception e){
            }
        }
    }

    @Override
    protected void initView() {
        mLayoutBack = (RelativeLayout) findViewById(R.id.back_re);
        mTvTop = (TextView) findViewById(R.id.topText);
        mTvSerial = (TextView) findViewById(R.id.tv_serial);
    }

    @Override
    protected void initEvent() {
        mTvTop.setText(getResources().getString(R.string.string_serial_top));
        mLayoutBack.setOnClickListener(this);
        if (StringUtils.isNotEmpty(ShareUtils.getSerialNumber(BreathSerialActivity.this))) {
            mTvSerial.setText(ShareUtils.getSerialNumber(BreathSerialActivity.this));
        } else {
            mTvSerial.setText(getResources().getString(R.string.string_serial_number));
        }
    }



    public static void actionStart(Context mContext) {

        Intent mIntent = new Intent();
        mIntent.setClass(mContext, BreathSerialActivity.class);
        mContext.startActivity(mIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back_re:
                BreathSerialActivity.this.finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
