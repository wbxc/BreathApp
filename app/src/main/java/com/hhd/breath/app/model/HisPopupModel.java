package com.hhd.breath.app.model;

/**
 * Created by familylove on 2016/7/12.
 */
public class HisPopupModel {

    private String name ;
    private String flag ;
    private String userId ;
    private String breath_type ;

    public String getBreath_type() {
        return breath_type;
    }

    public void setBreath_type(String breath_type) {
        this.breath_type = breath_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
