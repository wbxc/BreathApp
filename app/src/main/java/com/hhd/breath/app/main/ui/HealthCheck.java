package com.hhd.breath.app.main.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hhd.breath.app.BreathApplication;
import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.R;
import com.hhd.breath.app.andengine.HealthResourceManager;
import com.hhd.breath.app.andengine.HealthScreenManger;
import com.hhd.breath.app.andengine.ScreenSizeHelper;
import com.hhd.breath.app.db.HealthDataService;
import com.hhd.breath.app.imp.ConnectInterface;
import com.hhd.breath.app.model.HealthData;
import com.hhd.breath.app.net.ThreadPoolWrap;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.wchusbdriver.CH340AndroidDriver;
import com.hhd.breath.app.wchusbdriver.CH340CheckDriver;
import com.hhd.breath.app.wchusbdriver.Global340Driver;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HealthCheck extends SimpleBaseGameActivity {


    private HealthResourceManager resourceManager ;
    private HealthScreenManger screenManger ;
    private Scene scene ;
    private Camera camera ;
    public static final float CAMERA_HEIGHT =  900f ;//800f ;
    public static float CAMERA_WIDTH = 545f ;//485f ;
    private ParallaxBackground mBackground ;
    private boolean READ_ENABLE_340  = false;
    private Object ThreadLock = new Object() ;
    private int actualNumBytes = 0 ;
    private boolean isReceiveData = false ;
    private byte[] readBuffer = new byte[256] ;
    private byte[] writeBuffer = new byte[256] ;
    private static int GAME_STATE = 0 ;
    private static final int STATE_READY = 1 ;
    private static final int STATE_PLAYING = 2 ;
    private static final int STATE_DYING = 3 ;
    private static final int STATE_DEAD = 4 ;
    private int globalValue = 0 ;
    private float mCurrentWorldPosition ;  // 世界坐标
    private float SCROLL_SPEED = 4.5f ;
    private int maxValue = 0 ;
    private List<Integer> integerList = new ArrayList<Integer>() ;
    private String maxRate  ;
    private String secondValue ="0" ;
    private String comValue  ;
    private int current = 30 ;
    private boolean endFlag = false ;
    private int total = 0 ;
    private MyHandler handler = null;
    private Timer timer = null ;
    private TimerTask timerTask = null;


    private static class MyHandler extends Handler{

        private WeakReference<HealthCheck> reference ;
        private HealthCheck healthCheck ;

        public MyHandler(HealthCheck context) {
            reference = new WeakReference<HealthCheck>(context) ;
            healthCheck = reference.get() ;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 30:
                    healthCheck.stopTask();
                    healthCheck.showDialog();
                    break;
                case 31:
                    healthCheck.function31();
                    break;
                case 32:
                    healthCheck.function32();
                    break;
            }
        }
    }
    private void function31(){
        setResult(12);
        HealthCheck.this.finish();
    }

    private void function32(){
        setResult(11);
        HealthCheck.this.finish();
    }

    @Override
    public void onBackPressed() {
        setResult(13);
        super.onBackPressed();
    }

    private void startTask(){
        if (timer==null){
            timer = new Timer() ;
        }
        if (timerTask == null){
           timerTask = new TimerTask() {
               @Override
               public void run() {
                 receive();
               }
           } ;
        }
        timer.schedule(timerTask, 0, 50);
    }

    private void stopTask(){

        if (timer==null){
            timer.cancel();
            timer = null;
        }
        if (timerTask==null){
            timerTask.cancel() ;
            timerTask = null;
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new MyHandler(this) ;

        GAME_STATE = STATE_PLAYING ;
        integerList.clear();
        isReceiveData = true ;
        Global340Driver.getInstance(HealthCheck.this).setEnableRead(isReceiveData);
        startTask();
    }

    @Override
    protected synchronized void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        isReceiveData = false ;
        Global340Driver.getInstance(HealthCheck.this).setEnableRead(isReceiveData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isReceiveData = false ;
        stopTask();
        Global340Driver.getInstance(HealthCheck.this).setEnableRead(isReceiveData);
        Global340Driver.getInstance(HealthCheck.this).read() ;
        handler.removeMessages(30);
        handler.removeMessages(31);
        handler.removeMessages(32);
    }

    private void receive(){

        int value = 0 ;
        String result = Global340Driver.getInstance(HealthCheck.this).read() ;
        if (result.equals("123"))
            return;
        value = parseIntFromString(result);  //*distanceValue ;

        if (value>3){
            integerList.add(value) ;
            total =total+value ;
            if (value>maxValue)
                maxValue = value ;
        }
        globalValue = (globalValue+value)/2 ;
    }


    private int parseIntFromString(String result) {

        int value = 0;
        value = Integer.parseInt(result.substring(0, 1)) * 100
                + Integer.parseInt(result.substring(1, 2)) * 10
                + Integer.parseInt(result.substring(2, 3));
        return value;
    }




    @Override
    public EngineOptions onCreateEngineOptions() {
        CAMERA_WIDTH = ScreenSizeHelper.calculateScreenWidth(this,CAMERA_HEIGHT) ;
        camera = new Camera(0,0,CAMERA_WIDTH,CAMERA_HEIGHT){   //实时刷新界面
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                switch (GAME_STATE){
                    case STATE_READY:
                        break;
                    case STATE_PLAYING:
                        if (globalValue>3){
                            mCurrentWorldPosition -= SCROLL_SPEED ;
                            if (current<(CAMERA_WIDTH/3*2)){
                                screenManger.getBird().move((float)(current++),598-(globalValue*CommonValues.D_UNIT)) ;
                            }else {
                                screenManger.getBird().move((float)(current),598-(globalValue*CommonValues.D_UNIT)) ;
                            }
                            maxRate = String.valueOf((int) (getCurrentValue(maxValue)*CommonValues.M_UNIT)) ;
                            screenManger.getDisplayHeight().setText(maxRate);
                            comValue = String.valueOf((int) (total*CommonValues.M_UNIT)) ;
                            screenManger.getmDisplayDistance().setText(comValue);
                            endFlag  = true ;
                        }else {
                            if (endFlag){
                                endFlag = false ;
                                isReceiveData = false ;
                                Global340Driver.getInstance(HealthCheck.this).setEnableRead(false);
                                Global340Driver.getInstance(HealthCheck.this).read() ;

                                handler.sendEmptyMessage(30) ;
                            }else {
                                screenManger.getBird().move((float) (current), 598 - (globalValue*CommonValues.D_UNIT)) ;
                            }
                        }
                        break;
                    case STATE_DEAD:
                        break;
                }
            }
        } ;

        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,new RatioResolutionPolicy(CAMERA_WIDTH,CAMERA_HEIGHT),camera) ;
        engineOptions.getAudioOptions().setNeedsSound(true) ;
        engineOptions.getAudioOptions().setNeedsMusic(true) ;
        return engineOptions;
    }



    private Dialog endCheckDialog = null;
    private void showDialog(){

        if (endCheckDialog==null){
            endCheckDialog = new Dialog(HealthCheck.this,R.style.common_dialog) ;
            View mView = LayoutInflater.from(HealthCheck.this).inflate(R.layout.layout_dialog_check,null) ;

            Button btnSave = (Button)mView.findViewById(R.id.btnSave) ;
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    endCheckDialog.dismiss();
                    ThreadPoolWrap.getThreadPool().executeTask(saveCheckData);

                }
            });
            Button btnNoSave = (Button)mView.findViewById(R.id.btnNoSave) ;
            btnNoSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    endCheckDialog.dismiss();
                    HealthCheck.this.finish();
                }
            });
            endCheckDialog.setContentView(mView);
            endCheckDialog.setCancelable(false);
            endCheckDialog.setCanceledOnTouchOutside(false);
            endCheckDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
        endCheckDialog.show();
    }


    private Runnable saveCheckData = new Runnable() {
        @Override
        public void run() {
            int temp=0;
            if (integerList.size()>25){
                for (int i=0 ; i<25 ;i++){
                    temp = temp + integerList.get(i) ;
                }
                secondValue = String.valueOf((100 * temp)/total) ;
            }else if (integerList.size()>20){
                for (int i=0 ; i<20 ;i++){
                    temp = temp + integerList.get(i) ;
                }
                secondValue = String.valueOf((100 * temp)/total)  ;
            }

            HealthData healthData = new HealthData() ;
            healthData.setUserId(ShareUtils.getUserId(HealthCheck.this));
            healthData.setMaxRate(maxRate);
            healthData.setSecondValue(secondValue);
            healthData.setCompValue(comValue);

            SimpleDateFormat spf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss") ;
            String strTime = spf.format(new Date(System.currentTimeMillis())) ;
            //strTime = strTime.substring(5,10) ;
            strTime = strTime.substring(11,16) ;

            healthData.setTime(strTime);
            if (HealthDataService.getInstance(HealthCheck.this).add(healthData) ){
               handler.sendEmptyMessage(31) ;
            }else {
               handler.sendEmptyMessage(32) ;
            }
        }
    } ;



    @Override
    protected Scene onCreateScene() {


        mBackground = new ParallaxBackground(82/255f,190f/255f,206f/255f) {

            float prevX = 0 ;
            float parallaxValueOffset = 0 ;

            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                switch (GAME_STATE){
                    case STATE_READY:
                    case STATE_PLAYING:
                        final float cameraCurrentX = mCurrentWorldPosition ;
                        if (prevX !=cameraCurrentX){
                            parallaxValueOffset += cameraCurrentX-prevX ;
                            this.setParallaxValue(parallaxValueOffset);
                            prevX = cameraCurrentX ;
                        }
                        break;
                }
            }
        } ;
        screenManger = new HealthScreenManger(this,resourceManager,mBackground) ;
        scene = screenManger.createScene() ;
        return scene;
    }

    @Override
    protected void onCreateResources() {
        resourceManager = new HealthResourceManager(this) ;
        resourceManager.createResources();
    }


    private float getCurrentValue(int maxValue){
        float temp =0;
        switch (maxValue){
            case 3:
                temp = 4;
                break;
            case 4:
                temp = 6 ;
                break;
            case 5:
                temp = (float) 7.5 ;
                break;
            case 6:
                temp = 9 ;
                break;
            case 7:
                temp = 12 ;
                break;
            case 8:
                temp = 13 ;
                break;
            default:
                temp = (float) maxValue ;
                break;
        }
        return temp ;
    }


}
