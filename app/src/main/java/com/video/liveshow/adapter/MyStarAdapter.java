package com.video.liveshow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.video.liveshow.R;
import com.video.liveshow.bean.StarBean;
import com.video.liveshow.custom.RefreshAdapter;

import java.util.List;

/**
 * Created by cxf on 2018/7/30.
 */

public class MyStarAdapter extends RefreshAdapter<StarBean> {

    public MyStarAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_star, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position, List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh) vh).setData(mList.get(position), position, payload);
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView name, contribute, big, small, count, rate;

        public Vh(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            small = itemView.findViewById(R.id.small);
            count = itemView.findViewById(R.id.count);
            rate = itemView.findViewById(R.id.rate);
            contribute = (TextView) itemView.findViewById(R.id.contribute);
            big = (TextView) itemView.findViewById(R.id.big);
        }

        void setData(StarBean bean, int position, Object payload) {
            name.setText(bean.expert_name);
            contribute.setText(bean.contribution);
            small.setText(bean.small_activation);
            big.setText(bean.big_activation);
            count.setText(bean.lower_num);
            rate.setText(bean.rate);
        }
    }
}
