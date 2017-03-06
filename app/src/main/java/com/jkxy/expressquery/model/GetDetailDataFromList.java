package com.jkxy.expressquery.model;

import com.jkxy.expressquery.bean.CacheBean;
import com.jkxy.expressquery.bean.DetailInfoBean;
import com.jkxy.expressquery.bean.DetailInfoChildBean;
import com.jkxy.expressquery.bean.DetailInfoGroupBean;
import com.jkxy.expressquery.utils.RegularUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zh on 2017/3/1.
 */

public class GetDetailDataFromList {

    public static CacheBean getMap(List<DetailInfoBean> list) {

        String phoneNumber = "";

        List<DetailInfoGroupBean> groupList = new ArrayList<>();
        Map<DetailInfoGroupBean, List<DetailInfoChildBean>> dataMap = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            String dateYearMonthDay = list.get(i).getDateYearMonthDay();
            String dateWeek = list.get(i).getDateWeek();
            DetailInfoGroupBean bean;
            if (groupList.size() > 0) {
                if (!groupList.get(groupList.size() - 1)
                        .getDateYearMonthDay()
                        .equals(dateYearMonthDay)) {
                    bean = new DetailInfoGroupBean(dateYearMonthDay, dateWeek);
                    groupList.add(bean);
                }
            } else {
                bean = new DetailInfoGroupBean(dateYearMonthDay, dateWeek);
                groupList.add(bean);
            }
            if (i >= (list.size() - 2)) {
                String info = list.get(i).getInfo();
                String temp = RegularUtils.parsePhoneNumber(info).trim();
                if (!temp.equals("")) {
                    phoneNumber = temp;
                }
            }
        }
        System.out.println("键数量为:" + groupList.size());
        for (int i = 0; i < groupList.size(); i++) {
            List<DetailInfoChildBean> childBeanList = new ArrayList<>();
            DetailInfoGroupBean bean = groupList.get(i);
            for (DetailInfoBean l : list) {
                if (groupList.get(i)
                        .getDateYearMonthDay()
                        .equals(l.getDateYearMonthDay())) {
                    String dateTime = l.getDateTime();
                    String info = l.getInfo();
                    DetailInfoChildBean childBean = new DetailInfoChildBean(dateTime, info);
                    childBeanList.add(childBean);
                }
//                Collections.reverse(childBeanList);
                dataMap.put(bean, childBeanList);
            }
        }

        CacheBean bean = new CacheBean(groupList, phoneNumber, dataMap);

        return bean;
    }


}
