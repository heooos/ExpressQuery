package com.jkxy.expressquery.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by zh on 2017/3/1.
 */

public class CacheBean {

    private List<DetailInfoGroupBean> groupList;
    private String phoneNumber;
    private Map<DetailInfoGroupBean, List<DetailInfoChildBean>> dataMap;

    public CacheBean(List<DetailInfoGroupBean> groupList, String phoneNumber, Map<DetailInfoGroupBean, List<DetailInfoChildBean>> dataMap) {
        this.groupList = groupList;
        this.phoneNumber = phoneNumber;
        this.dataMap = dataMap;
    }

    public List<DetailInfoGroupBean> getGroupList() {
        return groupList;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Map<DetailInfoGroupBean, List<DetailInfoChildBean>> getDataMap() {
        return dataMap;
    }
}
