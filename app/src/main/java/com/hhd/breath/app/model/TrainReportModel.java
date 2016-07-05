package com.hhd.breath.app.model;

/**
 * Created by Administrator on 2015/12/17.
 */
public class TrainReportModel {

/*    public  static String TRAIN_REPORT_RATE = "train_report_rate" ;
    public  static String TRAIN_REPORT_TIME_LONG = "train_report_time_long" ;
    public  static String TRAIN_REPORT_GROUP = "train_report_group" ;
    public  static String TRAIN_REPORT_LEVEL = "train_report_level" ;
    public  static String TRAIN_REPORT_TIME = "train_report_time" ;
    public  static String TRAIN_REPORT_SUGGESTION = "train_report_suggestion" ;
    public  static String TRAIN_REPORT_ID = "train_report_id" ;*/

    private String reportId ;
    private String reportRate ;
    private String reportTimeLong ;
    private String reportGroup ;
    private String reportLevel ;
    private String reportTime ;
    private String reportSuggestion ;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportRate() {
        return reportRate;
    }

    public void setReportRate(String reportRate) {
        this.reportRate = reportRate;
    }

    public String getReportTimeLong() {
        return reportTimeLong;
    }

    public void setReportTimeLong(String reportTimeLong) {
        this.reportTimeLong = reportTimeLong;
    }

    public String getReportGroup() {
        return reportGroup;
    }

    public void setReportGroup(String reportGroup) {
        this.reportGroup = reportGroup;
    }

    public String getReportLevel() {
        return reportLevel;
    }

    public void setReportLevel(String reportLevel) {
        this.reportLevel = reportLevel;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getReportSuggestion() {
        return reportSuggestion;
    }

    public void setReportSuggestion(String reportSuggestion) {
        this.reportSuggestion = reportSuggestion;
    }
}
