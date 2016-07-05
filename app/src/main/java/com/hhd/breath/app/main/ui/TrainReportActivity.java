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
import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.R;
import com.hhd.breath.app.db.TrainUnitService;
import com.hhd.breath.app.model.BreathDetailReport;
import com.hhd.breath.app.model.BreathDetailSuccess;
import com.hhd.breath.app.model.RecordUnitData;
import com.hhd.breath.app.net.ManagerRequest;
import com.hhd.breath.app.net.ThreadPoolWrap;
import com.hhd.breath.app.tab.ui.TrainTabActivity;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.utils.StringUtils;
import com.hhd.breath.app.utils.UiUtils;
import com.hhd.breath.app.utils.Util;
import com.hhd.breath.app.utils.Utils;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXFileObject;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.File;
import java.io.FileOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 获取历史记录详情
 */
public class TrainReportActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mBackRelative;
    private TextView topTexView;
    private int values;
    private int sum;
    private TextView mTextStandardRate;
    private TextView mTextTimeLong;
    private TextView mTextGroupNumber;
    private RatingBar mRatingBar;
    private TextView mTextTrainTime;
    private TextView mTextSuggestionTime;
    private String mRecordDayDataId;
    private ImageView mImgShare;


    private String phone ;
    private String userName ;
    private String sex ;
    private String birthday ;
    private String mTrainTime ;





    private String getSuggestion(String id) {
        int level = Integer.parseInt(id);
        String result = "";
        switch (level) {
            case 1:
                result = CommonValues.SUGGESTION_LEVEL_1;
                break;
            case 2:
                result = CommonValues.SUGGESTION_LEVEL_2;
                break;
            case 3:
                result = CommonValues.SUGGESTION_LEVEL_3;
                break;
            case 4:
                result = CommonValues.SUGGESTION_LEVEL_4;
                break;
        }

        return result;
    }


    private int mWidth = 0 ;
    private int mHeight = 0  ;
    private int mStatusHeight = 0 ;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_report);
        WindowManager manager  = this.getWindowManager() ;
        mWidth = manager.getDefaultDisplay().getWidth() ;
        api = WXAPIFactory.createWXAPI(this, "wx92ff63ca90677197");
        initData();
        initView();
        initEvent();
        mStatusHeight = Utils.getStatusHeight(this) ;



        ManagerRequest.getInstance().getRequestNetApi().getBreathDetailReport(mRecordDayDataId).enqueue(new Callback<BreathDetailSuccess>() {
            @Override
            public void onResponse(Call<BreathDetailSuccess> call, Response<BreathDetailSuccess> response) {

                if (response.body().getCode().equals("200")){
                    BreathDetailReport breathDetailReport = response.body().getData() ;
                    mTextStandardRate.setText(breathDetailReport.getDifficulty());
                    mRatingBar.setRating(Float.parseFloat(breathDetailReport.getBreath_type()));  //
                    mTextTimeLong.setText(getTimeLong(Integer.parseInt(breathDetailReport.getTrain_last())));
                    mTextGroupNumber.setText(breathDetailReport.getTrain_group());
                    mTextTrainTime.setText(timeStampToData(breathDetailReport.getTrain_time()));
                    mTextSuggestionTime.setText(getSuggestion(breathDetailReport.getSuggestion()));


                    mTrainTime = longTimeToTime(breathDetailReport.getTrain_time()) ;


                    userName = ShareUtils.getUserName(TrainReportActivity.this);
                    sex =  ShareUtils.getUserSex(TrainReportActivity.this) ;

                    birthday = longTimeToTime(ShareUtils.getUserBirthday(TrainReportActivity.this)) ;
                    phone = ShareUtils.getUserPhone(TrainReportActivity.this) ;


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

    private void initData() {
       /* values = getIntent().getExtras().getInt("values") ;
        sum = getIntent().getExtras().getInt("sum") ;*/
        mRecordDayDataId = getIntent().getExtras().getString("mRecordDayDataId");
    }

    @Override
    protected void initView() {
        mBackRelative = (RelativeLayout) findViewById(R.id.back_re);
        topTexView = (TextView) findViewById(R.id.topText);
        mTextStandardRate = (TextView) findViewById(R.id.textStandardRate);
        mTextTimeLong = (TextView) findViewById(R.id.text_timeLong);
        mTextGroupNumber = (TextView) findViewById(R.id.tv_groupNumber);
        mRatingBar = (RatingBar) findViewById(R.id.ratingbar);
        mTextTrainTime = (TextView) findViewById(R.id.text_train_time);
        mTextSuggestionTime = (TextView) findViewById(R.id.text_suggestion_time);
        mImgShare = (ImageView) findViewById(R.id.img_share);
    }

    @Override
    protected void initEvent() {
        mBackRelative.setOnClickListener(this);
        mImgShare.setOnClickListener(this);

        topTexView.setText("训练报告");
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
