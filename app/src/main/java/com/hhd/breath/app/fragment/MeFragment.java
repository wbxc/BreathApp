package com.hhd.breath.app.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhd.breath.app.R;
import com.hhd.breath.app.main.ui.AboutUsActivity;
import com.hhd.breath.app.main.ui.ComQuestionActivity;
import com.hhd.breath.app.main.ui.FeedBackActivity;
import com.hhd.breath.app.main.ui.SystemSettingActivity;
import com.hhd.breath.app.main.ui.UserInfoActivity;
import com.hhd.breath.app.widget.CircularImage;

import java.io.File;


public class MeFragment extends Fragment implements View.OnClickListener{



    protected TextView mTopTextView ;
    private RelativeLayout userInfo ;
    private RelativeLayout systemSetting ;
    private RelativeLayout commonQuestion ;
    private RelativeLayout feedSuggestion ;
    private RelativeLayout aboutUs ;
    private CircularImage imgUserAvatar ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View meView = inflater.inflate(R.layout.fragment_me, container, false) ;
        imgUserAvatar = (CircularImage)meView.findViewById(R.id.img_UserAvatar) ;
        mTopTextView = (TextView)meView.findViewById(R.id.topText) ;
        userInfo = (RelativeLayout)meView.findViewById(R.id.me_userinfo) ;
        userInfo.setOnClickListener(this);
        systemSetting = (RelativeLayout)meView.findViewById(R.id.me_systemsSetting) ;
        systemSetting.setOnClickListener(this);
        commonQuestion = (RelativeLayout)meView.findViewById(R.id.me_commonQuestion) ;
        commonQuestion.setOnClickListener(this);
        feedSuggestion = (RelativeLayout)meView.findViewById(R.id.me_feedSuggestion) ;
        feedSuggestion.setOnClickListener(this);
        aboutUs = (RelativeLayout)meView.findViewById(R.id.me_aboutUS) ;
        aboutUs.setOnClickListener(this);
        return meView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTopTextView.setText("我");

        File mFile = new File(Environment.getExternalStorageDirectory().toString() + "/hyTriage/touxiang.jpg") ;
        if (mFile.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + "/hyTriage/touxiang.jpg");
            imgUserAvatar.setImageBitmap(bm);
        }
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
                startAction(FeedBackActivity.class);
                break;
            case R.id.me_userinfo:
                startAction(UserInfoActivity.class);
                break;
            case R.id.me_systemsSetting:
                startAction(SystemSettingActivity.class);
                break;
        }
    }

    public void startAction(Class mClass){
        Intent mIntent = new Intent() ;
        mIntent.setClass(getActivity(),mClass) ;
        startActivity(mIntent);
    }
}
