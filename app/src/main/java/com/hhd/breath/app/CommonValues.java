package com.hhd.breath.app;

import android.os.Environment;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.hhd.breath.app.model.RecordUnitData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by familylove on 2015/12/5.
 */
public class CommonValues {

    public static boolean APP_IS_ACTIVE = false ;
    public static boolean BREATH_IS_ACTIVE = false ;
    //用户信息
    public static String USER_NAME = "" ;
    public static String USER_ACCOUNT = "" ;
    public static String USB_CODE = "10c4:ea60" ;
    public static String USB_CODE340 = "1a86:7523" ;
    //public static String USB_CODE340 = "1a86:7524" ;
    public static final float M_UNIT= 16f ;
    public static final int   D_UNIT = 3 ;


    //share 字段表
    // 用户训练
    public static final String ACTION_USB_PERMISSION = "com.wch.wchusbdriver.USB_PERMISSION";
    public static final String ACTION_USB_DETECTION = "com.hhd.breath.usb.detection" ;
    public static final String ACTION_USB_STATUES_SUCCESS = "com.hhd.breath.usb.statues.success" ;
    public static final String ACTION_USB_STATUES_FAIL = "com.hhd.breath.usb.statues.fail" ;
    public static final String ACTION_USB_STATUES_PERMISSION = "com.hhd.breath.usb.statues.permission" ;
    public static final String ACTION_USB_STATUE_FLAG = "usb_status_flag" ;

    /**
     * USB 插入的时候监测
     */
    public static final String USB_CHECK_ACTION = "android.hardware.usb.action.USB_DEVICE_ATTACHED" ;
    /**
     * USB 拔出的时候检测
     */
    public static final String USB_DEVICE_DETACHED ="android.hardware.usb.action.USB_DEVICE_DETACHED" ;
    //上传数据成功
    public static final String UPLOAD_RECORD_DATA_SUCCESS = "com.hhd.upload.data.success" ;
    // 上传数据失败
    public static final String UPLOAD_RECORD_DATA_FAIL = "com.hhd.upload.fail" ;
    public static int VALUE_MAX_PROGRESS = 20 ;
    public static int VALUE_STANDARD = 10 ;
    public static  String sumTimeLong ;
    public static int  STANDARD_VALUE = 30 ;
    public static int  select_activity = 0 ;
    public static int MAX_VALUE = 30 ;
    public static String LEVEL_ONE_DS = "一、训练方式和意义\n" +
            "1.我们在循序渐进训练模式中进行的是呼吸训练中最经典的缩唇腹式呼吸；\n" +
            "2.缩唇呼吸训练可以改善肺内气体交换，减少呼吸耗氧，有利于肺泡排出更多的二氧化碳，改善肺功能，是健肺养肺的好方法；\n" +
            "3.腹式呼吸训练非常有意义，可以改善心肺功能；可以减少肺部感染几率；可以改善脾胃功能，有利于舒肝利胆；可以降血压，对高血压病人很有好处；还有安神益智的效果；\n" +
            "4.通过智能呼吸训练仪的循序渐进训练模式进行缩唇腹式呼吸训练可以让我们改善心肺功能，保持健康状态。\n" +
            "二、训练要求\n" +
            "1.姿势：坐位、站位、半卧位均可；\n" +
            "2.按照训练提示进行呼吸训练；\n" +
            "3.吸气：用鼻子吸气，吸气时让腹部凸起；可以一手置于腹部加强感受；\n" +
            "4.呼气：像吹口哨那样把嘴唇缩起，保持缩唇的姿势吹气；吹气时压缩腹部使之凹陷；\n" +
            "5.吹嘴选择：请选择罩式吹嘴进行健康训练；浮潜式吹嘴适用于运动型选手训练用的；\n" +
            "三、训练设置说明\n" +
            "1.循序渐进训练模式是按照最初级的训练难度开始进行设置的，必须通过训练后逐渐提高训练难度等级，是比较轻松愉快的训练模式选择。\n" +
            "2.控制力说明：训练你对呼气的力度控制，你需要控制吹气力度让小鸟顺利通过障碍，难度等级分为高中低三级；\n" +
            "3.强度说明：训练中对你的呼气气流强度有要求，小鸟飞过的障碍高度代表气流强度，强度要求分为高中低三级；\n" +
            "4.持久性说明：训练你呼气持久性，训练中小鸟飞过的柱子数量代表持续呼气的时间，一根柱子代表呼气一秒时长；\n" +
            "5.晋级的设置顺序是：控制力--强度--持久性\n" +
            "四、训练注意事项\n" +
            "1.每天训练三次，每次训练10分钟，老少皆宜，无特殊禁忌；\n" +
            "2.急性疾病、重症疾病的疾病发作期请勿进行训练；\n" +
            "3.健康之路需要循序渐进，不可贪多勉强；勿过长时间持续训练，训练期间如感觉头晕、心慌或劳累时请停止训练。" ;


