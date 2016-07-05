package com.hhd.breath.app.tab.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.R;
import com.hhd.breath.app.main.ui.AboutUsActivity;
import com.hhd.breath.app.main.ui.ComQuestionActivity;
import com.hhd.breath.app.main.ui.SystemSettingActivity;
import com.hhd.breath.app.main.ui.UserDetailsActivity;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.utils.StringUtils;
import com.hhd.breath.app.widget.CircularImage;
import com.umeng.fb.FeedbackAgent;
import com.umeng.message.PushAgent;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MeTabActivity extends BaseActivity implements View.OnClickListener {

    protected TextView mTopTextView ;
    private RelativeLayout commonQuestion ;
    private RelativeLayout feedSuggestion ;
    private RelativeLayout aboutUs ;
    private CircularImage imgUserAvatar ;
    private TextView mTvPhoneNumber ;
    private TextView mTvName ;
    private FeedbackAgent fb ;

    @Bind(R.id.me_userinfo) RelativeLayout userInfo ;
    @Bind(R.id.me_systemsSetting) RelativeLayout systemSetting ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_me_tab);
        ButterKnife.bind(this);
        initView();
        initEvent();
        setUpUmengFeedback();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonValues.select_activity = 3 ;
        mTopTextView.setText("我");
        File mFile = new File(Environment.getExternalStorageDirectory().toString() + "/hyTriage/touxiang.jpg") ;
        if (mFile.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + "/hyTriage/touxiang.jpg");
            imgUserAvatar.setImageBitmap(bm);
        }
    }

    @Override
    protected void initView() {
        imgUserAvatar = (CircularImage)findViewById(R.id.img_UserAvatar) ;
        mTopTextView = (TextView)findViewById(R.id.topText) ;
        userInfo.setOnClickListener(this);
        systemSetting.setOnClickListener(this);
        commonQuestion = (RelativeLayout)findViewById(R.id.me_commonQuestion) ;
        commonQuestion.setOnClickListener(this);
        feedSuggestion = (RelativeLayout)findViewById(R.id.me_feedSuggestion) ;
        feedSuggestion.setOnClickListener(this);
        aboutUs = (RelativeLayout)findViewById(R.id.me_aboutUS) ;
        aboutUs.setOnClickListener(this) ;
        mTvPhoneNumber = (TextView)findViewById(R.id.telephone_number) ;
        mTvName = (TextView)findViewById(R.id.tv_username) ;

    }

    @Override
    protected void initEvent() {

        if (StringUtils.isNotEmpty(ShareUtils.getUserPhone(MeTabActivity.this))){
            mTvPhoneNumber.setText(ShareUtils.getUserPhone(MeTabActivity.this));
        }

        if (StringUtils.isNotEmpty(ShareUtils.getUserName(MeTabActivity.this))){
            mTvName.setText(ShareUtils.getUserName(MeTabActivity.this));
        }

    }

    private void setUpUmengFeedback() {
        fb = new FeedbackAgent(this);
        // check if the app developer has replied to the feedback or not.
        fb.sync();
        fb.openAudioFeedback();
        fb.openFeedbackPush();

        PushAgent.getInstance(this).setDebugMode(true);
        PushAgent.getInstance(this).enable();

        //fb.setWelcomeInfo();
        //  fb.setWelcomeInfo("Welcome to use umeng feedback app");
//        FeedbackPush.getInstance(this).init(true);
//        PushAgent.getInstance(this).setPushIntentServiceClass(MyPushIntentService.class);


        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = fb.updateUserInfo();
            }
        }).start();
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.me_aboutUS:
                startAction(AboutUsActivity.class);
                break;
            case R.id.me_commonQuestion:
                startAction(ComQuestionActivity.class);
                break;
            case R.id.me_feedSuggestion:
                //startAction(FeedBackActivity.class);
                new FeedbackAgent(MeTabActivity.this).startFeedbackActivity();
                // fb.startFeedbackActivity();
                break;
            case R.id.me_userinfo:
                startAction(UserDetailsActivity.class);
                break;
            case R.id.me_systemsSetting:
                startAction(SystemSettingActivity.class);
                break;
        }
    }
    public void startAction(Class mClass){
        Intent mIntent = new Intent() ;
        mIntent.setClass(MeTabActivity.this,mClass) ;
        startActivity(mIntent);
    }
}
