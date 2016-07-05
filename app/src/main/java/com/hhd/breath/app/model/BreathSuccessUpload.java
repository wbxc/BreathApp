package com.hhd.breath.app.model;

import java.util.List;

/**
 * Created by familylove on 2016/5/18.
 */
public class BreathSuccessUpload {



/*    {
        "code" : "200",
            "data" :
        {
            "id" : "14635585524196",
                "file_path" : "http://101.201.39.122:8060/static/images/20160518/573c219821ccb.zip",
                "upload_time" : 1463558552
        }
    }*/

    private String code ;
    private List<SuccessUpload> data ;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<SuccessUpload> getData() {
        return data;
    }

    public void setData(List<SuccessUpload> data) {
        this.data = data;
    }
}
