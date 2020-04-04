package com.video.liveshow.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.video.liveshow.R;
import com.video.liveshow.adapter.MyMissionAdapter;
import com.video.liveshow.adapter.MyTradeRecordAdapter;
import com.video.liveshow.bean.TradeCenterBean;
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

public class ZSFTradeRecordActivity extends AbsActivity {

    public static final String TYPE_BUY = "buy";
    public static final String TYPE_SALE = "sell";
    public static final String TYPE_ON = "on";
    public static final String TYPE_DONE = "done";
    public static final String TYPE_FROZEN = "frozen";

    private String mTag;

    private ViewPagerIndicator mIndicator;
    private ViewPager mViewPager;
    private List<View> mViewList;
    private RefreshView mBuyRefreshView;
    private RefreshView mSaleRefreshView;
    private RefreshView mChangingRefreshView;
    private RefreshView mCompleteRefreshView;
    private RefreshView mFreezeRefreshView;

    private MyTradeRecordAdapter mBuyAdapter;
    private MyTradeRecordAdapter mSaleAdapter;
    private MyTradeRecordAdapter mChangingAdapter;
    private MyTradeRecordAdapter mCompleteAdapter;
    private MyTradeRecordAdapter mFreezeAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mission;
    }

    @Override
    protected void main() {
        mTag = this.toString();
        setTitle("交换记录");
        mBuyAdapter = new MyTradeRecordAdapter(mContext, MyMissionAdapter.TYPE_MINE);
        mSaleAdapter = new MyTradeRecordAdapter(mContext, MyMissionAdapter.TYPE_ALL);
        mChangingAdapter = new MyTradeRecordAdapter(mContext, MyMissionAdapter.TYPE_HISTORY);
        mCompleteAdapter = new MyTradeRecordAdapter(mContext, MyMissionAdapter.TYPE_HISTORY);
        mFreezeAdapter = new MyTradeRecordAdapter(mContext, MyMissionAdapter.TYPE_HISTORY);

//        mBuyAdapter.setOnItemClickListener(new OnItemClick(TYPE_BUY));
        mBuyAdapter.setOnCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TradeCenterBean.TradeInfo bean = (TradeCenterBean.TradeInfo) v.getTag();
                HttpUtil.cancelTradeBuy(mTag, bean.id, new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0) {
                            mBuyAdapter.getList().remove(bean);
                            mBuyAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtil.show(msg);
                        }
                    }
                });

            }
        });
//        mSaleAdapter.setOnItemClickListener(new OnItemClick(TYPE_SALE));
        mChangingAdapter.setOnItemClickListener(new OnItemClick(TYPE_ON));
//        mCompleteAdapter.setOnItemClickListener(new OnItemClick(TYPE_DONE));
//        mFreezeAdapter.setOnItemClickListener(new OnItemClick(TYPE_FROZEN));

        mIndicator = findViewById(R.id.indicator);
        mIndicator.setVisibleChildCount(5);
        mIndicator.setTitles(new String[]{"买单", "卖单", "交换中", "已完成", "冻结"});
        mIndicator.setChangeSize(false);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(5);
        mIndicator.setViewPager(mViewPager);
        mViewList = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mBuyRefreshView = getRefreshView(TYPE_BUY, mBuyAdapter);
        mSaleRefreshView = getRefreshView(TYPE_SALE, mSaleAdapter);
        mChangingRefreshView = getRefreshView(TYPE_ON, mChangingAdapter);
        mCompleteRefreshView = getRefreshView(TYPE_DONE, mCompleteAdapter);
        mFreezeRefreshView = getRefreshView(TYPE_FROZEN, mFreezeAdapter);
        ItemDecoration decoration = new ItemDecoration();
        mBuyRefreshView.getRecyclerView().addItemDecoration(decoration);
        mSaleRefreshView.getRecyclerView().addItemDecoration(decoration);
        mChangingRefreshView.getRecyclerView().addItemDecoration(decoration);
        mCompleteRefreshView.getRecyclerView().addItemDecoration(decoration);
        mFreezeRefreshView.getRecyclerView().addItemDecoration(decoration);
        mViewList.add(mBuyRefreshView);
        mViewList.add(mSaleRefreshView);
        mViewList.add(mChangingRefreshView);
        mViewList.add(mCompleteRefreshView);
        mViewList.add(mFreezeRefreshView);
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
        mBuyRefreshView.initData();
    }


    class ItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = 15;
        }
    }

    class OnItemClick implements OnItemClickListener<TradeCenterBean.TradeInfo> {

        String mType;

        OnItemClick(String type) {
            mType = type;
        }

        @Override
        public void onItemClick(TradeCenterBean.TradeInfo bean, int position) {
            Intent intent = new Intent(ZSFTradeRecordActivity.this, ZSFRecordDetailActivity.class);
            intent.putExtra("type", mType);
            intent.putExtra("order", bean);
            startActivity(intent);
        }
    }

    private RefreshView getRefreshView(final String type, final MyTradeRecordAdapter adapter) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final RefreshView mRefreshView = (RefreshView) inflater.inflate(R.layout.item_mission_page, mViewPager, false);
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_mission);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new RefreshView.DataHelper<TradeCenterBean.TradeInfo>() {
            @Override
            public RefreshAdapter<TradeCenterBean.TradeInfo> getAdapter() {
                return adapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.saleTradeList(mTag, p, type, callback);
            }

            @Override
            public List<TradeCenterBean.TradeInfo> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), TradeCenterBean.TradeInfo.class);
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
        return mRefreshView;

    }

    @Override
    protected void onDestroy() {

        if (mBuyRefreshView != null) {
            mBuyRefreshView.setDataHelper(null);
        }
        if (mSaleRefreshView != null) {
            mSaleRefreshView.setDataHelper(null);
        }
        if (mChangingRefreshView != null) {
            mChangingRefreshView.setDataHelper(null);
        }
        if (mFreezeRefreshView != null) {
            mFreezeRefreshView.setDataHelper(null);
        }
        if (mCompleteRefreshView != null) {
            mCompleteRefreshView.setDataHelper(null);
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
