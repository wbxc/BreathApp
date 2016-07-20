package com.hhd.breath.app.main.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.BreathApplication;
import com.hhd.breath.app.R;
import com.hhd.breath.app.adapter.GradViewAdapter;
import com.hhd.breath.app.db.CaseBookService;
import com.hhd.breath.app.imp.PerfectInterface;
import com.hhd.breath.app.model.MedicalHis;
import com.hhd.breath.app.model.SysDataModel;
import com.hhd.breath.app.net.ManagerRequest;
import com.hhd.breath.app.net.NetConfig;
import com.hhd.breath.app.net.ThreadPoolWrap;
import com.hhd.breath.app.net.UploadUtil;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.widget.CircularImage;
import com.hhd.breath.app.widget.DatePickerPopWindow;
import com.hhd.breath.app.widget.NoScrollGridView;
import com.hhd.breath.app.widget.WheelView;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 注册完成后
 * 修改用户信息
 */
public class PerUserInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView topTextView ;
    private EditText mEditTextName ;
    private TextView mTvSex ;
    private TextView mTvBirthday ;
    private TextView mTvMedicalHis ;
    private RelativeLayout mLayoutSex ;
    private RelativeLayout mLayoutBirthday ;
    private RelativeLayout mLayoutHis ;
    private Button mStartTrain ;
    private List<MedicalHis> medicalHises ;

    private DatePickerPopWindow popWindow = null ;
    private String dataResult = "" ;
    private NoScrollGridView mScrollGridView ;
    private GradViewAdapter mGradViewAdapter ;
    private LinearLayout mContent ;
    private  int selectMedical = 0 ;
    private CircularImage imgUserAvatar ;
    private String urlAvatar="" ;
    private String updateTime  ;
    private PopupWindow selectPopuWindow;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;
    private Bitmap bitmap;
    private String user_disease ;
    @Bind(R.id.img_user_Avatar)
    CircularImage imageCircular ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_user_info);
        ButterKnife.bind(this);
        initData();
        initView();
        initEvent();
        File mFile = new File(Environment.getExternalStorageDirectory().toString() + "/hyTriage/touxiang.jpg") ;
        if (mFile.exists()) {
            mFile.delete() ;
        }
        updateTime = updateTime(ShareUtils.getUserBirthday(PerUserInfoActivity.this)) ;

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    List<SysDataModel> sysDataModels = new ArrayList<SysDataModel>() ;
    List<String> sexStrings = new ArrayList<String>() ;

    private  void initData(){
        medicalHises = new ArrayList<MedicalHis>() ;
        medicalHises = CaseBookService.getInstance(PerUserInfoActivity.this).getMedicalHis(ShareUtils.getUserId(PerUserInfoActivity.this)) ;
        SysDataModel nan = new SysDataModel() ;
        nan.setId(String.valueOf(0));
        nan.setName("男");
        nan.setValue("0");
        sysDataModels.add(nan) ;
        SysDataModel nv = new SysDataModel() ;
        nv.setId(String.valueOf(1));
        nv.setValue("1");
        nv.setName("女");
        sysDataModels.add(nv) ;
        for (int i=0 ; i<sysDataModels.size(); i++){
            sexStrings.add(sysDataModels.get(i).getName()) ;
        }
    }
    @Override
    protected void initView() {
        topTextView = (TextView)findViewById(R.id.topText) ;
        mEditTextName = (EditText)findViewById(R.id.edit_username) ;
        mTvSex = (TextView)findViewById(R.id.tv_sex) ;
        mTvBirthday = (TextView)findViewById(R.id.tv_birthday) ;
        mTvMedicalHis = (TextView)findViewById(R.id.tv_medical) ;

        mLayoutSex = (RelativeLayout)findViewById(R.id.layout_sex) ;
        mLayoutHis = (RelativeLayout)findViewById(R.id.layout_medical_his) ;
        mLayoutBirthday = (RelativeLayout)findViewById(R.id.layout_birthday) ;
        mStartTrain = (Button)findViewById(R.id.btn_startTrain) ;
        mScrollGridView = (NoScrollGridView)findViewById(R.id.gridview) ;
        mContent = (LinearLayout)findViewById(R.id.content) ;
        imgUserAvatar = (CircularImage)findViewById(R.id.img_user_Avatar) ;
    }

    private int temp = 0 ;

    @Override
    protected void initEvent() {

        topTextView.setText(getResources().getString(R.string.string_perfect_userinfo));
        mStartTrain.setOnClickListener(this);

        mLayoutSex.setOnClickListener(this);
        mLayoutBirthday.setOnClickListener(this);
        mLayoutHis.setOnClickListener(this);
        imgUserAvatar.setOnClickListener(this);
        mGradViewAdapter = new GradViewAdapter(PerUserInfoActivity.this,medicalHises) ;
        mScrollGridView.setAdapter(mGradViewAdapter);
        mScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                temp = position ;
                if (position<medicalHises.size()) {


                    if (medicalHises.get(position).getType() > 0) {
                        medicalHises.get(position).setType(0);
                    } else {
                        medicalHises.get(position).setType(1);
                    }
                }
                PerUserInfoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        selectMedical = 0 ;
                        mGradViewAdapter.refresh(medicalHises);

                        for (int i=0 ; i< medicalHises.size() ; i++){
                            if (medicalHises.get(i).getType()>0){
                                user_disease = String.valueOf(medicalHises.get(i).getId()) ;
                                selectMedical = 1 ;
                            }
                        }

                        if (selectMedical>0)
                            mTvMedicalHis.setText("有呼吸病史");
                        else
                            mTvMedicalHis.setText("无呼吸病史");
                    }
                });

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Dialog mSexDialog = null ;
    private int sexIndex = 0 ;
    private View.OnClickListener mSexOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (mSexDialog.isShowing()){
                mSexDialog.dismiss();
            }
            mTvSex.setText(sysDataModels.get(sexIndex).getName());
            ShareUtils.setUserSex(PerUserInfoActivity.this, sysDataModels.get(sexIndex).getName());
        }
    } ;

    private WheelView.OnWheelViewListener mSexOnWheelViewListener = new WheelView.OnWheelViewListener(){

        @Override
        public void onSelected(int selectedIndex, String item) {
            super.onSelected(selectedIndex, item);
            sexIndex = selectedIndex ;
        }
    } ;





    private void showPopuwindow() {
        Date date=new Date();
        DateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");

        if (popWindow==null) {
            popWindow=new DatePickerPopWindow(PerUserInfoActivity.this,df.format(date),new PerfectInterface() {

                @Override
                public void setDateValue(final String dataValue) {
                    // TODO Auto-generated method stub
                    dataResult = dataValue ;
                    PerUserInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvBirthday.setText(dataResult);
                            updateTime = updateTime(dataResult) ;
                            ShareUtils.setUserBirthday(PerUserInfoActivity.this, dataResult);
                        }
                    });

                }
            },new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // birthday_value.setText(dataResult);
                    popWindow.dismiss();
                }
            });
        }

        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=0.3f;
        getWindow().setAttributes(lp);

        //popWindow.showAtLocation(findViewById(R.id.second_re), Gravity.CENTER, 0, 0);
        popWindow.showAsDropDown(mEditTextName) ;

        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                WindowManager.LayoutParams lp=getWindow().getAttributes();
                lp.alpha=1f;
                getWindow().setAttributes(lp);
            }
        });
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_startTrain:

                if (isNetworkConnected(PerUserInfoActivity.this)){
                    showProgressDialog("信息保存中");
                    CaseBookService.getInstance(PerUserInfoActivity.this).updateMedicalHis(medicalHises);

                    String user_id = ShareUtils.getUserId(PerUserInfoActivity.this) ;
                    String full_name = mEditTextName.getText().toString().trim() ;

                    ManagerRequest.getInstance().modifyUserInfo(user_id, urlAvatar, full_name, updateTime, String.valueOf(sexIndex), user_disease, new ManagerRequest.IDataCallBack() {
                        @Override
                        public void onNetError(String msg) {
                            BreathApplication.toast(PerUserInfoActivity.this, "网络连接异常");
                        }

                        @Override
                        public void onSuccess(String msg) {
                            Toast.makeText(PerUserInfoActivity.this,"信息保存成功",Toast.LENGTH_SHORT).show();
                            ShareUtils.setUserName(PerUserInfoActivity.this, mEditTextName.getText().toString().trim());
                            ShareUtils.setFirstLaunch(PerUserInfoActivity.this, true);
                            MainTabHomeActivity.actionStart(PerUserInfoActivity.this);
                        }

                        @Override
                        public void onFail(String msg) {

                            BreathApplication.toast(PerUserInfoActivity.this, "信息保存失败");
                        }

                        @Override
                        public void onComplete() {
                            hideProgress();
                        }
                    });
                }else {
                    Toast.makeText(PerUserInfoActivity.this,"网络连接异常",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.layout_sex:
                if (mSexDialog==null){
                    mSexDialog = showDialogPopuWindow(sexIndex,sexStrings,sysDataModels,mSexOnClickListener,mSexOnWheelViewListener) ;
                }
                mSexDialog.show();
                break;
            case R.id.layout_birthday:
                showPopuwindow() ;
                break;
            case R.id.layout_medical_his:
                mContent.setVisibility(View.VISIBLE);
                break;
            case R.id.img_user_Avatar:
                initSelectPupuWindow() ;
                break;
        }
    }



    private void initSelectPupuWindow() {
        View view = LayoutInflater.from(PerUserInfoActivity.this).inflate(R.layout.layout_popu_avatar, null);
        Button select_button_xiangji = (Button) view.findViewById(R.id.select_button_xiangji);
        select_button_xiangji.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                camera();
            }
        });
        Button select_button_xiangce = (Button) view.findViewById(R.id.select_button_xiangce);
        select_button_xiangce.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                gallery();
            }
        });
        Button mCancelButton = (Button) view.findViewById(R.id.quxiao);
        mCancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                selectPopuWindow.dismiss();
            }
        });
        selectPopuWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        selectPopuWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        selectPopuWindow.setBackgroundDrawable(dw);
        selectPopuWindow.setOutsideTouchable(true);
        selectPopuWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /*
     * 从相册获取
     */
    public void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /*
     * 从相机获取
     */
    public void camera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
        }
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("SdCardPath")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(PerUserInfoActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            try {
                selectPopuWindow.dismiss();
                if (selectPopuWindow.isShowing()) {
                    selectPopuWindow.dismiss();
                }
                bitmap = data.getParcelableExtra("data");
                imgUserAvatar.setImageBitmap(bitmap);
                String pathUrl = SavePicInLocal(bitmap, "touxiang.jpg");
                upload(pathUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 生日转变成为时间戳
     * @param time
     * @return
     */
    private String updateTime(String time){

        if (isNotEmpty(time)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
            try {
                return  String.valueOf(simpleDateFormat.parse(time).getTime()/1000) ;
            }catch (Exception e){
            }
        }
        return "" ;
    }


    /*
     * 上传图片
     */
    public void upload(String pathUrl) {

        try {
            Map<String,String> params = new HashMap<String,String>() ;
            params.put("user_id", ShareUtils.getUserId(PerUserInfoActivity.this)) ;
            showProgressDialog("图片上传中");

            UploadUtil.getInstance().uploadFile(pathUrl,"fname", NetConfig.URL_UPLOAD_PHOTO,params);
            UploadUtil.getInstance().setOnUploadProcessListener(new UploadUtil.OnUploadProcessListener() {
                @Override
                public void onUploadDone(int responseCode, String message) {

                  try {
                      JSONObject jsonMessage = new JSONObject(message) ;
                      if (jsonMessage.has(NetConfig.CODE)){
                          switch (Integer.parseInt(jsonMessage.getString(NetConfig.CODE))){
                              case NetConfig.SUCCESS_CODE:
                                  JSONObject jsonData = jsonMessage.getJSONObject(NetConfig.DATA) ;
                                  urlAvatar = jsonData.getString("new_head_url") ;
                                  ShareUtils.setUserId(PerUserInfoActivity.this, jsonData.getString("user_id"));
                                  PerUserInfoActivity.this.runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          hideProgress();
                                          Toast.makeText(PerUserInfoActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                                      }
                                  });
                                  break;
                              case NetConfig.FAIL_CODE:
                                  PerUserInfoActivity.this.runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          hideProgress();
                                          Toast.makeText(PerUserInfoActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                                      }
                                  });
                                  break;
                          }
                      }
                  }catch (Exception e){


                  }
                }
                @Override
                public void onUploadProcess(int uploadSize) {

                }

                @Override
                public void initUpload(int fileSize) {

                }
            });
        }catch (Exception e){


        }
    }

    private String path = Environment.getExternalStorageDirectory().toString() + "/hyTriage/";

    private String SavePicInLocal(Bitmap bitmap, String photoName) {
        String pathName ="";
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream baos = null; // 字节数组输出流
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] byteArray = baos.toByteArray();// 字节数组输出流转换成字节数组
            makeRootDirectory(path) ;
            File file = new File(path, photoName);
            pathName = file.getAbsolutePath() ;
            // 将字节数组写入到刚创建的图片文件中
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(byteArray);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        return pathName;
    }
    private  void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 剪切图片
     *
     * @function:
     * @author:Jerry
     * @date:2013-12-30
     * @param uri
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }


    private Dialog showDialogPopuWindow(int index , List<String> mStrings ,
                                        List<SysDataModel> mSysDataModels,
                                        View.OnClickListener mOnClickListener,WheelView.OnWheelViewListener mOnWheelViewListener){


        Dialog mDialog = null;

        if (mDialog == null){
            View mInspirationView  = LayoutInflater.from(PerUserInfoActivity.this).inflate(R.layout.popu_system_dialog, null) ;
            WheelView mWheelView = (WheelView)mInspirationView.findViewById(R.id.wheel_view_wv) ;
            Button mButton = (Button)mInspirationView.findViewById(R.id.queding) ;
            mButton.setOnClickListener(mOnClickListener);

            mWheelView.setOffset(2);
            mWheelView.setItems(mStrings);

            mWheelView.setSeletion(index-1);

            mWheelView.setOnWheelViewListener(mOnWheelViewListener);

            mDialog = new Dialog(PerUserInfoActivity.this, R.style.common_dialog) ;
            mDialog.setCancelable(true);
            ViewGroup.LayoutParams  mParmas = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) ;
            mDialog.setContentView(mInspirationView, mParmas);
        }

        return mDialog ;
    }
    public static void  actionStart(Activity mActivity){

        Intent mIntent = new Intent() ;
        mIntent.setClass(mActivity,PerUserInfoActivity.class) ;
        mActivity.startActivity(mIntent);
    }
}
