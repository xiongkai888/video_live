package com.video.liveshow.fragment;

import android.support.v7.widget.GridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.video.liveshow.Constants;
import com.video.liveshow.R;
import com.video.liveshow.activity.VideoPlayActivity;
import com.video.liveshow.adapter.FollowVideoAdapter;
import com.video.liveshow.bean.VideoBean;
import com.video.liveshow.custom.ItemDecoration;
import com.video.liveshow.custom.RefreshAdapter;
import com.video.liveshow.custom.RefreshView;
import com.video.liveshow.event.FollowEvent;
import com.video.liveshow.http.HttpCallback;
import com.video.liveshow.http.HttpUtil;
import com.video.liveshow.interfaces.OnItemClickListener;
import com.video.liveshow.utils.VideoStorge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cxf on 2018/6/5.
 */

public class FollowFragment extends AbsFragment implements OnItemClickListener<VideoBean> {

    private RefreshView mRefreshView;
    private FollowVideoAdapter mFollowAdapter;
    private boolean mPaused;
    private boolean mNeedRefresh;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_follow;
    }

    @Override
    protected void main() {
        mRefreshView = (RefreshView) mRootView.findViewById(R.id.refreshView);
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_follow_3);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<VideoBean>() {

            @Override
            public RefreshAdapter<VideoBean> getAdapter() {
                if (mFollowAdapter == null) {
                    mFollowAdapter = new FollowVideoAdapter(mContext);
                    mFollowAdapter.setOnItemClickListener(FollowFragment.this);
                }
                return mFollowAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getAttentionVideo(p, callback);
            }

            @Override
            public List<VideoBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), VideoBean.class);
            }

            @Override
            public void onRefresh(List<VideoBean> list) {
                VideoStorge.getInstance().put(Constants.VIDEO_FOLLOW, list);
            }
            @Override
            public void onNoData(boolean noData) {

            }

            @Override
            public void onLoadDataCompleted(int dataCount) {

            }

        });
        mRefreshView.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 2, 2);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRefreshView.setItemDecoration(decoration);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onItemClick(VideoBean bean, int position) {
        if (mRefreshView != null && bean != null && bean.getUserinfo() != null) {
            VideoPlayActivity.forwardVideoPlay(mContext, Constants.VIDEO_FOLLOW, position, mRefreshView.getPage(), bean.getUserinfo(), bean.getIsattent());
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mRefreshView.initData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mPaused = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPaused) {
            mPaused = false;
            if (mNeedRefresh) {
                mRefreshView.initData();
            }
        }
        mNeedRefresh = false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFollowEvent(FollowEvent e) {
        mNeedRefresh = true;
    }

    @Override
    public void onDestroy() {
        HttpUtil.cancel(HttpUtil.GET_ATTENTION_VIDEO);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
