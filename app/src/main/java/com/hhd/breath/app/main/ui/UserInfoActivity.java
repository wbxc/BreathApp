package com.hhd.breath.app.main.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.hhd.breath.app.model.BreathDataUser;
import com.hhd.breath.app.model.BreathSuccessUser;
import com.hhd.breath.app.model.BreathUser;
import com.hhd.breath.app.model.MedicalHis;
import com.hhd.breath.app.model.SysDataModel;
import com.hhd.breath.app.net.HttpUtil;
import com.hhd.breath.app.net.ManagerRequest;
import com.hhd.breath.app.net.NetConfig;
import com.hhd.breath.app.net.ThreadPoolWrap;
import com.hhd.breath.app.net.UploadUtil;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.utils.StringUtils;
import com.hhd.breath.app.widget.CircularImage;
import com.hhd.breath.app.widget.DatePickerPopWindow;
import com.hhd.breath.app.widget.NoScrollGridView;
import com.hhd.breath.app.widget.WheelView;
import com.nostra13.universalimageloader.core.ImageLoader;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 编辑用户界面
 * 修改用户信息
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView textTopText ;
    private RelativeLayout layoutBackRe ;
    private CircularImage imgUserInfo ;
    private RelativeLayout mLayoutUserName ;
    private RelativeLayout mLayoutUserSex ;
    private RelativeLayout mLayoutUserBirthday ;
    private RelativeLayout mLayoutBrMedicalRecord ;
    private TextView mTextName ;
    private TextView mTextAccount ;
    private TextView mTextBirthday  ;
    private TextView mTextSex ;
    private DatePickerPopWindow popWindow = null ;
    private String dataResult ;
    private String dataBirthday  ;
    private Dialog sexDialog = null ;
    private LinearLayout layoutContent ;
    private NoScrollGridView mNoScrollGridView ;
    private TextView medical_flag ;
    private Button layoutExitUser ;
    List<SysDataModel> sysDataModels = new ArrayList<SysDataModel>() ;
    List<String> sexStrings = new ArrayList<String>() ;
    private int flagIndex = 0 ;
    private int temp = 0 ;
    private ImageLoader imageLoader = ImageLoader.getInstance() ;
    private String urlAvatar = "" ;
    private boolean isVisiable = false ;
    private PopupWindow selectPopuWindow;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;
    private Bitmap bitmap;
    private int mSelectIndex = 0 ;
    private String mSexResult = "" ;
    private List<MedicalHis> medicalHises;
    private GradViewAdapter mGradViewAdapter ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initData();
        initView();
        initEvent();
        if (isNetworkConnected(UserInfoActivity.this)){
            showProgressDialog("请求中");
            userDetail();
        }else {
            Toast.makeText(UserInfoActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
        }
        File mFile = new File(Environment.getExternalStorageDirectory().toString() + "/hyTriage/touxiang.jpg") ;
        if (mFile.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + "/hyTriage/touxiang.jpg");
            imgUserInfo.setImageBitmap(bm);
        }
    }

    private void  userDetail(){

        ManagerRequest.getRequestApi().getUserInfo(ShareUtils.getUserId(UserInfoActivity.this)).enqueue(new Callback<BreathSuccessUser>() {
            @Override
            public void onResponse(Call<BreathSuccessUser> call, Response<BreathSuccessUser> response) {
                hideProgress();
                switch (Integer.parseInt(response.body().getCode())) {
                    case 200:
                        BreathDataUser breathUser = response.body().getData();
                        urlAvatar = breathUser.getUser_image();
                        imageLoader.displayImage(breathUser.getUser_image(), imgUserInfo);
                        mTextName.setText(breathUser.getUser_fullname());
                        mTextAccount.setText(breathUser.getUser_name());
                        mTextBirthday.setText(longTimeToTime(breathUser.getUser_birthday()));
                        mTextSex.setText(breathUser.getUser_sex().equals("0")?"男":"女");
                        if (CaseBookService.getInstance(UserInfoActivity.this).isHasMedical(ShareUtils.getUserId(UserInfoActivity.this))){
                            medical_flag.setText("有");
                        }else {
                            medical_flag.setText("无");
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<BreathSuccessUser> call, Throwable t) {
                hideProgress();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent() ;
        intent.setClass(UserInfoActivity.this, UserDetailsActivity.class) ;
        startActivity(intent);
    }


    private  void initData(){
        medicalHises = new ArrayList<MedicalHis>() ;
        medicalHises = CaseBookService.getInstance(UserInfoActivity.this).getMedicalHis(ShareUtils.getUserId(UserInfoActivity.this)) ;
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


    private int selectMedical = 0 ;
    private String user_disease ="0" ;
    @Override
    protected void initView() {

        textTopText = (TextView)findViewById(R.id.topText) ;
        layoutBackRe = (RelativeLayout)findViewById(R.id.back_re) ;
        imgUserInfo = (CircularImage)findViewById(R.id.img_user_Avatar) ;
        mLayoutUserName = (RelativeLayout)findViewById(R.id.layout_userName)  ;
        mLayoutUserSex = (RelativeLayout)findViewById(R.id.layout_sexName) ;
        mLayoutUserBirthday = (RelativeLayout)findViewById(R.id.layout_birthday) ;
        mLayoutBrMedicalRecord = (RelativeLayout)findViewById(R.id.layout_bMdicalRecord) ;

        mTextAccount  = (TextView)findViewById(R.id.text_account) ;
        mTextBirthday = (TextView)findViewById(R.id.text_birthday) ;
        mTextName = (TextView)findViewById(R.id.text_name) ;
        mTextSex = (TextView)findViewById(R.id.text_sex) ;
        layoutContent = (LinearLayout)findViewById(R.id.content) ;
        medical_flag = (TextView)findViewById(R.id.medical_flag) ;
        layoutExitUser = (Button)findViewById(R.id.layout_exit_user) ;
        mNoScrollGridView = (NoScrollGridView)findViewById(R.id.gridview) ;
    }

    @Override
    protected void initEvent() {
        textTopText.setText(getResources().getString(R.string.string_modify_user_info));
        mTextSex.setText(ShareUtils.getUserSex(UserInfoActivity.this));
        mTextBirthday.setText(ShareUtils.getUserBirthday(UserInfoActivity.this));
        layoutBackRe.setOnClickListener(this);
        imgUserInfo.setOnClickListener(this);
        mLayoutUserBirthday.setOnClickListener(this);
        mLayoutUserSex.setOnClickListener(this);
        mLayoutUserName.setOnClickListener(this);
        layoutExitUser.setOnClickListener(this);
        mLayoutUserName.setOnClickListener(this);
        mLayoutBrMedicalRecord.setOnClickListener(this);

        if (StringUtils.isNotEmpty(ShareUtils.getUserPhone(UserInfoActivity.this))){
            mTextAccount.setText(ShareUtils.getUserPhone(UserInfoActivity.this));
        }

        mGradViewAdapter = new GradViewAdapter(UserInfoActivity.this,medicalHises) ;
        mNoScrollGridView.setAdapter(mGradViewAdapter);
        mNoScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                UserInfoActivity.this.runOnUiThread(new Runnable() {
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
                            medical_flag.setText("有");
                        else
                            medical_flag.setText("无");
                    }
                });

            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        mTextName.setText(ShareUtils.getUserName(UserInfoActivity.this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_re:
                Intent intent = new Intent() ;
                intent.setClass(UserInfoActivity.this,UserDetailsActivity.class) ;
                startActivity(intent);
                UserInfoActivity.this.finish();
                break;
            case R.id.layout_birthday:
                showPopuwindow();
                break;
            case R.id.layout_bMdicalRecord:
                showMedical() ;
                break;
                 case R.id.img_user_Avatar:
                initSelectPupuWindow();
                break;
            case R.id.layout_sexName:
                if (sexDialog==null){
                    sexDialog = showDialogPopuWindow(0, sexStrings, sysDataModels, mOnClickListener,mOnWheelViewListener) ;
                }
                sexDialog.show();
                break;
            case R.id.layout_userName:
                ModifyNameActivity.actionActivity(UserInfoActivity.this,ShareUtils.getUserName(UserInfoActivity.this));
                break;

            case R.id.layout_exit_user:
                if (isNetworkConnected(UserInfoActivity.this)){
                    commitUserInfo() ;
                }else {
                    Toast.makeText(UserInfoActivity.this,"",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    String sex = "0" ;


    private void commitUserInfo(){

        showProgressDialog(getString(R.string.string_user_commit));
        CaseBookService.getInstance(UserInfoActivity.this).updateMedicalHis(medicalHises);

        ManagerRequest.getInstance().modifyUserInfo(ShareUtils.getUserId(UserInfoActivity.this), urlAvatar,
                mTextName.getText().toString().trim(), dataBirthday, sex, user_disease, new ManagerRequest.IDataCallBack() {
                    @Override
                    public void onNetError(String msg) {
                        BreathApplication.toast(UserInfoActivity.this, getString(R.string.string_user_commit_error));
                    }

                    @Override
                    public void onSuccess(String msg) {
                        BreathApplication.toast(UserInfoActivity.this, getString(R.string.string_user_commit_success));
                        Intent intent = new Intent() ;
                        intent.setClass(UserInfoActivity.this,UserDetailsActivity.class) ;
                        startActivity(intent);
                        UserInfoActivity.this.finish();
                    }

                    @Override
                    public void onFail(String msg) {
                        BreathApplication.toast(UserInfoActivity.this, getString(R.string.string_detail_user_net_error));
                    }

                    @Override
                    public void onComplete() {

                        hideProgress();
                    }
                });
    }

    private void showMedical(){

        if (isVisiable){
            layoutContent.setVisibility(View.GONE);
            isVisiable = false ;
        }else{
            layoutContent.setVisibility(View.VISIBLE);
            isVisiable = true ;
        }

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            if (sexDialog.isShowing()){
                sexDialog.dismiss();
            }
            mTextSex.setText(mSexResult);

        }
    } ;


    private WheelView.OnWheelViewListener  mOnWheelViewListener = new WheelView.OnWheelViewListener(){
        @Override
        public void onSelected(int selectedIndex, String item) {
            super.onSelected(selectedIndex, item);
            mSelectIndex = selectedIndex ;
            mSexResult = sysDataModels.get(mSelectIndex).getName() ;
            sex = sysDataModels.get(mSelectIndex).getValue() ;
            ShareUtils.setUserSex(UserInfoActivity.this,sysDataModels.get(mSelectIndex).getName());
            UserInfoActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTextSex.setText(mSexResult);
                }
            });
        }
    } ;




    private Dialog showDialogPopuWindow(int index , List<String> mStrings ,
                                        List<SysDataModel> mSysDataModels,
                                        View.OnClickListener mOnClickListener,WheelView.OnWheelViewListener mOnWheelViewListener){
        Dialog mDialog = null;
        if (mDialog == null){
            View mInspirationView  = LayoutInflater.from(UserInfoActivity.this).inflate(R.layout.popu_system_dialog, null) ;
            WheelView mWheelView = (WheelView)mInspirationView.findViewById(R.id.wheel_view_wv) ;
            Button mButton = (Button)mInspirationView.findViewById(R.id.queding) ;
            mButton.setOnClickListener(mOnClickListener);

            mWheelView.setOffset(2);
            mWheelView.setItems(mStrings);

            mSexResult = mStrings.get(index) ;
            mWheelView.setSeletion(index);

            mWheelView.setOnWheelViewListener(mOnWheelViewListener);

            mDialog = new Dialog(UserInfoActivity.this,R.style.common_dialog) ;
            mDialog.setCancelable(true);
            ViewGroup.LayoutParams  mParmas = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) ;
            mDialog.setContentView(mInspirationView, mParmas);
        }

        return mDialog ;
    }



    private String getData(String birthday){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd") ;

        try {
            long time = simpleDateFormat.parse(birthday).getTime()/1000 ;
            return  String.valueOf(time) ;
        }catch (Exception e){
        }
        return "" ;
    }


    private void showPopuwindow() {
        Date date=new Date();
        DateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");

        if (popWindow==null) {
            popWindow=new DatePickerPopWindow(UserInfoActivity.this,df.format(date),new PerfectInterface() {

                @Override
                public void setDateValue(final String dataValue) {
                    // TODO Auto-generated method stub
                    dataResult = dataValue ;
                    UserInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextBirthday.setText(dataResult);

                            dataBirthday = getData(dataResult) ;
                            ShareUtils.setUserBirthday(UserInfoActivity.this, dataResult);
                        }
                    });

                }
            },new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    popWindow.dismiss();
                   // mTextBirthday.setText(dataResult);
                    ShareUtils.setUserBirthday(UserInfoActivity.this, dataResult);
                }
            });
        }

        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=0.3f;
        getWindow().setAttributes(lp);

        //popWindow.showAtLocation(findViewById(R.id.second_re), Gravity.CENTER, 0, 0);
        popWindow.showAsDropDown(mLayoutUserName) ;

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


    private void initSelectPupuWindow() {
        View view = LayoutInflater.from(UserInfoActivity.this).inflate(R.layout.layout_popu_avatar, null);
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
                Toast.makeText(UserInfoActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            try {
                selectPopuWindow.dismiss();
                if (selectPopuWindow.isShowing()) {
                    selectPopuWindow.dismiss();
                }
                bitmap = data.getParcelableExtra("data");
                imgUserInfo.setImageBitmap(bitmap);
                String pathUrl = SavePicInLocal(bitmap, "touxiang.jpg");
                upload(pathUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /*
    * 上传图片
    */
    public void upload(String pathUrl) {
        try {
            Map<String,String> params = new HashMap<String,String>() ;
            params.put("user_id",ShareUtils.getUserId(UserInfoActivity.this)) ;
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
                                    ShareUtils.setUserId(UserInfoActivity.this, jsonData.getString("user_id"));
                                    // {"data":{"new_head_url":"http:\/\/101.201.39.122:8060\/static\/images\/20160422\/5719d27a4109a.png","user_id":"1442552148"},"code":"200"}
                                    UserInfoActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideProgress();
                                            BreathApplication.toast(UserInfoActivity.this,"上传成功");
                                        }
                                    });
                                    break;
                                case NetConfig.FAIL_CODE:
                                    UserInfoActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideProgress();
                                            BreathApplication.toast(UserInfoActivity.this,"上传失败");
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

            hideProgress() ;
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

}
