package com.video.liveshow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.video.liveshow.R;
import com.video.liveshow.bean.TradeMissionBean;
import com.video.liveshow.custom.RefreshAdapter;

import java.util.List;

/**
 * Created by cxf on 2018/7/30.
 */

public class MyMissionAdapter extends RefreshAdapter<TradeMissionBean> {

    public static final int TYPE_MINE = 1;
    public static final int TYPE_ALL = 2;
    public static final int TYPE_HISTORY = 3;

    private int mType;

    private View.OnClickListener mOnClickListener;

    public MyMissionAdapter(Context context, int type) {
        super(context);
        mType = type;
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (Integer) v.getTag();
                mOnItemClickListener.onItemClick(mList.get(pos), pos);
            }
        };
    }

    public void removeItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size(), "payload");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_my_mission, parent, false));
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

        TextView mName, mCostandCount, mEarn, mTask, mTaskTime, mTaskContribute, mTime;
        TextView mActive, mOver;
        View mHotGroup;

        public Vh(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mCostandCount = (TextView) itemView.findViewById(R.id.costandcount);
            mEarn = (TextView) itemView.findViewById(R.id.earn);
            mTask = (TextView) itemView.findViewById(R.id.task);
            mTaskTime = (TextView) itemView.findViewById(R.id.task_time);
            mTaskContribute = (TextView) itemView.findViewById(R.id.task_contribute);
            mTime = (TextView) itemView.findViewById(R.id.time);

            mHotGroup = itemView.findViewById(R.id.hotgroup);
            mActive = itemView.findViewById(R.id.active);
            mOver = itemView.findViewById(R.id.over);
            mOver.setOnClickListener(mOnClickListener);
        }

        void setData(TradeMissionBean bean, int position, Object payload) {
            switch (mType) {
                case TYPE_ALL:
                    mHotGroup.setVisibility(View.GONE);
                    mOver.setVisibility(View.VISIBLE);
                    mOver.setTag(position);
                    mTaskTime.setVisibility(View.VISIBLE);
                    mTaskContribute.setVisibility(View.VISIBLE);
                    mTime.setVisibility(View.GONE);
                    break;
                case TYPE_MINE:
                    mHotGroup.setVisibility(View.VISIBLE);
                    mOver.setVisibility(View.GONE);
                    mTaskTime.setVisibility(View.GONE);
                    mTaskContribute.setVisibility(View.GONE);
                    mTime.setVisibility(View.VISIBLE);
                    break;
                case TYPE_HISTORY:
                    mHotGroup.setVisibility(View.VISIBLE);
                    mOver.setVisibility(View.GONE);
                    mTaskTime.setVisibility(View.GONE);
                    mTaskContribute.setVisibility(View.GONE);
                    mTime.setVisibility(View.GONE);
                    break;


            }
            mName.setText(bean.name);
            mCostandCount.setText("兑换所需：" + bean.candyNumber + "（任务数量" + bean.receive_num + "/" + bean.total + "）");
            mEarn.setText("总收益 ：" + bean.theory_candy);
            mTask.setText("所需任务：" + bean.need_task);
            mTaskTime.setText("任务时间：" + bean.expire_time);
            mTaskContribute.setText("活跃度："+bean.activation);
            mTime.setText("倒计时："+bean.expire_time);
            mActive.setText(bean.activation);
        }
    }

    public interface ActionListener {
        void onItemRemoved(String toUid, int position);
    }
}
