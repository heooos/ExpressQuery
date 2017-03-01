package com.jkxy.expressquery.impl;

import android.view.View;

import com.jkxy.expressquery.bean.ListInfoBean;

/**
 * Created by zh on 2017/3/1.
 */
public interface IOnRecyclerViewItemClickListener {
    void onItemClick(ListInfoBean bean, View v, int position);
}
