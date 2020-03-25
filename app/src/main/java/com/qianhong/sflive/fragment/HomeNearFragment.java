package com.qianhong.sflive.fragment;

import android.support.v7.widget.GridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.qianhong.sflive.Constants;
import com.qianhong.sflive.R;
import com.qianhong.sflive.activity.VideoPlayActivity;
import com.qianhong.sflive.adapter.NearVideoAdapter;
import com.qianhong.sflive.bean.VideoBean;
import com.qianhong.sflive.custom.ItemDecoration;
import com.qianhong.sflive.custom.RefreshAdapter;
import com.qianhong.sflive.custom.RefreshView;
import com.qianhong.sflive.http.HttpCallback;
import com.qianhong.sflive.http.HttpUtil;
import com.qianhong.sflive.interfaces.OnItemClickListener;
import com.qianhong.sflive.utils.VideoStorge;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cxf on 2018/6/9.
 */

public class HomeNearFragment extends AbsFragment implements OnItemClickListener<VideoBean> {

    private RefreshView mRefreshView;
    private NearVideoAdapter mNearAdapter;
    private boolean mFirst = true;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_hot;
    }

    @Override
    protected void main() {
        mRefreshView = (RefreshView) mRootView.findViewById(R.id.refreshView);
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_default);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<VideoBean>() {

            @Override
            public RefreshAdapter<VideoBean> getAdapter() {
                if (mNearAdapter == null) {
                    mNearAdapter = new NearVideoAdapter(mContext);
                    mNearAdapter.setOnItemClickListener(HomeNearFragment.this);
                }
                return mNearAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getNearbyVideos(p, callback);
            }

            @Override
            public List<VideoBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), VideoBean.class);
            }

            @Override
            public void onRefresh(List<VideoBean> list) {
                VideoStorge.getInstance().put(Constants.VIDEO_NEAR, list);
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
    }

    public void init(){
        mRefreshView.initData(true);
    }

    @Override
    public void onItemClick(VideoBean bean, int position) {
        if (mRefreshView != null && bean != null && bean.getUserinfo() != null) {
            VideoPlayActivity.forwardVideoPlay(mContext, Constants.VIDEO_NEAR, position, mRefreshView.getPage(), bean.getUserinfo(), bean.getIsattent());
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (mFirst) {
                mFirst = false;
                mRefreshView.initData();
            }
        }
    }

    @Override
    public void onDestroyView() {
        HttpUtil.cancel(HttpUtil.GET_VIDEO_LIST);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (mRefreshView != null) {
            mRefreshView.setDataHelper(null);
        }
        super.onDestroy();
    }
}
