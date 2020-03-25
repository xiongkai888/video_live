package com.qianhong.sflive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.qianhong.sflive.R;
import com.qianhong.sflive.bean.TradeCenterBean;
import com.qianhong.sflive.custom.RefreshAdapter;
import com.qianhong.sflive.glide.ImgLoader;

import java.util.List;

/**
 * Created by cxf on 2018/7/30.
 */

public class MyTradeRecordAdapter extends RefreshAdapter<TradeCenterBean.TradeInfo> {

    private View.OnClickListener mOnClickListener;
    private int mType = -1;
    private View.OnClickListener mOnCancelListener;

    public MyTradeRecordAdapter(Context context, int type) {
        super(context);
        mType = type;
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object object = v.getTag();
                if (object instanceof Integer) {
                    if (mOnItemClickListener != null) {
                        int position = (int) v.getTag();
                        mOnItemClickListener.onItemClick(mList.get(position), position);
                    }
                } else if (object instanceof TradeCenterBean.TradeInfo) {
                    if (mOnCancelListener != null) {
                        mOnCancelListener.onClick(v);
                    }
                }

            }
        };
    }

    public void setOnCancelListener(View.OnClickListener cancelListener) {
        mOnCancelListener = cancelListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_my_trade_record, parent, false));
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

        View mItem;
        TextView mName, mPrice, mCount, mAction;
        RoundedImageView avatar;
//        TextView mSaleView;

        public Vh(View itemView) {
            super(itemView);
            mItem = itemView;
            mName = itemView.findViewById(R.id.name);
            mPrice = itemView.findViewById(R.id.money);
            mCount = itemView.findViewById(R.id.number);
            mAction = itemView.findViewById(R.id.action);
            avatar = itemView.findViewById(R.id.avatar);
            mItem.setOnClickListener(mOnClickListener);
            mAction.setOnClickListener(mOnClickListener);

        }

        void setData(TradeCenterBean.TradeInfo bean, int position, Object payload) {
            mItem.setTag(position);
            mAction.setTag(bean);
            mName.setText(bean.user_nicename);
            mPrice.setText("单价：￥" + bean.price);
            mCount.setText("数量：" + bean.number + "个硕果");
            ImgLoader.display(bean.avatar_thumb, avatar);
            if (mType == MyMissionAdapter.TYPE_MINE) {
                mAction.setText("撤销");
            } else {
                mAction.setText(bean.type == 0 ? "买入" : "卖出");
            }
        }
    }
}
