package com.hhd.breath.app.main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.R;
import com.hhd.breath.app.net.HttpUtil;
import com.hhd.breath.app.net.NetConfig;
import com.hhd.breath.app.net.ThreadPoolWrap;
import com.hhd.breath.app.utils.ShareUtils;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterBreathActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.back_re) RelativeLayout backLayout ;
    @Bind(R.id.topText) TextView topText ;
    @Bind(R.id.edit_username) EditText editUserName ;
    @Bind(R.id.edit_password) EditText editPassword ;

    @Bind(R.id.edit_verification) EditText editVerification ;
    @Bind(R.id.btn_verification) Button btnVerification ;
    @Bind(R.id.layout_register) RelativeLayout layoutRegister ;


    private String userName ;
    private String password ;
    private String getCode ;
    private String realName ;
    private Timer mTimer = null ;
    private TimerTask mTimerTask = null;
    private int sumTime = 0 ;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NetConfig.HANDLER_SUCCESS:
                    startTimer();
                    break;
                case NetConfig.HANDLER_FAIL:
                    btnVerification.setEnabled(true);
                    Toast.makeText(RegisterBreathActivity.this,"校验码获取失败",Toast.LENGTH_SHORT).show();
                    break;
                case 12:
                    sumTime++ ;
                    if (sumTime < 180){
                        btnVerification.setText("重新发送"+(180-sumTime));
                        btnVerification.setTextColor(getResources().getColor(R.color.common_color_878787));
                    }else {
                        stopTimer();
                        sumTime = 0 ;
                        btnVerification.setEnabled(true);
                        btnVerification.setText("获取验证码");
                        btnVerification.setTextColor(getResources().getColor(R.color.white));
                    }
                    break;
                case 11:
                    btnVerification.setEnabled(true);
                    Toast.makeText(RegisterBreathActivity.this,"手机号已被注册",Toast.LENGTH_SHORT).show();
                    break;
                case 21:
                    Intent intent = new Intent() ;
                    intent.setClass(RegisterBreathActivity.this,PerUserInfoActivity.class) ;
                    startActivity(intent);
                    RegisterBreathActivity.this.finish();
                    break;
                case 22:
                    String result = (String) msg.obj ;
                    if (isNotEmpty(result)){
                        Toast.makeText(RegisterBreathActivity.this,result,Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(RegisterBreathActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    } ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_breath);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {

        topText.setText("注册");
        backLayout.setOnClickListener(this);
        layoutRegister.setOnClickListener(this);
        btnVerification.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_verification:
                userName = editUserName.getText().toString().trim() ;
                if (isNumberPhone(userName)){
                    btnVerification.setEnabled(false);
                    ThreadPoolWrap.getThreadPool().executeTask(getCodeRunnable);
                }else {
                    Toast.makeText(RegisterBreathActivity.this,"手机格式不正确",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.layout_register:
                if (!isNotEmpty(userName)){
                    userName = editUserName.getText().toString().trim() ;
                }
                password = editPassword.getText().toString().trim() ;
                getCode = editVerification.getText().toString().trim() ;

                if (isNotEmpty(userName)&&isNotEmpty(password)&&isNotEmpty(getCode)){

                    ThreadPoolWrap.getThreadPool().executeTask(registerRunnable);
                }else {
                    Toast.makeText(RegisterBreathActivity.this,"请将信息填写完成",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.back_re:
                RegisterBreathActivity.this.finish();
                break;
        }
    }


    /**
     * 校验手机号是否注册
     * 获取验证码
     */
    private Runnable getCodeRunnable = new Runnable() {
        @Override
        public void run() {

            Bundle bundle = new Bundle() ;
            bundle.putString("user_name",userName);
            try {
                JSONObject jsonObject =HttpUtil.getJSONByPost(NetConfig.URL_CHECK_PHONE,bundle,0) ;
                Log.e("jsonObject",jsonObject.toString()) ;


                String code = jsonObject.getString(NetConfig.CODE) ;
                if (code.equals("0")){
                    mHandler.sendEmptyMessage(11) ;
                }else if (code.equals("200")){
                    Bundle codeBundle = new Bundle() ;
                    codeBundle.putString("phone",userName);
                    JSONObject json1 = HttpUtil.getJSONByPost(NetConfig.URL_SEND_CODE,codeBundle,0)  ;
                    String code1 = json1.getString(NetConfig.CODE) ;
                    Log.e("jsonObject",json1.toString()+">>>") ;

                    if (code1.equals("200")){  // 验证码获取成功
                        mHandler.sendEmptyMessage(NetConfig.HANDLER_SUCCESS) ;
                    }else if (code1.equals("0")){
                        mHandler.sendEmptyMessage(NetConfig.HANDLER_FAIL) ;
                    }
                }
            }catch (Exception e){

            }
        }
    } ;

    /**
     * 注册
     */
    private Runnable registerRunnable = new Runnable() {
        @Override
        public void run() {
            Bundle registerBundle = new Bundle() ;
            registerBundle.putString("user_name",userName);
            registerBundle.putString("full_name",realName);
            registerBundle.putString("user_password",password);
            registerBundle.putString("code",getCode);

            Log.e("jsonObject",userName+":"+realName+":"+password+":"+getCode) ;
            try {
               JSONObject jsonObject = HttpUtil.getJSONByPost(NetConfig.URL_REGISTER_USER,registerBundle,30) ;
                if (jsonObject.has(NetConfig.CODE)){

                    // {"data":{"user_id":"14612921293140"},"code":"200"}
                    //{"data":"","code":"0"}
                    if (jsonObject.getString(NetConfig.CODE).equals("200")){  // (code=200：注册成功；code=0：注册失败)
                        JSONObject data = jsonObject.getJSONObject(NetConfig.DATA) ;
                        if (data.has("user_id")){
                            ShareUtils.setUserId(RegisterBreathActivity.this,data.getString("user_id"));
                            ShareUtils.setUserName(RegisterBreathActivity.this,realName);
                            ShareUtils.setUserPhone(RegisterBreathActivity.this,userName);
                        }
                        mHandler.sendEmptyMessage(21) ;
                    }else if (jsonObject.getString(NetConfig.CODE).equals("0")){
                        Message message = Message.obtain() ;
                        message.what = 22 ;
                        message.obj = jsonObject.toString() ;
                        mHandler.sendMessage(message) ;
                    }else if (jsonObject.getString(NetConfig.CODE).equals("100")){
                        Message message = Message.obtain() ;
                        message.what = 23 ;
                        message.obj = jsonObject.getString(NetConfig.MSG) ;
                        mHandler.sendMessage(message) ;
                    }else {

                    }
                }else {

                }
            }catch (Exception e){

            }
        }
    } ;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
        mHandler.removeMessages(NetConfig.HANDLER_SUCCESS);
        mHandler.removeMessages(NetConfig.HANDLER_FAIL);
    }

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
    protected void onPause() {
        super.onPause();

    }



}
