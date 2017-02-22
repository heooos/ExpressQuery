package com.jkxy.expressquery.bean;

/**
 * Created by zh on 2017/2/20.
 */

public class DetailInfoGroupBean {

    private String dateYearMonthDay;
    private String dateWeek;

    public DetailInfoGroupBean(String dateYearMonthDay, String dateWeek) {
        this.dateYearMonthDay = dateYearMonthDay;
        this.dateWeek = dateWeek;
    }

    public String getDateYearMonthDay() {
        return dateYearMonthDay;
    }

    public String getDateWeek() {
        return dateWeek;
    }
}
