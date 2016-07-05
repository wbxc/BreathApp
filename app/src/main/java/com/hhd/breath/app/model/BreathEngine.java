package com.hhd.breath.app.model;

/**
 * Created by familylove on 2016/5/26.
 */
public class BreathEngine {

    private int playInt = 0 ;
    private boolean playFlag = false ;

    private int stopInt = 0 ;
    private boolean stopFlag = false ;

    public boolean isPlayFlag() {
        return playFlag;
    }

    public void setPlayFlag(boolean playFlag) {
        this.playFlag = playFlag;
    }

    public boolean isStopFlag() {
        return stopFlag;
    }

    public void setStopFlag(boolean stopFlag) {
        this.stopFlag = stopFlag;
    }

    public int getPlayInt() {
        return playInt;
    }

    public void setPlayInt(int playInt) {
        this.playInt = playInt;
    }

    public int getStopInt() {
        return stopInt;
    }

    public void setStopInt(int stopInt) {
        this.stopInt = stopInt;
    }
}
