package com.hhd.breath.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hhd.breath.app.service.GlobalUsbService;

/**
 * 开机自启动
 */
public class LaunchBroadReceiver extends BroadcastReceiver {
    public LaunchBroadReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
       // if (intent.getAction().equals())
        Intent intentService = new Intent(context, GlobalUsbService.class) ;
        context.startService(intentService) ;
    }
}
