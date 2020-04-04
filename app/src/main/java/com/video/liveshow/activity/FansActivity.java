package com.video.liveshow.activity;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.video.liveshow.AppConfig;
import com.video.liveshow.Constants;
import com.video.liveshow.R;
import com.video.liveshow.adapter.SearchAdapter;
import com.video.liveshow.bean.SearchBean;
import com.video.liveshow.custom.RefreshAdapter;
import com.video.liveshow.custom.RefreshView;
import com.video.liveshow.http.HttpCallback;
import com.video.liveshow.http.HttpUtil;
import com.video.liveshow.interfaces.OnItemClickListener;
import com.video.liveshow.utils.WordUtil;

import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * Created by cxf on 2017/8/10.
 * 我的 粉丝
 */

public class FansActivity extends AbsActivity {

    private SearchAdapter mAdapter;
    private RefreshView mRefreshView;
    private String mToUid;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fans;
    }

    @Override
    protected void main() {
        mToUid = getIntent().getStringExtra(Constants.TO_UID);
        if (TextUtils.isEmpty(mToUid)) {
            return;
        }
        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
        if (mToUid.equals(AppConfig.getInstance().getUid())) {
            setTitle(WordUtil.getString(R.string.my_fans));
            mRefreshView.setNoDataLayoutId(R.layout.view_no_data_fans);
        } else {
            setTitle(WordUtil.getString(R.string.my_fans_2));
            mRefreshView.setNoDataLayoutId(R.layout.view_no_data_fans_2);
        }
        mRefreshView.setDataHelper(new RefreshView.DataHelper<SearchBean>() {
            @Override
            public RefreshAdapter<SearchBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new SearchAdapter(mContext);
                    mAdapter.setOnItemClickListener(new OnItemClickListener<SearchBean>() {
                        @Override
                        public void onItemClick(SearchBean bean, int position) {
                            OtherUserActivity.forwardOtherUser(mContext, bean.getId());
                        }
                    });
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getFansList(mToUid, p, callback);
            }

            @Override
            public List<SearchBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), SearchBean.class);
            }

            @Override
            public void onRefresh(List<SearchBean> list) {

            }

            @Override
            public void onNoData(boolean noData) {

            }

            @Override
            public void onLoadDataCompleted(int dataCount) {

            }
        });
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRefreshView.initData();
    }

    @Override
    protected void onDestroy() {
        HttpUtil.cancel(HttpUtil.GET_FANS_LIST);
        super.onDestroy();
    }
}
