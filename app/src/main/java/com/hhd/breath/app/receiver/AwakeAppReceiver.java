package com.hhd.breath.app.receiver;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.main.ui.LogoActivity;
import com.hhd.breath.app.main.ui.MainTabHomeActivity;
import com.hhd.breath.app.service.TransmitDataDriver;
import com.hhd.breath.app.utils.ShareUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * APP唤醒
 */
public class AwakeAppReceiver extends BroadcastReceiver {

    public AwakeAppReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(CommonValues.ACTION_USB_STATUES_SUCCESS)){
            if (ResumeUsbList1(context) == 1){
                if (!CommonValues.APP_IS_ACTIVE && !CommonValues.BREATH_IS_ACTIVE){
                    Intent i = new Intent(context, LogoActivity.class) ;
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                    context.startActivity(i);
                }else if(CommonValues.APP_IS_ACTIVE && !CommonValues.BREATH_IS_ACTIVE){
                    Intent i = new Intent(context, MainTabHomeActivity.class) ;
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                    context.startActivity(i);
                    CommonValues.select_activity = 0 ;
                }else {
                }
                return;
            }
        }else if (intent.getAction().equals(CommonValues.USB_DEVICE_DETACHED)){
            ShareUtils.setSerialNumber(context,"");
        }
    }

    public int ResumeUsbList1(Context context) {
        ArrayList<String> DeviceNum = new ArrayList<String>();
        DeviceNum.add(CommonValues.USB_CODE340) ; //"1a86:7523"
        UsbManager mUsbmanager = (UsbManager)context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = mUsbmanager.getDeviceList();
        if (deviceList.isEmpty()) {
            return 2;
        }
        int temp= 0 ;
        Iterator<UsbDevice> localIterator = deviceList.values().iterator();
        while (localIterator.hasNext()) {
            UsbDevice localUsbDevice = localIterator.next();
            for (int i = 0; i < DeviceNum.size(); ++i) {
                if (String.format(
                        "%04x:%04x",
                        new Object[] {
                                Integer.valueOf(localUsbDevice.getVendorId()),
                                Integer.valueOf(localUsbDevice.getProductId())
                        }).equals(DeviceNum.get(i))) {
                    temp = 1 ;
                }
            }
        }
        return temp;
    }

}
