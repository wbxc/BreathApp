package com.hhd.breath.app.model;

/**
 * Created by familylove on 2015/12/7.
 */
public class TrainingData {

    private int mXiqiValue ;
    private int mHuxiValue ;
    private int id ;
    private boolean mXiqiFlag ;
    private boolean mHuqiFlag ;
    private int standardValue ;  // 标准值
    private int measureValue ;  // 测量值

    private int mPauseTime ;

    private boolean mPauseFlag ;


    public boolean ismPauseFlag() {
        return mPauseFlag;
    }

    public void setmPauseFlag(boolean mPauseFlag) {
        this.mPauseFlag = mPauseFlag;
    }

    public int getmPauseTime() {
        return mPauseTime;
    }

    public void setmPauseTime(int mPauseTime) {
        this.mPauseTime = mPauseTime;
    }

    public int getStandardValue() {
        return standardValue;
    }

    public void setStandardValue(int standardValue) {
        this.standardValue = standardValue;
    }

    public int getMeasureValue() {
        return measureValue;
    }

    public void setMeasureValue(int measureValue) {
        this.measureValue = measureValue;
    }

    public int getmXiqiValue() {
        return mXiqiValue;
    }

    public void setmXiqiValue(int mXiqiValue) {
        this.mXiqiValue = mXiqiValue;
    }

    public int getmHuxiValue() {
        return mHuxiValue;
    }

    public void setmHuxiValue(int mHuxiValue) {
        this.mHuxiValue = mHuxiValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean ismXiqiFlag() {
        return mXiqiFlag;
    }

    public void setmXiqiFlag(boolean mXiqiFlag) {
        this.mXiqiFlag = mXiqiFlag;
    }

    public boolean ismHuqiFlag() {
        return mHuqiFlag;
    }

    public void setmHuqiFlag(boolean mHuqiFlag) {
        this.mHuqiFlag = mHuqiFlag;
    }
}
