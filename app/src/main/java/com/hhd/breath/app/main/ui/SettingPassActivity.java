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

import java.io.BufferedReader;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingPassActivity extends BaseActivity {

    @Bind(R.id.back_re) RelativeLayout layoutBack ;
    @Bind(R.id.topText) TextView tvTop ;
    private String phone ;
    @Bind(R.id.editNewPass) EditText editNewPass ;
    @Bind(R.id.editOkPass) EditText editOkPass ;
    @Bind(R.id.btnSubmit) Button btnSubmit ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pass);
        ButterKnife.bind(this);
        phone = getIntent().getExtras().getString("phone") ;
        initView();
        initEvent();
    }

    @Override
    protected void initView() {
        tvTop.setText("忘记密码");
    }


    @Override
    protected void initEvent() {
        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingPassActivity.this.finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNotEmpty(editNewPass.getText().toString().trim()) && isNotEmpty(editOkPass.getText().toString().trim())){

                    if (editNewPass.getText().toString().trim().equals(editOkPass.getText().toString().trim())){

                        if (isNetworkConnected(SettingPassActivity.this)){
                            showProgressDialog("提交中,请稍后");
                            ManagerRequest.getInstance().forgetPassword(phone, editNewPass.getText().toString().trim(), new ManagerRequest.IDataCallBack() {
                                @Override
                                public void onNetError(String msg) {
                                    BreathApplication.toast(SettingPassActivity.this,msg);
                                }

                                @Override
                                public void onSuccess(String msg) {
                                    Intent intent = new Intent() ;
                                    intent.setClass(SettingPassActivity.this,LoginBreathActivity.class) ;
                                    startActivity(intent);
                                    SettingPassActivity.this.finish();
                                }

                                @Override
                                public void onFail(String msg) {
                                    BreathApplication.toast(SettingPassActivity.this,msg);
                                }

                                @Override
                                public void onComplete() {

                                    hideProgress();
                                }
                            });
                        }else {
                            BreathApplication.toast(SettingPassActivity.this, getString(R.string.string_error_net));
                        }
                    }else {
                        BreathApplication.toast(SettingPassActivity.this,getString(R.string.string_password_no_same));
                    }
                }else {
                    BreathApplication.toast(SettingPassActivity.this,getString(R.string.string_password_no_empty));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
