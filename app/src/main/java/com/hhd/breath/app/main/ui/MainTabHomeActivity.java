package com.hhd.breath.app.main.ui;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.hhd.breath.app.BaseDialog;
import com.hhd.breath.app.BreathApplication;
import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.R;
import com.hhd.breath.app.db.TrainPlanService;
import com.hhd.breath.app.model.TrainPlan;
import com.hhd.breath.app.service.GlobalUsbService;
import com.hhd.breath.app.tab.ui.BreathCheck;
import com.hhd.breath.app.tab.ui.BreathTrainPlan;
import com.hhd.breath.app.tab.ui.HisTabActivity;
import com.hhd.breath.app.utils.ActivityCollector;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.utils.StringUtils;
import com.hhd.breath.app.wchusbdriver.Global340Driver;
import com.umeng.update.UmengUpdateAgent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainTabHomeActivity extends TabActivity implements TabHost.OnTabChangeListener {

    private TabHost mTabHost; // 内容
    private TabWidget mTabWidget; // 导航
    private List<View> list = new ArrayList<View>();
    //线程锁
    protected Object threadLock = new Object();
    protected byte[] readBuffer = new byte[512];
    protected int actualNumBytes = 0;
    private boolean isFlag = false ;
   // private TransmitDataDriver transmitDataDriver = null;
    private long firstTime = 0;
    private MyHandler mHandler = null;
    private BaseDialog mCommonDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab_home);
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
        mTabHost.setOnTabChangedListener(this);
        mHandler  = new MyHandler(this) ;
        CommonValues.BREATH_IS_ACTIVE = true;
        if (isNotEmpty(ShareUtils.getSerialNumber(MainTabHomeActivity.this))){
            GlobalUsbService.isOpenBreath = true ;
        }
        initBottomView();
        switch (CommonValues.select_activity) {
            case 0:
                mTabHost.setCurrentTab(0);
                break;
            case 1:
                mTabHost.setCurrentTab(1);
                break;
            case 2:
                mTabHost.setCurrentTab(2);
                break;
        }
        UmengUpdateAgent.update(this) ;
        initEvent();

    }

    private void initEvent(){
        switch (Global340Driver.getInstance(MainTabHomeActivity.this).checkUsbStatus()){
            case 2:
                Global340Driver.getInstance(MainTabHomeActivity.this).getPermission();
                startTask();
                break;
            case 1:
                if (!StringUtils.isNotEmpty(ShareUtils.getSerialNumber(MainTabHomeActivity.this))){
                    Global340Driver.getInstance(MainTabHomeActivity.this).initDriver() ;
                    Global340Driver.getInstance(MainTabHomeActivity.this).initUart() ;
                }else {
                    Global340Driver.getInstance(MainTabHomeActivity.this).initDriver() ;
                }
                break;
            case 0:
                break;
        }
    }

    private void initUsbDevice(){

        Global340Driver.getInstance(MainTabHomeActivity.this).setEnableRead(true);
        try {
            boolean flag = Global340Driver.getInstance(MainTabHomeActivity.this).send("1");
            if (flag) {
                showProgressDialog(MainTabHomeActivity.this,"") ;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        String result = Global340Driver.getInstance(MainTabHomeActivity.this).readSerial() ;
                        if (isNotEmpty(result)){
                            hideProgressDialog();
                            ShareUtils.setSerialNumber(MainTabHomeActivity.this,result);
                        }else {
                            try {
                                Global340Driver.getInstance(MainTabHomeActivity.this).send("4");
                            }catch (Exception e){

                            }
                            mHandler.sendEmptyMessage(30) ;
                        }

                    }
                }, 500);
            } else {
                BreathApplication.toast(MainTabHomeActivity.this, "设备获取序列号失败");
            }
        }catch (Exception e){
            BreathApplication.toast(MainTabHomeActivity.this, "设备获取序列号失败");
        }
    }
    private Timer mTimer = null ;
    private TimerTask mTimerTask = null;

    private void startTask(){

        if (mTimer == null){
            mTimer = new Timer() ;
        }

        if (mTimerTask == null){
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(25) ;
                }
            } ;
        }

        mTimer.schedule(mTimerTask, 0, 100);

    }

    private void stopTask(){

        if (mTimer!=null){
            mTimer.cancel();
            mTimer =null;
        }

        if (mTimerTask!=null){
            mTimerTask.cancel() ;
            mTimerTask = null;
        }
    }

    private boolean isNotEmpty(String str){

        if(str==null||str.equals("")){
            return false ;
        }else {
            return true ;
        }
    }

    private static class MyHandler extends Handler{

        private WeakReference<MainTabHomeActivity>  reference ;
        private MainTabHomeActivity mainTabHomeActivity ;

        public MyHandler(MainTabHomeActivity context){
            reference = new WeakReference<MainTabHomeActivity>(context) ;
            mainTabHomeActivity = reference.get() ;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 25:
                    mainTabHomeActivity.function25();
                    break;
                case 30:
                    mainTabHomeActivity.hideProgressDialog();
                    String result = Global340Driver.getInstance(mainTabHomeActivity).readSerial();
                    ShareUtils.setSerialNumber(mainTabHomeActivity, result);
                    BreathApplication.toastTest(mainTabHomeActivity, result + ">>>");
                    break;

            }
        }
    }



    protected void function25(){
        if (Global340Driver.getInstance(MainTabHomeActivity.this).checkUsbStatus() == 1){
            stopTask();
            Global340Driver.getInstance(MainTabHomeActivity.this).initDriver() ;
            Global340Driver.getInstance(MainTabHomeActivity.this).initUart() ;
            Global340Driver.getInstance(MainTabHomeActivity.this).setEnableRead(true); ;
            initUsbDevice();
        }
    }

    protected void showProgressDialog(Context mContext , String message){

        try {
            mCommonDialog = new BaseDialog(mContext, R.style.Theme_Transparent,"progress_dialog",message) ;
            mCommonDialog.show();
        }catch (Exception e){

        }
    }

    protected void hideProgressDialog(){
        if (mCommonDialog!=null && mCommonDialog.isShowing()){
            mCommonDialog.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public static void actionStart(Activity mActivity){

        Intent mIntent = new Intent() ;
        mIntent.setClass(mActivity, MainTabHomeActivity.class) ;
        mActivity.startActivity(mIntent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        switch (CommonValues.select_activity) {
            case 0:
                mTabHost.setCurrentTab(0);
                break;
            case 1:
                mTabHost.setCurrentTab(1);
                break;
            case 2:
                mTabHost.setCurrentTab(2);
                break;
           /* case 3:
                mTabHost.setCurrentTab(3);
                break;*/
        }
    }

    @Override
    public void onTabChanged(String tabId) {

        int tabID = Integer.valueOf(tabId);
        for (int i = 0; i < mTabWidget.getChildCount(); i++) {
            if (i == tabID) // 设置为选中状态
            {
                if (list.size() != 0) {
                    if (i == 0) {
                        ((ImageView) list.get(i).findViewById(R.id.btn_tab_bottom_weixin))
                                .setBackgroundResource(R.mipmap.icon_tab_train_select);
                        ((TextView) list.get(i).findViewById(R.id.tv_main))
                                .setTextColor(getResources().getColor(R.color.common_bottom_bar_blue));
                    }
                    if (i == 1) {
                        ((ImageView) list.get(i).findViewById(R.id.btn_tab_bottom_weixin))
                                .setBackgroundResource(R.mipmap.icon_tab_hisrecord_select);
                        ((TextView) list.get(i).findViewById(R.id.tv_main))
                                .setTextColor(getResources().getColor(R.color.common_bottom_bar_blue));
                    } else if (i == 2) {
                        ((ImageView) list.get(i).findViewById(R.id.btn_tab_bottom_weixin))
                                .setBackgroundResource(R.mipmap.icon_tab_evaluate);
                        ((TextView) list.get(i).findViewById(R.id.tv_main))
                                .setTextColor(getResources().getColor(R.color.common_bottom_bar_blue));
                    } /*else if (i == 3) {
                        ((ImageView) list.get(i).findViewById(R.id.btn_tab_bottom_weixin))
                                .setBackgroundResource(R.mipmap.icon_tab_me_select);
                        ((TextView) list.get(i).findViewById(R.id.tv_main))
                                .setTextColor(getResources().getColor(R.color.common_bottom_bar_blue));
                    }*/
                }
            } else {// 其他按钮设置为没选中
                mTabWidget.getChildAt(Integer.valueOf(i)).setBackgroundDrawable(null);
                if (list.size() != 0) {
                    if (i == 0 && i != tabID) {
                        ((ImageView) list.get(i).findViewById(R.id.btn_tab_bottom_weixin))
                                .setBackgroundResource(R.mipmap.icon_tab_train_unselect);
                        ((TextView) list.get(i).findViewById(R.id.tv_main))
                                .setTextColor(getResources().getColor(R.color.common_color_d5d5d5));
                    }
                    if (i == 1 && i != tabID) {
                        ((ImageView) list.get(i).findViewById(R.id.btn_tab_bottom_weixin))
                                .setBackgroundResource(R.mipmap.icon_tab_hisrecord_unselect);
                        ((TextView) list.get(i).findViewById(R.id.tv_main))
                                .setTextColor(getResources().getColor(R.color.common_color_d5d5d5));
                    } else if (i == 2 && i != tabID) {
                        ((ImageView) list.get(i).findViewById(R.id.btn_tab_bottom_weixin))
                                .setBackgroundResource(R.mipmap.icon_tab_un_evaluate);
                        ((TextView) list.get(i).findViewById(R.id.tv_main))
                                .setTextColor(getResources().getColor(R.color.common_color_d5d5d5));
                    }
                }
            }
        }

    }

    private void initBottomView() {
        setIndicator(R.mipmap.icon_tab_train_select, 0, new Intent(this, BreathTrainPlan.class));
        setIndicator(R.mipmap.icon_tab_hisrecord_unselect, 1, new Intent(this, HisTabActivity.class));
        setIndicator(R.mipmap.icon_tab_me_unselect, 2, new Intent(this, BreathCheck.class));

    }


    /**
     * 初始化标签的方法 作用：为每一个table键设置图片，ID，以及跳转功能
     *
     * @param icon
     *            标签的图片
     * @param tabID 标签的ID
     * @param intent 跳转的对象
     */
    private void setIndicator(int icon, int tabID, Intent intent) {
        View localView = LayoutInflater.from(this.mTabHost.getContext()).inflate(R.layout.main_tab_menu_item, null);
        RelativeLayout mRela = (RelativeLayout) localView.findViewById(R.id.content_item);
        ImageView iv = (ImageView) mRela.findViewById(R.id.btn_tab_bottom_weixin);
        TextView mText = (TextView) mRela.findViewById(R.id.tv_main);
        switch (tabID) {
            case 0:
                mText.setText(getResources().getString(R.string.string_tab_train));
                mText.setTextColor(getResources().getColor(R.color.common_bottom_bar_blue));
                break;
            case 1:
                mText.setText(getResources().getString(R.string.string_tab_report));
                mText.setTextColor(getResources().getColor(R.color.common_color_d5d5d5));
                break;
            case 2:
                mText.setText(getResources().getString(R.string.string_tab_check));
                mText.setTextColor(getResources().getColor(R.color.common_color_d5d5d5));
                break;
           /* case 3:

                mText.setText(getResources().getString(R.string.string_tab_me));
                mText.setTextColor(getResources().getColor(R.color.common_color_d5d5d5));
                break;*/

        }
        iv.setBackgroundResource(icon);
        String str = String.valueOf(tabID);
        // 创建tabSpec
        TabHost.TabSpec localTabSpec = mTabHost.newTabSpec(str).setIndicator(localView).setContent(intent);
        // 加载tabSpec
        mTabHost.addTab(localTabSpec);
        // 保存tab菜单中子菜单
        list.add(mRela);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 1000) {// 如果两次按键时间间隔大于800毫秒，则不退出
                Toast.makeText(MainTabHomeActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;// 更新firstTime
                return true;
            } else {
                ActivityCollector.finshAll();
                CommonValues.select_activity = 0 ;
                CommonValues.APP_IS_ACTIVE = false ;
                CommonValues.BREATH_IS_ACTIVE = false ;
                finish();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(25);
        mHandler.removeMessages(30);
    }
}
