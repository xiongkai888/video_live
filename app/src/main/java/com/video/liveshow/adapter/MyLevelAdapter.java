package com.video.liveshow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.video.liveshow.R;
import com.video.liveshow.bean.LevelBean;
import com.video.liveshow.custom.RefreshAdapter;

import java.util.List;

/**
 * Created by cxf on 2018/7/30.
 */

public class MyLevelAdapter extends RefreshAdapter<LevelBean> {

    public MyLevelAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_level, parent, false));
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

        TextView mLevel;
        TextView mRequest;
        TextView mOrder;

        public Vh(View itemView) {
            super(itemView);
            mLevel = itemView.findViewById(R.id.level);
            mRequest = (TextView) itemView.findViewById(R.id.request);
            mOrder = (TextView) itemView.findViewById(R.id.order);
        }

        void setData(LevelBean bean, int position, Object payload) {
            mLevel.setText("会员等级" + bean.lv);
            mRequest.setText("贡献值>" + bean.contribution + "   实名直推人数≥" + bean.zhitui);
            mOrder.setText("交易手续费" + bean.rate);
        }
    }

    public interface ActionListener {
        void onItemRemoved(String toUid, int position);
    }
}
