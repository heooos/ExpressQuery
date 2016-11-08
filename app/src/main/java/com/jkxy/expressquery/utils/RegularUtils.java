package com.jkxy.expressquery.utils;

/**
 * Created by zh on 16/9/23.
 * 快递单号正则判断
 */

public class RegularUtils {
    public static String parseExpressCode(String code) {
        String reg = ":";
        String[] content = code.split(reg);
        return content.length>0?content[1]:null;
    }
}
