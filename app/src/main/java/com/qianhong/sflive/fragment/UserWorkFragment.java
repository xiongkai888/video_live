package com.qianhong.sflive.fragment;

import android.support.v7.widget.GridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.qianhong.sflive.Constants;
import com.qianhong.sflive.R;
import com.qianhong.sflive.activity.OtherUserActivity;
import com.qianhong.sflive.activity.VideoPlayActivity;
import com.qianhong.sflive.adapter.UserWorkAdapter;
import com.qianhong.sflive.bean.VideoBean;
import com.qianhong.sflive.custom.ItemDecoration;
import com.qianhong.sflive.custom.RefreshAdapter;
import com.qianhong.sflive.custom.RefreshView;
import com.qianhong.sflive.event.NeedRefreshWorkEvent;
import com.qianhong.sflive.event.VideoDeleteEvent;
import com.qianhong.sflive.http.HttpCallback;
import com.qianhong.sflive.http.HttpUtil;
import com.qianhong.sflive.interfaces.OnItemClickListener;
import com.qianhong.sflive.utils.VideoStorge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cxf on 2018/6/10.
 * 个人中心  作品
 */

public class UserWorkFragment extends UserItemFragment implements OnItemClickListener<VideoBean> {

    private RefreshView mRefreshView;
    private UserWorkAdapter mAdapter;
    private boolean mNeedRefresh;
    private boolean mPaused;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_video_work;
    }

    @Override
    protected void main() {
        super.main();
        mRefreshView = (RefreshView) mRootView.findViewById(R.id.refreshView);
        if (!mIsMainUserCenter) {
            mRefreshView.setNoDataLayoutId(R.layout.view_no_data_user_work);
        }
        mRefreshView.setDataHelper(new RefreshView.DataHelper<VideoBean>() {

            @Override
            public RefreshAdapter<VideoBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new UserWorkAdapter(mContext);
                    mAdapter.setOnItemClickListener(UserWorkFragment.this);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                if (!Constants.NOT_LOGIN_UID.equals(mUid)) {
                    HttpUtil.getHomeVideo(mUid, p, callback);
                }
            }

            @Override
            public List<VideoBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), VideoBean.class);
            }

            @Override
            public void onRefresh(List<VideoBean> list) {
                VideoStorge.getInstance().put(mHashCode, list);
            }

            @Override
            public void onNoData(boolean noData) {

            }

            @Override
            public void onLoadDataCompleted(int dataCount) {
                if (dataCount <= 0) {
                    mRefreshView.setLoadMoreEnable(false);
                } else {
                    mRefreshView.setLoadMoreEnable(true);
                }
                if (mIsMainUserCenter && dataCount > 0) {
                    ((UserFragment) getParentFragment()).onWorkCountChanged(dataCount);
                }
            }
        });
        mRefreshView.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 2, 2);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRefreshView.setItemDecoration(decoration);
        if (mIsMainUserCenter) {
            mRefreshView.initData();
            EventBus.getDefault().register(this);
        }
        if (mContext instanceof OtherUserActivity) {
            mRefreshView.initData();
        }
    }

    @Override
    public void onItemClick(VideoBean bean, int position) {
        if (mRefreshView != null && bean != null && bean.getUserinfo() != null) {
            VideoPlayActivity.forwardVideoPlay(mContext, mHashCode, position, mRefreshView.getPage(), bean.getUserinfo(), bean.getIsattent());
        }
    }


    @Override
    public void loadData() {
//        if (mFirst) {
//            mFirst = false;
//
//        }
        if (mRefreshView != null) {
            mRefreshView.initData();
        }
    }

    @Override
    public void onDestroy() {
        if (mIsMainUserCenter) {
            EventBus.getDefault().unregister(this);
        }
        HttpUtil.cancel(HttpUtil.GET_HOME_VIDEO);
        super.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNeedRefreshWorkEvent(NeedRefreshWorkEvent e) {
        if (mPaused) {
            mNeedRefresh = true;
        } else {
            if (mRefreshView != null) {
                mRefreshView.initData();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoDeleteEvent(VideoDeleteEvent e) {
        mNeedRefresh = true;
    }


    @Override
    public void onLoginUserChanged(String uid) {
        mUid = uid;
//        if (mRefreshView != null) {
//            mRefreshView.initData();
//        }
        mNeedRefresh = true;
    }

    @Override
    public void clearData() {
        if (mAdapter != null) {
            mAdapter.clearData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPaused) {
            mPaused = false;
            if (mNeedRefresh && mRefreshView != null) {
                mNeedRefresh = false;
                mRefreshView.initData();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mPaused = true;
    }

}
