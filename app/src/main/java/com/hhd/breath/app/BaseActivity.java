package com.hhd.breath.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;


import com.hhd.breath.app.utils.ActivityCollector;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Administrator on 2015/11/23.
 */
public abstract  class BaseActivity extends FragmentActivity {


    private  BaseDialog mCommonDialog = null ;
    private Context mContext ;

    protected int baudRate;
    protected byte dataBit;
    protected byte stopBit;
    protected byte parity;
    protected byte flowControl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this ;

        baudRate = 115200;
        dataBit = 8;
        stopBit = 1;
        parity = 0;
        flowControl = 0;
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected   void initView(){} ;

    protected   void initEvent(){} ;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    protected String getTimeLong(int timeLong) {

        StringBuffer sb = new StringBuffer();
        int fen = timeLong / 60;
        int second = timeLong % 60;
        if (fen <= 9) {
            sb.append("0" + fen + ":");
        } else {
            sb.append(String.valueOf(fen) + ":");
        }

        if (second <= 9) {
            sb.append("0" + second);
        } else {
            sb.append(String.valueOf(second));
        }
        return sb.toString();
    }

    protected String timeStampToData(String timeStamp){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss") ;
        long timeLong = Long.parseLong(timeStamp)*1000 ;
        return sdf.format(new Date(timeLong)) ;
    }


    protected void showProgressDialog(Context mContext , String message){

        try {
            mCommonDialog = new BaseDialog(mContext, R.style.Theme_Transparent,"progress_dialog",message) ;

            mCommonDialog.show();
        }catch (Exception e){

        }
    }
    protected void showProgressDialog(String message){

        try {
            mCommonDialog = new BaseDialog(mContext,R.style.Theme_Transparent,"progress_dialog",message) ;

            mCommonDialog.show();
        }catch (Exception e){

        }
    }

    public void hideProgress() {// 关闭"请稍等"
        if (mCommonDialog != null && mCommonDialog.isShowing()) {
            mCommonDialog.dismiss();
        }
    }

    protected  boolean isNumberPhone(String phone){

        Pattern p = Pattern.compile("^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,0-9]))\\d{8}$") ;
        Matcher matcher = p.matcher(phone) ;
        return matcher.matches() ;
    }
    public boolean isNotEmpty(String str){

        if(str!=null && !"".equals(str)){
            return true ;
        }else {
            return false ;
        }
    }

    /**
     * 判断网络连接是否可用
     * @param context
     * @return
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     *  判断wifi是否可用
     * @param context
     * @return
     */
    public boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 移动网络类型是否可用
     * @param context
     * @return
     */
    public boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    protected String longTimeToTime(String time){
        if (isNotEmpty(time)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
            long timeLong = Long.parseLong(time)*1000 ;

            return simpleDateFormat.format(new Date(timeLong)) ;
        }

        return "" ;
    }

    protected String timeToLongtime(String time){

        try {
            if (isNotEmpty(time)){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
                return  String.valueOf(simpleDateFormat.parse(time).getTime()/1000) ;
            }
        }catch (Exception e){

        }
       return ""  ;
    }

}
