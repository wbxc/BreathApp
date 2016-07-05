package com.hhd.breath.app.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.widget.Toast;

import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.model.BreathTrainingResult;
import com.hhd.breath.app.net.NetConfig;
import com.hhd.breath.app.net.ThreadPoolWrap;
import com.hhd.breath.app.net.UploadRecordData;
import com.hhd.breath.app.utils.FileUtils;
import com.hhd.breath.app.utils.MD5Util;
import com.hhd.breath.app.utils.ZipUtil;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class UploadDataService extends Service {
    BreathTrainingResult breathTrainingResult =null ;
    private String receiver_success = "com.hhd.breath.upload_success" ;
    private String receiver_fail = "com.hhd.breath.upload.fail" ;
    /***
     * 请求使用多长时间
     */
    private String  filepath ;
    private String  file_zip_path  ;


    public UploadDataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter() ;
        intentFilter.addAction(receiver_success);
        intentFilter.addAction(receiver_fail);
        registerReceiver(uploadReceiver,intentFilter) ;

    }



    @Override
    public void onStart(final Intent intent, int startId) {
        super.onStart(intent, startId);
        breathTrainingResult= (BreathTrainingResult) intent.getExtras().getSerializable("breath_result") ;
        filepath =CommonValues.PATH_ZIP+breathTrainingResult.getUser_id()+"/"+breathTrainingResult.getFname();
        file_zip_path = CommonValues.PATH_ZIP+breathTrainingResult.getUser_id()+"/"+breathTrainingResult.getFname()+"_zip" ;

        UploadRecordData.getInstance().setOnUploadProcessListener(new UploadRecordData.OnUploadProcessListener() {
            @Override
            public void onUploadDone(int responseCode, String message) {

               // Log.e("onUploadDone", message) ;
                try {
                    JSONObject mesJsonObject = new JSONObject(message) ;
                    if (mesJsonObject.has("code") && mesJsonObject.getString("code").equals("200")){
                        Intent receiverIntent = new Intent() ;
                        Bundle bundle = new Bundle() ;
                        receiverIntent.setAction(receiver_success) ;
                        bundle.putString("message",message);
                        bundle.putInt("responseCode",responseCode);
                        receiverIntent.putExtras(bundle) ;
                        sendBroadcast(receiverIntent);
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onUploadProcess(int uploadSize) {

            }

            @Override
            public void initUpload(int fileSize) {

            }
        });
        UploadRecordData.getInstance().uploadRecordData(breathTrainingResult);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private BroadcastReceiver uploadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int code = intent.getExtras().getInt("responseCode") ;
            //String message = intent.getExtras().getString("message") ;
            switch (code){
                case UploadRecordData.UPLOAD_SUCCESS_CODE:
                    FileUtils.deleteFolder(filepath) ;
                    FileUtils.deleteFolder(file_zip_path) ;
                    break;
                case UploadRecordData.UPLOAD_FILE_NOT_EXISTS_CODE:
                    break;
                case UploadRecordData.UPLOAD_SERVER_ERROR_CODE:
                    break;
            }
        }
    } ;

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(uploadReceiver);
    }
}
