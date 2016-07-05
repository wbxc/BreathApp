package com.hhd.breath.app.main.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.R;

public class FeedBackActivity extends BaseActivity {

    private RelativeLayout mBackRelative ;
    private TextView mTopTextView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        initView();
        initEvent();
    }

    @Override
    protected void initView() {

        mBackRelative = (RelativeLayout)findViewById(R.id.back_re) ;
        mTopTextView = (TextView)findViewById(R.id.topText) ;
    }

    @Override
    protected void initEvent() {

        mBackRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedBackActivity.this.finish();
            }
        });

        mTopTextView.setText("意见反馈");
    }
}
