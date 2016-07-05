package com.hhd.breath.app.main.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.BoolRes;
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

import butterknife.Bind;
import butterknife.ButterKnife;

public class ForgetPasswordActivity extends BaseActivity {

    @Bind(R.id.back_re) RelativeLayout layoutBack ;
    @Bind(R.id.topText) TextView tvTop ;
    @Bind(R.id.edit_phone) EditText edtPhone  ;
    @Bind(R.id.btnGetCheckCode) Button btnGetCheckCode ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    @Override
    protected void initView() {
        tvTop.setText("忘记密码");
        btnGetCheckCode.setText("获取验证码");
    }

    @Override
    protected void initEvent() {
        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPasswordActivity.this.finish();
            }
        });
        btnGetCheckCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNotEmpty(edtPhone.getText().toString().trim())){
                    if (isNumberPhone(edtPhone.getText().toString().trim())){

                        if (isNetworkConnected(ForgetPasswordActivity.this)){
                            showProgressDialog("");

                            ManagerRequest.getInstance().sendPhoneCode(edtPhone.getText().toString().trim(), new ManagerRequest.IDataCallBack() {
                                @Override
                                public void onNetError(String msg) {
                                    BreathApplication.toast(ForgetPasswordActivity.this,msg);
                                }

                                @Override
                                public void onSuccess(String msg) {

                                    BreathApplication.toast(ForgetPasswordActivity.this,msg);
                                    Intent intent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("phone", edtPhone.getText().toString().trim());
                                    intent.setClass(ForgetPasswordActivity.this, ForgetTooPassActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    ForgetPasswordActivity.this.finish();
                                }

                                @Override
                                public void onFail(String msg) {
                                    BreathApplication.toast(ForgetPasswordActivity.this,msg);
                                }

                                @Override
                                public void onComplete() {
                                    hideProgress();
                                }
                            });
                        }else {
                            BreathApplication.toast(ForgetPasswordActivity.this, getString(R.string.string_error_net));
                        }
                    }else {
                        BreathApplication.toast(ForgetPasswordActivity.this, getString(R.string.string_error_phone));
                    }
                }else {
                    BreathApplication.toast(ForgetPasswordActivity.this, getString(R.string.string_error_phone_empty));
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
