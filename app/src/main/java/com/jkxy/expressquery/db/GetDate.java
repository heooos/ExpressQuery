package com.jkxy.expressquery.db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zh on 2016/11/9.
 * 获取时间
 */

public class GetDate {
    public static String getTime() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss", Locale.CHINA);
        Date date = new Date();
        String time = dateFormatter.format(date);
        System.out.print(time);
        return time;
    }
}
