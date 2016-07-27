package com.hhd.breath.app.main.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.R;
import com.hhd.breath.app.db.TrainHisService;
import com.hhd.breath.app.db.TrainPlanService;
import com.hhd.breath.app.model.BreathDetailReport;
import com.hhd.breath.app.model.BreathDetailSuccess;
import com.hhd.breath.app.model.BreathHisLog;
import com.hhd.breath.app.model.BreathTrainingResult;
import com.hhd.breath.app.model.TrainPlan;
import com.hhd.breath.app.model.TrainPlanLog;
import com.hhd.breath.app.net.ManagerRequest;
import com.hhd.breath.app.net.ThreadPoolWrap;
import com.hhd.breath.app.net.UploadRecordData;
import com.hhd.breath.app.utils.FileUtils;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.utils.StringUtils;
import com.hhd.breath.app.utils.UiUtils;
import com.hhd.breath.app.utils.Util;
import com.hhd.breath.app.utils.Utils;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXFileObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;


/**
 * 训练完成后查看
 * 训练完成查看的训练报告
 */
public class BreathReportActivity extends BaseActivity implements View.OnClickListener {


    private BreathTrainingResult mRecordDayData;
    private ImageView mImgShare;
    private String phone ;
    private String userName ;
    private String sex ;
    private String birthday ;
    private String mTrainTime ;
    private int mWidth = 0 ;
    private int mHeight = 0  ;
    private int mStatusHeight = 0 ;
    private IWXAPI api;
    private Dialog completeDialog = null;
    private TrainPlan trainPlan ;
    private String  filepath ;     // 文件路径
    private String  file_zip_path  ;  // 压缩文件路径
    private TrainPlanLog trainPlanLog   ;
    private BreathHisLog breathHisLog ;
    private String record_id = "" ;
    private RelativeLayout layoutBack;
    private TextView tvTop;
    private TextView tvScore;
    private TextView tvTrainTime;
    @Bind(R.id.tvBreathGroups)
    TextView tvBreathGroups ;
    @Bind(R.id.levelControlRa)
    RatingBar levelControlRa ;
    @Bind(R.id.levelStrengthRa)
    RatingBar levelStrengthRa ;
    @Bind(R.id.levelPersistentRa)
    RatingBar levelPersistentRa ;
    @Bind(R.id.tvDifficultyShow)
    TextView tvDifficultyShow ; // 难度系数显示
    @Bind(R.id.levelControlInitRa)
    RatingBar levelControlInitRa ;
    @Bind(R.id.levelStrengthInitRa)
    RatingBar levelStrengthInitRa ;
    @Bind(R.id.levelPrensterInitRa)
    RatingBar levelPrensterInitRa ;
    @Bind(R.id.levelControlCurrentRa)
    RatingBar levelControlCurrentRa ;
    @Bind(R.id.levelStrengthCurrentRa)
    RatingBar levelStrengthCurrentRa ;
    @Bind(R.id.levelPrensterCurrentRa)
    RatingBar levelPrensterCurrentRa ;
    @Bind(R.id.tvTrainResult)
    TextView tvTrainResult ;   // 训练结果
    @Bind(R.id.tvHisContent)
    TextView tvHisContent ;

