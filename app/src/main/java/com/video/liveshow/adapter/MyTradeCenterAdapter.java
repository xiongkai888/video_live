package com.video.liveshow.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.video.liveshow.R;
import com.video.liveshow.bean.TradeCenterBean;
import com.video.liveshow.custom.RefreshAdapter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by cxf on 2018/7/30.
 */

public class MyTradeCenterAdapter extends RefreshAdapter<TradeCenterBean.TradeInfo> {

    private View.OnClickListener mOnClickListener;

    public MyTradeCenterAdapter(Context context) {
        super(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = (int) v.getTag();
                    mOnItemClickListener.onItemClick(mList.get(position), position);
                }
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_my_trade_center, parent, false));
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

        ImageView mAvatar;
        TextView mName, mTips, mPirce, mNumber, mSaleView;

        public Vh(View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.avatar);
            mName = itemView.findViewById(R.id.name);
            mTips = itemView.findViewById(R.id.tips);
            mPirce = itemView.findViewById(R.id.price);
            mNumber = itemView.findViewById(R.id.number);
            mSaleView = itemView.findViewById(R.id.sale);
            mSaleView.setOnClickListener(mOnClickListener);
        }

        void setData(TradeCenterBean.TradeInfo bean, int position, Object payload) {
//            if (payload == null) {
//                ImgLoader.display(bean.avatar_thumb, mAvatar);
//            }
//            mName.setText(bean.user_nicename);
            mPirce.setText("单价： ￥"+bean.price);
            mNumber.setText("数量："+bean.number+"硕果");
            mSaleView.setTag(position);
        }
    }

}
