package com.hhd.breath.app.net;

import android.support.annotation.StringDef;

import com.hhd.breath.app.model.BreathDetailSuccess;
import com.hhd.breath.app.model.BreathSuccessUser;
import com.hhd.breath.app.model.BreathTempData;
import com.hhd.breath.app.model.Data;
import com.hhd.breath.app.model.PhoneCode;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/5/5.
 */
public interface RequestNetApi {


    /**
     * 校验手机号是否可用
     * @param user_name
     * @return
     */
    @FormUrlEncoded
    @POST("/api/iphone/user/CheckMobile/?auth_code=hHx_qHbMKvaV5c8k6d3z")
    public Call<ResponseBody> checkMobile(@Field("user_name") String user_name) ;

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    @FormUrlEncoded
    @POST("/api/iphone/user/SendPhoneCode/?auth_code=hHx_qHbMKvaV5c8k6d3z")
    public Call<PhoneCode> sendVerificationCode(@Field("phone") String phone) ;


    // {"data":{"user_id":"14612921293140"},"code":"200"}
    //{"data":"","code":"0"}

    /**
     * 用户注册
     * @param user_name
     * @param user_password
     * @param code
     * @return
     */
    @FormUrlEncoded
    @POST("/api/iphone/user/Register/?auth_code=hHx_qHbMKvaV5c8k6d3z")
    public Call<ResponseBody> register(@Field("user_name") String user_name,
                                       @Field("user_password") String user_password,
                                       @Field("code") String code,
                                       @Field("full_name") String full_name,
                                       @Field("user_birthday") String user_birthday,
                                       @Field("user_disease") String user_disease);
    /**
     * 用户登录
     * @param user_name
     * @param user_password
     * @return
     */
    @FormUrlEncoded
    @POST("/api/iphone/user/OwnLogin/?auth_code=hHx_qHbMKvaV5c8k6d3z")
    public Call<String> login(@Field("user_name") String user_name,
                              @Field("user_password") String user_password) ;

    /**
     * @param user_id
     * @param page_num
     * @param page_few
     * @return 返回历史记录
     */
    @FormUrlEncoded
    @POST("/api/iphone/ecg/EcGHistoryData/?auth_code=hHx_qHbMKvaV5c8k6d3z")
    public Call<BreathTempData> getBreathHisList(
            @Field("user_id") String user_id,
            @Field("page_num") String page_num,
            @Field("page_few") String page_few) ;


    /**
     * @return 返回历史记录
     * /api/iphone/ecg/EcGHistoryData/?auth_code=hHx_qHbMKvaV5c8k6d3z
     */
    @FormUrlEncoded
    @POST("/api/iphone/ecg/EcGHistoryData/?auth_code=hHx_qHbMKvaV5c8k6d3z")
    public Call<BreathTempData> getBreathHisListMap(@FieldMap Map<String,String> mapParam) ;

    /**
     * @return 返回历史记录
     * /api/iphone/ecg/EcGHistoryData/?auth_code=hHx_qHbMKvaV5c8k6d3z
     */
    @FormUrlEncoded
    @POST("/api/iphone/ecg/EcGHistoryData/?auth_code=hHx_qHbMKvaV5c8k6d3z")
    public Call<BreathTempData> getBreathHisListMoreMap(@FieldMap Map<String,String> mapParam) ;



    /**
     * 获取详细的报告
     * @param record_id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/iphone/ecg/GetHistoryInfo/?auth_code=hHx_qHbMKvaV5c8k6d3z")
    public Call<BreathDetailSuccess> getBreathDetailReport(@Field("record_id") String record_id) ;

    /**
     * @param user_name
     * @param user_password
     * @return
     */
    @FormUrlEncoded
    @POST("/api/iphone/user/OwnLogin/?auth_code=hHx_qHbMKvaV5c8k6d3z")
    public Call<BreathSuccessUser> loginUser(@Field("user_name") String user_name ,@Field("user_password") String user_password) ;

    /**
     * @param user_id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/iphone/user/UserInfoByID/?auth_code=hHx_qHbMKvaV5c8k6d3z")
    public Call<BreathSuccessUser> getUserInfo(@Field("user_id") String user_id) ;

    /**
     * 修改用户信息
     * @param user_id
     * @param head_img
     * @param full_name
     * @param gender
     * @param user_birthday
     * @param user_disease
     * @return
     */
    @FormUrlEncoded
    @POST("/api/iphone/user/ModifyUserInfo/?auth_code=hHx_qHbMKvaV5c8k6d3z")
    public Call<Data> modifyUserInfo(@Field("user_id") String user_id ,
                                             @Field("head_img") String head_img,
                                             @Field("full_name") String full_name,
                                             @Field("gender") String gender,
                                             @Field("user_birthday") String user_birthday,
                                             @Field("user_disease") String user_disease) ;

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    @FormUrlEncoded
    @POST("/api/iphone/user/SendPhoneCode/?auth_code=hHx_qHbMKvaV5c8k6d3z")
    public Call<Data> sendPhoneCode(@Field("phone") String phone ) ;

    /**
     * 检验验证码
     * @param phone
     * @param code
     * @return
     */
    @FormUrlEncoded
    @POST("/api/iphone/user/ValidateCode/?auth_code=hHx_qHbMKvaV5c8k6d3z")
    public Call<Data> validateCode(@Field("phone") String phone ,@Field("code") String code) ;

    /**
     * 忘记密码
     * @param user_name
     * @param new_password
     * @return
     */
    @FormUrlEncoded
    @POST("/api/iphone/user/ForgetPassword/?auth_code=hHx_qHbMKvaV5c8k6d3z")
    public Call<Data> forgetPassword(@Field("user_name") String user_name , @Field("new_password") String new_password) ;

    ///api/iphone/user/UpdatePassword/?auth_code=hHx_qHbMKvaV5c8k6d3z


    /**
     * 修改密码
     * @param user_id
     * @param old_password
     * @param new_password
     * @return
     */
    @FormUrlEncoded
    @POST("/api/iphone/user/UpdatePassword/?auth_code=hHx_qHbMKvaV5c8k6d3z")
    public Call<Data> updatePassword(@Field("user_id") String user_id ,@Field("old_password") String old_password , @Field("new_password") String new_password) ;
}
