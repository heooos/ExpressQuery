package com.jkxy.expressquery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jkxy.expressquery.R;
import com.jkxy.expressquery.bean.ListInfoBean;
import com.jkxy.expressquery.impl.IOnRecyclerViewItemClickListener;
import com.jkxy.expressquery.impl.ISwipeMenuClickListener;

import java.util.List;

/**
 * Created by zh on 16/9/20.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<ListInfoBean> mData;
    private LayoutInflater mInflater;
    private IOnRecyclerViewItemClickListener itemClickListener = null;
    private ISwipeMenuClickListener swipeMenuClickListener;

    public CustomAdapter(Context context, List<ListInfoBean> datas) {
        this.mData = datas;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.item, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.mTvNumber.setText(mData.get(position).getLogisticCode());
        holder.mTvTag.setText("备注:" + mData.get(position).getCustomRemark());
        holder.mTvTime.setText("添加时间:" + mData.get(position).getDate());
        holder.mImg.setImageResource(R.drawable.sf);
        holder.mContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(mData.get(position), holder.itemView, holder.getLayoutPosition());
                }
            }
        });
        holder.mCustomRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swipeMenuClickListener != null){
                    swipeMenuClickListener.onSwipeMenuClick(mData.get(position),1,position);
                }
            }
        });

        holder.mNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swipeMenuClickListener != null){
                    swipeMenuClickListener.onSwipeMenuClick(mData.get(position),2,position);
                }
            }
        });

        holder.mDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swipeMenuClickListener != null){
                    swipeMenuClickListener.onSwipeMenuClick(mData.get(position),3,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public void setOnItemClickListener(IOnRecyclerViewItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnSwipeMenuClickListener(ISwipeMenuClickListener listener){
        this.swipeMenuClickListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTvNumber;
        TextView mTvTag;
        TextView mTvTime;
        ImageView mImg;
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

            mCustomRemark = (Button) itemView.findViewById(R.id.btnChange);
            mNotification = (Button) itemView.findViewById(R.id.btnNotify);
            mDeleteItem = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }


}

