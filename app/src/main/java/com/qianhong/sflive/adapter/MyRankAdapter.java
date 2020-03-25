package com.qianhong.sflive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qianhong.sflive.R;
import com.qianhong.sflive.bean.UserBean;
import com.qianhong.sflive.custom.RefreshAdapter;
import com.qianhong.sflive.glide.ImgLoader;

import java.util.List;

/**
 * Created by cxf on 2018/7/30.
 */

public class MyRankAdapter extends RefreshAdapter<UserBean> {

    public static final int TYPE_ACTIVE = 1;
    public static final int TYPE_LX = 2;
    public static final int TYPE_CONTRIBUTE = 3;

    private int mType;

    private View.OnClickListener mOnClickListener;
    private ActionListener mActionListener;

    public MyRankAdapter(Context context, int type) {
        super(context);
        mType = type;
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionListener != null) {
                    int position = (int) v.getTag();
                    mActionListener.onItemRemoved(mList.get(position).getId(), position);
                }
            }
        };
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public void removeItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size(), "payload");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_my_rank, parent, false));
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

        ImageView mAvatar, mPositionBg;
        TextView mPosition, mName, mNumber, mRank, mRankLabel;

        public Vh(View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.avatar);
            mPositionBg = itemView.findViewById(R.id.positionbg);
            mPosition = itemView.findViewById(R.id.position);
            mName = itemView.findViewById(R.id.name);
            mNumber = itemView.findViewById(R.id.number);
            mRank = itemView.findViewById(R.id.rank);
            mRankLabel = itemView.findViewById(R.id.rank_label);
        }

        void setData(UserBean bean, int position, Object payload) {
            int p = position + 1;
            if (p < 10)
                mPosition.setText(String.format("%02d", p));
            else
                mPosition.setText(String.valueOf(p));
            if (position < 3) {
                mPositionBg.setVisibility(View.VISIBLE);
            } else {
                mPositionBg.setVisibility(View.GONE);
            }
            switch (mType) {
                case TYPE_ACTIVE:
                    mRankLabel.setText("活跃度");
                    break;
                case TYPE_LX:
                    mRankLabel.setText("LX值");
                    break;
                case TYPE_CONTRIBUTE:
                    mRankLabel.setText("贡献值");

                    break;
            }
            ImgLoader.display(bean.getAvatar(), mAvatar);
            mName.setText(bean.user_login);
            mNumber.setText(bean.getId());
            mRank.setText(bean.activation);
        }
    }

    public interface ActionListener {
        void onItemRemoved(String toUid, int position);
    }
}
