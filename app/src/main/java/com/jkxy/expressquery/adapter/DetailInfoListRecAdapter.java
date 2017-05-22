package com.jkxy.expressquery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jkxy.expressquery.R;
import com.jkxy.expressquery.entity.DetailInfoChildBean;
import com.jkxy.expressquery.entity.DetailInfoGroupBean;
import com.jkxy.expressquery.entity.Info;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zh on 2017/5/16.
 */

public class DetailInfoListRecAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final List<DetailInfoGroupBean> groupList;
    private final Map<DetailInfoGroupBean, List<DetailInfoChildBean>> newMap;
    private List<Info> mData;


    public DetailInfoListRecAdapter(Context context, List<DetailInfoGroupBean> groupList, Map<DetailInfoGroupBean, List<DetailInfoChildBean>> dataMap) {
        this.context = context;
        this.groupList = groupList;
        this.newMap = reverseList(dataMap);
        mData = getData();
        System.out.println("打印测试数据：" + "   共有" + groupList.size() + "组");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0) {
            //子列表
            return new childViewHolder(LayoutInflater
                    .from(context)
                    .inflate(R.layout.expandable_list_view_child, null, false));
        }
        if (viewType == 1) {
            //父列表
            return new groupViewHolder(LayoutInflater
                    .from(context)
                    .inflate(R.layout.expandable_list_view_group, null, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mData.get(position) instanceof DetailInfoChildBean) {
            ((childViewHolder) holder).dateTime.setText(((DetailInfoChildBean) mData.get(position)).getDateTime());
            ((childViewHolder) holder).info.setText(((DetailInfoChildBean) mData.get(position)).getInfo());
        }
        if (mData.get(position) instanceof DetailInfoGroupBean) {
            ((groupViewHolder) holder).dateYearMonthDay.setText(((DetailInfoGroupBean) mData.get(position)).getDateYearMonthDay());
            ((groupViewHolder) holder).dateWeek.setText(((DetailInfoGroupBean) mData.get(position)).getDateWeek());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) instanceof DetailInfoChildBean) {
            return 0;
        }
        if (mData.get(position) instanceof DetailInfoGroupBean) {
            return 1;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    private Map<DetailInfoGroupBean, List<DetailInfoChildBean>> reverseList(Map<DetailInfoGroupBean, List<DetailInfoChildBean>> dataMap) {
        Map<DetailInfoGroupBean, List<DetailInfoChildBean>> map = new LinkedHashMap<>();
        for (int i = 0; i < this.groupList.size(); i++) {
            List<DetailInfoChildBean> list = dataMap.get(groupList.get(i));
            Collections.reverse(list);
            map.put(groupList.get(i), list);
        }
        return map;
    }

    public List<Info> getData() {
        List<Info> data = new ArrayList<>();
        for (int i = 0; i < groupList.size(); i++) {
            data.add(groupList.get(i));
            List<DetailInfoChildBean> l = newMap.get(groupList.get(i));
            for (int j = 0; j < l.size(); j++) {
                data.add(l.get(j));
            }
        }
        return data;
    }

    class groupViewHolder extends RecyclerView.ViewHolder {

        TextView dateYearMonthDay;
        TextView dateWeek;

        public groupViewHolder(View convertView) {
            super(convertView);
            dateYearMonthDay = (TextView) convertView.findViewById(R.id.date_year_month_day);
            dateWeek = (TextView) convertView.findViewById(R.id.date_week);
        }
    }

    class childViewHolder extends RecyclerView.ViewHolder {

        TextView dateTime;
        TextView info;

        public childViewHolder(View convertView) {
            super(convertView);
            dateTime = (TextView) convertView.findViewById(R.id.date_time);
            info = (TextView) convertView.findViewById(R.id.info);
        }
    }
}
