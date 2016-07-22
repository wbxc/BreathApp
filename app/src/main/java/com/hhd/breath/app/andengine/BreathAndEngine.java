

package com.hhd.breath.app.andengine;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.R;
import com.hhd.breath.app.main.ui.BreathReportActivity;
import com.hhd.breath.app.model.BreathEngine;
import com.hhd.breath.app.model.BreathTrainingResult;
import com.hhd.breath.app.model.TrainPlan;
import com.hhd.breath.app.service.UploadDataService;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.wchusbdriver.Global340Driver;
import com.hhd.breath.app.widget.BreathToast;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BreathAndEngine extends SimpleBaseGameActivity {


    private static final float SCROLL_SPEED = 2.5f;  //游戏速度
    private Camera mCamera;
    private static final int STATE_READY = 1;
    private static final int STATE_PLAYING = 2;
    private static final int STATE_DYING = 3;
    private static final int STATE_DEAD = 4;
    private int GAME_STATE = 0;
    private int PREPARE_GAME_STATE = 0;
    private float mCurrentWorldPosition;  // 世界坐标
    private ResourceManager mResourceManager;
    private SceneManager mSceneManager;
    private Scene mScene;
    private ParallaxBackground mBackground;
    private ArrayList<PipePair> pipePairs = new ArrayList<PipePair>();
    private int mPipeSpawnCount;
    private static final int PIPE_SPAWN_INTERVAL = 80;
    private List<BreathEngine> breathEngines = new ArrayList<BreathEngine>();
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private int mCurrentEngine = 0;
    private int mBreathTime;
    private int mPauseTime;
    private boolean isNoStop = false;
    private List<Float> floats;
    private int totalTime = 0;
    private int totalTime1 = 0;
    private int groupNumbers;
    private long trainTime;
    private float birdPosition = 0f;
    float newY = 0.0f;
    private int scoreResult = 0;
    private MyHandler mHandler = null;
    private float globalValue = 0f;
    private Dialog trainEndDialog = null;
    private TrainPlan trainPlan;
    private TimerHandler gameTimer = null;


    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        //吸气时间
        mBreathTime = ShareUtils.getBrathTime(BreathAndEngine.this);
        mPauseTime = ShareUtils.getIntervalTime(BreathAndEngine.this);
        mHandler = new MyHandler(this);
        trainPlan = (TrainPlan) getIntent().getExtras().getSerializable("train_plan");
        groupNumbers = Integer.parseInt(trainPlan.getGroupNumber());
        mBreathTime = Integer.parseInt(trainPlan.getPersistentLevel());   ///持久力  表现为呼气时间
        mPauseTime = Integer.parseInt(trainPlan.getInspirerTime());
        totalTime = (mBreathTime + mPauseTime) * groupNumbers; // 总时间长
        totalTime1 = totalTime;
        for (int i = 0; i < groupNumbers; i++) {
            BreathEngine breathEngine = new BreathEngine();
            breathEngine.setPlayFlag(false);
            breathEngine.setStopFlag(false);
            breathEngine.setPlayInt(mBreathTime);
            breathEngine.setStopInt(mPauseTime);
            breathEngines.add(breathEngine);
        }
        floats = new ArrayList<Float>();
        Global340Driver.getInstance(BreathAndEngine.this).setEnableRead(true);
    }


    @Override
    protected synchronized void onResume() {
        super.onResume();
    }
    private static class MyHandler extends Handler {
        private WeakReference<BreathAndEngine> reference;
        private BreathAndEngine breathAndEngine;

        public MyHandler(BreathAndEngine context) {
            reference = new WeakReference<BreathAndEngine>(context);
            breathAndEngine = reference.get();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            breathAndEngine.GAME_STATE = 4;
            breathAndEngine.showDialog();
        }
    }


    private void showDialog() {

        if (trainEndDialog == null) {
            trainEndDialog = new Dialog(BreathAndEngine.this, R.style.common_dialog);
            View mView = LayoutInflater.from(BreathAndEngine.this).inflate(R.layout.layout_dialog_success, null);
            Button btnTrainEnd = (Button) mView.findViewById(R.id.btnTrainEnd);
            ImageView imgClose = (ImageView) mView.findViewById(R.id.imgDialogClose);
            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(BreathAndEngine.this, UploadDataService.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("breath_result", uploadService());
                    bundle.putSerializable("train_plan", trainPlan);
                    intent.putExtras(bundle);
                    startService(intent);
                    trainEndDialog.dismiss();
                    BreathAndEngine.this.finish();
                }
            });


            btnTrainEnd.setOnClickListener(new View.OnClickListener() {    //跳转到查看训练报告界面
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(BreathAndEngine.this, BreathReportActivity.class);
                    //训练建议
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("breathTrainingData", uploadService());
                    bundle.putSerializable("train_plan", trainPlan);
                    bundle.putString("train_type", trainPlan.getTrainType());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    BreathAndEngine.this.finish();
                    trainEndDialog.dismiss();
                }
            });
            trainEndDialog.setContentView(mView);
            trainEndDialog.setCanceledOnTouchOutside(false);
            trainEndDialog.setCancelable(false);
        }
        if (!trainEndDialog.isShowing())
            trainEndDialog.show();
    }


    /**
     * @return BreathTrainingResult 训练结果
     */
    private BreathTrainingResult uploadService() {
        BreathTrainingResult breathTrainingResult = new BreathTrainingResult();
        breathTrainingResult.setUser_id(ShareUtils.getUserId(BreathAndEngine.this));
        breathTrainingResult.setTrain_group(groupNumbers + "");
        breathTrainingResult.setBreath_type(trainPlan.getTrainType());
        breathTrainingResult.setBreath_name(trainPlan.getName());
        breathTrainingResult.setTrain_last(totalTime1 + "");
        breathTrainingResult.setTrain_result("");  // 训练结果
        breathTrainingResult.setDifficulty((scoreResult > 100 ? 0 : (100 - scoreResult)) + "");
        breathTrainingResult.setTrain_time(trainTime + "");
        breathTrainingResult.setSuggestion("");
        breathTrainingResult.setPlatform("1");
        String fileName = "file_" + String.valueOf(System.currentTimeMillis() / 1000);
        breathTrainingResult.setFname(fileName);  //  文件id  和文件名称 定义一致
        breathTrainingResult.setFile_id(fileName);
        breathTrainingResult.setDevice_sn(ShareUtils.getSerialNumber(BreathAndEngine.this));
        return breathTrainingResult;
    }


    //控制游戏的开始,暂停的操作
    private void controlGamePlay() {

        if (!breathEngines.get(mCurrentEngine).isPlayFlag()) {
            if (breathEngines.get(mCurrentEngine).getPlayInt() > 0) {
                isNoStop = true;
                int play = breathEngines.get(mCurrentEngine).getPlayInt() - 1;
                breathEngines.get(mCurrentEngine).setPlayInt(play);
                totalTime--;
                mSceneManager.displayCurrentTimes(totalTime);
                return;
            } else {
                breathEngines.get(mCurrentEngine).setPlayFlag(true);

            }
        }


        if (!breathEngines.get(mCurrentEngine).isStopFlag()) {
            if (breathEngines.get(mCurrentEngine).getStopInt() > 0) {
                isNoStop = false;
                int stop = breathEngines.get(mCurrentEngine).getStopInt() - 1;
                breathEngines.get(mCurrentEngine).setStopInt(stop);
                totalTime--;
                mSceneManager.displayCurrentTimes(totalTime);
                return;
            } else {
                breathEngines.get(mCurrentEngine).setStopFlag(true);
                if (mCurrentEngine < (groupNumbers - 1)) {
                    mCurrentEngine++;
                    mSceneManager.displayCurrentGroups(groupNumbers - mCurrentEngine);
                    isNoStop = true;;
                    mSceneManager.displayCurrentTimes(totalTime);
                    return;
                } else {
                    isNoStop = false;
                    mSceneManager.displayCurrentGroups(0);
                    mHandler.sendEmptyMessageDelayed(10, 500);
                }

            }
        }



    }


    private void startTimeAccount() {

        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            controlGamePlay();
                        }
                    });
                }
            };
        }
        mTimer.schedule(mTimerTask, 0, 1000);
    }


    private void stopTimeAccount() {

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    protected void onDestroy() {
        Global340Driver.getInstance(BreathAndEngine.this).setEnableRead(true);
        super.onDestroy();
        mHandler.removeMessages(10);
        stopTimeAccount();
    }


    String result = "";

    private void receiveData() {

        float value = 0f;
        result = Global340Driver.getInstance(BreathAndEngine.this).read();
        if (result.equals("123"))
            return;
        value = parseIntFromString(result);  //*distanceValue ;
        globalValue = (globalValue + value) / 2;

    }


    private float parseIntFromString(String result) {

        float value = 0f;
        if (result != null && !"".equals(result)) {
            if (result.length() == 6) {
                ShareUtils.setSerialNumber(BreathAndEngine.this, result);
            } else {
                char num[] = result.toCharArray();//把字符串转换为字符数组

                if (isDia(num)) {
                    value = Integer.parseInt(result.substring(0, 1)) * 100
                            + Integer.parseInt(result.substring(1, 2)) * 10
                            + Integer.parseInt(result.substring(2, 3));
                }
            }

        }
        return value;
    }

    private boolean isDia(char[] num) {
        boolean flag = true;
        for (int i = 0; i < num.length; i++) {
            if (!Character.isDigit(num[i])) {
                flag = false;
                return flag;
            }
        }
        return flag;
    }


    private void displayBirdPosition() {
        if (globalValue == 0) {
            newY = mSceneManager.mBird.move(CommonValues.SKY_BIRD_ALLOW);
            return;
        } else if (globalValue < 3) {
            birdPosition = CommonValues.SKY_BIRD_ALLOW - (CommonValues.BIRD_DISTANCE_SPEED * 1.5f);
            newY = mSceneManager.mBird.move(birdPosition);
            return;
        } else {
            birdPosition = CommonValues.SKY_BIRD_ALLOW - (CommonValues.BIRD_DISTANCE_SPEED * globalValue);
            newY = mSceneManager.mBird.move(birdPosition);
            return;
        }
    }

    private boolean isBreathShowIngFlag = false ;
    private boolean isInhelaShowIngFlag = false ;

    @Override
    public EngineOptions onCreateEngineOptions() {

        //根据给出的高度900  计算出应显示的屏幕的宽度
        CommonValues.CAMERA_WIDTH = ScreenSizeHelper.calculateScreenWidth(this, CommonValues.CAMERA_HEIGHT);
        mCamera = new Camera(0, 0, CommonValues.CAMERA_WIDTH, CommonValues.CAMERA_HEIGHT) {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                switch (GAME_STATE) {
                    case STATE_READY:
                        ready();
                        break;
                    case STATE_PLAYING:
                        //场景平移
                        mCurrentWorldPosition -= SCROLL_SPEED;
                        receiveData();  // globalValue
                        play(isNoStop);
                        displayBirdPosition();
                        break;
                    case STATE_DYING:
                        dead();
                        break;
                }
                super.onUpdate(pSecondsElapsed);
            }

            // 准备阶段
            private void ready() {
                mCurrentWorldPosition -= SCROLL_SPEED;
                if (!mResourceManager.mMusic.isPlaying()) {
                    mResourceManager.mMusic.play();
                }
            }

            private void play(boolean flag) {
                if (flag){  //呼气
                    if (!isBreathShowIngFlag){
                         isBreathShowIngFlag = true ;
                        mSceneManager.inhaleSprite.setVisible(false);
                        mSceneManager.breathSprite.setVisible(true);
                    }
                    isInhelaShowIngFlag = false ;
                }else {  //吸气
                    if (!isInhelaShowIngFlag){
                        isInhelaShowIngFlag = false ;
                        mSceneManager.breathSprite.setVisible(false);
                        mSceneManager.inhaleSprite.setVisible(true);
                    }
                    isBreathShowIngFlag = false ;

                }

                mPipeSpawnCount++;
                if (flag) {
                    if (mPipeSpawnCount > PIPE_SPAWN_INTERVAL) {
                        mPipeSpawnCount = 0;
                        spawnNewPipe();
                    }
                }
                for (int i = 0; i < pipePairs.size(); i++) {
                    if (startFlag) {
                        startTimeAccount();
                        trainTime = System.currentTimeMillis() / 1000;
                        startFlag = false;
                    }
                    PipePair pipe = pipePairs.get(i);
                    if (pipe.isOnScreen()) {
                        pipe.move(SCROLL_SPEED);
                        floats.add(globalValue);
                        switch (pipe.collidesWidth(mSceneManager.mBird.getSprite())) {
                            case 1:
                            case 2:
                                showText("碰壁上面");
                                break;
                            case 3:
                            case 4:
                                showText("碰壁下面");
                                break;
                            case 0:
                                break;
                        }
                    } else {
                        pipe.destroy();
                        pipePairs.remove(pipe);
                    }
                }
            }
        };
        EngineOptions options = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CommonValues.CAMERA_WIDTH, CommonValues.CAMERA_HEIGHT), mCamera);
        options.getAudioOptions().setNeedsMusic(true);
        options.getAudioOptions().setNeedsSound(true);
        return options;
    }

    private void prepareGame() {
        mScene.detachChild(mSceneManager.initSprite);
    }


    boolean is = false;
    private boolean startFlag = true;

    private void gameOver() {
        GAME_STATE = STATE_DYING;
        mResourceManager.mDieSound.play();
        mSceneManager.mBird.getSprite().stopAnimation();
    }

    private boolean showFlag = false;
    long tempTime = 0;

    private void showText(final String message) {
        if (!showFlag) {
            tempTime = System.currentTimeMillis();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    scoreResult += 5;
                    BreathToast.getInstance(BreathAndEngine.this).showToast();
                }
            });
            showFlag = true;
        } else if (showFlag) {
            long time = System.currentTimeMillis();

            if ((time - tempTime) > 150) {
                BreathToast.getInstance(BreathAndEngine.this).hideToast();
            }

            if ((time - tempTime) > 1000) {
                showFlag = false;
            }
        }
    }


    private void dead() {
        gameTimer = new TimerHandler(1.6f, false, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler timerHandler) {
                mScene.unregisterUpdateHandler(gameTimer);
                restartGame();
            }
        });
        mScene.registerUpdateHandler(gameTimer);
    }

    /**
     * 重新开始
     */
    private void restartGame() {
        GAME_STATE = STATE_PLAYING;
        mResourceManager.mMusic.resume();
        mSceneManager.mBird.getSprite().animate(25);
        mSceneManager.mBird.getSprite().setZIndex(2);
        mSceneManager.mBird.move(341);
    }


    // 导入资源
    @Override
    protected void onCreateResources() {
        mResourceManager = new ResourceManager(this);
        mResourceManager.createResources(2);
    }

    // 创建场景
    @Override
    protected Scene onCreateScene() {
        mBackground = new ParallaxBackground(82 / 255f, 190 / 255f, 206 / 255f) {

            float prevX = 0;
            float parallaxValueOffset = 0;

            @Override
            public void onUpdate(float pSecondsElapsed) {
                switch (GAME_STATE) {
                    case STATE_READY:
                    case STATE_PLAYING:
                        final float cameraCurrentX = mCurrentWorldPosition;
                        if (prevX != cameraCurrentX) {
                            parallaxValueOffset += cameraCurrentX - prevX;
                            this.setParallaxValue(parallaxValueOffset);

                            prevX = cameraCurrentX;
                        }
                        GAME_STATE = STATE_PLAYING;
                        break;
                }
                super.onUpdate(pSecondsElapsed);
            }
        };

        mSceneManager = new SceneManager(this, mResourceManager, mBackground);
        mScene = mSceneManager.createScene(totalTime);
        mScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene scene, TouchEvent touchEvent) {

                if (touchEvent.isActionDown()) {
                    switch (PREPARE_GAME_STATE) {
                        case 0:
                            mScene.detachChild(mSceneManager.mInstructionsSprite);
                            PREPARE_GAME_STATE = 1;
                            GAME_STATE = PREPARE_GAME_STATE;
                            isNoStop = true;
                            mSceneManager.displayCurrentGroups(groupNumbers);
                            prepareGame();
                            break;
                    }
                }
                return false;
            }
        });
        mSceneManager.displayCurrentGroups(groupNumbers);

        return mScene;
    }

    //
    private void spawnNewPipe() {
        PipePair newPiper = new PipePair(CommonValues.CENTER_HEIGHT, this.getVertexBufferObjectManager(), mScene);
        pipePairs.add(newPiper);
    }

}
