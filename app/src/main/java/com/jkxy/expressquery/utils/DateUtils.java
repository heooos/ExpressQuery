package com.jkxy.expressquery.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zh on 2017/2/20.
 */

public class DateUtils {


    public static String getYearMonthDay(String date) {
        Date d;
        String str="";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            d = format.parse(date);
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            str = format1.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getTime(String date) {
        Date d;
        String time="";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            d = format.parse(date);
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
            time = format1.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getWeek(String date){
        Date d;
        String week="";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            d = format.parse(date);
            SimpleDateFormat format1 = new SimpleDateFormat("EEEE");
            week = format1.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return week;
    }
}
