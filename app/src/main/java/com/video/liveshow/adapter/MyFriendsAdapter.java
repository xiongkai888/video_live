package com.video.liveshow.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.video.liveshow.R;
import com.video.liveshow.bean.FriendsBean;
import com.video.liveshow.custom.RefreshAdapter;
import com.video.liveshow.glide.ImgLoader;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by cxf on 2018/7/30.
 */

public class MyFriendsAdapter extends RefreshAdapter<FriendsBean.Team> {


    public MyFriendsAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_friends, parent, false));
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
        TextView mActive;
        TextView mTeam;
        TextView mTeamActive;
        TextView mPhone;

        public Vh(View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.avatar);
            mActive = (TextView) itemView.findViewById(R.id.active);
            mTeam = (TextView) itemView.findViewById(R.id.team);
            mTeamActive = (TextView) itemView.findViewById(R.id.team_active);
            mPhone = itemView.findViewById(R.id.phone);
        }

        void setData(FriendsBean.Team bean, int position, Object payload) {
            ImgLoader.display(bean.avatar, mAvatar);
            mActive.setText(bean.activation);
            mTeam.setText(bean.team_num);
            mTeamActive.setText(bean.team_activation);
            mPhone.setText("手机号码：" + bean.mobile);
        }
    }
}
