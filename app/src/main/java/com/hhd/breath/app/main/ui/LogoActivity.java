package com.hhd.breath.app.main.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.R;
import com.hhd.breath.app.service.GlobalUsbService;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.utils.permission.AfterPermissionGranted;
import com.hhd.breath.app.utils.permission.EasyPermissions;

import java.util.List;

/**
 * Created by familylove on 2015/11/23.
 */
public class LogoActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private static final int RC_LOCATION_CONTACTS_PERM = 124;
    private ImageView mImageView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ShareUtils.setIsLaunchFirst(this,false);
        setContentView(R.layout.activity_logo);
        CommonValues.APP_IS_ACTIVE = true ;
        CommonValues.BREATH_IS_ACTIVE = false ;
        initView();

        Intent intent = new Intent() ;
        intent.setClass(LogoActivity.this, GlobalUsbService.class) ;
        startService(intent) ;
        requestPermission();
    }

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    private void requestPermission(){
        String[] perms = { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE };
        if (EasyPermissions.hasPermissions(LogoActivity.this,perms)){
            initEvent();
        }else {
            EasyPermissions.requestPermissions(this, getString(R.string.string_permission_read_sdk), RC_LOCATION_CONTACTS_PERM, perms);
        }

    }
    @Override
    protected void initView() {
        mImageView = (ImageView)findViewById(R.id.loadingImage) ;
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

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        initEvent();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {


        Log.e("EasyPermissions","EasyPermissions>L>>>>"+requestCode) ;
        //checkDeniedPermissionsNeverAskAgain() ;
/*        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this,
                getString(R.string.rationale_ask_again),
                R.string.setting, R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initEvent();
                        dialog.cancel();
                    }
                }, perms);*/
        //initEvent();

        EasyPermissions.checkDeniedPermissionsSetting(this,getString(R.string.rationale_ask_again),
                R.string.setting, R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initEvent();
                        dialog.cancel();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initEvent();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void startAction(){
        Intent mIntent = new Intent() ;
        //mIntent.setClass(LogoActivity.this,LoginBreathActivity.class) ;
        if (!ShareUtils.getLaunchFirst(LogoActivity.this)){
            mIntent.setClass(LogoActivity.this,SplashActivity.class) ;
        }else {
            mIntent.setClass(LogoActivity.this,LoginBreathActivity.class) ;
        }
        startActivity(mIntent);
        this.finish();
    }

}
