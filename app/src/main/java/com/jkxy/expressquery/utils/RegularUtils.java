package com.jkxy.expressquery.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zh on 16/9/23.
 * 正则判断
 */

public class RegularUtils {

    /**
     *
     * @param code
     * @return
     */
    public static String parseExpressCode(String code) {
        String reg = ":";
        String[] content = code.split(reg);
        return content.length > 0 ? content[1] : null;
    }

    /**
     * 正则判断手机号码
     * @param info
     * @return
     */
    public static String parsePhoneNumber(String info) {

        Pattern pattern = Pattern.compile("0?(13|14|15|18)[0-9]{9}");
        Matcher matcher = pattern.matcher(info);
        return matcher.find() ? matcher.group() : "";
    }

    public static boolean isExpressCode(String str){

        String reg = "^[0-9a-zA-Z]{10,}$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();

    }
}
