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

   public void testAddTrainPlog() throws Exception{

      // TrainPlanLog{name='我的训练', trainType='3', userId='14612921293140', days=1, trainTimes=1, trainStartTime='1469600597664', trainDayFlag='2016-07-27'}




       TrainPlanLog trainPlanLog1 = TrainPlanService.getInstance(getContext()).findTrainPlanLog("我的训练",ShareUtils.getUserId(getContext())) ;
/*       trainPlanLog1.setTrainType("3");
       trainPlanLog1.setName("我的训练");
       trainPlanLog1.setUserId("14612921293140");
       trainPlanLog1.setDays(2);
       trainPlanLog1.setTrainTimes(3);*/

       TrainPlanService.getInstance(getContext()).addTrainLog(trainPlanLog1);

       TrainPlanLog trainPlanLog =TrainPlanService.getInstance(getContext()).findTrainPlanLog("我的训练",ShareUtils.getUserId(getContext())) ;

       Log.e("trainPlanLog",trainPlanLog.toString()) ;

   }


    public void  testLevel() throws Exception{

        int level = 20 ;
        level = level -1 ;
        int s = level/9+1 ;
        int m_m = level%9 ;
        int m = m_m/6+1 ;
        int star = m_m%6+1 ;

        Log.e("testLevel",s+">>>"+m+">>>"+star) ;
    }




}