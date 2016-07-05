package com.hhd.breath.app.main.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.BreathApplication;
import com.hhd.breath.app.R;
import com.hhd.breath.app.model.SysDataModel;
import com.hhd.breath.app.net.ThreadPoolWrap;
import com.hhd.breath.app.service.GlobalUsbService;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.utils.Utils;
import com.hhd.breath.app.wchusbdriver.Global340Driver;
import com.hhd.breath.app.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

public class SystemSettingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout inspirationTime ; //吸气时间
    private RelativeLayout intervalTime ; // 间隔时间
    private RelativeLayout breathTime ; // 呼气时间
    private RelativeLayout actionGroup ; // 动作组数

    private TextView inspirationText ;
    private TextView intervalText ;
    private TextView breathText ;
    private TextView actionGroupText;
    private LayoutInflater mLayoutInflater ;

    private int inspirationValue =1;
    private int intervalValue  = 1;
    private int breathValue = 1 ;
    private int actionGroupValue = 1 ;
    private TextView topTextView ;
    private RelativeLayout backRe ;
    private RelativeLayout mLayoutSerial ;
    private final Handler handler = new Handler() ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        mLayoutInflater = LayoutInflater.from(SystemSettingActivity.this) ;
        Global340Driver.getInstance(SystemSettingActivity.this).setEnableRead(false);
        initData();
        initView();
        initEvent();
        try {
            boolean flag = Global340Driver.getInstance(SystemSettingActivity.this).send("3") ;
            if (flag){
                GlobalUsbService.isOpenBreath = false ;
            }
            Global340Driver.getInstance(SystemSettingActivity.this).setEnableRead(true);

            ThreadPoolWrap.getThreadPool().executeTask(readRunable);
        }catch (Exception e){

        }
    }

    private Runnable readRunable = new Runnable() {
        @Override
        public void run() {

            try {
                while (isNotEmpty(Global340Driver.getInstance(SystemSettingActivity.this).readSerial()));

            }catch (Exception e){

            }

        }
    } ;
    @Override
    protected void initView() {
        inspirationTime = (RelativeLayout)findViewById(R.id.inspirationTime) ;
        intervalTime = (RelativeLayout)findViewById(R.id.intervalTime) ;
        breathTime = (RelativeLayout)findViewById(R.id.breathTime) ;
        actionGroup = (RelativeLayout)findViewById(R.id.actionGroup) ;
        inspirationText = (TextView)findViewById(R.id.inspiration_textview) ;
        intervalText = (TextView)findViewById(R.id.interval_textView) ;
        breathText = (TextView)findViewById(R.id.breath_textView) ;
        actionGroupText = (TextView)findViewById(R.id.actionGroup_textView) ;
        topTextView = (TextView)findViewById(R.id.topText) ;
        backRe = (RelativeLayout)findViewById(R.id.back_re) ;
        mLayoutSerial = (RelativeLayout)findViewById(R.id.layout_serial) ;
    }

    @Override
    protected void initEvent() {
        topTextView.setText(getResources().getString(R.string.string_system_setting_top));
        inspirationTime.setOnClickListener(this);
        intervalTime.setOnClickListener(this);
        breathTime.setOnClickListener(this);
        actionGroup.setOnClickListener(this);
        backRe.setOnClickListener(this);
        mLayoutSerial.setOnClickListener(this);
        inspirationText.setText(inspirationModels.get(inspirationValue - 1).getName());
        intervalText.setText(intervalModels.get(intervalValue-1).getName());
        breathText.setText(breathModels.get(breathValue-1).getName());
        actionGroupText.setText(actionModels.get(actionGroupValue-1).getName());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.inspirationTime:
                showInspirationTime();
                break;
            case R.id.back_re:
                SystemSettingActivity.this.finish();
                break;
            case R.id.intervalTime:
                if (interalDialog == null){
                    interalDialog = showDialogPopuWindow(intervalValue,intervalStrings,
                            intervalModels,mIntervalOnclickListener,mIntervalWhellOnListener);
                }
                interalDialog.show();
                break ;
            case R.id.breathTime:
                if (breathDialog == null){
                    breathDialog = showDialogPopuWindow(breathValue,breathStrings,breathModels,breathOnclick,breathWheelOnclick);
                }
                breathDialog.show();
                break;
            case R.id.actionGroup:
                if (actionDialog == null){
                    actionDialog = showDialogPopuWindow(actionGroupValue,actionStrings,actionModels,actionGroupOnclick,actionWheelOnclick);
                }
                actionDialog.show();
                break;
            case R.id.layout_serial:
                if (!isNotEmpty(ShareUtils.getSerialNumber(SystemSettingActivity.this))){
                    getSerial();
                }else {
                    BreathSerialActivity.actionStart(SystemSettingActivity.this);
                }
                break;
        }
    }

    private String  result = "" ;
    private void getSerial(){
        try {
            Global340Driver.getInstance(SystemSettingActivity.this).setEnableRead(true);
           // Global340Driver.getInstance(SystemSettingActivity.this).readSerial() ;

            boolean flag1 = Global340Driver.getInstance(SystemSettingActivity.this).send("4") ;
            if (flag1){
                showProgressDialog("");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        result = Global340Driver.getInstance(SystemSettingActivity.this).readSerial();

                        hideProgress();
                        if (!isNotEmpty(result)) {

                            try {
                                Thread.sleep(100);
                                result = Global340Driver.getInstance(SystemSettingActivity.this).readSerial();
                            }catch (Exception e){

                            }
                        }
                        if (Utils.isNoEmpty(result)){
                            ShareUtils.setSerialNumber(SystemSettingActivity.this, result);
                        }
                        BreathSerialActivity.actionStart(SystemSettingActivity.this);
                    }
                }, 1000) ;
            }else {
                BreathApplication.toast(SystemSettingActivity.this,getResources().getString(R.string.string_health_error_serial));
            }
            Global340Driver.getInstance(SystemSettingActivity.this).setEnableRead(false);

        }catch (Exception e){
            Global340Driver.getInstance(SystemSettingActivity.this).setEnableRead(false);
            BreathApplication.toast(SystemSettingActivity.this, getString(R.string.string_health_no_connect));
            Global340Driver.getInstance(SystemSettingActivity.this).read() ;
        }
    }

    private View.OnClickListener actionGroupOnclick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (actionDialog.isShowing()){
                actionDialog.dismiss();
            }
            ShareUtils.setActionGroup(SystemSettingActivity.this, actionGroupValue);

        }
    } ;

    private WheelView.OnWheelViewListener actionWheelOnclick = new WheelView.OnWheelViewListener(){

        @Override
        public void onSelected(int selectedIndex, String item) {
            super.onSelected(selectedIndex, item);

            actionGroupValue = Integer.parseInt(actionModels.get(selectedIndex).getValue()) ;
            actionGroupText.setText(actionModels.get(selectedIndex).getName());
        }
    } ;

    private View.OnClickListener breathOnclick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if (breathDialog.isShowing()){
                breathDialog.dismiss();
            }
            ShareUtils.setBrathTime(SystemSettingActivity.this,breathValue);

        }
    } ;

    private WheelView.OnWheelViewListener breathWheelOnclick = new WheelView.OnWheelViewListener(){

        @Override
        public void onSelected(int selectedIndex, String item) {
            super.onSelected(selectedIndex, item);

            breathValue = Integer.parseInt(breathModels.get(selectedIndex).getValue()) ;
            breathText.setText(breathModels.get(selectedIndex).getName());
        }
    } ;





    private View.OnClickListener mIntervalOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (interalDialog.isShowing()){
                interalDialog.dismiss();
            }
            ShareUtils.setIntervalTime(SystemSettingActivity.this,intervalValue);
        }
    } ;
    private WheelView.OnWheelViewListener  mIntervalWhellOnListener = new WheelView.OnWheelViewListener(){

        @Override
        public void onSelected(int selectedIndex, String item) {
            super.onSelected(selectedIndex, item);
            intervalValue = Integer.parseInt(intervalModels.get(selectedIndex).getValue()) ;
            intervalText.setText(intervalModels.get(selectedIndex).getName());
        }
    } ;



    private List<SysDataModel> inspirationModels ;
    private List<String> inspirationStrings ;
    private Dialog inspirationDialog = null ;

    private List<SysDataModel> intervalModels ;
    private List<String> intervalStrings ;
    private Dialog interalDialog  = null ;



    private List<SysDataModel> breathModels ;
    private List<String> breathStrings ;
    private Dialog breathDialog = null ;

    private List<SysDataModel> actionModels ;
    private List<String> actionStrings ;
    private Dialog actionDialog = null ;





    private void initData(){
        inspirationValue = ShareUtils.getInspirationTime(SystemSettingActivity.this) ;
        intervalValue = ShareUtils.getIntervalTime(SystemSettingActivity.this) ;
        breathValue = ShareUtils.getBrathTime(SystemSettingActivity.this) ;
        actionGroupValue = ShareUtils.getActionGroup(SystemSettingActivity.this) ;


        inspirationModels = new ArrayList<SysDataModel>() ;
        inspirationStrings = new ArrayList<String>() ;

        for (int i=0  ; i<10 ; i++){
            SysDataModel mSysDataModel = new SysDataModel() ;
            mSysDataModel.setId(String.valueOf(i));
            mSysDataModel.setName(String.valueOf(i + 1) + "秒");
            mSysDataModel.setValue(String.valueOf(i + 1));
            inspirationStrings.add(String.valueOf(i+1)+"秒")  ;
            inspirationModels.add(mSysDataModel) ;
        }

        intervalModels = new ArrayList<SysDataModel>() ;
        intervalStrings = new ArrayList<String>()  ;

        for (int i=0  ; i<5 ; i++){
            SysDataModel mSysDataModel = new SysDataModel() ;
            mSysDataModel.setId(String.valueOf(i));
            mSysDataModel.setName(String.valueOf(i + 1) + "秒");
            mSysDataModel.setValue(String.valueOf(i + 1));
            intervalStrings.add(String.valueOf(i + 1) + "秒")  ;
            intervalModels.add(mSysDataModel) ;
        }

        breathModels = new ArrayList<SysDataModel>() ;
        breathStrings = new ArrayList<String>() ;

        for (int i=0  ; i<20 ; i++){
            SysDataModel mSysDataModel = new SysDataModel() ;
            mSysDataModel.setId(String.valueOf(i));
            mSysDataModel.setName(String.valueOf(i + 1) + "秒");
            mSysDataModel.setValue(String.valueOf(i + 1));
            breathStrings.add(String.valueOf(i + 1) + "秒")  ;
            breathModels.add(mSysDataModel) ;
        }



        actionModels = new ArrayList<SysDataModel>() ;
        actionStrings = new ArrayList<String>() ;

        for (int i=0  ; i<40 ; i++){
            SysDataModel mSysDataModel = new SysDataModel() ;
            mSysDataModel.setId(String.valueOf(i));
            mSysDataModel.setName(String.valueOf(i + 1) + "组");
            mSysDataModel.setValue(String.valueOf(i + 1));
            actionStrings.add(String.valueOf(i + 1) + "组")  ;
            actionModels.add(mSysDataModel) ;
        }

    }


    private Dialog showDialogPopuWindow(int index , List<String> mStrings ,
                                      List<SysDataModel> mSysDataModels,
                                      View.OnClickListener mOnClickListener,WheelView.OnWheelViewListener mOnWheelViewListener){


        Dialog mDialog = null;

        if (mDialog == null){
            View mInspirationView  = mLayoutInflater.inflate(R.layout.popu_system_dialog,null) ;
            WheelView mWheelView = (WheelView)mInspirationView.findViewById(R.id.wheel_view_wv) ;
            Button mButton = (Button)mInspirationView.findViewById(R.id.queding) ;
            mButton.setOnClickListener(mOnClickListener);

            mWheelView.setOffset(2);
            mWheelView.setItems(mStrings);

            mWheelView.setSeletion(index-1);

            mWheelView.setOnWheelViewListener(mOnWheelViewListener);

            mDialog = new Dialog(SystemSettingActivity.this,R.style.common_dialog) ;
            mDialog.setCancelable(true);
            ViewGroup.LayoutParams  mParmas = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) ;
            mDialog.setContentView(mInspirationView, mParmas);
        }

        return mDialog ;
    }




    private void showInspirationTime(){

        if (inspirationDialog == null){
            View mInspirationView  = mLayoutInflater.inflate(R.layout.popu_system_dialog,null) ;
            WheelView mWheelView = (WheelView)mInspirationView.findViewById(R.id.wheel_view_wv) ;
            Button mButton = (Button)mInspirationView.findViewById(R.id.queding) ;
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareUtils.setInspirationTime(SystemSettingActivity.this, inspirationValue);
                    inspirationDialog.dismiss();
                }
            });

            mWheelView.setOffset(2);
            mWheelView.setItems(inspirationStrings);

            mWheelView.setSeletion(inspirationValue-1);

            mWheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(final int selectedIndex, String item) {
                    super.onSelected(selectedIndex, item);

                    if (selectedIndex < inspirationModels.size()) {
                        SystemSettingActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                inspirationText.setText(inspirationModels.get(selectedIndex).getName());
                                inspirationValue = Integer.parseInt(inspirationModels.get(selectedIndex).getValue()) ;
                            }
                        });
                    }
                }
            });

            inspirationDialog = new Dialog(SystemSettingActivity.this,R.style.common_dialog) ;
            inspirationDialog.setCancelable(true);
            ViewGroup.LayoutParams  mParmas = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) ;
            inspirationDialog.setContentView(mInspirationView, mParmas);
        }
        inspirationDialog.show();
    }
}
