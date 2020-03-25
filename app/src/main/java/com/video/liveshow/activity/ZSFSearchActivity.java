package com.video.liveshow.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.video.liveshow.R;
import com.video.liveshow.adapter.MyTradeCenterAdapter;
import com.video.liveshow.bean.TradeCenterBean;
import com.video.liveshow.custom.RefreshAdapter;
import com.video.liveshow.custom.RefreshView;
import com.video.liveshow.http.HttpCallback;
import com.video.liveshow.http.HttpUtil;
import com.video.liveshow.interfaces.OnItemClickListener;

import java.util.List;

public class ZSFSearchActivity extends AbsActivity {

    private EditText mSearchEdit;
    private TextView mSendView;
    private RefreshView mRefreshView;
    private MyTradeCenterAdapter mAdapter;
    private String mTag;
    private String mKey;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sf_search;
    }

    @Override
    protected void main() {
        mTag = this.toString();
        setTitle("搜索");
        findViewById(R.id.titlegroup).setBackgroundColor(getResources().getColor(R.color.sf_item_bg));
        mSearchEdit = findViewById(R.id.et_search);
        findViewById(R.id.tv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_default);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<TradeCenterBean.TradeInfo>() {
            @Override
            public RefreshAdapter<TradeCenterBean.TradeInfo> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new MyTradeCenterAdapter(mContext);
                    mAdapter.setOnItemClickListener(new OnItemClickListener<TradeCenterBean.TradeInfo>() {
                        @Override
                        public void onItemClick(TradeCenterBean.TradeInfo bean, int position) {
                            Intent intent = new Intent();
                            intent.putExtra("bean", bean);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    });
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getTradeList(mTag, mKey, p, 4, callback);
            }

            @Override
            public List<TradeCenterBean.TradeInfo> processData(String[] info) {
                if(info.length>0){
                    JSONObject obj = JSON.parseObject(info[0]);
                    TradeCenterBean bean = JSON.toJavaObject(obj, TradeCenterBean.class);
                    return bean.list;
                }
                return null;

            }

            @Override
            public void onRefresh(List<TradeCenterBean.TradeInfo> list) {

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
    }

    private void search() {
        String key = mSearchEdit.getText().toString();
        if (TextUtils.isEmpty(key))
            return;
        mKey = key;
        mRefreshView.initData();
    }
}
