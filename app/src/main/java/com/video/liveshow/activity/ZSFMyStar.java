package com.video.liveshow.activity;

import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.video.liveshow.R;
import com.video.liveshow.adapter.MyStarAdapter;
import com.video.liveshow.bean.StarBean;
import com.video.liveshow.custom.RefreshAdapter;
import com.video.liveshow.custom.RefreshView;
import com.video.liveshow.http.HttpCallback;
import com.video.liveshow.http.HttpUtil;

import java.util.Arrays;
import java.util.List;

public class ZSFMyStar extends AbsActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_star;
    }

    private RefreshView mRefreshView;
    private MyStarAdapter mAdapter;

    @Override
    protected void main() {
        setTitle("达人介绍");
        findViewById(R.id.titlegroup).setBackgroundColor(getResources().getColor(R.color.sf_item_bg));
        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_default);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<StarBean>() {
            @Override
            public RefreshAdapter<StarBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new MyStarAdapter(mContext);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getStarList(p, callback);
            }

            @Override
            public List<StarBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), StarBean.class);
            }

            @Override
            public void onRefresh(List<StarBean> list) {

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
