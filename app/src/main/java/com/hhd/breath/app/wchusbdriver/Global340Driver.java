package com.hhd.breath.app.wchusbdriver;

import android.app.PendingIntent;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Message;
import android.widget.Toast;

import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.andengine.BreathAndEngine;
import com.hhd.breath.app.imp.ConnectInterface;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.utils.Util;
import com.hhd.breath.app.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Administrator on 2016/6/22.
 */
public class Global340Driver {
    private static Global340Driver instance = null  ;
    private CH340AndroidDriver ch340AndroidDriver ;
    private PendingIntent mPendingIntent ;
    protected int baudRate;
    protected byte dataBit;
    protected byte stopBit;
    protected byte parity;
    protected byte flowControl;
    private int actualNumBytes = 0 ;
    private byte[] readBuffer = new byte[512] ;
    private ReadThread readThread ;
    private Context context ;
    private UsbManager usbManager ;
    private HashMap<String,UsbDevice> deviceList ;
    private ArrayList<String> DeviceNum = new ArrayList<String>();
    private int DeviceCount = 0 ;
    private UsbDevice localUsbDevice ;
    private Global340Driver(Context context1){
        baudRate = 115200;
        dataBit = 8;
        stopBit = 1;
        parity = 0;
        flowControl = 0;
        deviceList = new HashMap<String,UsbDevice>() ;
        readThread = new ReadThread(true) ;
        this.context = context1 ;
        usbManager = (UsbManager)context.getSystemService(Context.USB_SERVICE) ;
        ch340AndroidDriver = new CH340AndroidDriver(usbManager,context, CommonValues.ACTION_USB_PERMISSION) ;
        mPendingIntent = PendingIntent.getBroadcast(context,0,new Intent(CommonValues.ACTION_USB_PERMISSION),0) ;
        ch340AndroidDriver.setConnectInterface(connectInterface);

        ArrayAddDevice(CommonValues.USB_CODE340);
    }
    private void ArrayAddDevice(String str) {
        DeviceNum.add(str);
        DeviceCount = DeviceNum.size();
    }
    private ConnectInterface connectInterface = new ConnectInterface() {
        @Override
        public void errorConnect() {

        }

        @Override
        public void rightConnect(String data) {

        }
    } ;
    public static Global340Driver getInstance(Context context){
        if (instance ==null){
            instance = new Global340Driver(context) ;
            instance.readThread.start();
        }
        return instance ;
    }

    /**
     * 获取权限
     */
    public void getPermission(){
        if (isNotEmptyDevice() == 1){
            usbManager.requestPermission(localUsbDevice, mPendingIntent);
        }
    }



    /**
     * 判断设备是否为空
     * @return 1、表示有设备匹配
     * @return 2、没有呼吸训练设备
     */
    private int isNotEmptyDevice(){
        deviceList = usbManager.getDeviceList() ;
        if (!deviceList.isEmpty()){
            Iterator<UsbDevice> localIterator = deviceList.values().iterator() ;
            while (localIterator.hasNext()){
                UsbDevice localUsbDevice1 = localIterator.next() ;
                String deviceNum = String.format("%04x:%04x",
                        new Object[]{
                                Integer.valueOf(localUsbDevice1.getVendorId()),
                                Integer.valueOf(localUsbDevice1.getProductId())
                        }) ;
                for (int i=0 ; i<DeviceCount ; i++){
                    if (deviceNum.equals(DeviceNum.get(i))){
                        localUsbDevice = localUsbDevice1 ;
                        return 1 ;
                    }
                }
            }
            return 2 ;
        }else {
            return 2 ;
        }
    }


    /**
     * 获取状态
     * @return
     */
    public int  checkUsbStatus(){
        return ch340AndroidDriver.checkUsbStatus() ;
    }




    /**
     * 初始化Ch340AndroidDriver
     * @return
     */
    public int initDriver(){
        int flag = ch340AndroidDriver.ResumeUsbList() ;
        switch (flag){
            case 2:
                ch340AndroidDriver.CloseDevice();
                break;
            case 1:
                break;
            case 0:
                break;
        }
        return flag ;
    }

    /**
     * 初始化端口
     * @return
     */
    public boolean initUart(){
        boolean flag1 = ch340AndroidDriver.UartInit() ;
        if (flag1)
            ch340AndroidDriver.SetConfig(baudRate, dataBit, stopBit, parity, flowControl) ;
        return flag1 ;
    }

    public void setEnableRead(boolean isRead){
        instance.readThread.setReadFlag(isRead);
    }

    public String read(){
        if (actualNumBytes!=0){
            try {
                String result = new String(readBuffer, 0, actualNumBytes, "UTF-8");

                Utils.write1(result);
                if (result.length() >=7) {
                    return result.substring(0,3);
                }else {
                     return result.substring(0,3);
                }
            }catch (Exception e){
            }
            actualNumBytes = 0 ;
        }
        return "000" ;
    }

    public String readSerial(){
        if (actualNumBytes!=0x00){
            try {
                String result = new String(readBuffer, 0, actualNumBytes, "UTF-8");
                if (Utils.isNoEmpty(result)){
                    result = result.substring(0,result.length()-1) ;
                }
               return  result ;
            }catch (Exception e){
            }
            actualNumBytes = 0 ;
        }
        return "" ;
    }

    /**
     * 发送数据
     * @param strData
     * @return
     * @throws Exception
     */
    public boolean send(String strData) throws Exception{

        int count_int;
        int NumBytes = 0;
        int mLen = 0;
        byte[] writeBuffer;
        if (strData.getBytes().length != 0) {
            NumBytes = strData.getBytes().length;
            writeBuffer = new byte[NumBytes];
            for (count_int = 0; count_int < NumBytes; count_int++) {
                writeBuffer[count_int] = strData.getBytes()[count_int];
            }
            mLen =  ch340AndroidDriver.WriteData(writeBuffer, NumBytes);
            if (NumBytes != mLen) {
                return false  ;
            } else {
                return true ;
            }
        }
        return false ;
    }


    public void close(){

        if (ch340AndroidDriver!=null){
            setEnableRead(false);
            ch340AndroidDriver.CloseDevice();
        }
    }
    /**
     * 数据接收的线程
     */
    private  class ReadThread extends Thread{
        private Object ThreadLock = new Object() ;
        private boolean isRead  = true ;

        public ReadThread(boolean isRead){
            this.isRead = isRead ;
            this.setPriority(Thread.MIN_PRIORITY);
        }
        public void setReadFlag(boolean isRead){
            this.isRead = isRead ;
        }

        @Override
        public void run() {
            super.run();

            while (true) {
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {

                }
                if (this.isRead){
                    synchronized (ThreadLock) {
                        if (ch340AndroidDriver != null) {
                            actualNumBytes = ch340AndroidDriver.ReadData(readBuffer, 64);
                        }
                    }
                }
            }
        }
    }
}
