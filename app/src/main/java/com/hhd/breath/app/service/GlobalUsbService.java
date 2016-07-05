package com.hhd.breath.app.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;

import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.main.ui.LogoActivity;
import com.hhd.breath.app.main.ui.MainTabHomeActivity;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.utils.Utils;
import com.hhd.breath.app.wchusbdriver.Global340Driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 定义全局的Service
 */
public class GlobalUsbService extends Service {
    private MonitorUsbReceiver monitorUsbReceiver = null;
    public static boolean isOpenBreath = false ;

    public GlobalUsbService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        monitorUsbReceiver = new MonitorUsbReceiver();
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(CommonValues.USB_CHECK_ACTION);
        mIntentFilter.addAction(CommonValues.USB_DEVICE_DETACHED);
        registerReceiver(monitorUsbReceiver, mIntentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(monitorUsbReceiver);
    }


    /**
     *Usb  监测接收广播
     */
    private class MonitorUsbReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(CommonValues.USB_CHECK_ACTION)) {  // USBcharule

                switch (checkUsbType(context)) {
                    case 1:
                        Intent nextIntent = new Intent();
                        nextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (CommonValues.APP_IS_ACTIVE) {
                            nextIntent.setClass(context, MainTabHomeActivity.class);
                            context.startActivity(nextIntent);
                        } else if (!CommonValues.APP_IS_ACTIVE) {
                            nextIntent.setClass(context, LogoActivity.class);
                            context.startActivity(nextIntent);
                        }
                        break;
                    case 2:
                        break;
                }
            } else if (intent.getAction().equals(CommonValues.USB_DEVICE_DETACHED)) {
                ShareUtils.setSerialNumber(context, "");
                isOpenBreath = false ;
                Toast.makeText(context,"设备已拔出",Toast.LENGTH_SHORT).show();
                Global340Driver.getInstance(context).close();
            }
        }
    }

    /**
     * 检查usb状态
     * @param context
     * 2 是没有usb
     * 1 有匹配的usb
     * 0 有usb 但是没有匹配
     * @return
     */
    private int checkUsbType(Context context) {
        ArrayList<String> DeviceNum = new ArrayList<String>();
        DeviceNum.add(CommonValues.USB_CODE340); //"1a86:7523"
        UsbManager mUsbManger = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = mUsbManger.getDeviceList();
        if (deviceList.isEmpty()) {
            return 2;
        }
       // int temp = 0;
        Iterator<UsbDevice> localIterator = deviceList.values().iterator();
        while (localIterator.hasNext()) {
            UsbDevice localUsbDevice = localIterator.next();
            for (int i = 0; i < DeviceNum.size(); i++) {
                if (String.format(
                        "%04x:%04x",
                        new Object[]{
                                Integer.valueOf(localUsbDevice.getVendorId()),
                                Integer.valueOf(localUsbDevice.getProductId())
                        }).equals(DeviceNum.get(i))) {
                   return 1 ;
                }
            }
        }
        return 2;
    }
}