    private String str_startTime ;
    private String str_train_days ;
    private String str_train_times ;
    private String str_train_aver_times ;
    private String str_train_result ;
    @Bind(R.id.tv1) TextView tv1 ;
    @Bind(R.id.tv2) TextView tv2 ;
    @Bind(R.id.tv3) TextView tv3 ;
    @Bind(R.id.tv4) TextView tv4 ;
    @Bind(R.id.tv5) TextView tv5 ;
    @Bind(R.id.tvAverValue) TextView tvAverValue ;
    private String timeLast = "0" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_report);
        ButterKnife.bind(this);
        WindowManager manager  = this.getWindowManager() ;
        mWidth = manager.getDefaultDisplay().getWidth() ;
        trainPlanLog = new TrainPlanLog() ;
        breathHisLog = new BreathHisLog() ;
        api = WXAPIFactory.createWXAPI(this, "wx92ff63ca90677197");
        mRecordDayData = (BreathTrainingResult) getIntent().getExtras().getSerializable("breathTrainingData");
        trainPlan = (TrainPlan)getIntent().getExtras().getSerializable("train_plan") ;
        trainPlanLog = TrainPlanService.getInstance(BreathReportActivity.this).findTrainPlanLog(trainPlan.getName(),ShareUtils.getUserId(BreathReportActivity.this)) ;
        timeLast = mRecordDayData.getTrain_last() ;
        initView();
        initEvent();
        mStatusHeight = Utils.getStatusHeight(this) ;
        filepath =CommonValues.PATH_ZIP+mRecordDayData.getUser_id()+"/"+mRecordDayData.getFname();
        file_zip_path = CommonValues.PATH_ZIP+mRecordDayData.getUser_id()+"/"+mRecordDayData.getFname()+"_zip" ;
        showProgressDialog("");
        UploadRecordData.getInstance().setOnUploadProcessListener(new UploadRecordData.OnUploadProcessListener() {
            @Override
            public void onUploadDone(int responseCode, final String message) {

                switch (responseCode){
                    case UploadRecordData.UPLOAD_SUCCESS_CODE:

                        FileUtils.deleteFolder(filepath) ;
                        FileUtils.deleteFolder(file_zip_path) ;
                        String id = "" ;
                        try {
                            JSONObject mesJsonObject = new JSONObject(message) ;
                            if (mesJsonObject.has("code") && mesJsonObject.getString("code").equals("200")){
                                JSONObject dataJsonObject = mesJsonObject.getJSONObject("data") ;
                                id = dataJsonObject.getString("id") ;  // 获取到的是
                            }
                        }catch (Exception e){

                        }
                        if (trainPlanLog==null){
                            trainPlanLog.setUserId(ShareUtils.getUserId(BreathReportActivity.this));
                            trainPlanLog.setName(trainPlan.getName());
                            trainPlanLog.setTrainType(trainPlan.getTrainType());
                            trainPlanLog.setTrainStartTime(String.valueOf(System.currentTimeMillis()));
                        }
                        TrainPlanService.getInstance(BreathReportActivity.this).addTrainLog(trainPlanLog);  //本地记录 记录一次
                        Log.e("BreathReportActivity",trainPlanLog.toString()) ;
                        TrainPlanService.getInstance(BreathReportActivity.this).countSumTime(timeLast,trainPlan); // 训练计划 时间累计  //时间累计
                        Message msg = Message.obtain() ;
                        msg.what = 40 ;
                        msg.obj = id ;
                        handler.sendMessage(msg) ;
                        break;
                }
            }

            @Override
            public void onUploadProcess(int uploadSize) {

            }

            @Override
            public void initUpload(int fileSize) {

            }
        });
        UploadRecordData.getInstance().uploadRecordData(mRecordDayData);

        tvScore.setText(mRecordDayData.getDifficulty());
        tvTrainTime.setText(timeStampToData(mRecordDayData.getTrain_time()));
        mTrainTime = longTimeToTime(mRecordDayData.getTrain_time()) ;
        tvBreathGroups.setText(mRecordDayData.getTrain_group());


        phone = ShareUtils.getUserPhone(BreathReportActivity.this) ;
        sex = ShareUtils.getUserSex(BreathReportActivity.this) ;

    }
    @Override
    protected void initView() {
        layoutBack = (RelativeLayout) findViewById(R.id.back_re);
        tvTop = (TextView) findViewById(R.id.topText);
        tvScore = (TextView) findViewById(R.id.tvScore);
        tvTrainTime = (TextView) findViewById(R.id.tvTrainTime);
        mImgShare = (ImageView) findViewById(R.id.img_share);
    }

    private  Handler  handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 40 :
                    //Toast.makeText(BreathReportActivity.this,"显示40",Toast.LENGTH_SHORT).show();
                    record_id = (String) msg.obj ;
                    hideProgress();
                    ManagerRequest.getInstance().getRequestNetApi().getBreathDetailReport(record_id).enqueue(new retrofit2.Callback<BreathDetailSuccess>() {
                        @Override
                        public void onResponse(Call<BreathDetailSuccess> call, Response<BreathDetailSuccess> response) {



                            if (response.body().getCode().equals("200")){

                                if (TrainHisService.getInstance(BreathReportActivity.this).addBreathDetialReport(response.body().getData())){
                                    new Thread(runnable).start();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<BreathDetailSuccess> call, Throwable t) {


                        }
                    });
                    break;
            }
        }
    } ;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            TrainPlanLog PlanLog = TrainPlanService.getInstance(BreathReportActivity.this).findTrainPlanLog(trainPlan.getName(),trainPlan.getUserId()) ;
            List<BreathDetailReport> breathDetailReports = new ArrayList<BreathDetailReport>() ;
            List<BreathDetailReport>  breathDetailReports1 = TrainHisService.getInstance(BreathReportActivity.this).findFiveBreathDetailReports(trainPlan.getTrainType(),ShareUtils.getUserId(BreathReportActivity.this)) ;
            for (int k= 0 ; k <breathDetailReports1.size() ; k++ ){
                breathDetailReports.add(breathDetailReports1.get(breathDetailReports1.size()-1-k)) ;

            }
            if (breathDetailReports!=null){

                StringBuffer sb = new StringBuffer() ;
                float sumValue = 0f;
                for (int i=0 ; i< breathDetailReports.size() ; i++){
                    sb.append(breathDetailReports.get(i).getDifficulty()) ;
                    sumValue +=Integer.parseInt(breathDetailReports.get(i).getDifficulty()) ;
                    if (i!=4){
                        sb.append(",") ;
                    }
                }

                if (breathDetailReports.size()<5){

                    for (int j = breathDetailReports.size() ; j<5 ; j++){

                        sb.append("-")  ;

                        if (j!=4){
                            sb.append(",") ;
                        }
                    }
                }
                String averValue = String.valueOf(sumValue/breathDetailReports.size()) ; // 最近五次 或小于五次的平均值
                breathHisLog.setTrainStageValue(sb.toString());
                breathHisLog.setTrainAverValue(averValue);
                breathHisLog.setControlLevel(trainPlan.getControl());
                breathHisLog.setStrengthLevel(trainPlan.getStrength());
                breathHisLog.setPersistentLevel(trainPlan.getPersistent());


                int times = Integer.parseInt(trainPlan.getTimes()) ;
                int upgradeValue = 0 ;
                if (breathDetailReports.size()>times){
                    for (int k = breathDetailReports.size()-times ; k<breathDetailReports.size();k++){
                        upgradeValue +=Integer.parseInt(breathDetailReports.get(k).getDifficulty()) ;
                    }
                }

                if (upgradeValue == (times*100)){
                    istigao = true ;
                    int c = Integer.parseInt(trainPlan.getCurrentControl()) ;
                    switch (c){
                        case 1:
                            trainPlan.setCurrentControl(String.valueOf(2));
                            trainPlan.setControlLevel(String.valueOf(CommonValues.c_z_value));
                            break;
                        case 2:
                            trainPlan.setCurrentControl(String.valueOf(3));
                            trainPlan.setControlLevel(String.valueOf(CommonValues.c_h_value));
                            break;
                        case 3:
                            int s = Integer.parseInt(trainPlan.getCurrentStrength()) ;
                            switch (s){
                                case 1:
                                    trainPlan.setCurrentStrength(String.valueOf(2));
                                    trainPlan.setStrengthLevel(CommonValues.s_z_value);
                                    break;
                                case 2:
                                    trainPlan.setCurrentStrength(String.valueOf(3));
                                    trainPlan.setStrengthLevel(CommonValues.s_h_value);
                                    break;
                                case 3:
                                    int p = Integer.parseInt(trainPlan.getCurrentPersistent());
                                    switch (p){
                                        case 1:
                                            trainPlan.setCurrentPersistent(String.valueOf(2));
                                            trainPlan.setPersistentLevel(CommonValues.p_z_value);
                                            break;
                                        case 2:
                                            trainPlan.setCurrentPersistent(String.valueOf(3));
                                            trainPlan.setPersistentLevel(CommonValues.p_z_value);
                                            break;
                                        case 3:
                                            break;
                                    }
                                    break;
                            }
                            break;
                    }
                    TrainPlanService.getInstance(BreathReportActivity.this).updateTrainPlan(trainPlan) ;
                    // 时间的累加
                }

                breathHisLog.setCurrentControlLevel(trainPlan.getCurrentControl());
                breathHisLog.setCurrentStrengthLevel(trainPlan.getCurrentStrength());
                breathHisLog.setCurrentPersistentLevel(trainPlan.getCurrentPersistent());
                breathHisLog.setTrainDays(String.valueOf(PlanLog.getDays()));
                breathHisLog.setRecord_id(record_id);
                breathHisLog.setTrainStartTime(PlanLog.getTrainStartTime());
                breathHisLog.setTrainTimes(String.valueOf(PlanLog.getTrainTimes()));
                breathHisLog.setTrainSuccessTimes(trainPlan.getTimes());
                breathHisLog.setTrainAverTimes(String.valueOf(PlanLog.getTrainTimes()/PlanLog.getDays()));


            }
            //结果判断
            if (istigao){
                breathHisLog.setTrainResult("有提高，继续努力!!");
            }else {
                breathHisLog.setTrainResult("继续努力!!");
            }
            TrainHisService.getInstance(BreathReportActivity.this).addBreathHisLog(breathHisLog);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    str_startTime = breathHisLog.getTrainStartTime() ;
                    str_train_days = breathHisLog.getTrainDays() ;
                    str_train_result = breathHisLog.getTrainResult() ;
                    str_train_times = breathHisLog.getTrainTimes() ;
                    str_train_aver_times = breathHisLog.getTrainAverTimes() ;
                    tvHisContent.setText(Html.fromHtml(reportHisBack(str_startTime,str_train_days,str_train_times,str_train_aver_times,str_train_result)));
                    levelControlRa.setRating(Float.valueOf(breathHisLog.getCurrentControlLevel()));
                    levelStrengthRa.setRating(Float.valueOf(breathHisLog.getCurrentStrengthLevel()));
                    levelPersistentRa.setRating(Float.valueOf(breathHisLog.getCurrentPersistentLevel()));


                    levelControlInitRa.setRating(Float.valueOf(breathHisLog.getControlLevel()));
                    levelStrengthInitRa.setRating(Float.valueOf(breathHisLog.getStrengthLevel()));
                    levelPrensterInitRa.setRating(Float.valueOf(breathHisLog.getPersistentLevel()));

                    levelControlCurrentRa.setRating(Float.valueOf(breathHisLog.getCurrentControlLevel()));
                    levelStrengthCurrentRa.setRating(Float.valueOf(breathHisLog.getCurrentStrengthLevel()));
                    levelPrensterCurrentRa.setRating(Float.valueOf(breathHisLog.getCurrentPersistentLevel()));
                    tvAverValue.setText(breathHisLog.getTrainAverValue());
                    tvDifficultyShow.setText("本难度系数最近"+trainPlan.getTimes()+"次训练分数");
                    tvTrainResult.setText(breathHisLog.getTrainResult());
                    String value = breathHisLog.getTrainStageValue() ;
                    String[] arrayStr = value.split(",") ;

                    if (arrayStr.length == 5){
                        tv1.setText(arrayStr[0]);
                        tv2.setText(arrayStr[1]);
                        tv3.setText(arrayStr[2]);
                        tv4.setText(arrayStr[3]);
                        tv5.setText(arrayStr[4]);
                    }
                }
            }) ;
        }
    } ;


    boolean istigao = false ;
    protected String timeStampToData(String timeStamp){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss") ;
        long timeLong = Long.parseLong(timeStamp)*1000 ;
        return sdf.format(new Date(timeLong)) ;
    }


    protected String getTimeLong(int timeLong) {

        if (timeLong>=0){
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
        return "00:00" ;
    }







    @Override
    protected void initEvent() {
        layoutBack.setOnClickListener(this);
        mImgShare.setOnClickListener(this);
        tvTop.setText("训练报告");
    }




    public static void actionStart(Activity mActivity, String mRecordDayDataId) {

        Intent mIntent = new Intent();
        mIntent.setClass(mActivity, BreathReportActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("mRecordDayDataId", mRecordDayDataId);
        mIntent.putExtras(mBundle);

        mActivity.startActivity(mIntent);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(40);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_re:
                BreathReportActivity.this.finish();
                break;
            case R.id.img_share:
                createLoading();
                getandSaveCurrentImage() ;
                shareSdk() ;
                break;
        }
    }


    private void shareSdk(){

        String path = Environment.getExternalStorageDirectory() +"/Android/hhdImage/breath_img.jpg"  ;//"/test.png";
        File file = new File(path);
        if (!file.exists()) {
            return ;
        }
        WXFileObject mFi = new WXFileObject() ;
        mFi.filePath = path ;
        mFi.setFilePath(path);
        WXMediaMessage msg = new WXMediaMessage();
        Bitmap photo = BitmapFactory.decodeResource(this.getResources(), R.mipmap.icon_logo);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(photo, 150, 150, true) ;
        msg.thumbData =  Util.bmpToByteArray(thumbBmp, true);
        msg.mediaObject = mFi;
        mTrainTime = mTrainTime.replace(".","-") ;
        mTrainTime = mTrainTime.replace("  ","\n") ;

        msg.title = mTrainTime+".jpeg";
        msg.description = "分享到微信好友" ;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction(null);
        req.message = msg;
        req.scene =  SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }




    private void createLoading(){

        if (completeDialog == null){
            completeDialog = new Dialog(BreathReportActivity.this,R.style.common_dialog) ;
            View mView = LayoutInflater.from(BreathReportActivity.this).inflate(R.layout.dialog_create_loading,null) ;

            completeDialog.setContentView(mView);
            completeDialog.setCanceledOnTouchOutside(true);
        }
        completeDialog.show();
    }





    /**
     * 获取和保存当前屏幕的截图
     */
    private void getandSaveCurrentImage() {
        //1.构建Bitmap
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();

        Bitmap Bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        //2.获取屏幕
        View decorview = this.getWindow().getDecorView();
        decorview.setDrawingCacheEnabled(true);
        Bmp = decorview.getDrawingCache();
        String SavePath = getSDCardPath() + "/Android/hhdImage";

        StringBuffer sb = new StringBuffer() ;
        if (StringUtils.isNotEmpty(phone)){
            sb.append(phone+"/" ) ;
        }else {
            sb.append("null/") ;
        }
        if (StringUtils.isNotEmpty(userName)){

            sb.append(userName+"/") ;
        }else {
            sb.append("null/") ;
        }
        if (StringUtils.isNotEmpty(sex)){
            sb.append(sex+"/") ;
        }else {
            sb.append("null/") ;
        }
        if (StringUtils.isNotEmpty(birthday)){
            sb.append(birthday) ;
        }else {
            sb.append("null") ;
        }



        Bitmap temp = drawTextToLogo(BreathReportActivity.this, sb.toString(), Bmp) ;

        //4.保存Bitmap
        try {
            File path = new File(SavePath);
            //文件
            String filepath = SavePath + "/breath_img.jpg";
            File file = new File(filepath);
            if (!path.exists()) {
                path.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            if (null != fos) {
                temp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (completeDialog!=null && completeDialog.isShowing()){
            completeDialog.dismiss();
        }
        Toast.makeText(BreathReportActivity.this,"报告生成成功，正在跳转，请稍后...",Toast.LENGTH_SHORT).show() ;
    }

    /**
     * 获取SDCard的目录路径功能
     * @return
     */
    private String getSDCardPath() {
        File sdcardDir = null;
        //判断SDCard是否存在
        boolean sdcardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdcardExist) {
            sdcardDir = Environment.getExternalStorageDirectory();
        }
        return sdcardDir.toString();
    }


    //////////////////////////////////////////////////////////////

    /**
     * 添加文字到图片，类似水印文字。
     * @param gContext
     * @param gText
     * @return
     */
    private Bitmap drawTextToLogo(Context gContext, String gText, Bitmap bitmap) {
       // Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/AndyDemo/ScreenImage/Screen_1.png");

        Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);





        // text color - #3D3D3D
        //#0xff1fbaf3
        paint.setColor(0xff1fbaf3);
        
        RectF mRectF = new RectF() ;
        mRectF.set(0, 0, mWidth, mStatusHeight+UiUtils.dip2px(BreathReportActivity.this,50f));
        canvas.drawRect(mRectF, paint);
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        textPaint.setColor(Color.WHITE);




        Rect  mRet = new Rect() ;
        String re = "训练报告" ;
        textPaint.setTextSize(55f);
        textPaint.getTextBounds(re,0,re.length(),mRet);
        canvas.drawText(re, mWidth / 2 - mRet.centerX(), (mStatusHeight/2-mRet.centerY()+UiUtils.dip2px(BreathReportActivity.this,30f)),textPaint);


        textPaint.setTextSize(45f);
        Rect bounds = new Rect();
        textPaint.getTextBounds(gText, 0, gText.length(), bounds);
        canvas.drawText(gText, mWidth / 2 - (bounds.centerX()), (mStatusHeight/2+mStatusHeight+UiUtils.dip2px(BreathReportActivity.this,30f)) - (bounds.centerY())-(mRet.centerY()*2), textPaint);


        return bitmap;
    }

}
