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
import android.text.Html;
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
import com.hhd.breath.app.R;
import com.hhd.breath.app.db.TrainHisService;
import com.hhd.breath.app.model.BreathDetailReport;
import com.hhd.breath.app.model.BreathDetailSuccess;
import com.hhd.breath.app.model.BreathHisLog;
import com.hhd.breath.app.net.ManagerRequest;
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

import java.io.File;
import java.io.FileOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 获取历史记录详情
 * 在线获取
 *
 * 上拉加载  下拉刷新
 * 没有加上筛选
 *
 */
public class TrainReportActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout layoutBack;
    private TextView tvTop;
    private TextView tvScore;

    private TextView tvTrainTime;
    private String mRecordDayDataId;
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

    private BreathHisLog breathHisLog ;

    private String str_startTime ;
    private String str_train_days ;
    private String str_train_result ;
    private String str_train_times ;
    private String str_train_aver_times ;

    @Bind(R.id.tv1) TextView tv1 ;
    @Bind(R.id.tv2) TextView tv2 ;
    @Bind(R.id.tv3) TextView tv3 ;
    @Bind(R.id.tv4) TextView tv4 ;
    @Bind(R.id.tv5) TextView tv5 ;
    @Bind(R.id.tvAverValue) TextView tvAverValue ;



    private void showBreathHisLog(){


        if (breathHisLog==null){
            return  ;
        }
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
        tvDifficultyShow.setText("本难度系数最近"+breathHisLog.getTrainSuccessTimes()+"次训练分数");
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_report);
        ButterKnife.bind(this);
        breathHisLog = new BreathHisLog() ;
        mRecordDayDataId = getIntent().getExtras().getString("mRecordDayDataId");
        WindowManager manager  = this.getWindowManager() ;
        mWidth = manager.getDefaultDisplay().getWidth() ;
        api = WXAPIFactory.createWXAPI(this, "wx92ff63ca90677197");
        initView();
        initEvent();
        mStatusHeight = Utils.getStatusHeight(this) ;

        breathHisLog = TrainHisService.getInstance(TrainReportActivity.this).findBreathHisLog(mRecordDayDataId) ;

        ManagerRequest.getInstance().getRequestNetApi().getBreathDetailReport(mRecordDayDataId).enqueue(new Callback<BreathDetailSuccess>() {
            @Override
            public void onResponse(Call<BreathDetailSuccess> call, Response<BreathDetailSuccess> response) {

                if (response.body().getCode().equals("200")){
                    BreathDetailReport breathDetailReport = response.body().getData() ;

                    tvScore.setText(breathDetailReport.getDifficulty());
                    tvTrainTime.setText(timeStampToData(breathDetailReport.getTrain_time()));
                    mTrainTime = longTimeToTime(breathDetailReport.getTrain_time()) ;
                    userName = ShareUtils.getUserName(TrainReportActivity.this);
                    sex =  ShareUtils.getUserSex(TrainReportActivity.this) ;
                    birthday = longTimeToTime(ShareUtils.getUserBirthday(TrainReportActivity.this)) ;
                    phone = ShareUtils.getUserPhone(TrainReportActivity.this) ;
                    tvBreathGroups.setText(breathDetailReport.getTrain_group());
                    showBreathHisLog() ;
                }else {
                    Toast.makeText(TrainReportActivity.this,"获取数据失败",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<BreathDetailSuccess> call, Throwable t) {

            }
        });
    }




    public static void actionStart(Activity mActivity, String mRecordDayDataId) {
        Intent mIntent = new Intent();
        mIntent.setClass(mActivity, TrainReportActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("mRecordDayDataId", mRecordDayDataId);
        mIntent.putExtras(mBundle);
        mActivity.startActivity(mIntent);
    }


    @Override
    protected void initView() {
        layoutBack = (RelativeLayout) findViewById(R.id.back_re);
        tvTop = (TextView) findViewById(R.id.topText);
        tvScore = (TextView) findViewById(R.id.tvScore);
        tvTrainTime = (TextView) findViewById(R.id.tvTrainTime);
        mImgShare = (ImageView) findViewById(R.id.img_share);
    }

    @Override
    protected void initEvent() {
        layoutBack.setOnClickListener(this);
        mImgShare.setOnClickListener(this);
        tvTop.setText("训练报告");
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_re:
                TrainReportActivity.this.finish();
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


    private Dialog completeDialog = null;

    private void createLoading(){

        if (completeDialog == null){
            completeDialog = new Dialog(TrainReportActivity.this,R.style.common_dialog) ;
            View mView = LayoutInflater.from(TrainReportActivity.this).inflate(R.layout.dialog_create_loading,null) ;

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



        Bitmap temp = drawTextToLogo(TrainReportActivity.this, sb.toString(), Bmp) ;

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
        Toast.makeText(TrainReportActivity.this,"报告生成成功，正在跳转，请稍后...",Toast.LENGTH_SHORT).show() ;
    }

    /**
     * 获取SDCard的目录路径功能
     * @return
     */
    private String getSDCardPath() {
        File sdcardDir = null;
        //判断SDCard是否存在
        boolean sdcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
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

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
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
        mRectF.set(0, 0, mWidth, mStatusHeight+UiUtils.dip2px(TrainReportActivity.this,50f));
        canvas.drawRect(mRectF, paint);
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        textPaint.setColor(Color.WHITE);




        Rect  mRet = new Rect() ;
        String re = "训练报告" ;
        textPaint.setTextSize(55f);
        textPaint.getTextBounds(re,0,re.length(),mRet);
        canvas.drawText(re, mWidth / 2 - mRet.centerX(), (mStatusHeight/2-mRet.centerY()+UiUtils.dip2px(TrainReportActivity.this,30f)),textPaint);


        textPaint.setTextSize(45f);
        Rect bounds = new Rect();
        textPaint.getTextBounds(gText, 0, gText.length(), bounds);
        canvas.drawText(gText, mWidth / 2 - (bounds.centerX()), (mStatusHeight/2+mStatusHeight+UiUtils.dip2px(TrainReportActivity.this,30f)) - (bounds.centerY())-(mRet.centerY()*2), textPaint);


        return bitmap;
    }

}
