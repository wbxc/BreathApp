package com.hhd.breath.app.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.db.TrainUnitService;
import com.hhd.breath.app.model.RecordUnitData;
import com.hhd.breath.app.net.UploadRecordData;

public class NetService extends Service {

    private UploadNotifyReceiver uploadNotifyReceiver = null;

    private UploadRecordData uploadRecordData = null;
    protected MyBinder myBinder  = new MyBinder() ;

    public NetService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return myBinder ;
    }

    protected  class MyBinder extends Binder{

        public void addUploadData(RecordUnitData recordUnitData){
            //uploadRecordData.recordUnitDatas.add(recordUnitData) ;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        uploadNotifyReceiver = new UploadNotifyReceiver() ;
        uploadRecordData = UploadRecordData.getInstance() ;
        IntentFilter intentFilter = new IntentFilter() ;
        intentFilter.addAction(CommonValues.UPLOAD_RECORD_DATA_SUCCESS);
        intentFilter.addAction(CommonValues.UPLOAD_RECORD_DATA_FAIL);
        registerReceiver(uploadNotifyReceiver,intentFilter) ;
       /* UploadRecordData.recordUnitDatas = TrainUnitService.getInstance(getApplication()).findUnUploads() ;
        UploadRecordData.getInstance().setOnUploadProcessListener(new UploadRecordData.OnUploadProcessListener() {
            @Override
            public void onUploadDone(int responseCode, String message) {

                UploadRecordData.recordUnitDatas.remove(0) ;
                //
                mHandler.sendEmptyMessage(1) ;
            }

            @Override
            public void onUploadProcess(int uploadSize) {

            }

            @Override
            public void initUpload(int fileSize) {

            }
        });*/
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mHandler.sendEmptyMessage(1) ;
        return START_STICKY;
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:

                    break;
            }
        }
    }  ;
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(uploadNotifyReceiver);
    }


    //上传数据经停
    private class UploadNotifyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