    public static boolean check_his_report = false ;
    public static String TRAIN_LEVEL_1 = "" ;

    public static String xiqi = "请从鼻孔缓慢吸入空气\n" +
            "嘴唇务必保持紧闭状态\n"  ;
    public static String huqi = "对准呼吸器，缓慢吹气\n" +
            "让波纹停留在中间位置" ;

    public static String pause = "                 \n" +
            "                   " ;

    public static String SUGGESTION_LEVEL_1 ="您本次呼吸训练控制的比较差，请和医生或健康助理确认是否训练方式有问题，如果没有请加强呼吸训练，养成每天定时训练的习惯。长期坚持使用“汇呼吸”进行训练能有效改善肺循环、提高支气管内压，避免塌陷、提高膈肌肌力、降低胸腔压力、增加肺活量。" ;
    public static String SUGGESTION_LEVEL_2 ="您本次呼吸训练控制的一般般，请与您的医生或健康助理联系并反复学习呼吸训练技巧。长期坚持使用“汇呼吸”进行训练能有效改善肺循环、提高支气管内压，避免塌陷、提高膈肌肌力、降低胸腔压力、增加肺活量。" ;
    public static String SUGGESTION_LEVEL_3 ="您本次呼吸训练控制的比较好，不过还有提升空间。请继续保持每天定时训练的习惯。长期坚持使用“汇呼吸”进行训练能有效改善肺循环、提高支气管内压、提高膈肌肌力、降低胸腔压力、增加肺活量。" ;
    public static String SUGGESTION_LEVEL_4 ="您本次呼吸训练控制的非常好，请继续保持每天定时训练的习惯。长期坚持使用“汇呼吸”进行训练能有效改善肺循环、提高支气管内压、提高膈肌肌力、降低胸腔压力、增加肺活量。" ;

    //判断网络的链接类型
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_MOBILE = 0x04;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    public static final int  SERIAL_COUNT = 6 ; //    serial number

    public static String PATH_ZIP = Environment.getExternalStorageDirectory().toString()+"/Breath/data/breath/" ;
    //public static boolean isOpenBreath = false ;

    public static float SKY_HEIGHT =   860;//765;//602f ;
    public static float SKY_BIRD_ALLOW =  615 ;//450f ;
    public static float SKY_STRENGTH_VALE = 0f ;
    public static float CONTROLLER_VALUE = 0f ;
    public static float TOP_PAIR_HEIGHT  = 0f ;
    public static float BOTTOM_PAIR_HEIGHT = 0f ;
    public static float PAIR_WIDTH =  114.8f ;//82f ;
    public static float PAIR_WIDTH_CENTER =  123f ;//88f ;
    public static float CENTER_HEIGHT = 0f ;
    public static float PAIR_WIDTH_CENTER_HEIGHT = 41f ;
    public static int BIRD_DISTANCE_SPEED = 6 ;
    public static int BIRTH_HEIGHT = 80 ;

    /**
     * 视野环境创建的宽度和高度
     */
    public static float CAMERA_WIDTH = 768 ; //485  545 485
    public static float CAMERA_HEIGHT = 1280 ; //800 ;
    public static String c_l_value = "23" ; //
    public static String c_z_value = "18" ; //
    public static String c_h_value = "13" ;  //
    public static String s_l_value = "15" ;
    public static String s_z_value = "30" ;
    public static String s_h_value = "45" ;   // 60
    public static String p_l_value = "1" ;
    public static String p_z_value = "2" ;
    public static String p_h_value = "3" ;
    public static float SUM_VALUE = 860f ;
    public static float S_VALUE = 0f;
    public static float C_VALUE_TOP = 0F ;

    public static float S_VALUE_LEVEL = 0F ;
    public static float C_VALUE_LEVEL = 0F ;
    public static boolean TRAIN_IS_PLAYING = false ;



}
