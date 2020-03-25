package com.qianhong.sflive.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qianhong.sflive.R;
import com.qianhong.sflive.adapter.MyMoneyAdapter;
import com.qianhong.sflive.bean.CandyRecordBean;
import com.qianhong.sflive.custom.RefreshAdapter;
import com.qianhong.sflive.custom.RefreshView;
import com.qianhong.sflive.http.HttpCallback;
import com.qianhong.sflive.http.HttpUtil;

import java.util.List;

public class ZSFMyMoney extends AbsActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_money;
    }

    private RefreshView mRefreshView;
    private MyMoneyAdapter mAdapter;

    private TextView mCandy;

    @Override
    protected void main() {
        setTitle("我的资产", getResources().getColor(R.color.sf_money_color));
        findViewById(R.id.titlegroup).setBackgroundColor(getResources().getColor(R.color.sf_item_bg));
        mCandy = findViewById(R.id.candy);
        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_default);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<CandyRecordBean>() {
            @Override
            public RefreshAdapter<CandyRecordBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new MyMoneyAdapter(mContext);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getMyMoney(p, callback);
            }

            @Override
            public List<CandyRecordBean> processData(String[] info) {
                if(info.length>0){
                    JSONObject obj = JSON.parseObject(info[0]);
                    mCandy.setText(obj.getString("candy"));
                    return JSON.parseArray(obj.getString("data"), CandyRecordBean.class);
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
