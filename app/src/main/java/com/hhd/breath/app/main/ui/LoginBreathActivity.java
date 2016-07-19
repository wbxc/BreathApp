package com.hhd.breath.app.main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.BreathApplication;
import com.hhd.breath.app.R;
import com.hhd.breath.app.model.BreathDataUser;
import com.hhd.breath.app.model.BreathSuccessUser;
import com.hhd.breath.app.net.ManagerRequest;
import com.hhd.breath.app.net.NetConfig;
import com.hhd.breath.app.utils.ActivityCollector;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.utils.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginBreathActivity extends BaseActivity {

    private TextView topTextView;
    private EditText mEditTextPhone;
    private EditText mEditTextPassword;
    private Button mLoginButton;
    private RelativeLayout layoutDeleteName;
    private RelativeLayout layoutRegister;
    private RelativeLayout layoutForgetPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (StringUtils.isNotEmpty(ShareUtils.getUserPhone(LoginBreathActivity.this))
                && StringUtils.isNotEmpty(ShareUtils.getUserPassword(LoginBreathActivity.this))) {
            MainTabHomeActivity.actionStart(LoginBreathActivity.this);
            finish();
            return;
        }
        setContentView(R.layout.activity_login_breath);
        initView();
        initEvent();
    }

    @Override
    protected void initView() {
        topTextView = (TextView) findViewById(R.id.topText);
        mEditTextPhone = (EditText) findViewById(R.id.edit_phone);
        mEditTextPassword = (EditText) findViewById(R.id.edit_password);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        layoutDeleteName = (RelativeLayout) findViewById(R.id.layout_delete_name);
        layoutRegister = (RelativeLayout) findViewById(R.id.layout_register);
        layoutForgetPass = (RelativeLayout) findViewById(R.id.layout_forget_password);
    }

    @Override
    protected void initEvent() {

        layoutRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginBreathActivity.this, BreathRegisterFirst.class);
                startActivity(intent);
            }
        });
        layoutForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent() ;
                intent.setClass(LoginBreathActivity.this,ForgetPasswordActivity.class) ;
                startActivity(intent);
            }
        });

        topTextView.setText(getResources().getText(R.string.string_login));
        mEditTextPhone.setText(ShareUtils.getUserPhone(LoginBreathActivity.this));

        mEditTextPhone.addTextChangedListener(mTextWatcher);
        mEditTextPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    mEditTextPassword.findFocus();
                }
                return false;
            }
        });
        layoutDeleteName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditTextPhone.setText("");
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (StringUtils.isNotEmpty(mEditTextPhone.getText().toString().trim()) && StringUtils.isNotEmpty(mEditTextPassword.getText().toString().trim())) {
                    if (isNumberPhone(mEditTextPhone.getText().toString().trim())) {
                        showProgressDialog("登录中请稍后");
                        ManagerRequest.getInstance().getRequestNetApi().loginUser(mEditTextPhone.getText().toString().trim(),mEditTextPassword.getText().toString().trim()).enqueue(new Callback<BreathSuccessUser>() {
                            @Override
                            public void onResponse(Call<BreathSuccessUser> call, Response<BreathSuccessUser> response) {
                                hideProgress();
                                String code = response.body().getCode();
                                switch (Integer.parseInt(code)) {
                                    case NetConfig.SUCCESS_CODE:
                                        ShareUtils.setUserId(LoginBreathActivity.this, response.body().getData().getUser_id());
                                        ManagerRequest.getInstance().getRequestNetApi().getUserInfo(response.body().getData().getUser_id()).enqueue(new Callback<BreathSuccessUser>() {
                                            @Override
                                            public void onResponse(Call<BreathSuccessUser> call, Response<BreathSuccessUser> response) {
                                                hideProgress();
                                                String code = response.body().getCode();
                                                switch (Integer.parseInt(code)) {
                                                    case NetConfig.SUCCESS_CODE:
                                                        BreathDataUser breathDataUser = response.body().getData();
                                                        ShareUtils.setUserId(LoginBreathActivity.this, breathDataUser.getUser_id());
                                                        ShareUtils.setUserName(LoginBreathActivity.this, breathDataUser.getUser_fullname());
                                                        ShareUtils.setUserPhone(LoginBreathActivity.this, breathDataUser.getUser_name());
                                                        ShareUtils.setUserBirthday(LoginBreathActivity.this, breathDataUser.getUser_birthday());
                                                        ShareUtils.setUserPassword(LoginBreathActivity.this, breathDataUser.getUser_password());
                                                        ShareUtils.setUserSex(LoginBreathActivity.this, breathDataUser.getUser_sex().equals("0") ? "男" : "女");
                                                        MainTabHomeActivity.actionStart(LoginBreathActivity.this);
                                                        LoginBreathActivity.this.finish();
                                                        break;
                                                    case NetConfig.FAIL_CODE:
                                                        Toast.makeText(LoginBreathActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                                        break;
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<BreathSuccessUser> call, Throwable t) {
                                                hideProgress();
                                            }
                                        });
                                        break;
                                    default:
                                        BreathApplication.toast(LoginBreathActivity.this,response.body().getMsg());
                                        break;
                                }
                            }

                            @Override
                            public void onFailure(Call<BreathSuccessUser> call, Throwable t) {
                                hideProgress();
                                Toast.makeText(LoginBreathActivity.this, getResources().getString(R.string.string_user_login_error), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(LoginBreathActivity.this, getResources().getString(R.string.string_login_error), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.length() > 0) {
                layoutDeleteName.setVisibility(View.VISIBLE);
            } else {
                layoutDeleteName.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (mEditTextPhone.getText().toString().trim() != null && !"".equals(mEditTextPhone.getText().toString().trim())) {

            layoutDeleteName.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCollector.finshAll();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
