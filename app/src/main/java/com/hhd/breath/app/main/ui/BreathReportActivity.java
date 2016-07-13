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
import com.hhd.breath.app.BreathApplication;
import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.R;
import com.hhd.breath.app.andengine.BreathAndEngine;
import com.hhd.breath.app.db.TrainUnitService;
import com.hhd.breath.app.model.BreathDetailReport;
import com.hhd.breath.app.model.BreathDetailSuccess;
import com.hhd.breath.app.model.BreathTrainingResult;
import com.hhd.breath.app.model.RecordUnitData;
import com.hhd.breath.app.model.TrainPlan;
import com.hhd.breath.app.net.ManagerRequest;
import com.hhd.breath.app.net.UploadRecordData;
import com.hhd.breath.app.service.UploadDataService;
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
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 训练完成后查看
 * 训练完成查看的训练报告
 */
public class BreathReportActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mBackRelative;
    private TextView topTexView;
    private int values;
    private int sum;
    private TextView mTextStandardRate;
    private TextView mTextTrainTime;
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
    private final Handler  handler = new Handler() ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_report);
        WindowManager manager  = this.getWindowManager() ;
        mWidth = manager.getDefaultDisplay().getWidth() ;
        api = WXAPIFactory.createWXAPI(this, "wx92ff63ca90677197");
        mRecordDayData = (BreathTrainingResult) getIntent().getExtras().getSerializable("breathTrainingData");
        trainPlan = (TrainPlan)getIntent().getExtras().getSerializable("train_plan") ;
        initView();
        initEvent();
        mStatusHeight = Utils.getStatusHeight(this) ;



        filepath =CommonValues.PATH_ZIP+mRecordDayData.getUser_id()+"/"+mRecordDayData.getFname();
        file_zip_path = CommonValues.PATH_ZIP+mRecordDayData.getUser_id()+"/"+mRecordDayData.getFname()+"_zip" ;

        UploadRecordData.getInstance().setOnUploadProcessListener(new UploadRecordData.OnUploadProcessListener() {
            @Override
            public void onUploadDone(int responseCode, String message) {

                // Log.e("onUploadDone", message) ;
                switch (responseCode){
                    case UploadRecordData.UPLOAD_SUCCESS_CODE:
                        FileUtils.deleteFolder(filepath) ;
                        FileUtils.deleteFolder(file_zip_path) ;
                        try {
                            final JSONObject mesJsonObject = new JSONObject(message) ;
                            if (mesJsonObject.has("code") && mesJsonObject.getString("code").equals("200")){

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Utils.write(mesJsonObject.toString());

                                        BreathApplication.toast(BreathReportActivity.this,mesJsonObject.toString());
                                    }
                                }) ;
                            }
                        }catch (Exception e){

                        }
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






        mTextStandardRate.setText(mRecordDayData.getDifficulty());
        mTextTrainTime.setText(timeStampToData(mRecordDayData.getTrain_time()));
        mTrainTime = longTimeToTime(mRecordDayData.getTrain_time()) ;
        phone = ShareUtils.getUserPhone(BreathReportActivity.this) ;
        sex = ShareUtils.getUserSex(BreathReportActivity.this) ;

    }

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
    protected void initView() {
        mBackRelative = (RelativeLayout) findViewById(R.id.back_re);
        topTexView = (TextView) findViewById(R.id.topText);
        mTextStandardRate = (TextView) findViewById(R.id.textStandardRate);
        mTextTrainTime = (TextView) findViewById(R.id.text_train_time);
        mImgShare = (ImageView) findViewById(R.id.img_share);
    }

    @Override
    protected void initEvent() {
        mBackRelative.setOnClickListener(this);
        mImgShare.setOnClickListener(this);
        topTexView.setText("训练报告");
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
