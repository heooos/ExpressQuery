package com.jkxy.expressquery.bean;

/**
 * Created by zh on 2017/2/21.
 */

public class DetailInfoChildBean {
    private String dateTime;
    private String info;

    public DetailInfoChildBean(String dateTime, String info) {
        this.dateTime = dateTime;
        this.info = info;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getInfo() {
        return info;
    }

}
