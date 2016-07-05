package com.hhd.breath.app.main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.R;
import com.hhd.breath.app.service.GlobalUsbService;

/**
 * Created by familylove on 2015/11/23.
 */
public class LogoActivity extends BaseActivity {


    private ImageView mImageView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        CommonValues.APP_IS_ACTIVE = true ;
        CommonValues.BREATH_IS_ACTIVE = false ;
        initView();
        initEvent();

        Intent intent = new Intent() ;
        intent.setClass(LogoActivity.this, GlobalUsbService.class) ;
        startService(intent) ;
    }


    @Override
    protected void initEvent() {
        AlphaAnimation mAlphaAnimation = new AlphaAnimation(0.2f,1.0f) ;
        mAlphaAnimation.setDuration(2000);
        mAlphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                startAction();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mImageView.startAnimation(mAlphaAnimation);
    }

    private void startAction(){
        Intent mIntent = new Intent() ;
        mIntent.setClass(LogoActivity.this,LoginBreathActivity.class) ;
        startActivity(mIntent);
        this.finish();
    }
    @Override
    protected void initView() {
        mImageView = (ImageView)findViewById(R.id.loadingImage) ;
    }

}
