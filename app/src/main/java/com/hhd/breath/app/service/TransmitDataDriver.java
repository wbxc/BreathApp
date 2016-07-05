package com.hhd.breath.app.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.provider.Settings;
import android.widget.Toast;

import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.net.ThreadPoolWrap;
import com.hhd.breath.app.wchusbdriver.CH340AndroidDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by familylove on 2016/4/27.
 * 数据的传输
 */
public class TransmitDataDriver {

    public int totalBytes = 0 ;
    protected final int maxNumBytes = 65536 ;
    protected final Object  ReadQueueLock = new Object() ;
    protected final Object  WriteQueueLock = new Object() ;
    protected byte[] usbData ;
    protected int ReadTimeOutMillis = 10000 ;
    protected int WriteTimeOutMillis = 10000 ;
    private int readCount = 0  ;
    protected byte[] readBuffer ;
    protected byte[] writeBuffer ;
    private int readIndex = 0 ;
    private int writeIndex = 0 ;

    private Context context ;
    private UsbManager usbManager ;
    private static  TransmitDataDriver instance = null ;
    private UsbEndpoint mBulkInPoint ;
    private UsbEndpoint mBulkOutPoint ;
    private int mBulkPacketSize = 0 ;
    private UsbEndpoint mCtrlPoint ;



    private int baudRate = 115200;
    private byte dataBit = 8;
    private byte stopBit = 1;
    private byte parity = 0;
    private byte flowControl = 0;

    private UsbDeviceConnection  mDeviceConnection = null ;
    private UsbInterface mInterface = null ;
    private UsbDevice mUsbDevice ;



    private HashMap<String,UsbDevice> deviceList = null ;
    private UsbDevice localUsbDevice = null;
    private ArrayList<String> DeviceNum = new ArrayList<String>();
    private int DeviceCount = 0 ;
    private PendingIntent mPendingIntent = null;


    private TransmitDataDriver(Context context) {
        this.context = context;
        usbData = new byte[1024] ;
        readBuffer = new byte[maxNumBytes] ;
        ArrayAddDevice(CommonValues.USB_CODE340);
        deviceList = new HashMap<String,UsbDevice>() ;
        mPendingIntent = PendingIntent.getBroadcast(this.context,0,new Intent(CommonValues.ACTION_USB_PERMISSION),0) ;
    }

    public  static  TransmitDataDriver getInstance(Context context){
        if (instance==null){
            instance = new TransmitDataDriver(context) ;
        }
        if (instance.usbManager==null){
            instance.usbManager = (UsbManager)context.getSystemService(Context.USB_SERVICE) ;
        }
       /* instance = new TransmitDataDriver(context) ;

        instance.usbManager = (UsbManager)context.getSystemService(Context.USB_SERVICE) ;*/

        return instance ;
    }

    private void ArrayAddDevice(String str) {
        DeviceNum.add(str);
        DeviceCount = DeviceNum.size();
    }


