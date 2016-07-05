package com.hhd.breath.app.main.ui;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.R;

public class AboutUsActivity extends BaseActivity {


    private TextView topTextView ;
    private RelativeLayout backRe ;
    private TextView mTextVersion ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initView();
        initEvent();
    }

    @Override
    protected void initView() {

        topTextView = (TextView)findViewById(R.id.topText) ;
        backRe = (RelativeLayout)findViewById(R.id.back_re) ;
        mTextVersion = (TextView)findViewById(R.id.text_version) ;

    }

    @Override
    protected void initEvent() {
        topTextView.setText(getResources().getString(R.string.string_about_us_top));
        mTextVersion.setText(getVersion(AboutUsActivity.this));

        backRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutUsActivity.this.finish();
            }
        });
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    private String getVersion(Context mActivity) {
        try {
            PackageManager manager = mActivity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mActivity.getPackageName(), 0);
            String version = "Version "+info.versionName;
            return  version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Version" ;
    }
    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;

            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }



}
