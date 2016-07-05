package com.hhd.breath.app.tab.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.R;
import com.hhd.breath.app.andengine.BreathAndEngine;
import com.hhd.breath.app.main.ui.BreathHealthActivity;
import com.hhd.breath.app.main.ui.BreathTrainActivity;
import com.hhd.breath.app.main.ui.TrainReportActivity;
import com.hhd.breath.app.model.BreathTrainingResult;
import com.hhd.breath.app.net.ThreadPoolWrap;
import com.hhd.breath.app.net.UploadRecordData;
import com.hhd.breath.app.service.TransmitDataDriver;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.utils.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author weibin
 */
public class TrainTabActivity extends BaseActivity {

    @Bind(R.id.breath_chuji)
    RelativeLayout breath_chuji;
    @Bind(R.id.breath_zhongji)
    RelativeLayout breath_zhongji;

    @Bind(R.id.layoutHealthTest)
    RelativeLayout layoutHealthTest ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_tab);
        ButterKnife.bind(this);
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonValues.select_activity = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {

        breath_chuji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNotEmpty(ShareUtils.getSerialNumber(TrainTabActivity.this))){

                    BreathTrainActivity.actionStart(TrainTabActivity.this, "缩唇呼吸训练", String.valueOf(3), String.valueOf(102),true);
                }else {
                    BreathTrainActivity.actionStart(TrainTabActivity.this, "缩唇呼吸训练", String.valueOf(3), String.valueOf(102),false);
                }

            }
        });

        breath_zhongji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int actionGroupNumber = ShareUtils.getActionGroup(TrainTabActivity.this);
                int sum = ShareUtils.getBrathTime(TrainTabActivity.this)
                        + ShareUtils.getIntervalTime(TrainTabActivity.this);
                int timeLong = sum * ShareUtils.getActionGroup(TrainTabActivity.this);


                Intent intent = new Intent() ;
                Bundle bundle = new Bundle() ;
                bundle.putInt("timeLong",timeLong);
                bundle.putString("level","2");
                bundle.putInt("groupNumbers",actionGroupNumber);
                intent.putExtras(bundle) ;
                intent.setClass(TrainTabActivity.this, BreathAndEngine.class) ;
                startActivity(intent);
            }
        });
        layoutHealthTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent() ;
                intent.setClass(TrainTabActivity.this, BreathHealthActivity.class) ;
                startActivity(intent);
            }
        });
    }


    private void writeFileToSD(String fileName ,String content) {
        String sdStatus = Environment.getExternalStorageState();
        if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        try {
            String pathName="/sdcard/test/";
            File path = new File(pathName);
            File file = new File(pathName + fileName);
            if( !path.exists()) {
                path.mkdir();
            }
            if( !file.exists()) {
                file.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(file);
            byte[] buf = content.getBytes();
            stream.write(buf);
            stream.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }





}
