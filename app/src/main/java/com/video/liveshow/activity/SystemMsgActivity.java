package com.video.liveshow.activity;

import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.video.liveshow.Constants;
import com.video.liveshow.R;
import com.video.liveshow.adapter.SystemMsgAdapter;
import com.video.liveshow.bean.SystemMsgBean;
import com.video.liveshow.custom.RefreshAdapter;
import com.video.liveshow.custom.RefreshView;
import com.video.liveshow.http.HttpCallback;
import com.video.liveshow.http.HttpUtil;
import com.video.liveshow.interfaces.OnItemClickListener;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cxf on 2018/7/27.
 */

public class SystemMsgActivity extends AbsActivity implements OnItemClickListener<SystemMsgBean> {

    private RefreshView mRefreshView;
    private SystemMsgAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_admin_msg;
    }

    @Override
    protected void main() {
        setTitle(Constants.YB_NAME_2);
        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_default);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new RefreshView.DataHelper<SystemMsgBean>() {
            @Override
            public RefreshAdapter<SystemMsgBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new SystemMsgAdapter(mContext);
                    mAdapter.setOnItemClickListener(SystemMsgActivity.this);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getSystemMsgList(p, callback);
            }

            @Override
            public List<SystemMsgBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), SystemMsgBean.class);
            }

            @Override
            public void onRefresh(List<SystemMsgBean> list) {

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
        HttpUtil.cancel(HttpUtil.GET_SYSTEM_MSG_LIST);
        super.onDestroy();
    }

    @Override
    public void onItemClick(SystemMsgBean bean, int position) {
    }
}
