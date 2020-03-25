package com.video.liveshow.activity;

import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.video.liveshow.R;
import com.video.liveshow.adapter.MyLevelAdapter;
import com.video.liveshow.bean.LevelBean;
import com.video.liveshow.custom.RefreshAdapter;
import com.video.liveshow.custom.RefreshView;
import com.video.liveshow.http.HttpCallback;
import com.video.liveshow.http.HttpUtil;

import java.util.Arrays;
import java.util.List;

public class ZSFMyLevel extends AbsActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_level;
    }

    private RefreshView mRefreshView;
    private MyLevelAdapter mAdapter;

    @Override
    protected void main() {
        setTitle("我的等级");
        findViewById(R.id.titlegroup).setBackgroundColor(getResources().getColor(R.color.sf_item_bg));
        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_default);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<LevelBean>() {
            @Override
            public RefreshAdapter<LevelBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new MyLevelAdapter(mContext);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getLevelList(p, callback);
            }

            @Override
            public List<LevelBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), LevelBean.class);
            }

            @Override
            public void onRefresh(List<LevelBean> list) {

            }

            @Override
            public void onNoData(boolean noData) {

            }

            @Override
            public void onLoadDataCompleted(int dataCount) {
                if (dataCount < 20) {
                    mRefreshView.setLoadMoreEnable(false);
                } else {
                    mRefreshView.setLoadMoreEnable(true);
                }
            }
        });
        mRefreshView.initData();
    }

    @Override
    protected void onDestroy() {
        HttpUtil.cancel(HttpUtil.GET_BLACK_LIST);
        mRefreshView.setDataHelper(null);
        super.onDestroy();
    }

}
