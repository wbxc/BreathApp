package com.hhd.breath.app.net;

import java.net.URL;

/**
 * Created by familylove on 2016/4/21.
 */
public class NetConfig {

    public static final String  URL_PREFIX = "http://101.201.39.122:8060" ;
    //校验手机号
    public static final String  URL_CHECK_PHONE =URL_PREFIX+"/api/iphone/user/PhoneCode/?auth_code=hHx_qHbMKvaV5c8k6d3z" ;
    //注册用户
    public static final String  URL_REGISTER_USER = URL_PREFIX+"/api/iphone/user/Register/?auth_code=hHx_qHbMKvaV5c8k6d3z" ;
    //发送验证码
    ///api/iphone/user/SendPhoneCode/?auth_code=hHx_qHbMKvaV5c8k6d3z
    public static final String  URL_SEND_CODE = URL_PREFIX+"/api/iphone/user/SendPhoneCode/?auth_code=hHx_qHbMKvaV5c8k6d3z" ;


    //修改用户
    public static final String URL_MODEFY_USER = URL_PREFIX+"/api/iphone/user/ModifyUserInfo/?auth_code=hHx_qHbMKvaV5c8k6d3z" ;

    //用户登录
    public static final String URL_LOGIN = URL_PREFIX+"/api/iphone/user/OwnLogin/?auth_code=hHx_qHbMKvaV5c8k6d3z" ;

    //用户详情获取
    public static final String URL_USER_DETAIL  = URL_PREFIX+"/api/iphone/user/UserInfoByID/?auth_code=hHx_qHbMKvaV5c8k6d3z" ;
    //上传头像
    public static final String URL_UPLOAD_PHOTO = URL_PREFIX+"/api/iphone/user/UploadImage/?auth_code=hHx_qHbMKvaV5c8k6d3z" ;


    /**
     * 呼吸报告
     */
    public static final String URL_UPLOAD_RECORD = URL_PREFIX+"/api/iphone/ecg/RecordInfo/?auth_code=hHx_qHbMKvaV5c8k6d3z" ;

    //获取呼吸训练报告 获取呼吸测量数据的历史数据报告
    public static final String URL_GET_REPORT = URL_PREFIX+"/api/iphone/ecg/EcGHistoryData/?auth_code=hHx_qHbMKvaV5c8k6d3z" ;

    //根据record_id获取详细数据报告: /ecg/GetHistoryInfo/
    public static final String URL_GET_INFO_REPORT = URL_PREFIX+"/api/iphone/ecg/GetHistoryInfo/?auth_code=hHx_qHbMKvaV5c8k6d3z" ;

    //验证: /user/ValidateCode/
    public static final String URL_VALIDATE_CODE = URL_PREFIX+"/api/iphone/user/ValidateCode/?auth_code=hHx_qHbMKvaV5c8k6d3z" ;

    //忘记密码: /user/ForgetPassword/
    public static final String URL_FORGET_PASSWORD = URL_PREFIX+"/api/iphone/user/ForgetPassword/?auth_code=hHx_qHbMKvaV5c8k6d3z" ;
    //修改密码: /user/UpdatePassword/

    public static final String URL_UPDATE_PASSWORD = URL_PREFIX+"/api/iphone/user/UpdatePassword/?auth_code=hHx_qHbMKvaV5c8k6d3z" ;

    public static final String CODE = "code" ;
    public static final String DATA = "data" ;
    public static final String MSG = "msg" ;

    public static final int SUCCESS_CODE = 200 ; //成功
    public static final int FAIL_CODE =  0 ; //失败 未成功
    public static final int ERROR_500 =500  ; //禁止登陆
    public static final int ERROR_NO_DATA = 404 ; // 没有数据
    public static final int ERROR_400 = 400 ;


    public static final int HANDLER_SUCCESS = 0x000101;
    public static final int HANDLER_FAIL = 0x000102 ;

}
