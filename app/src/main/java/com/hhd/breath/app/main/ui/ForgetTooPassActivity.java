package com.hhd.breath.app.main.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.BreathApplication;
import com.hhd.breath.app.R;
import com.hhd.breath.app.net.HttpUtil;
import com.hhd.breath.app.net.ManagerRequest;
import com.hhd.breath.app.net.NetConfig;
import com.hhd.breath.app.net.ThreadPoolWrap;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ForgetTooPassActivity extends BaseActivity {

    @Bind(R.id.topText) TextView tvTop ;
    @Bind(R.id.back_re) RelativeLayout layoutBack ;
    @Bind(R.id.btnGetCheckCode) Button btnGetCheckCode ;
    @Bind(R.id.btnCheckCode) Button btnCheckCode ;
    @Bind(R.id.editCheckCode) EditText editCheckCode ;

    private String phone ;
    private Timer mTimer = null ;
    private TimerTask mTimerTask = null;
    private int sumTime = 0 ;
    private ForgetHandler mForgetHandler ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_too_pass);
        ButterKnife.bind(this);
        phone = getIntent().getExtras().getString("phone") ;
        mForgetHandler = new ForgetHandler(this) ;
        initView();
        initEvent();
        startTimer();
    }

    @Override
    protected void initView() {
        tvTop.setText("忘记密码");
        btnCheckCode.setText("验证");
    }


    private static class ForgetHandler extends Handler{

        private WeakReference<ForgetTooPassActivity> reference ;
        private ForgetTooPassActivity activity ;
        public ForgetHandler(ForgetTooPassActivity context){
            reference = new WeakReference<ForgetTooPassActivity>(context) ;
            activity = reference.get() ;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 23:
                    activity.function23();
                    break;
            }
        }
    }

    protected void function23(){
        sumTime++ ;
        if (sumTime < 180){
            btnGetCheckCode.setText(""+(180-sumTime));
            btnGetCheckCode.setTextColor(getResources().getColor(R.color.common_color_878787));
        }else {
            stopTimer();
            sumTime = 0 ;
            btnGetCheckCode.setEnabled(true);
            btnGetCheckCode.setText("重新获取");
            btnGetCheckCode.setTextColor(getResources().getColor(R.color.white));
        }
    }
    @Override
    protected void initEvent() {
        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetTooPassActivity.this.finish();
            }
        });

        /**
         * 校验验证码
         */
        btnCheckCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNotEmpty(editCheckCode.getText().toString().trim())){
                    if (isNetworkConnected(ForgetTooPassActivity.this)){

                        showProgressDialog("");
                        ManagerRequest.getInstance().validateCode(phone, editCheckCode.getText().toString().trim(), new ManagerRequest.IDataCallBack() {
                            @Override
                            public void onNetError(String msg) {

                                BreathApplication.toast(ForgetTooPassActivity.this,msg);
                            }

                            @Override
                            public void onSuccess(String msg) {
                                stopTimer();
                                Intent intent = new Intent() ;
                                Bundle bundle = new Bundle() ;
                                bundle.putString("phone",phone);
                                intent.putExtras(bundle) ;
                                intent.setClass(ForgetTooPassActivity.this, SettingPassActivity.class) ;
                                startActivity(intent);
                                ForgetTooPassActivity.this.finish();
                            }

                            @Override
                            public void onFail(String msg) {
                                BreathApplication.toast(ForgetTooPassActivity.this,msg);
                            }

                            @Override
                            public void onComplete() {
                                hideProgress();
                            }
                        });
                    }else {
                        BreathApplication.toast(ForgetTooPassActivity.this, getString(R.string.string_error_net));
                    }
                }
            }
        });
        /**
         * 获取验证码
         */
        btnGetCheckCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog("");
                ManagerRequest.getInstance().sendPhoneCode(phone, new ManagerRequest.IDataCallBack() {
                    @Override
                    public void onNetError(String msg) {

                        btnGetCheckCode.setEnabled(true);
                        BreathApplication.toast(ForgetTooPassActivity.this,msg);
                    }

                    @Override
                    public void onSuccess(String msg) {
                        startTimer();
                        btnGetCheckCode.setEnabled(false);
                    }

                    @Override
                    public void onFail(String msg) {
                        btnGetCheckCode.setEnabled(true);
                        BreathApplication.toast(ForgetTooPassActivity.this,msg);
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
            }
        });
    }




    protected void startTimer(){
        if (mTimer ==null){
            mTimer = new Timer() ;
        }
        if (mTimerTask == null){
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mForgetHandler.sendEmptyMessage(23) ;
                }
            } ;
        }
        if (mTimer!=null && mTimerTask!=null){
            mTimer.schedule(mTimerTask,1000,1000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    private void stopTimer() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
