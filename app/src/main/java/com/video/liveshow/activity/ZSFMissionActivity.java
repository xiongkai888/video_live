package com.video.liveshow.activity;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.video.liveshow.R;
import com.video.liveshow.adapter.MyMissionAdapter;
import com.video.liveshow.bean.TradeMissionBean;
import com.video.liveshow.custom.RefreshAdapter;
import com.video.liveshow.custom.RefreshView;
import com.video.liveshow.custom.ViewPagerIndicator;
import com.video.liveshow.http.HttpCallback;
import com.video.liveshow.http.HttpUtil;
import com.video.liveshow.interfaces.OnItemClickListener;
import com.video.liveshow.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ZSFMissionActivity extends AbsActivity {

    private ViewPagerIndicator mIndicator;
    private ViewPager mViewPager;
    private List<View> mViewList;
    private RefreshView mMineRefreshView;
    private RefreshView mMissionRefreshView;
    private RefreshView mHistoryRefreshView;
    private MyMissionAdapter mMineAdapter;
    private MyMissionAdapter mMissionAdapter;
    private MyMissionAdapter mHistoryAdapter;

    private String mTag;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mission;
    }

    @Override
    protected void main() {
        mTag = this.toString();
        setTitle("任务卷轴");
        mMineAdapter = new MyMissionAdapter(mContext, MyMissionAdapter.TYPE_MINE);
        mMissionAdapter = new MyMissionAdapter(mContext, MyMissionAdapter.TYPE_ALL);
        mMissionAdapter.setOnItemClickListener(new OnItemClickListener<TradeMissionBean>() {
            @Override
            public void onItemClick(TradeMissionBean bean, int position) {
                change(bean);
            }
        });
        mHistoryAdapter = new MyMissionAdapter(mContext, MyMissionAdapter.TYPE_HISTORY);
        mIndicator = findViewById(R.id.indicator);
        mIndicator.setVisibleChildCount(3);
        mIndicator.setTitles(new String[]{"任务卷轴", "我的任务", "历史任务"});
        mIndicator.setChangeSize(false);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(3);
        mIndicator.setViewPager(mViewPager);
        mViewList = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mMineRefreshView = (RefreshView) inflater.inflate(R.layout.item_mission_page, mViewPager, false);
        mMineRefreshView.setNoDataLayoutId(R.layout.view_no_data_mission);
        mMineRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mMineRefreshView.setDataHelper(new RefreshView.DataHelper<TradeMissionBean>() {
            @Override
            public RefreshAdapter<TradeMissionBean> getAdapter() {
                return mMineAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getTradeMissionMine(mTag, p, callback);
            }

            @Override
            public List<TradeMissionBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), TradeMissionBean.class);
            }

            @Override
            public void onRefresh(List<TradeMissionBean> list) {

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
        mMissionRefreshView.setDataHelper(new RefreshView.DataHelper<TradeMissionBean>() {
            @Override
            public RefreshAdapter<TradeMissionBean> getAdapter() {
                return mMissionAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getTradeMission(mTag, p, callback);
            }

            @Override
            public List<TradeMissionBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), TradeMissionBean.class);
            }

            @Override
            public void onRefresh(List<TradeMissionBean> list) {

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
        mHistoryRefreshView.setDataHelper(new RefreshView.DataHelper<TradeMissionBean>() {
            @Override
            public RefreshAdapter<TradeMissionBean> getAdapter() {
                return mHistoryAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getTradeMissionHistory(mTag, p, callback);
            }

            @Override
            public List<TradeMissionBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), TradeMissionBean.class);
            }

            @Override
            public void onRefresh(List<TradeMissionBean> list) {

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
        ItemDecoration decoration = new ItemDecoration();
        mMineRefreshView.getRecyclerView().addItemDecoration(decoration);
        mMissionRefreshView.getRecyclerView().addItemDecoration(decoration);
        mHistoryRefreshView.getRecyclerView().addItemDecoration(decoration);
        mViewList.add(mMissionRefreshView);
        mViewList.add(mMineRefreshView);
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
        mMissionRefreshView.initData();
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

        HttpUtil.cancel(mTag);
        super.onDestroy();
    }

    private void change(final TradeMissionBean bean) {
        HttpUtil.receiveTradeMissionTask(mTag, bean.id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {

                if (code == 0) {
//                    JSONObject obj = JSON.parseObject(mInfo[0]);
//                    bean.receive_num = obj.getInteger("num");
//                    mMissionAdapter.notifyDataSetChanged();
                    ToastUtil.show(msg);
                } else {
                    ToastUtil.show(msg);
                }


            }
        });

    }
}
