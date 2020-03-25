package com.qianhong.sflive.activity;

import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.qianhong.sflive.R;
import com.qianhong.sflive.adapter.MyRankAdapter;
import com.qianhong.sflive.bean.UserBean;
import com.qianhong.sflive.custom.RefreshAdapter;
import com.qianhong.sflive.custom.RefreshView;
import com.qianhong.sflive.custom.ViewPagerIndicator;
import com.qianhong.sflive.http.HttpCallback;
import com.qianhong.sflive.http.HttpUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZSFRankActivity extends AbsActivity {

    private ViewPagerIndicator mIndicator;
    private ViewPager mViewPager;
    private List<View> mViewList;
    private RefreshView mMineRefreshView;
    private RefreshView mMissionRefreshView;
    private RefreshView mHistoryRefreshView;
    private MyRankAdapter mMineAdapter;
    private MyRankAdapter mMissionAdapter;
    private MyRankAdapter mHistoryAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rank;
    }

    @Override
    protected void main() {
        setTitle("排行榜");
        mMineAdapter = new MyRankAdapter(mContext, MyRankAdapter.TYPE_ACTIVE);
        mMissionAdapter = new MyRankAdapter(mContext, MyRankAdapter.TYPE_LX);
        mHistoryAdapter = new MyRankAdapter(mContext, MyRankAdapter.TYPE_CONTRIBUTE);
        mIndicator = findViewById(R.id.indicator);
        mIndicator.setVisibleChildCount(3);
        mIndicator.setTitles(new String[]{"活跃度", "LX值", "贡献值"});
        mIndicator.setChangeSize(false);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(3);
        mIndicator.setViewPager(mViewPager);
        mViewList = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mMineRefreshView = (RefreshView) inflater.inflate(R.layout.item_mission_page, mViewPager, false);
        mMineRefreshView.setNoDataLayoutId(R.layout.view_no_data_mission);
        mMineRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mMineRefreshView.setDataHelper(new RefreshView.DataHelper<UserBean>() {
            @Override
            public RefreshAdapter<UserBean> getAdapter() {
                return mMineAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getRankList(p, "ac", callback);
            }

            @Override
            public List<UserBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), UserBean.class);
            }

            @Override
            public void onRefresh(List<UserBean> list) {

            }

            @Override
            public void onNoData(boolean noData) {

            }

            @Override
            public void onLoadDataCompleted(int dataCount) {
                if (dataCount < 20) {
                    mMineRefreshView.setLoadMoreEnable(false);
                } else {
                    mMineRefreshView.setLoadMoreEnable(true);
                }
            }
        });
        mMissionRefreshView = (RefreshView) inflater.inflate(R.layout.item_mission_page, mViewPager, false);
        mMissionRefreshView.setNoDataLayoutId(R.layout.view_no_data_mission);
        mMissionRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mMissionRefreshView.setDataHelper(new RefreshView.DataHelper<UserBean>() {
            @Override
            public RefreshAdapter<UserBean> getAdapter() {
                return mMissionAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getRankList(p, "lx", callback);
            }

            @Override
            public List<UserBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), UserBean.class);
            }

            @Override
            public void onRefresh(List<UserBean> list) {

            }

            @Override
            public void onNoData(boolean noData) {

            }

            @Override
            public void onLoadDataCompleted(int dataCount) {
                if (dataCount < 20) {
                    mMissionRefreshView.setLoadMoreEnable(false);
                } else {
                    mMissionRefreshView.setLoadMoreEnable(true);
                }
            }
        });
        mHistoryRefreshView = (RefreshView) inflater.inflate(R.layout.item_mission_page, mViewPager, false);
        mHistoryRefreshView.setNoDataLayoutId(R.layout.view_no_data_mission);
        mHistoryRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mHistoryRefreshView.setDataHelper(new RefreshView.DataHelper<UserBean>() {
            @Override
            public RefreshAdapter<UserBean> getAdapter() {
                return mHistoryAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getRankList(p, "gong", callback);
            }

            @Override
            public List<UserBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), UserBean.class);
            }

            @Override
            public void onRefresh(List<UserBean> list) {

            }

            @Override
            public void onNoData(boolean noData) {

            }

            @Override
            public void onLoadDataCompleted(int dataCount) {
                if (dataCount < 20) {
                    mHistoryRefreshView.setLoadMoreEnable(false);
                } else {
                    mHistoryRefreshView.setLoadMoreEnable(true);
                }
            }
        });
//        ItemDecoration decoration = new ItemDecoration();
//        mMineRefreshView.getRecyclerView().addItemDecoration(decoration);
//        mMissionRefreshView.getRecyclerView().addItemDecoration(decoration);
//        mHistoryRefreshView.getRecyclerView().addItemDecoration(decoration);
        mViewList.add(mMineRefreshView);
        mViewList.add(mMissionRefreshView);
        mViewList.add(mHistoryRefreshView);
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mViewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View v = mViewList.get(position);
                container.addView(v);
                return v;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mViewList.get(position));
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((RefreshView) mViewPager.getChildAt(position)).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mMineRefreshView.initData();
    }


    class ItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = 15;
        }
    }

    @Override
    protected void onDestroy() {

        if (mMineRefreshView != null) {
            mMineRefreshView.setDataHelper(null);
        }
        if (mMissionRefreshView != null) {
            mMissionRefreshView.setDataHelper(null);
        }
        if (mHistoryRefreshView != null) {
            mHistoryRefreshView.setDataHelper(null);
        }
        if (mViewPager != null) {
            mViewPager.clearOnPageChangeListeners();
        }
        super.onDestroy();
    }

    boolean mPaused;

    @Override
    protected void onPause() {
        super.onPause();
        mPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPaused = false;
    }
}
