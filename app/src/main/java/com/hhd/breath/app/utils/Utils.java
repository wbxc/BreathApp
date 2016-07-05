package com.hhd.breath.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.hhd.breath.app.CommonValues;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/12/23.
 */
public class Utils {

    public static int getStatusHeight(Activity mContext){
        int statusHeight = 0;
        Rect localRect = new Rect();
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = mContext.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    //判断是否是手机号
    public static boolean isMobile(String mobile){
        Pattern p = null;
        Matcher matcher = null;
        p = Pattern.compile("^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,0-9]))\\d{8}$") ;

        matcher = p.matcher(mobile) ;
        return  matcher.matches() ;
    }

    public static String subPasswordMobile(String mobile){

        String temp = "";
        if (StringUtils.isNotEmpty(mobile) && mobile.length()>7) {
            temp = mobile.substring(mobile.length()-6, mobile.length());
        }
        return temp ;
    }

    public static void write(String number){
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "mumber.txt");
            //第二个参数意义是说是否以append方式添加内容
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));

            bw.write(number);
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write1(String number){
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "mumber1.txt");
            //第二个参数意义是说是否以append方式添加内容
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));

            bw.write(number);
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write2(String number){
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "mumber2.txt");
            //第二个参数意义是说是否以append方式添加内容
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));

            bw.write(number);
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断网络的链接
     * @param mContext
     * @return
     */
    public static boolean isNetConnection(Context mContext){

        ConnectivityManager mCManger = (ConnectivityManager)(mContext).getSystemService(Context.CONNECTIVITY_SERVICE) ;

        NetworkInfo mNetworkInfo = mCManger.getActiveNetworkInfo() ;
        if (mNetworkInfo!=null && mNetworkInfo.isConnectedOrConnecting()){
            return  true ;
        }
        return false ;
    }

    /***
     * 获取网络类型
     * @param mContext
     * @return
     */
    public static int getNetWorkType(Context mContext){

        ConnectivityManager manager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE) ;
        NetworkInfo mInfo  = manager.getActiveNetworkInfo() ;
        int netType = 0 ;

        if (mInfo!=null){
            switch (mInfo.getType()){
                case ConnectivityManager.TYPE_MOBILE:
                    netType = CommonValues.NETTYPE_MOBILE ;
                    break;
                case ConnectivityManager.TYPE_WIFI:
                    netType = CommonValues.NETTYPE_WIFI ;
                    break;
            }
        }
        return  netType ;
    }

    /**
     *
     * @return 返回用户的Ip地址
     */
    public static String getPhoneIp(){

        try {
            for ( Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces() ; enumeration.hasMoreElements();){
                NetworkInterface nf =  enumeration.nextElement() ;
                for (Enumeration<InetAddress> eI = nf.getInetAddresses();eI.hasMoreElements();){
                    InetAddress mInetAdd = eI.nextElement() ;
                    if (!mInetAdd.isLoopbackAddress() && mInetAdd instanceof Inet4Address){

                        return mInetAdd.getHostAddress().toString() ;
                    }
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "" ;
    }

    public static boolean isNoEmpty(String str){

        if (str!=null && !"".equals(str)){
            return true ;
        }

        return false ;
    }
}
