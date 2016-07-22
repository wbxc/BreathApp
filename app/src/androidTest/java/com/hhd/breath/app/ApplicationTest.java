package com.hhd.breath.app;

import android.app.Application;
import android.os.Environment;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.hhd.breath.app.db.CaseBookService;
import com.hhd.breath.app.db.HealthDataService;
import com.hhd.breath.app.db.ReportService;
import com.hhd.breath.app.db.TrainDaySevice;
import com.hhd.breath.app.db.TrainHisService;
import com.hhd.breath.app.db.TrainPlanService;
import com.hhd.breath.app.db.TrainUnitService;
import com.hhd.breath.app.model.BreathDetailReport;
import com.hhd.breath.app.model.BreathHisLog;
import com.hhd.breath.app.model.BreathTrainingResult;
import com.hhd.breath.app.model.HealthData;
import com.hhd.breath.app.model.MedicalHis;
import com.hhd.breath.app.model.RecordDayData;
import com.hhd.breath.app.model.RecordUnitData;
import com.hhd.breath.app.model.TrainPlan;
import com.hhd.breath.app.model.TrainPlanLog;
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


    public void testAddTrainPlanLog() throws  Exception{




        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss") ;
        String dateFlag = sdf.format(new Date(System.currentTimeMillis())) ;



        TrainPlanLog trainPlanLog = new TrainPlanLog() ;
        trainPlanLog.setUserId(ShareUtils.getUserId(getContext()));
        trainPlanLog.setName("循序渐进训练");
        trainPlanLog.setTrainType("0");
        trainPlanLog.setTrainStartTime(String.valueOf(System.currentTimeMillis()));
        trainPlanLog.setTrainTimes(0);
        trainPlanLog.setDays(0);
        trainPlanLog.setTrainDayFlag(dateFlag);

        TrainPlanService.getInstance(getContext()).addTrainLog(trainPlanLog);
    }

    public void testSan() throws Exception{

        /*TrainPlanLog trainPlanLog = TrainPlanService.getInstance(getContext()).findTrainPlanLog("循序渐进训练","0") ;
        Log.e("trainPlanLog","trainPlanLog:"+trainPlanLog.getName()+">>>>>"+trainPlanLog.getTrainTimes()+">>>"+trainPlanLog.getDays()+">>>"+trainPlanLog.getTrainDayFlag()) ;
*/
        List<TrainPlan> trainPlens = TrainPlanService.getInstance(getContext()).getTrainPlans(ShareUtils.getUserId(getContext())) ;


        //TrainPlan trainPlan = TrainPlanService.getInstance(getContext()).findTrainPlan(ShareUtils.getUserId(getContext()),"0") ;
        for (int i=0 ; i<trainPlens.size() ; i++){
            Log.e("trainPlanLog","trainPlanLog:"+trainPlens.get(i).toString()) ;
        }


    }


    public void testTrainPlan() throws Exception{

       /* TrainPlanLog trainPlanLog = TrainPlanService.getInstance(getContext()).findTrainPlanLog("循序渐进训练","0") ;

        if (trainPlanLog==null){

            Log.e("trainPlanLog","查询的结果是空值") ;
        }else {
            Log.e("trainPlanLog","查询的结果是有值") ;
        }*/



 /*       TrainPlan trainPlan = TrainPlanService.getInstance(getContext()).findTrainPlan(ShareUtils.getUserId(getContext()),"0") ;


        TrainPlanService.getInstance(getContext()).countSumTime("100",trainPlan);


        TrainPlan trainPlan1 = TrainPlanService.getInstance(getContext()).findTrainPlan(ShareUtils.getUserId(getContext()),"0") ;*/

        List<HealthData> healthDatas = HealthDataService.getInstance(getContext()).findHealths(ShareUtils.getUserId(getContext())) ;
        Log.e("trainPlanLog",""+healthDatas.size()+">>>.") ;

        HealthData healthData = new HealthData() ;
        healthData.setUserId(ShareUtils.getUserId(getContext()));
        healthData.setMaxRate("1900");
        healthData.setSecondValue("90");
        healthData.setCompValue("2000");

        SimpleDateFormat spf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss") ;
        String strTime = spf.format(new Date(System.currentTimeMillis())) ;
        strTime = strTime.substring(5,10) ;
        healthData.setTime(strTime);



        boolean falg = HealthDataService.getInstance(getContext()).add(healthData) ;

        Log.e("trainPlanLog",""+falg+">>>.") ;

       /* List<BreathDetailReport> breathDetailReports = TrainHisService.getInstance(getContext()).findFiveBreathDetailReports("0",ShareUtils.getUserId(getContext())) ;

        Log.e("trainPlanLog","breathDetailReports"+breathDetailReports.size()) ;
        if (breathDetailReports!=null && !breathDetailReports.isEmpty()){

            for (int i=0 ; i<breathDetailReports.size() ; i++){
                Log.e("trainPlanLog",""+breathDetailReports.get(i).toString()) ;
            }
        }*/
    }

    public void testUpdateTrainPlanLog() throws Exception{

/*        TrainPlanLog trainPlanLog = TrainPlanService.getInstance(getContext()).findTrainPlanLog("循序渐进训练","0") ;

        Log.e("trainPlanLog",trainPlanLog.toString()) ;*/

        BreathHisLog trainPlanLog = TrainHisService.getInstance(getContext()).findBreathHisLog("14688209464508") ;
        Log.e("trainPlanLog",trainPlanLog.toString()) ;



        // TrainPlanService.getInstance(getContext()).addTrainTimes(trainPlanLog);
       /* SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm") ;
        String dateFlag = sdf.format(new Date(System.currentTimeMillis())) ;
        if (!dateFlag.equals(trainPlanLog.getTrainDayFlag())){
            trainPlanLog.setTrainDayFlag(dateFlag);
            TrainPlanService.getInstance(getContext()).addTrainDays(trainPlanLog);
        }*/
    }

    public void testHuanSun() throws  Exception {
        int sends = 10000  ;
        StringBuffer sb = new StringBuffer() ;
        int shi = sends/3600 ;
        if (shi>9){
            sb.append(shi).append(":") ;
        }else {
            sb.append("0"+shi).append(":") ;
        }
        int fens = sends % 3600 ;

        int fen1 = fens /60 ;
        if (fen1>9){
            sb.append(fen1).append(":") ;
        }else {
            sb.append("0"+fen1).append(":") ;
        }

        int second = fens%60 ;
        if (second>9){
            sb.append(second) ;
        }else {
            sb.append("0"+second) ;
        }


        ///cr_file_path" : "http://127.0.0.1:3000/static/images/20160519/573d2e172e33f.zip"

        Log.e("trainPlanLog","查询的"+sb.toString()+"fens"+fens) ;

    }

    public void  testUpdateTrainPlan() throws Exception{


        TrainPlan trainPlan = new TrainPlan() ;
        trainPlan.setTrainType("0");  // 循序渐进呼气类型
        trainPlan.setName("循序渐进训练");   // 训练的名称
        trainPlan.setInspirerTime("3");   // 吸气时间 就是暂停时间


        trainPlan.setGroupNumber("10");    // 呼吸训练的组数
        trainPlan.setTimes("1");           // 完成多少次可以晋级
        trainPlan.setControlLevel("10");     // 控制强度
        trainPlan.setControl("1");
        trainPlan.setStrength("1");


        trainPlan.setStrengthLevel("15");
        trainPlan.setPersistent("1");
        trainPlan.setPersistentLevel("3");   // 吸气时间
        trainPlan.setUserId(ShareUtils.getUserId(getContext()));
        trainPlan.setCumulativeTime("300");   // 训练累计时间
        trainPlan.setCreateTime("");


        trainPlan.setCurrentStrength("1");
        trainPlan.setCurrentControl("2");
        trainPlan.setCurrentPersistent("1");




        TrainPlanService.getInstance(getContext()).updateTrainPlan(trainPlan);



    }






}