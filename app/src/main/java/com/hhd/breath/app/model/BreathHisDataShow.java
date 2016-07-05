package com.hhd.breath.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by familylove on 2016/5/16.
 * 列表显示
 */
public class BreathHisDataShow {
    private String measure_time ;
    private List<BreathHistoricalData> breathHistoricalDatas = new ArrayList<BreathHistoricalData>();

    public String getMeasure_time() {
        return measure_time;
    }

    public void setMeasure_time(String measure_time) {
        this.measure_time = measure_time;
    }

    public List<BreathHistoricalData> getBreathHistoricalDatas() {
        return breathHistoricalDatas;
    }

    public void setBreathHistoricalDatas(List<BreathHistoricalData> breathHistoricalDatas) {
        this.breathHistoricalDatas = breathHistoricalDatas;
    }
}
