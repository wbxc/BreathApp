package com.hhd.breath.app.main.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.R;
import com.hhd.breath.app.utils.ShareUtils;

public class ModifyNameActivity extends BaseActivity {

    private TextView mTextTop ;
    private RelativeLayout mLayoutBackRe ;
    private RelativeLayout mLayoutRight ;
    private EditText mEditName  ;
    private InputMethodManager imm ;

    private String userName ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_name);
        userName = getIntent().getExtras().getString("userName") ;
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        initEvent();
    }

    @Override
    protected void initView() {
        mTextTop = (TextView)findViewById(R.id.topText) ;
        mLayoutBackRe = (RelativeLayout)findViewById(R.id.back_re) ;
        mLayoutRight = (RelativeLayout)findViewById(R.id.layout_right) ;
        mEditName = (EditText)findViewById(R.id.edit_username) ;
        mLayoutRight.setVisibility(View.VISIBLE);
    }

    public static void actionActivity(Activity mActivity , String userName){

        Bundle mBundle = new Bundle() ;
        mBundle.putString("userName",userName);
        Intent mIntent = new Intent() ;
        mIntent.setClass(mActivity,ModifyNameActivity.class) ;
        mIntent.putExtras(mBundle) ;
        mActivity.startActivity(mIntent);
    }

    @Override
    protected void initEvent() {
        mTextTop.setText("修改用户名");
        mEditName.setText(userName);
        mLayoutBackRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(mEditName.getWindowToken(), 0); //强制隐藏键盘
                }
                ModifyNameActivity.this.finish();
            }
        });

        mLayoutRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(mEditName.getWindowToken(), 0); //强制隐藏键盘
                }
                ShareUtils.setUserName(ModifyNameActivity.this,mEditName.getText().toString().trim());

                ModifyNameActivity.this.finish();
            }
        });
    }
}
