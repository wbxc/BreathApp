package com.hhd.breath.app.main.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.R;
import com.hhd.breath.app.net.ManagerRequest;
import com.hhd.breath.app.utils.ShareUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BreathRegisterSecond extends BaseActivity  implements View.OnClickListener{

    @Bind(R.id.tvShowPhone)
    TextView tvShowPhone ;
    @Bind(R.id.editCheckCode)
    EditText editCheckCode ;
    @Bind(R.id.btnGetCheckCode)
    Button btnGetCheckCode ;
    @Bind(R.id.btnCommit)
    Button btnCommit ;
    private String phone ;
    private String password ;
    @Bind(R.id.back_re)
    RelativeLayout back_re ;
    @Bind(R.id.topText)
    TextView topTextView ;
    private int sumTime ;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            sumTime++ ;
            if (sumTime < 180){
                btnGetCheckCode.setText("重新发送"+(180-sumTime));
                btnGetCheckCode.setTextColor(getResources().getColor(R.color.common_color_878787));
            }else {
                stopTimer();
                sumTime = 0 ;
                btnGetCheckCode.setEnabled(true);
                btnGetCheckCode.setText("获取验证码");
                btnGetCheckCode.setTextColor(getResources().getColor(R.color.white));
            }


        }
    }  ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath_register_second);
        ButterKnife.bind(this);
        phone = getIntent().getExtras().getString("phone") ;
        password = getIntent().getExtras().getString("password") ;
        initView();
        initEvent();
    }

    @Override
    protected void initView() {
        topTextView.setText(getResources().getString(R.string.string_register));
    }

    @Override
    protected void initEvent() {

        btnCommit.setOnClickListener(this);
        btnGetCheckCode.setOnClickListener(this);
        back_re.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnCommit:
                register();
                break;
            case R.id.btnGetCheckCode:  // 发送验证码
                btnGetCheckCode();
                break;
            case R.id.back_re:
                BreathRegisterSecond.this.finish();
                break;
        }
    }

    private Timer mTimer ;
    private TimerTask mTimerTask ;


    protected void startTimer(){
        if (mTimer ==null){
            mTimer = new Timer() ;
        }
        if (mTimerTask == null){
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(12) ;
                }
            } ;
        }
        if (mTimer!=null && mTimerTask!=null){
            mTimer.schedule(mTimerTask,1000,1000);
        }
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




    private void register(){

        String code = editCheckCode.getText().toString().trim() ;

        if (isNotEmpty(code)){
            showProgressDialog("");
            ManagerRequest.getInstance().register(phone, password, code, new ManagerRequest.IDataCallBack() {
                @Override
                public void onNetError(String msg) {
                    Log.e("onNetError",msg) ;

                }

                @Override
                public void onSuccess(String msg) {

                    ShareUtils.setUserId(BreathRegisterSecond.this,msg);

                    Intent intent = new Intent() ;
                    intent.setClass(BreathRegisterSecond.this,PerUserInfoActivity.class) ;
                    startActivity(intent);
                    BreathRegisterSecond.this.finish();
                }

                @Override
                public void onFail(String msg) {

                    if (isNotEmpty(msg)){
                        Toast.makeText(BreathRegisterSecond.this,msg,Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onComplete() {
                    hideProgress();
                }
            });
        }else {
            Toast.makeText(BreathRegisterSecond.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
        }

    }
    private void btnGetCheckCode(){

        showProgressDialog("");
        ManagerRequest.getInstance().sendPhoneCode(phone, new ManagerRequest.IOnCallBack() {
            @Override
            public void onNetError(String msg) {

                Toast.makeText(BreathRegisterSecond.this,"网络连接错误",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {

                btnGetCheckCode.setEnabled(false);
                startTimer();
                tvShowPhone.setVisibility(View.VISIBLE);
                tvShowPhone.setText("验证码发送至:"+phone);
            }

            @Override
            public void onFail() {

                Toast.makeText(BreathRegisterSecond.this,"验证码获取失败",Toast.LENGTH_SHORT).show() ;
            }

            @Override
            public void onComplete() {
                hideProgress();
            }
        });
    }
}