    /**
     * 判断设备是否为空
     * @return 1、表示有设备匹配
     * @return 2、没有呼吸训练设备
     */
    public int isNotEmptyDevice(){
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
                        localUsbDevice = localUsbDevice1  ;
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
     * 检查设备的连接状态
     * @return 1、有设备并获得到了权限
     * @return 2、有设备并没有或得到权限
     * @return 0、没有设备连接
     */
    public int checkUsbStatus(){

        if (isNotEmptyDevice()==1){
            if (usbManager.hasPermission(localUsbDevice)){
                return 1;
            }else {
                return 2 ;
            }
        }
        return 0 ;
    }

    public boolean isHasPermission(){
        if (usbManager.hasPermission(localUsbDevice)){
            return true ;
        }
        return false ;
    }

    /**
     *调用系统Api获取权限
     */
    public void getPermission(){
        this.usbManager.requestPermission(localUsbDevice,mPendingIntent);
    }


    public UsbDevice getLocalUsbDevice(){
        if (localUsbDevice!=null){
            return localUsbDevice ;
        }else {
            return null;
        }
    }

    /**
     * 打开usb
     * @return
     */
    public synchronized boolean openUsbDevice(){

        Object localObject ;
        UsbInterface usbInterface ;
        if (localUsbDevice==null){
            return false;
        }
        usbInterface = getUsbInterface(localUsbDevice) ;
        if (localUsbDevice!=null && usbInterface!=null){
            localObject = this.usbManager.openDevice(localUsbDevice) ;
            if (localObject != null){
               if (((UsbDeviceConnection)localObject).claimInterface(usbInterface,true)){
                   this.mUsbDevice = localUsbDevice ;
                   this.mDeviceConnection = (UsbDeviceConnection)localObject ;
                   this.mInterface = usbInterface ;
                   if (!enumerateEndPoint(mInterface)){
                       return  false ;
                   }

                   return  true ;
               }
            }else {
                return  false ;
            }
        }
        return false ;
    }

    public boolean isConnection(){
        if (this.mUsbDevice!=null &&  this.mInterface!=null && this.mDeviceConnection !=null &&localUsbDevice!=null){
            return true ;
        }else {
            return false ;
        }
    }

    private boolean enumerateEndPoint(UsbInterface usbInterface){

        if (usbInterface == null){
            return false ;
        }
        for (int i=0 ; i<usbInterface.getEndpointCount() ; i++){
            UsbEndpoint endpoint = usbInterface.getEndpoint(i) ;
            if (endpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK
                    && endpoint.getMaxPacketSize() == 0x20){
                if (endpoint.getDirection() == UsbConstants.USB_DIR_IN){
                    mBulkInPoint = endpoint ;
                }else {
                    mBulkOutPoint = endpoint ;
                }
                this.mBulkPacketSize = endpoint.getMaxPacketSize() ;
            }else if (endpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK){
                mCtrlPoint = endpoint ;
            }

        }
        return true ;
    }



    /**
     *
     * 代表USB设备的一个接口，注意：UsbInterface本身是一个类，并不是一个接口。此类的主要方法有以下：
     1) getId() 得到给接口的id号。
     2) getInterfaceClass() 得到该接口的类别。
     3) getInterfaceSubclass() 得到该接口的子类。
     4) getInterfaceProtocol() 得到该接口的协议类别。
     5) getEndpointCount() 获得关于此接口的节点数量。
     6) getEndpoint(int index) 对于指定的index获得此接口的一个节点，返回一个UsbEndpoint.
     * @param paramUsbDevice
     * @return UsbInterface
     */
    private UsbInterface getUsbInterface(UsbDevice paramUsbDevice){
        if (this.mDeviceConnection !=null){
            if (this.mInterface!=null){
                this.mDeviceConnection.releaseInterface(this.mInterface) ;
                this.mInterface = null;
            }
            this.mDeviceConnection.close();
            this.mUsbDevice = null;
            this.mInterface = null;

        }
        if (paramUsbDevice==null){

            return null;
        }
        for (int i=0 ; i<paramUsbDevice.getInterfaceCount() ; i++){
            UsbInterface usbInterface = paramUsbDevice.getInterface(i) ;
            if (usbInterface.getInterfaceClass() == 0xff
                    && usbInterface.getInterfaceSubclass() == 0x01
                    && usbInterface.getInterfaceProtocol() ==0x02){

                return usbInterface ;
            }
        }
        return null;
    }

    /**
     * 读取数据 Thread
     */
    protected  class  ReadThread extends Thread{

        private  UsbEndpoint endpoint  ;
        private  UsbDeviceConnection connection ;
        private  boolean isReadEnable ;

        public  ReadThread(UsbEndpoint endpoint ,UsbDeviceConnection connection,boolean isReadEnable){
            this.endpoint = endpoint ;
            this.connection = connection ;
            this.isReadEnable = isReadEnable ;
            this.setPriority(Thread.MAX_PRIORITY);
        }

        public  void  setReadThreadEnable(boolean isReadEnable){
            this.isReadEnable = isReadEnable ;
        }

        @Override
        public void run() {
            super.run();

            while (isReadEnable){

                while (totalBytes>(maxNumBytes - 63)){

                    try {
                        Thread.sleep(5);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                synchronized (ReadQueueLock){
                    if (endpoint !=null ){
                        readCount = connection.bulkTransfer(endpoint,usbData,64,ReadTimeOutMillis) ;
                        if (readCount>0){

                            for (int count =0 ; count <readCount ; count++){
                                readBuffer[writeIndex] = usbData[count] ;
                                writeIndex++ ;
                                writeIndex %= maxNumBytes ;
                            }
                            if (writeIndex >= readIndex){
                                totalBytes = writeIndex-readIndex ;
                            }else {
                                totalBytes = (maxNumBytes-readIndex)+writeIndex ;
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean SetConfig(int baudRate, byte dataBit, byte stopBit, byte parity, byte flowControl) {
        int value = 0;
        int index = 0;
        char valueHigh = 0, valueLow = 0, indexHigh = 0, indexLow = 0;
        switch (parity) {
            case 0: /* NONE */
                valueHigh = 0x00;
                break;
            case 1: /* ODD */
                valueHigh |= 0x08;
                break;
            case 2: /* Even */
                valueHigh |= 0x18;
                break;
            case 3: /* Mark */
                valueHigh |= 0x28;
                break;
            case 4: /* Space */
                valueHigh |= 0x38;
                break;
            default: /* None */
                valueHigh = 0x00;
                break;
        }

        if (stopBit == 2) {
            valueHigh |= 0x04;
        }

        switch (dataBit) {
            case 5:
                valueHigh |= 0x00;
                break;
            case 6:
                valueHigh |= 0x01;
                break;
            case 7:
                valueHigh |= 0x02;
                break;
            case 8:
                valueHigh |= 0x03;
                break;
            default:
                valueHigh |= 0x03;
                break;
        }

        valueHigh |= 0xc0;
        valueLow = 0x9c;

        value |= valueLow;
        value |= (int) (valueHigh << 8);

        switch (baudRate) {
            case 50:
                indexLow = 0;
                indexHigh = 0x16;
                break;
            case 75:
                indexLow = 0;
                indexHigh = 0x64;
                break;
            case 110:
                indexLow = 0;
                indexHigh = 0x96;
                break;
            case 135:
                indexLow = 0;
                indexHigh = 0xa9;
                break;
            case 150:
                indexLow = 0;
                indexHigh = 0xb2;
                break;
            case 300:
                indexLow = 0;
                indexHigh = 0xd9;
                break;
            case 600:
                indexLow = 1;
                indexHigh = 0x64;
                break;
            case 1200:
                indexLow = 1;
                indexHigh = 0xb2;
                break;
            case 1800:
                indexLow = 1;
                indexHigh = 0xcc;
                break;
            case 2400:
                indexLow = 1;
                indexHigh = 0xd9;
                break;
            case 4800:
                indexLow = 2;
                indexHigh = 0x64;
                break;
            case 9600:
                indexLow = 2;
                indexHigh = 0xb2;
                break;
            case 19200:
                indexLow = 2;
                indexHigh = 0xd9;
                break;
            case 38400:
                indexLow = 3;
                indexHigh = 0x64;
                break;
            case 57600:
                indexLow = 3;
                indexHigh = 0x98;
                break;
            case 115200:
                indexLow = 3;
                indexHigh = 0xcc;
                break;
            case 230400:
                indexLow = 3;
                indexHigh = 0xe6;
                break;
            case 460800:
                indexLow = 3;
                indexHigh = 0xf3;
                break;
            case 500000:
                indexLow = 3;
                indexHigh = 0xf4;
                break;
            case 921600:
                indexLow = 7;
                indexHigh = 0xf3;
                break;
            case 1000000:
                indexLow = 3;
                indexHigh = 0xfa;
                break;
            case 2000000:
                indexLow = 3;
                indexHigh = 0xfd;
                break;
            case 3000000:
                indexLow = 3;
                indexHigh = 0xfe;
                break;
            default: // default baudRate "9600"
                indexLow = 2;
                indexHigh = 0xb2;
                break;
        }

        index |= 0x88 | indexLow;
        index |= (int) (indexHigh << 8);

        Uart_Control_Out(UartCmd.VENDOR_SERIAL_INIT, value, index);
        if (flowControl == 1) {
            Uart_Tiocmset(UartModem.TIOCM_DTR | UartModem.TIOCM_RTS, 0x00);
        }
        return true;
    }

    /**
     * @param data
     * @param length
     * @return
     */
    public int readData(byte[] data , int length){

        int mLen ;

        if ((length<1) || (totalBytes == 0)){
            mLen = 0 ;
            return mLen ;
        }
        if (length>totalBytes){
            length = totalBytes ;
        }
        totalBytes -= length ;
        mLen = length ;
        for (int count =0 ; count <length ; count++){
            data[count] = readBuffer[readIndex] ;
            readIndex++ ;
            readIndex %=maxNumBytes ;
        }
        return mLen ;
    }

    /**
     * 写数据
     * @param writeBuffer
     * @param length
     * @return
     * @throws IOException
     */
    public int writeData(byte[] writeBuffer , int length) throws IOException{

        int mLen = 0 ;
        mLen = writeData(writeBuffer,length,this.WriteTimeOutMillis) ;
        if (mLen<0){
            throw new IOException("Expected Write Actual Bytes") ;
        }
        return mLen ;
    }
    /**
     *
     * @param writeBuffer
     * @param length
     * @param timeoutMillis
     * @return
     */
    private  int writeData(byte[] writeBuffer , int length , int timeoutMillis){

        int offset = 0 ;
        int HasWritten = 0 ;
        int odd_len = length ;

        if (this.mBulkOutPoint == null){
            return -1 ;
        }
        while (offset<length){
            synchronized (this.WriteQueueLock){
                int mLen = Math.min(odd_len,this.mBulkPacketSize) ;
                byte[] arrayOfByte = new byte[mLen] ;
                if (offset == 0){
                    System.arraycopy(writeBuffer,0,arrayOfByte,0,mLen);
                }else {
                    System.arraycopy(writeBuffer,offset,arrayOfByte,0,mLen);
                }

                HasWritten = this.mDeviceConnection.bulkTransfer(this.mBulkOutPoint,arrayOfByte,mLen,timeoutMillis) ;

                if (HasWritten < 0 ){
                    return -2 ;
                }else {
                    offset += HasWritten ;
                    odd_len -= HasWritten ;
                }
            }
        }
        return  offset ;
    }

    private int DEFAULT_TIMEOUT = 500 ;
    public int Uart_Control_Out(int request , int value , int index){
        int reTval = 0 ;
        reTval = mDeviceConnection.controlTransfer(UsbType.USB_TYPE_VENDOR |UsbType.USB_RECIP_DEVICE | UsbType.USB_DIR_OUT,
                                    request,value,index,null,0,DEFAULT_TIMEOUT) ;
        return reTval ;
    }

    public int Uart_Control_In(int request , int value ,int index , byte[] buffer , int length){

        int retval = 0 ;
        retval = mDeviceConnection.controlTransfer(UsbType.USB_TYPE_VENDOR|UsbType.USB_RECIP_DEVICE|UsbType.USB_DIR_IN,
                request,value,index,buffer,length,DEFAULT_TIMEOUT) ;
        return retval ;
    }

    private int Uart_Set_Handshake(int control) {
        return Uart_Control_Out(UartCmd.VENDOR_MODEM_OUT, ~control, 0);
    }

    public int Uart_Tiocmset(int set, int clear) {

        int control = 0;
        if ((set & UartModem.TIOCM_RTS) == UartModem.TIOCM_RTS)
            control |= UartIoBits.UART_BIT_RTS;
        if ((set & UartModem.TIOCM_DTR) == UartModem.TIOCM_DTR)
            control |= UartIoBits.UART_BIT_DTR;
        if ((clear & UartModem.TIOCM_RTS) == UartModem.TIOCM_RTS)
            control &= ~UartIoBits.UART_BIT_RTS;
        if ((clear & UartModem.TIOCM_DTR) == UartModem.TIOCM_DTR)
            control &= ~UartIoBits.UART_BIT_DTR;

        return Uart_Set_Handshake(control);
    }


    // 连接数据  第一步 初始化
    public boolean UartInit() {
        int ret;
        int size = 8;
        byte[] buffer = new byte[size];
        Uart_Control_Out(UartCmd.VENDOR_SERIAL_INIT, 0x0000, 0x0000);
        ret = Uart_Control_In(UartCmd.VENDOR_VERSION, 0x0000, 0x0000, buffer, 2);
        if (ret < 0)
            return false;
        Uart_Control_Out(UartCmd.VENDOR_WRITE, 0x1312, 0xD982);
        Uart_Control_Out(UartCmd.VENDOR_WRITE, 0x0f2c, 0x0004);
        ret = Uart_Control_In(UartCmd.VENDOR_READ, 0x2518, 0x0000, buffer, 2);
        if (ret < 0)
            return false;
        Uart_Control_Out(UartCmd.VENDOR_WRITE, 0x2727, 0x0000);
        Uart_Control_Out(UartCmd.VENDOR_MODEM_OUT, 0x00ff, 0x0000);
        return true;
    }


    /**
     *  第二步 配置串口
     * @return
     */
    public boolean setUartConfig(){
        return SetConfig(baudRate,dataBit,stopBit,parity,flowControl) ;
    }

    /**
     * 第三不 开启数据收发
     *
     */

    protected  ReadThread readThread = null ;

    /**
     * 开启数据接收
     * @param isReadEnable
     */
    public void startThread(boolean isReadEnable){
        if (readThread == null){
            readThread = new ReadThread(this.mBulkInPoint,mDeviceConnection,isReadEnable) ;
            readThread.start();
        }else {
            readThread.setReadThreadEnable(isReadEnable);
        }
    }

    public void startThreadData(){
        if (readThread!=null){
            readThread.setReadThreadEnable(true);
            readThread.start();
        }else {
            readThread = new ReadThread(this.mBulkInPoint,mDeviceConnection,true) ;
            readThread.start();
            //Toast.makeText(context,"开启",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 关闭数据接收
     */
    public void stopThreadData(){
        if (readThread!=null){
            readThread.setReadThreadEnable(false);
        }
    }

    /**
     * 关闭
     */
    public synchronized void  closeDevice(){
        try {
            Thread.sleep(10);
        } catch (Exception e) {
        }

        if (this.mDeviceConnection != null) {
            if (this.mInterface != null) {
                this.mDeviceConnection.releaseInterface(this.mInterface);
                this.mInterface = null;
            }

            this.mDeviceConnection.close();
            this.mDeviceConnection = null;
        }

        if (this.mUsbDevice != null) {
            this.mUsbDevice = null;
        }
        if (localUsbDevice!=null){
            localUsbDevice = null;
        }

       /* if (this.usbManager != null) {
            this.usbManager = null;
        }*/
    }


















    public final class UartModem {
        public static final int TIOCM_LE = 0x001;
        public static final int TIOCM_DTR = 0x002;
        public static final int TIOCM_RTS = 0x004;
        public static final int TIOCM_ST = 0x008;
        public static final int TIOCM_SR = 0x010;
        public static final int TIOCM_CTS = 0x020;
        public static final int TIOCM_CAR = 0x040;
        public static final int TIOCM_RNG = 0x080;
        public static final int TIOCM_DSR = 0x100;
        public static final int TIOCM_CD = TIOCM_CAR;
        public static final int TIOCM_RI = TIOCM_RNG;
        public static final int TIOCM_OUT1 = 0x2000;
        public static final int TIOCM_OUT2 = 0x4000;
        public static final int TIOCM_LOOP = 0x8000;
    }

    public final class UsbType {
        public static final int USB_TYPE_VENDOR = (0x02 << 5);
        public static final int USB_RECIP_DEVICE = 0x00;
        public static final int USB_DIR_OUT = 0x00; /* to device */
        public static final int USB_DIR_IN = 0x80; /* to host */
    }

    public final class UartCmd {
        public static final int VENDOR_WRITE_TYPE = 0x40;
        public static final int VENDOR_READ_TYPE = 0xC0;
        public static final int VENDOR_READ = 0x95;
        public static final int VENDOR_WRITE = 0x9A;
        public static final int VENDOR_SERIAL_INIT = 0xA1;
        public static final int VENDOR_MODEM_OUT = 0xA4;
        public static final int VENDOR_VERSION = 0x5F;
    }

    public final class UartState {
        public static final int UART_STATE = 0x00;
        public static final int UART_OVERRUN_ERROR = 0x01;
        public static final int UART_PARITY_ERROR = 0x02;
        public static final int UART_FRAME_ERROR = 0x06;
        public static final int UART_RECV_ERROR = 0x02;
        public static final int UART_STATE_TRANSIENT_MASK = 0x07;
    }

    public final class UartIoBits {
        public static final int UART_BIT_RTS = (1 << 6);
        public static final int UART_BIT_DTR = (1 << 5);
    }

}
