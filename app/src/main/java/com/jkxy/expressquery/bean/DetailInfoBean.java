package com.jkxy.expressquery.bean;

/**
 * Created by zh on 2017/2/21.
 */

public class DetailInfoBean {

    private String dateYearMonthDay;
    private String dateWeek;
    private String dateTime;
    private String info;

    public DetailInfoBean(String dateYearMonthDay, String dateWeek, String dateTime, String info) {
        this.dateYearMonthDay = dateYearMonthDay;
        this.dateWeek = dateWeek;
        this.dateTime = dateTime;
        this.info = info;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getInfo() {
        return info;
    }

    public String getDateYearMonthDay() {
        return dateYearMonthDay;
    }

    public String getDateWeek() {
        return dateWeek;
    }
}
