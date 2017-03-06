package com.jkxy.expressquery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jkxy.expressquery.R;
import com.jkxy.expressquery.bean.ListInfoBean;
import com.jkxy.expressquery.impl.IOnRecyclerViewItemClickListener;
import com.jkxy.expressquery.impl.ISwipeMenuClickListener;

import java.util.List;
import java.util.Objects;

/**
 * Created by zh on 16/9/20.
 */
public class CustomAdapter extends RecyclerView.Adapter<MyViewHolder> {

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

        if (Objects.equals("3", mData.get(position).getState())){
            holder.mFinish.setVisibility(View.VISIBLE);
        }else {
            holder.mFinish.setVisibility(View.INVISIBLE);
        }

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

}

