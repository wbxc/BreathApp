package com.hhd.breath.app.net;

import android.content.Context;
import android.util.Log;

import com.hhd.breath.app.model.Data;
import com.hhd.breath.app.model.PhoneCode;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/5/5.
 */
public class ManagerRequest {

    private static ManagerRequest instance = null;
    private RequestNetApi  requestNetApi = null ;
    public static ManagerRequest getInstance(){
        if (instance == null){
            instance = new ManagerRequest() ;
        }

        return instance ;
    }


    private ManagerRequest() {
        requestNetApi = new Retrofit.Builder()
                                    .baseUrl("http://101.201.39.122:8060")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build()
                                    .create(RequestNetApi.class) ;
    }

    public RequestNetApi getRequestNetApi(){
        if (instance==null)
            instance = new ManagerRequest() ;
        return instance.requestNetApi ;
    }

    /**
     * 获取APi的请求
     * @return
     */
    public static RequestNetApi getRequestApi(){
        if (instance==null)
            instance = new ManagerRequest() ;
        return instance.requestNetApi ;
    }


    /**
     * 手机校验
     * @param iOnCallBack
     * @param phone
     */
    public void checkMobile(final IOnCallBack iOnCallBack ,String phone){
        requestNetApi.checkMobile(phone).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String json = response.body().string() ;
                    Log.e("JSONObject",json) ;
                    JSONObject jsonObject = new JSONObject(json) ;
                    Log.e("JSONObject",jsonObject.toString()) ;
                    if (jsonObject.has("code")){
                        int code = Integer.parseInt(jsonObject.getString("code")) ;
                        switch (code){
                            case 200:
                                iOnCallBack.onSuccess();
                                iOnCallBack.onComplete();
                                break;
                            case 0:
                                iOnCallBack.onFail();
                                iOnCallBack.onComplete();
                                break;
                        }
                    }
                }catch (Exception e){

                    iOnCallBack.onComplete();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                iOnCallBack.onNetError(t.getMessage());
                iOnCallBack.onComplete();
            }
        });
    }

    /**
     * 发送验证码
     * @param phone
     * @param iOnCallBack
     */
    public void  sendPhoneCode(final String phone, final IOnCallBack iOnCallBack){

        requestNetApi.sendVerificationCode(phone).enqueue(new Callback<PhoneCode>() {
            @Override
            public void onResponse(Call<PhoneCode> call, Response<PhoneCode> response) {
                PhoneCode phoneCode = response.body() ;
                if (phoneCode!=null){
                    switch (Integer.parseInt(phoneCode.getCode())){
                        case 200:
                            iOnCallBack.onComplete();
                            iOnCallBack.onSuccess();
                            break;
                        default:
                            iOnCallBack.onComplete();
                            iOnCallBack.onFail();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<PhoneCode> call, Throwable t) {

                iOnCallBack.onNetError(t.getMessage());
                iOnCallBack.onComplete();
            }
        });

    }

    /**
     * 用户注册
     * @param userName
     * @param password
     * @param code
     * @param iDataCallBack
     * 1831024
     */

    public void register(String userName, String password, String code, final IDataCallBack iDataCallBack){
        requestNetApi.register(userName,password,code,"","","").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string()) ;
                    if (jsonObject.has("code")){
                        switch (Integer.parseInt(jsonObject.getString("code"))){
                            case 200:
                                iDataCallBack.onComplete();
                                JSONObject data = jsonObject.getJSONObject("data") ;
                                iDataCallBack.onSuccess(data.getString("user_id"));
                                break;
                            case 100 :
                                iDataCallBack.onComplete();
                                if (jsonObject.has("msg")){
                                    iDataCallBack.onFail(jsonObject.getString("msg"));
                                }
                                break;
                            case 0:
                                iDataCallBack.onComplete();
                                iDataCallBack.onFail("注册失败");
                                break;
                        }
                    }

                }catch (Exception e){

                    iDataCallBack.onComplete();
                    iDataCallBack.onFail(e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                iDataCallBack.onComplete();
                iDataCallBack.onNetError(t.getMessage());
            }
        });
    }

    ///user/ModifyUserInfo/


    /**
     * 修改用户信息
     * @param user_id
     * @param head_img
     * @param full_name
     * @param user_birthday
     * @param gender
     * @param user_disease
     * @param dataCallBack
     */
    public void  modifyUserInfo(String user_id , String head_img,String full_name, String user_birthday,String gender,String user_disease , final IDataCallBack dataCallBack ){

        requestNetApi.modifyUserInfo(user_id,head_img,full_name,gender,user_birthday,user_disease).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {

                switch (Integer.parseInt(response.body().getCode())){
                    case 200:
                        dataCallBack.onSuccess("");
                        dataCallBack.onComplete();
                        break;
                    default:
                        dataCallBack.onFail("");
                        dataCallBack.onComplete();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

                dataCallBack.onNetError(t.getMessage());
                dataCallBack.onComplete();
            }
        });
    }


    /**
     * 发送验证码
     * @param phone
     * @param dataCallBack
     */
    public void sendPhoneCode(String phone, final IDataCallBack dataCallBack){

        requestNetApi.sendPhoneCode(phone).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                switch (Integer.parseInt(response.body().getCode())){
                    case 200:
                        dataCallBack.onComplete();
                        dataCallBack.onSuccess(response.body().getMsg());

                        break;
                    default:
                        dataCallBack.onComplete();
                        dataCallBack.onFail(response.body().getMsg());
                        break;
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                dataCallBack.onComplete();
                dataCallBack.onNetError(t.getMessage());

            }
        });
    }


    public void validateCode(String phone , String code ,final IDataCallBack dataCallBack){

        requestNetApi.validateCode(phone, code).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                switch (Integer.parseInt(response.body().getCode())){
                    case 200 :
                        dataCallBack.onComplete();
                        dataCallBack.onSuccess(response.body().getMsg());
                        break;
                    default:
                        dataCallBack.onComplete();
                        dataCallBack.onFail(response.body().getMsg());
                        break;
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                dataCallBack.onComplete();
                dataCallBack.onNetError(t.getMessage());
            }
        });

    }


    /**
     * 忘记密码
     * @param phone
     * @param password
     * @param dataCallBack
     */
    public void forgetPassword(final String phone, final String password, final IDataCallBack dataCallBack){

        requestNetApi.forgetPassword(phone, password).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                switch (Integer.parseInt(response.body().getCode())) {
                    case 200:
                        dataCallBack.onSuccess(response.body().getData());
                        dataCallBack.onComplete();
                        break;
                    default:
                        dataCallBack.onFail(response.body().getData());
                        dataCallBack.onComplete();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

                dataCallBack.onNetError(t.getMessage());
                dataCallBack.onComplete();
            }
        });






    }

    /**
     * 修改密码
     * @param userId
     * @param old_password
     * @param new_password
     */
    public void updatePassword(String userId , String old_password ,String new_password, final IDataCallBack dataCallBack){

        requestNetApi.updatePassword(userId,old_password, new_password).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                switch (Integer.parseInt(response.body().getCode())) {
                    case 200:
                        dataCallBack.onSuccess(response.body().getData());
                        dataCallBack.onComplete();
                        break;
                    default:
                        dataCallBack.onFail(response.body().getData());
                        dataCallBack.onComplete();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                dataCallBack.onNetError(t.getMessage());
                dataCallBack.onComplete();
            }
        });
    }





    //17744467920

    public interface IOnCallBack{
        public void  onNetError(String msg) ;
        public void  onSuccess() ;
        public void  onFail() ;
        public void  onComplete() ;
    }

    public interface IDataCallBack{

        public void onNetError(String msg) ;
        public void onSuccess(String msg);
        public void onFail(String msg) ;
        public void onComplete() ;
    }
}
