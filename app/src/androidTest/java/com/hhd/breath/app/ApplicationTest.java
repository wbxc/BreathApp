package com.hhd.breath.app;

import android.app.Application;
import android.os.Environment;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.hhd.breath.app.db.CaseBookService;
import com.hhd.breath.app.db.HealthDataService;
import com.hhd.breath.app.db.ReportService;
import com.hhd.breath.app.db.TrainDaySevice;
import com.hhd.breath.app.db.TrainPlanService;
import com.hhd.breath.app.db.TrainUnitService;
import com.hhd.breath.app.model.BreathTrainingResult;
import com.hhd.breath.app.model.HealthData;
import com.hhd.breath.app.model.MedicalHis;
import com.hhd.breath.app.model.RecordDayData;
import com.hhd.breath.app.model.RecordUnitData;
import com.hhd.breath.app.model.TrainPlan;
import com.hhd.breath.app.model.TrainReportModel;
import com.hhd.breath.app.net.UploadRecordData;
import com.hhd.breath.app.utils.FileUtils;
import com.hhd.breath.app.utils.MD5Util;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.utils.TimeUtils;
import com.hhd.breath.app.utils.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testHealthData()throws Exception{

        HealthData healthData = new HealthData() ;
        healthData.setUserId(ShareUtils.getUserId(getContext()));

        healthData.setMaxRate("800");
        healthData.setSecondValue("80");
        healthData.setCompValue("2000");

        SimpleDateFormat spf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss") ;
        String strTime = spf.format(new Date( System.currentTimeMillis())) ;
        //strTime = strTime.substring(11,16) ;
        strTime = strTime.substring(5,10) ;
        healthData.setTime(strTime);

        boolean flag = HealthDataService.getInstance(getContext()).add(healthData) ;

        Log.e("HealthDataService", "HealthDataService" + flag) ;
    }


    public void testCheck() throws Exception{

        TrainPlan trainPlan = TrainPlanService.getInstance(getContext()).find() ;

        if (trainPlan!=null){

            Log.e("HealthDataService", "HealthDataService" + trainPlan.getName()+">>>"+trainPlan.getControl()) ;
        }
    }

    public void testAddCheck() throws  Exception{

        TrainPlan trainPlan = new TrainPlan() ;
        trainPlan.setTrainType("0");  // 循序渐进呼气类型
        trainPlan.setName("循序渐进训练");   // 训练的名称
        trainPlan.setInspirerTime("3");   // 吸气时间 就是暂停时间
        trainPlan.setGroupNumber("10");    // 呼吸训练的组数
        trainPlan.setTimes("1");           // 完成多少次可以晋级
        trainPlan.setControlLevel("13");     // 控制强度
        trainPlan.setControl("1");
        trainPlan.setStrength("1");
        trainPlan.setStrengthLevel("15");
        trainPlan.setPersistent("1");
        trainPlan.setPersistentLevel("3");
        trainPlan.setUserId(ShareUtils.getUserId(getContext()));
        trainPlan.setCumulativeTime("");   // 训练累计时间
        trainPlan.setCreateTime("");
        boolean fla = TrainPlanService.getInstance(getContext()).add(trainPlan) ;

        Log.e("HealthDataService", "HealthDataService" + fla) ;
    }

    public void testFindCount() throws  Exception{

        int result = TrainPlanService.getInstance(getContext()).countTrainPlan(ShareUtils.getUserId(getContext())) ;

        Log.e("HealthDataService", "HealthDataService" + result) ;

    }

}