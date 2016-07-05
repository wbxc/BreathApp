package com.hhd.breath.app.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.IBinder;

import com.hhd.breath.app.CommonValues;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class DetectionUsbService extends Service {

    private Timer mTimer;
    // private UsbManager mUsbManager ;
    private Intent mIntentBroadCaster;
    private Bundle mBundle;

    public DetectionUsbService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mIntentBroadCaster = new Intent();
        mBundle = new Bundle();
        // mUsbManager = (UsbManager)getApplication().getSystemService(Context.USB_SERVICE) ;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                switch (TransmitDataDriver.getInstance(getApplicationContext()).checkUsbStatus()) {
                    case 0:  // 没有设备
                        mBundle.putBoolean(CommonValues.ACTION_USB_STATUE_FLAG, false);
                        mIntentBroadCaster.putExtras(mBundle);
                        mIntentBroadCaster.setAction(CommonValues.ACTION_USB_DETECTION);
                        sendBroadcast(mIntentBroadCaster);
                        break;
                    case 1:  // 有设备 并获取了权限
                        mBundle.putBoolean(CommonValues.ACTION_USB_STATUE_FLAG, true);
                        mIntentBroadCaster.putExtras(mBundle);
                        mIntentBroadCaster.setAction(CommonValues.ACTION_USB_DETECTION);
                        sendBroadcast(mIntentBroadCaster);

                        break;
                    case 2: //有设备插入
                        mBundle.putBoolean(CommonValues.ACTION_USB_STATUE_FLAG, true);
                        mIntentBroadCaster.putExtras(mBundle);
                        mIntentBroadCaster.setAction(CommonValues.ACTION_USB_DETECTION);
                        sendBroadcast(mIntentBroadCaster);
                        break;
                }
            }
        }, 0, 2 * 100);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
