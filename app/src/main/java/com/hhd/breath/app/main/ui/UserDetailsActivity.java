package com.hhd.breath.app.main.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.BreathApplication;
import com.hhd.breath.app.R;
import com.hhd.breath.app.db.CaseBookService;
import com.hhd.breath.app.model.BreathDataUser;
import com.hhd.breath.app.model.BreathSuccessUser;
import com.hhd.breath.app.net.ManagerRequest;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.widget.CircularImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 获取用户详情
 * 个人资料页
 */
public class UserDetailsActivity extends BaseActivity {

    @Bind(R.id.text_account)
    TextView tvAccount;
    @Bind(R.id.text_name)
    TextView tvName;
    @Bind(R.id.text_sex)
    TextView tvSex;
    @Bind(R.id.text_birthday)
    TextView tvBirthday;
    @Bind(R.id.layout_exit_user)
    Button btnExitUser;
    @Bind(R.id.topText)
    TextView tvTopText;
    @Bind(R.id.back_re)
    RelativeLayout layoutBack;
    @Bind(R.id.layout_right)
    RelativeLayout layoutRight;
    @Bind(R.id.tvRight)
    TextView tvRight;
    @Bind(R.id.img_user_Avatar)
    CircularImage imgUserAvatar;
    @Bind(R.id.medical_flag)
    TextView medical_flag ;

    DisplayImageOptions.Builder builder ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        initView();
        initEvent();
        builder = new DisplayImageOptions.Builder() ;
        builder.showImageOnFail(R.mipmap.main_moren) ;
        builder.showImageForEmptyUri(R.mipmap.main_moren)
                .cacheInMemory(true)                               //启用内存缓存
                .cacheOnDisk(true)                                 //启用外存缓存
                .considerExifParams(true)  ;                        //启用EXIF和JPEG图像格式;   ;;

        if (isNetworkConnected(UserDetailsActivity.this)) {
            showProgressDialog("获取个人信息");
            userDetail();
        } else {
            Toast.makeText(UserDetailsActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
        }
      /*  File mFile = new File(Environment.getExternalStorageDirectory().toString() + "/hyTriage/touxiang.jpg");
        if (mFile.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + "/hyTriage/touxiang.jpg");
            imgUserAvatar.setImageBitmap(bm);
        }*/
    }

    private void userDetail() {

        ManagerRequest.getRequestApi().getUserInfo(ShareUtils.getUserId(UserDetailsActivity.this)).enqueue(new Callback<BreathSuccessUser>() {
            @Override
            public void onResponse(Call<BreathSuccessUser> call, Response<BreathSuccessUser> response) {
                hideProgress();
                switch (Integer.parseInt(response.body().getCode())) {
                    case 200:
                        BreathDataUser breathDataUser = response.body().getData();
                        ShareUtils.setUserId(UserDetailsActivity.this, breathDataUser.getUser_id());
                        ShareUtils.setUserPhone(UserDetailsActivity.this, breathDataUser.getUser_name());
                        ShareUtils.setUserName(UserDetailsActivity.this, breathDataUser.getUser_fullname());
                        ShareUtils.setUserStatus(UserDetailsActivity.this, breathDataUser.getUser_state());
                        ShareUtils.setUserBirthday(UserDetailsActivity.this, breathDataUser.getUser_birthday());
                        ShareUtils.setUserImage(UserDetailsActivity.this, breathDataUser.getUser_image());
                        tvAccount.setText(breathDataUser.getUser_name());
                        tvName.setText(breathDataUser.getUser_fullname());
                        tvBirthday.setText(longTimeToTime(breathDataUser.getUser_birthday()));
                        tvSex.setText(breathDataUser.getUser_sex().equals("0")?"男":"女");
                        ImageLoader.getInstance().displayImage(breathDataUser.getUser_image(),imgUserAvatar,builder.build());

                        if (CaseBookService.getInstance(UserDetailsActivity.this).isHasMedical(ShareUtils.getUserId(UserDetailsActivity.this))){
                            medical_flag.setText("有");
                        }else {
                            medical_flag.setText("无");
                        }
                        break;
                    default:
                        BreathApplication.toast(UserDetailsActivity.this, getString(R.string.string_detail_user_error));
                        break;
                }
            }

            @Override
            public void onFailure(Call<BreathSuccessUser> call, Throwable t) {
                hideProgress();
                BreathApplication.toast(UserDetailsActivity.this, getString(R.string.string_detail_user_net_error));
            }
        });

    }


    @Override
    protected void initView() {
        layoutRight.setVisibility(View.VISIBLE);
    }

    private Dialog completeDialog = null;

    private void showDialog() {

        if (completeDialog == null) {
            completeDialog = new Dialog(UserDetailsActivity.this, R.style.common_dialog);
            View mView = LayoutInflater.from(UserDetailsActivity.this).inflate(R.layout.dialog_user_exit, null);
            RelativeLayout layoutExit = (RelativeLayout) mView.findViewById(R.id.layout_user_exit);
            RelativeLayout layoutNoExitUser = (RelativeLayout) mView.findViewById(R.id.layout_user_unexit);
            layoutExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    completeDialog.dismiss();
                    exitUserInfo();
                }
            });
            layoutNoExitUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (completeDialog != null && completeDialog.isShowing()) {
                        completeDialog.dismiss();
                    }
                }
            });
            completeDialog.setContentView(mView);
            completeDialog.setCanceledOnTouchOutside(true);
        }
        completeDialog.show();
    }

    private void exitUserInfo() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CaseBookService.getInstance(UserDetailsActivity.this).clearAllData();

                ShareUtils.clearUserInfo(UserDetailsActivity.this);
                Intent mIntent = new Intent();
                mIntent.setClass(UserDetailsActivity.this, LoginBreathActivity.class);
                startActivity(mIntent);
            }
        });
    }


    @Override
    protected void initEvent() {
        tvTopText.setText("个人资料");
        tvRight.setText("编辑");
        btnExitUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailsActivity.this.finish();
            }
        });
        layoutRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(UserDetailsActivity.this, UserInfoActivity.class);
                startActivityForResult(intent,10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 10:
                if (isNetworkConnected(UserDetailsActivity.this)) {
                    showProgressDialog("获取个人信息");
                    userDetail();
                }
                break;
        }
    }
}
