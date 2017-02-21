package com.jkxy.expressquery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jkxy.expressquery.R;
import com.jkxy.expressquery.bean.DetailInfoBean;

import java.util.List;

/**
 * Created by zh on 2017/2/20.
 */

public class DetailInfoAdapter extends BaseAdapter {

    private final List<DetailInfoBean> list;
    private final Context context;

    public DetailInfoAdapter(Context context, List<DetailInfoBean> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.detail_info_list_item_view,null);
            holder.point = (TextView) convertView.findViewById(R.id.point);
            holder.line = (TextView) convertView.findViewById(R.id.line);
            holder.dateYearMonthDay = (TextView) convertView.findViewById(R.id.date_year_month_day);
            holder.dateWeek = (TextView) convertView.findViewById(R.id.date_week);
            holder.dateTime = (TextView) convertView.findViewById(R.id.date_time);
            holder.info = (TextView) convertView.findViewById(R.id.info);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        DetailInfoBean infoBean = list.get(position);

        holder.dateYearMonthDay.setText(infoBean.getDateYearMonthDay());
        holder.dateWeek.setText(infoBean.getDateWeek());
        holder.dateTime.setText(infoBean.getDateTime());
        holder.info.setText(infoBean.getInfo());

        if (position == 0){
            holder.point.setBackground(context.getDrawable(R.drawable.point_now));
            holder.dateYearMonthDay.setTextColor(context.getResources().getColor(R.color.textview_now));
            holder.dateWeek.setTextColor(context.getResources().getColor(R.color.textview_now));
            holder.dateTime.setTextColor(context.getResources().getColor(R.color.textview_now));
            holder.info.setTextColor(context.getResources().getColor(R.color.textview_now));
        }else {
            holder.point.setBackground(context.getDrawable(R.drawable.point));
            holder.dateYearMonthDay.setTextColor(context.getResources().getColor(R.color.textview_normal));
            holder.dateWeek.setTextColor(context.getResources().getColor(R.color.textview_normal));
            holder.dateTime.setTextColor(context.getResources().getColor(R.color.textview_normal));
            holder.info.setTextColor(context.getResources().getColor(R.color.textview_normal));
        }
        if (position == list.size()){
            holder.line.setVisibility(View.INVISIBLE);
        }else {
            holder.line.setVisibility(View.VISIBLE);
        }


        return convertView;
    }

    private class ViewHolder {
        TextView point;
        TextView line;
        TextView dateYearMonthDay;
        TextView dateWeek;
        TextView dateTime;
        TextView info;
    }
}
