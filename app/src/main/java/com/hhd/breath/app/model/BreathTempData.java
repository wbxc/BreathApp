package com.hhd.breath.app.model;

import java.util.List;

/**
 * Created by familylove on 2016/5/17.
 */
public class BreathTempData {
    private String code ;
    private List<BreathHistoricalData> data ;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public List<BreathHistoricalData> getData() {
        return data;
    }

    public void setData(List<BreathHistoricalData> data) {
        this.data = data;
    }
}
