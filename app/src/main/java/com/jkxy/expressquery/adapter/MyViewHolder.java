package com.jkxy.expressquery.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jkxy.expressquery.R;

/**
 * Created by zh on 2017/3/4.
 */
class MyViewHolder extends RecyclerView.ViewHolder {

    TextView mTvNumber;
    TextView mTvTag;
    TextView mTvTime;
    ImageView mImg;
    ImageView mFinish;
    LinearLayout mContent;

    Button mCustomRemark;
    Button mNotification;
    Button mDeleteItem;

    MyViewHolder(View itemView) {
        super(itemView);
        mContent = (LinearLayout) itemView.findViewById(R.id.content);
        mTvNumber = (TextView) itemView.findViewById(R.id.tv_show);
        mTvTag = (TextView) itemView.findViewById(R.id.tv_tag);
        mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
        mImg = (ImageView) itemView.findViewById(R.id.img_logo);
        mFinish = (ImageView) itemView.findViewById(R.id.img_finish);
        mCustomRemark = (Button) itemView.findViewById(R.id.btnChange);
        mNotification = (Button) itemView.findViewById(R.id.btnNotify);
        mDeleteItem = (Button) itemView.findViewById(R.id.btnDelete);
    }
}
