package com.video.liveshow.activity;

import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.video.liveshow.R;
import com.video.liveshow.adapter.MyFriendsAdapter;
import com.video.liveshow.bean.FriendsBean;
import com.video.liveshow.custom.RefreshAdapter;
import com.video.liveshow.custom.RefreshView;
import com.video.liveshow.http.HttpCallback;
import com.video.liveshow.http.HttpUtil;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

public class ZSFMyFriends extends AbsActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_friends;
    }

    private RefreshView mRefreshView;
    private MyFriendsAdapter mAdapter;

    TextView mZhi, mShi, mWeiShi, mXiao, mDa, mZong,superiorTv;

    @Override
    protected void main() {
        setTitle("我的好友");
        findViewById(R.id.titlegroup).setBackgroundColor(getResources().getColor(R.color.sf_item_bg));
        mZhi = findViewById(R.id.zhi);
        mShi = findViewById(R.id.shi);
        mWeiShi = findViewById(R.id.weishi);//团队总人数
        mXiao = findViewById(R.id.xiao);
        mDa = findViewById(R.id.da);
        mZong = findViewById(R.id.zong);
        superiorTv = findViewById(R.id.superior_tv);//我的上级
        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_default);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<FriendsBean.Team>() {
            @Override
            public RefreshAdapter<FriendsBean.Team> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new MyFriendsAdapter(mContext);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getMyFriends(p, callback);
            }

            @Override
            public List<FriendsBean.Team> processData(String[] info) {
                if (info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    FriendsBean bean = JSON.toJavaObject(obj, FriendsBean.class);
                    mDa.setText(bean.big_activation);
                    mXiao.setText(bean.small_activation);
                    mZhi.setText(bean.current_team_num);//直推人数
                    mShi.setText(bean.lower_real_num);//已实名人数
                    mWeiShi.setText(bean.lower_num);//团队总人数
                    mZong.setText(bean.teams_real_num);//团队总实名人数
                    superiorTv.setText("我的上级："+bean.higher_account);
                    return bean.lower_level;
                }
                return null;

            }

            @Override
            public void onRefresh(List<FriendsBean.Team> list) {

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
