package com.jkxy.expressquery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jkxy.expressquery.R;
import com.jkxy.expressquery.bean.DetailInfoChildBean;
import com.jkxy.expressquery.bean.DetailInfoGroupBean;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zh on 2017/2/21.
 */

public class DetailInfoListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final Map<DetailInfoGroupBean, List<DetailInfoChildBean>> newMap;
    private List<DetailInfoGroupBean> groupList;

    public DetailInfoListAdapter(Context context, List<DetailInfoGroupBean> groupList, Map<DetailInfoGroupBean, List<DetailInfoChildBean>> dataMap) {
        this.context = context;
        this.groupList = groupList;
        this.newMap = reverseList(dataMap);
    }

    private Map<DetailInfoGroupBean, List<DetailInfoChildBean>> reverseList(Map<DetailInfoGroupBean, List<DetailInfoChildBean>> dataMap) {
        Map<DetailInfoGroupBean, List<DetailInfoChildBean>> map = new HashMap<>();
        for (int i = 0; i < this.groupList.size(); i++) {
            List<DetailInfoChildBean> list = dataMap.get(groupList.get(i));
            Collections.reverse(list);
            map.put(groupList.get(i), list);
        }
        return map;
    }

    @Override
    public int getGroupCount() {
        return newMap.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return newMap.get(groupList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return newMap.get(groupList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //父项的显示
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.expandable_list_view_group, null);
        }
        TextView dateYearMonthDay = (TextView) convertView.findViewById(R.id.date_year_month_day);
        TextView dateWeek = (TextView) convertView.findViewById(R.id.date_week);
        dateYearMonthDay.setText(groupList.get(groupPosition).getDateYearMonthDay());
        dateWeek.setText(groupList.get(groupPosition).getDateWeek());

        return convertView;
    }

    //子项的显示
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.expandable_list_view_child, null);
        }
        TextView dateTime = (TextView) convertView.findViewById(R.id.date_time);
        TextView info = (TextView) convertView.findViewById(R.id.info);
        dateTime.setText(newMap.get(groupList.get(groupPosition)).get(childPosition).getDateTime());
        info.setText(newMap.get(groupList.get(groupPosition)).get(childPosition).getInfo());

        return convertView;
    }

    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
