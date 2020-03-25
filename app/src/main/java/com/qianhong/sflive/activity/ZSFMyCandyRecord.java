package com.qianhong.sflive.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qianhong.sflive.R;
import com.qianhong.sflive.adapter.MyCandyRecordAdapter;
import com.qianhong.sflive.bean.CandyRecordBean;
import com.qianhong.sflive.bean.CandyRecordResponse;
import com.qianhong.sflive.custom.RefreshAdapter;
import com.qianhong.sflive.custom.RefreshView;
import com.qianhong.sflive.http.HttpCallback;
import com.qianhong.sflive.http.HttpUtil;

import java.util.List;

public class ZSFMyCandyRecord extends AbsActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_candy;
    }

    private RefreshView mRefreshView;
    private MyCandyRecordAdapter mAdapter;
    TextView mCandy;

    private int mType = 0;

    @Override
    protected void main() {
        mType = getIntent().getIntExtra("type", 0);
        if (mType == 0) {
            setTitle("日产硕果记录");
        } else {
            setTitle("总产硕果记录");
        }
        mCandy = findViewById(R.id.candy);
        findViewById(R.id.titlegroup).setBackgroundColor(getResources().getColor(R.color.sf_item_bg));
        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
        mRefreshView.setLoadMoreEnable(false);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_default);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<CandyRecordBean>() {
            @Override
            public RefreshAdapter<CandyRecordBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new MyCandyRecordAdapter(mContext);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getMyCandyList(p, callback);
            }

            @Override
            public List<CandyRecordBean> processData(String[] info) {
                if (info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    CandyRecordResponse bean = JSON.toJavaObject(obj, CandyRecordResponse.class);
                    if (mType == 0) {
                        mCandy.setText(bean.today);
                        return bean.task;
                    } else if (mType == 1) {
                        mCandy.setText(bean.candy);
                        return bean.total;
                    }
                }
                return null;

            }

            @Override
            public void onRefresh(List<CandyRecordBean> list) {

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
