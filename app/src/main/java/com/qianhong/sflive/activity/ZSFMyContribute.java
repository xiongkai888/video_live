package com.qianhong.sflive.activity;

import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.qianhong.sflive.R;
import com.qianhong.sflive.adapter.MyContributeAdapter;
import com.qianhong.sflive.bean.ContributeBean;
import com.qianhong.sflive.custom.RefreshAdapter;
import com.qianhong.sflive.custom.RefreshView;
import com.qianhong.sflive.http.HttpCallback;
import com.qianhong.sflive.http.HttpUtil;

import java.util.Arrays;
import java.util.List;

public class ZSFMyContribute extends AbsActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_contribute;
    }

    private RefreshView mRefreshView;
    private MyContributeAdapter mAdapter;

    @Override
    protected void main() {
        setTitle("活跃度");
        findViewById(R.id.titlegroup).setBackgroundColor(getResources().getColor(R.color.sf_item_bg));
        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_default);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<ContributeBean>() {
            @Override
            public RefreshAdapter<ContributeBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new MyContributeAdapter(mContext);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getContributeList(p, callback);
            }

            @Override
            public List<ContributeBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), ContributeBean.class);
            }

            @Override
            public void onRefresh(List<ContributeBean> list) {

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
