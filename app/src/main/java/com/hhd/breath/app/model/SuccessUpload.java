package com.hhd.breath.app.model;

/**
 * Created by familylove on 2016/5/18.
 */
public class SuccessUpload {
    /*{
        "code" : "200",
            "data" :
        {
            "id" : "14635585524196",
                "file_path" : "http://101.201.39.122:8060/static/images/20160518/573c219821ccb.zip",
                "upload_time" : 1463558552
        }
    }*/

    private String id ;
    private String file_path ;
    private String upload_time ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    @Override
    public String toString() {
        return "SuccessUpload{" +
                "id='" + id + '\'' +
                ", file_path='" + file_path + '\'' +
                ", upload_time='" + upload_time + '\'' +
                '}';
    }
}
