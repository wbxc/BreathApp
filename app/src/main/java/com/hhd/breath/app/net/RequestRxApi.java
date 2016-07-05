package com.hhd.breath.app.net;

import com.hhd.breath.app.model.BreathSuccessUser;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by familylove on 2016/5/22.
 */
public interface RequestRxApi {

    @FormUrlEncoded
    @POST("/api/iphone/user/UserInfoByID/?auth_code=hHx_qHbMKvaV5c8k6d3z")
    Observable<BreathSuccessUser> getUserInfo(@Field("user_id") String user_id) ;
}
