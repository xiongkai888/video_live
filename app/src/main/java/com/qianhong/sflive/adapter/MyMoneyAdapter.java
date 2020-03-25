package com.qianhong.sflive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

public class MyMoneyAdapter extends RefreshAdapter<CandyRecordBean> {

    public MyMoneyAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_my_money, parent, false));
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

        TextView mName;
        TextView mTime;
        TextView mMoney;
        TextView deduction;

        public Vh(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mTime = (TextView) itemView.findViewById(R.id.time);
            mMoney = (TextView) itemView.findViewById(R.id.money);
            deduction = (TextView) itemView.findViewById(R.id.deduction);
        }

        void setData(CandyRecordBean bean, int position, Object payload) {
            mTime.setText(bean.time);
            mName.setText(bean.title);
            mMoney.setText(bean.get_candy);
            deduction.setText(TextUtils.isEmpty(bean.deduction)?"0":bean.deduction);

        }
    }

    public interface ActionListener {
        void onItemRemoved(String toUid, int position);
    }
}
