package com.jkxy.expressquery.listener;

import android.view.View;

import com.jkxy.expressquery.entity.ListInfoBean;

/**
 * Created by zh on 2017/3/1.
 */
public interface IOnRecyclerViewItemClickListener {
    void onItemClick(ListInfoBean bean, View v, int position);
}
