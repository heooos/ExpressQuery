package com.jkxy.expressquery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkxy.expressquery.R;
import com.jkxy.expressquery.bean.ListInfoBean;

import java.util.List;

/**
 * Created by zh on 16/9/20.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<ListInfoBean> mData;
    private LayoutInflater mInflater;
    private onRecyclerViewItemClickListener itemClickListener = null;

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
//        holder.mTv.setText();
        holder.mTvNumber.setText("快递单号:"+mData.get(position).getLogisticCode());
        holder.mTvTag.setText("备注:"+mData.get(position).getCustomRemark());
        holder.mTvTime.setText("添加时间:"+mData.get(position).getDate());
        holder.mImg.setImageResource(R.drawable.sf);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null){
                    itemClickListener.onItemClick(mData.get(position),holder.itemView,holder.getLayoutPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public void setOnItemClickListener(onRecyclerViewItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface onRecyclerViewItemClickListener {
        void onItemClick(ListInfoBean bean,View v,int position);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTvNumber;
        TextView mTvTag;
        TextView mTvTime;
        ImageView mImg;

        MyViewHolder(View itemView) {
            super(itemView);

            mTvNumber = (TextView) itemView.findViewById(R.id.tv_show);
            mTvTag = (TextView) itemView.findViewById(R.id.tv_tag);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mImg = (ImageView) itemView.findViewById(R.id.img_logo);
        }
    }



}

