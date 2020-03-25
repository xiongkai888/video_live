package com.qianhong.sflive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qianhong.sflive.R;
import com.qianhong.sflive.bean.CandyRecordBean;
import com.qianhong.sflive.custom.RefreshAdapter;

import java.util.List;

/**
 * Created by cxf on 2018/7/30.
 */

public class MyMoneyRecordAdapter extends RefreshAdapter<CandyRecordBean> {

    public MyMoneyRecordAdapter(Context context) {
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

        void setData(CandyRecordBean bean, int position, Object payload) {
            mLevel.setText(bean.name);
            mRequest.setText(bean.addtime);
            mOrder.setText("+" + bean.number);
        }
    }

    public interface ActionListener {
        void onItemRemoved(String toUid, int position);
    }
}
