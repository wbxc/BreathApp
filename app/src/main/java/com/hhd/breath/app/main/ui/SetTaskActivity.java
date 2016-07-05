package com.hhd.breath.app.main.ui;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.R;
import com.hhd.breath.app.imp.ConnectInterface;
import com.hhd.breath.app.wchusbdriver.CH340AndroidDriver;
import com.hhd.breath.app.widget.CommonProgressBar;
import com.hhd.breath.app.widget.DynamicWave;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class SetTaskActivity extends BaseActivity {

    private Timer mTimer = null;
    private TimerTask mTimerTask;

    private int current = 0;
    private CommonProgressBar mRoundLinerLayoutBar;
    private DynamicWave dyWave_layout;
    private boolean flag = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    if (!flag) {
                        if (current < 300) {
                            current++;
                            dyWave_layout.addProgress(current * 5);
                        } else {
                            flag = true;
                        }
                    } else {
                        if (current > 0) {
                            current--;
                            dyWave_layout.addProgress(current * 5);
                        } else {
                            flag = false;
                        }
                    }
                    break;
                case 2:
                    if (actualNumBytes != 0x00) {
                        tvShowData.append(new String(readBuffer, 0, actualNumBytes));
                        actualNumBytes = 0;
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_task);

        initView();
        initEvent();
        initCh340Data();
    }

    private Button btnSend;
    private EditText editSendData;
    private TextView tvShowData;

    @Override
    protected void initView() {
        btnSend = (Button) findViewById(R.id.btn_send);
        editSendData = (EditText) findViewById(R.id.edit_send);
        tvShowData = (TextView) findViewById(R.id.tv_show);
    }

    @Override
    protected void initEvent() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count_int;
                int NumBytes = 0;
                int mLen = 0;

                if (editSendData.length() != 0) {
                    NumBytes = editSendData.length();
                    for (count_int = 0; count_int < NumBytes; count_int++) {

                        writeBuffer[count_int] = (byte) editSendData.getText().charAt(count_int);
                    }
                }
                try {
                    mLen = mCh340Driver.WriteData(writeBuffer, NumBytes);
                } catch (IOException e1) {
                    Toast.makeText(SetTaskActivity.this, "WriteDataError mm" + e1.getMessage(), Toast.LENGTH_SHORT).show();
                }

                if (NumBytes != mLen) {
                    Toast.makeText(SetTaskActivity.this, "WriteData Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private CH340AndroidDriver mCh340Driver;
    private boolean READ_ENABLE_340 = false;
    private Object ThreadLock = new Object();
    private byte[] writeBuffer;
    private byte[] readBuffer;
    private int actualNumBytes;
    private readThread_340 handlerThread_340;

    private int baudRate;
    private byte dataBit;
    private byte stopBit;
    private byte parity;
    private byte flowControl;

    private void initCh340Data() {

        baudRate = 115200;
        dataBit = 8;
        stopBit = 1;
        parity = 0;
        flowControl = 0;


        writeBuffer = new byte[512];
        readBuffer = new byte[512];

        mCh340Driver = new CH340AndroidDriver((UsbManager) getSystemService(Context.USB_SERVICE), this,
                CommonValues.ACTION_USB_PERMISSION);
        mCh340Driver.setConnectInterface(new ConnectInterface() {
            @Override
            public void errorConnect() {

            }

            @Override
            public void rightConnect(String data) {

            }
        });

        if (READ_ENABLE_340 == false) {
            READ_ENABLE_340 = true;
            handlerThread_340 = new readThread_340(mHandler);
            handlerThread_340.start();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (2 == mCh340Driver.ResumeUsbList()) {
            mCh340Driver.CloseDevice();

            Toast.makeText(SetTaskActivity.this, "111初始化失败", Toast.LENGTH_SHORT).show();
        }


        if (mCh340Driver.isConnected()) {
            boolean flag = mCh340Driver.UartInit();

            if (flag) {
                mCh340Driver.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
            } else {
                Toast.makeText(SetTaskActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(SetTaskActivity.this, "链接失败", Toast.LENGTH_SHORT).show();
        }

    }

    /* usb input data handler */
    private class readThread_340 extends Thread {
        Handler mHandler;

        /* constructor */
        Handler mhandler;

        readThread_340(Handler h) {
            this.mhandler = h;
            this.setPriority(Thread.MIN_PRIORITY);
        }

        public void run() {
            while (READ_ENABLE_340) {
                Message msg = mhandler.obtainMessage();
                msg.what = 2;
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
                synchronized (ThreadLock) {
                    if (mCh340Driver != null) {
                        actualNumBytes = mCh340Driver.ReadData(readBuffer, 64);

                        if (actualNumBytes > 0) {
                            mhandler.sendMessage(msg);
                        }
                    }
                }
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (READ_ENABLE_340 == true) {
            READ_ENABLE_340 = false;
        }
    }

    @Override
    protected void onDestroy() {

        if (mCh340Driver != null) {
            if (mCh340Driver.isConnected()) {
                mCh340Driver.CloseDevice();
            }
            mCh340Driver = null;
        }

        super.onDestroy();
    }
}
