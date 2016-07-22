package com.hhd.breath.app;

import android.os.Environment;

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
    public static String LEVEL_ONE_DS = "缩唇呼吸训练的目的其实都是为了大家的身体健康着想的，可以帮助大家肺循环得以改善，而且还可以提高大家支气管的内压，避免自己的支气管塌陷；更主要的是多联系缩唇呼吸训练还可以提高膈肌肌力的。也可以慢慢的将自己的胸腔压力降低。肺活量得以增加，所以缩唇呼吸训练是很好的一种训练方式。\n" +
            "\n" +
            "缩唇呼吸训练的方式主要是强调膈肌呼吸的一种改善不一样的呼吸的方法，比较适合一些肺疾病的患者。大家在进行缩唇呼吸的时候，大家将自己的唇部慢慢的放松，然后经过自己的鼻部慢慢的深吸气，吸气的时候将空气吸往自己的唇部。增加大家唇内的内压，促进自己气呼出。\n" +
            "\n" +
            "大家在进行缩唇呼吸训练的时候，一定要注意自己呼气与吸气之间的时间频率，呼吸的时候不要频率不一样的，而且要深呼吸，减慢自己的呼吸频率，提高自己的通气的节奏。每次练习缩唇呼吸训练的次数不能太多，每天三次左右就足够了。\n" +
            "\n" +
            "注意在进行缩唇呼吸训练的时候呼吸次数不能太多，尤其是肺疾病的患者。避免他们的肺活量下降，如果过度的用力的话，有的患者会有可能导致呼吸困难的。\n" +
            "\n" +
            "缩唇呼吸训练能帮助到大家的有很多，所以大家在平常没事的时候可以多联系下缩唇呼吸训练。保持自己肺循环的稳定，提高自己的肺活量，让自己的身体越来越好。而且缩唇呼吸训练还能让自己的唇部变得好看。" ;
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

    public static String PATH_ZIP = Environment.getExternalStorageDirectory().toString()+"/Android/data/breath/" ;
    //public static boolean isOpenBreath = false ;

    public static float SKY_HEIGHT = 602f ;
    public static float SKY_BIRD_ALLOW = 450f ;
    public static float SKY_STRENGTH_VALE = 0f ;
    public static float CONTROLLER_VALUE = 0f ;
    public static float TOP_PAIR_HEIGHT  = 0f ;
    public static float BOTTOM_PAIR_HEIGHT = 0f ;
    public static float PAIR_WIDTH = 82f ;
    public static float PAIR_WIDTH_CENTER = 88f ;
    public static float CENTER_HEIGHT = 0f ;
    public static float PAIR_WIDTH_CENTER_HEIGHT = 41f ;
    public static int BIRD_DISTANCE_SPEED = 4 ;

    /**
     * 视野环境创建的宽度和高度
     */
    public static float CAMERA_WIDTH = 485 ; //485  545 485
    public static float CAMERA_HEIGHT = 900 ; //800 ;


    public static String c_l_value = "13" ;
    public static String c_z_value = "10" ;
    public static String c_h_value = "7" ;


    public static String s_l_value = "15" ;
    public static String s_z_value = "25" ;
    public static String s_h_value = "35" ;

    public static String p_l_value = "3" ;
    public static String p_z_value = "4" ;
    public static String p_h_value = "5" ;




}
