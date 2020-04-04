package com.video.liveshow.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.video.liveshow.R;
import com.video.liveshow.bean.UserBean;
import com.video.liveshow.bean.VideoBean;
import com.video.liveshow.custom.RefreshAdapter;
import com.video.liveshow.glide.ImgLoader;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by cxf on 2018/6/7.
 */

public class FollowVideoAdapter extends RefreshAdapter<VideoBean> {

    public FollowVideoAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_list_follow, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        ((Vh) vh).setData(mList.get(position), position);
    }


    class Vh extends RecyclerView.ViewHolder {

        ImageView mThumb;
        ImageView mAvatar;
        TextView mName;
        TextView mTitle;
        VideoBean mBean;
        int mPosition;


        public Vh(View itemView) {
            super(itemView);
            mThumb = (ImageView) itemView.findViewById(R.id.thumb);
            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            mName = (TextView) itemView.findViewById(R.id.name);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mBean, mPosition);
                    }
                }
            });
        }

        void setData(VideoBean bean, int position) {
            mBean = bean;
            mPosition = position;
            ImgLoader.display(bean.getThumb(), mThumb);
            UserBean u = bean.getUserinfo();
            ImgLoader.display(u.getAvatar(), mAvatar);
            mName.setText(u.getUser_nicename());
            mTitle.setText(bean.getTitle());
        }
    }
}
