package com.hhd.breath.app;

import android.app.Application;
import android.os.Environment;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.hhd.breath.app.db.CaseBookService;
import com.hhd.breath.app.db.HealthDataService;
import com.hhd.breath.app.db.ReportService;
import com.hhd.breath.app.db.TrainDaySevice;
import com.hhd.breath.app.db.TrainUnitService;
import com.hhd.breath.app.model.BreathTrainingResult;
import com.hhd.breath.app.model.HealthData;
import com.hhd.breath.app.model.MedicalHis;
import com.hhd.breath.app.model.RecordDayData;
import com.hhd.breath.app.model.RecordUnitData;
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

        SimpleDateFormat spf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss") ;
        String strTime = spf.format(new Date( System.currentTimeMillis())) ;
        strTime = strTime.substring(5,10) ;

       // boolean flag = HealthDataService.getInstance(getContext()).isExists(ShareUtils.getUserId(getContext()),strTime) ;

      //  Log.e("HealthDataService","HealthDataService"+flag) ;
    }

}