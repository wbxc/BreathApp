package com.hhd.breath.app.main.ui;

import android.content.Intent;
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
import com.hhd.breath.app.net.ManagerRequest;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BreathRegisterFirst extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.topText)
    TextView tvTop ;
    @Bind(R.id.back_re)
    RelativeLayout layoutBack ;

    @Bind(R.id.edtPhone)
    EditText edtPhone ;
    @Bind(R.id.edtPassword)
    EditText edtPassword ;
    @Bind(R.id.edtOkPassword)
    EditText edtOkPassword ;
    @Bind(R.id.btnNext)
    Button btnNext ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath_register_first);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    @Override
    protected void initView() {
        tvTop.setText(getResources().getString(R.string.string_register));
    }

    @Override
    protected void initEvent() {

        layoutBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back_re:
                BreathRegisterFirst.this.finish();
                break;
            case R.id.btnNext:
                next();
                break;
        }
    }

    private void next(){

        final String phone = edtPhone.getText().toString().trim() ;
        final String password = edtPassword.getText().toString().trim() ;
        String okPassword = edtOkPassword.getText().toString().trim() ;

        if (isNotEmpty(phone)){
            if (!isNumberPhone(phone)){
                BreathApplication.toast(BreathRegisterFirst.this,getResources().getString(R.string.string_phone_type_error));
                return;
            }
            if (isNotEmpty(password)){
                if (okPassword!=null && okPassword.equals(password)){

                    showProgressDialog("");
                    ManagerRequest.getInstance().checkMobile(new ManagerRequest.IOnCallBack() {
                        @Override
                        public void onNetError(String msg) {

                            Toast.makeText(BreathRegisterFirst.this,"网路错误",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess() {

                            Intent intent = new Intent() ;
                            intent.setClass(BreathRegisterFirst.this,BreathRegisterSecond.class) ;
                            Bundle bundle = new Bundle() ;
                            bundle.putString("password",password);
                            bundle.putString("phone",phone);
                            intent.putExtras(bundle) ;
                            startActivity(intent);
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(BreathRegisterFirst.this,"已经注册了",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            hideProgress();
                        }
                    },phone);
                }else {
                    BreathApplication.toast(BreathRegisterFirst.this,getResources().getString(R.string.string_pass_no_same));
                   /* edtOkPassword.setText("");
                    edtPassword.setText("");*/
                }
            }else {
                Toast.makeText(BreathRegisterFirst.this,"密码不能为空",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(BreathRegisterFirst.this,"手机不能为空",Toast.LENGTH_SHORT).show();
            //edtPassword.setText("");
        }
    }
}
