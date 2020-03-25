package com.video.liveshow.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.video.liveshow.R;
import com.video.liveshow.adapter.MyTradeCenterAdapter;
import com.video.liveshow.bean.TradeCenterBean;
import com.video.liveshow.custom.GestureForbidViewPager;
import com.video.liveshow.custom.RefreshAdapter;
import com.video.liveshow.custom.RefreshView;
import com.video.liveshow.custom.ViewPagerIndicator;
import com.video.liveshow.http.HttpCallback;
import com.video.liveshow.http.HttpUtil;
import com.video.liveshow.interfaces.OnItemClickListener;
import com.video.liveshow.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/***
 * 交易中心
 */
public class ZSFTradeCenter extends AbsActivity implements View.OnClickListener {
    private String mTag;
    private String mCandy;
    TextView total, danjia, day, low, high, fu;
    private GestureForbidViewPager mViewPager;
    private List<View> mViewList;
    private List<View> mViewList1;
    //搜索界面
    private View mSearchView;
    private RefreshView mRefreshView1;
    private RefreshView mRefreshView2;
    private RefreshView mRefreshView3;
    private MyTradeCenterAdapter mAdapter1;
    private MyTradeCenterAdapter mAdapter2;
    private MyTradeCenterAdapter mAdapter3;

    private ViewPagerIndicator mIndicator;
    private ViewPager mViewPager1;
    //结果界面
    private View mTradeView;
    private TextView mMine, mPrice, mCount, mTotal;
    private EditText mSaleCount, mSalePwd;

    private TradeCenterBean.TradeInfo mTradeInfo;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_trade_center;
    }

    @Override
    protected void main() {
        mTag = this.toString();
        setTitle("交易中心");
        findViewById(R.id.record).setOnClickListener(this);
        total = findViewById(R.id.total);
        danjia = findViewById(R.id.danjia);
        day = findViewById(R.id.day);
        low = findViewById(R.id.low);
        high = findViewById(R.id.high);
        fu = findViewById(R.id.fu);

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(2);
        mViewList = new ArrayList<>();
        //searchview
        mSearchView = inflater.inflate(R.layout.view_trade_center_list, mViewPager, false);
        mSearchView.findViewById(R.id.et_search).setOnClickListener(this);
        mSearchView.findViewById(R.id.tv_send).setOnClickListener(this);

        mIndicator = mSearchView.findViewById(R.id.indicator);
        mIndicator.setVisibleChildCount(3);
        mIndicator.setTitles(new String[]{"0-20", "20-100", "100以上"});
        mIndicator.setChangeSize(false);
        mViewPager1 = (ViewPager) mSearchView.findViewById(R.id.viewPager);
        mViewPager1.setOffscreenPageLimit(2);
        mIndicator.setViewPager(mViewPager1);

        mViewList1 = new ArrayList<>();
        LayoutInflater inflater1 = LayoutInflater.from(mContext);
        mRefreshView1 = (RefreshView) inflater1.inflate(R.layout.item_mission_page, mViewPager1, false);
        mRefreshView1.setNoDataLayoutId(R.layout.view_no_data_mission);
        mRefreshView1.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView1.setDataHelper(new RefreshView.DataHelper<TradeCenterBean.TradeInfo>() {
            int pg;

            @Override
            public RefreshAdapter<TradeCenterBean.TradeInfo> getAdapter() {
                if (mAdapter1 == null) {
                    mAdapter1 = new MyTradeCenterAdapter(mContext);
                    mAdapter1.setOnItemClickListener(new OnItemClickListener<TradeCenterBean.TradeInfo>() {
                        @Override
                        public void onItemClick(TradeCenterBean.TradeInfo bean, int position) {//Trade.Sell
                            mTradeInfo = bean;
                            mViewPager.setCurrentItem(1);
                        }
                    });
                }
                return mAdapter1;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                pg = p;
                HttpUtil.getTradeList(mTag, "", p, 1, callback);
            }

            @Override
            public List<TradeCenterBean.TradeInfo> processData(String[] info) {
                if (info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    TradeCenterBean bean = JSON.toJavaObject(obj, TradeCenterBean.class);
                    initTop(bean.dayData);
                    List<TradeCenterBean.TradeInfo> list = bean.list;
                    if (pg == 1) {
                        Collections.shuffle(list);
                    }
                    return list;
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
                    mRefreshView1.setLoadMoreEnable(false);
                } else {
                    mRefreshView1.setLoadMoreEnable(true);
                }
            }
        });
        mRefreshView2 = (RefreshView) inflater1.inflate(R.layout.item_mission_page, mViewPager1, false);
        mRefreshView2.setNoDataLayoutId(R.layout.view_no_data_mission);
        mRefreshView2.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView2.setDataHelper(new RefreshView.DataHelper<TradeCenterBean.TradeInfo>() {
            int pg;

            @Override
            public RefreshAdapter<TradeCenterBean.TradeInfo> getAdapter() {
                if (mAdapter2 == null) {
                    mAdapter2 = new MyTradeCenterAdapter(mContext);
                    mAdapter2.setOnItemClickListener(new OnItemClickListener<TradeCenterBean.TradeInfo>() {
                        @Override
                        public void onItemClick(TradeCenterBean.TradeInfo bean, int position) {//Trade.Sell
                            mTradeInfo = bean;
                            mViewPager.setCurrentItem(1);
                        }
                    });
                }
                return mAdapter2;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                pg = p;
                HttpUtil.getTradeList(mTag, "", p, 2, callback);
            }

            @Override
            public List<TradeCenterBean.TradeInfo> processData(String[] info) {
                if (info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    TradeCenterBean bean = JSON.toJavaObject(obj, TradeCenterBean.class);
                    initTop(bean.dayData);
                    List<TradeCenterBean.TradeInfo> list = bean.list;
                    if (pg == 1) {
                        Collections.shuffle(list);
                    }
                    return list;
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
                    mRefreshView2.setLoadMoreEnable(false);
                } else {
                    mRefreshView2.setLoadMoreEnable(true);
                }
            }
        });
        mRefreshView3 = (RefreshView) inflater1.inflate(R.layout.item_mission_page, mViewPager1, false);
        mRefreshView3.setNoDataLayoutId(R.layout.view_no_data_mission);
        mRefreshView3.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView3.setDataHelper(new RefreshView.DataHelper<TradeCenterBean.TradeInfo>() {
            int pg;

            @Override
            public RefreshAdapter<TradeCenterBean.TradeInfo> getAdapter() {
                if (mAdapter3 == null) {
                    mAdapter3 = new MyTradeCenterAdapter(mContext);
                    mAdapter3.setOnItemClickListener(new OnItemClickListener<TradeCenterBean.TradeInfo>() {
                        @Override
                        public void onItemClick(TradeCenterBean.TradeInfo bean, int position) {//Trade.Sell
                            mTradeInfo = bean;
                            mViewPager.setCurrentItem(1);
                        }
                    });
                }
                return mAdapter3;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                pg = p;
                HttpUtil.getTradeList(mTag, "", p, 3, callback);
            }

            @Override
            public List<TradeCenterBean.TradeInfo> processData(String[] info) {
                if (info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    TradeCenterBean bean = JSON.toJavaObject(obj, TradeCenterBean.class);
                    List<TradeCenterBean.TradeInfo> list = bean.list;
                    if (pg == 1) {
                        Collections.shuffle(list);
                    }
                    return list;
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
                    mRefreshView3.setLoadMoreEnable(false);
                } else {
                    mRefreshView3.setLoadMoreEnable(true);
                }
            }
        });
        mViewList1.add(mRefreshView1);
        mViewList1.add(mRefreshView2);
        mViewList1.add(mRefreshView3);
        mViewPager1.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mViewList1.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View v = mViewList1.get(position);
                container.addView(v);
                return v;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mViewList1.get(position));
            }
        });
        mViewPager1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((RefreshView) mViewPager1.getChildAt(position)).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mRefreshView1.initData();

        //        mRefreshView = mSearchView.findViewById(R.id.refreshView);
        //        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        //        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_default);
        //        mRefreshView.setDataHelper(new RefreshView.DataHelper<TradeCenterBean.TradeInfo>() {
        //            @Override
        //            public RefreshAdapter<TradeCenterBean.TradeInfo> getAdapter() {
        //                if (mAdapter == null) {
        //                    mAdapter = new MyTradeCenterAdapter(mContext);
        //                    mAdapter.setOnItemClickListener(new OnItemClickListener<TradeCenterBean.TradeInfo>() {
        //                        @Override
        //                        public void onItemClick(TradeCenterBean.TradeInfo bean, int position) {//Trade.Sell
        //                            mTradeInfo = bean;
        //                            mViewPager.setCurrentItem(1);
        //                        }
        //                    });
        //                }
        //                return mAdapter;
        //            }
        //
        //            @Override
        //            public void loadData(int p, HttpCallback callback) {
        //                HttpUtil.getTradeList(mTag, "", p, callback);
        //            }
        //
        //            @Override
        //            public List<TradeCenterBean.TradeInfo> processData(String[] info) {
        //                if (info.length > 0) {
        //                    JSONObject obj = JSON.parseObject(info[0]);
        //                    TradeCenterBean bean = JSON.toJavaObject(obj, TradeCenterBean.class);
        //                    initTop(bean.dayData);
        //                    return bean.list;
        //                }
        //                return null;
        //
        //            }
        //
        //            @Override
        //            public void onRefresh(List<TradeCenterBean.TradeInfo> list) {
        //
        //            }
        //
        //            @Override
        //            public void onNoData(boolean noData) {
        //
        //            }
        //
        //            @Override
        //            public void onLoadDataCompleted(int dataCount) {
        //                if (dataCount < 20) {
        //                    mRefreshView.setLoadMoreEnable(false);
        //                } else {
        //                    mRefreshView.setLoadMoreEnable(true);
        //                }
        //            }
        //        });

        //Tradeview
        mTradeView = inflater.inflate(R.layout.view_trade_center_trade, mViewPager, false);
        mMine = mTradeView.findViewById(R.id.mine);
        mPrice = mTradeView.findViewById(R.id.price);
        mCount = mTradeView.findViewById(R.id.count);
        mTotal = mTradeView.findViewById(R.id.total);
        mSaleCount = mTradeView.findViewById(R.id.salecount);
        mSalePwd = mTradeView.findViewById(R.id.salepwd);
        mTradeView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });
        mTradeView.findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sale();
            }
        });
        mViewList.add(mSearchView);
        mViewList.add(mTradeView);
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
                if (position == 1) {
                    initTrade();
                    //tradeview init
                } else if (position == 0) {//返回刷新
                    //                    mRefreshView.initData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //        mRefreshView.initData();
        //        EventBus.getDefault().register(this);
    }


    //    @Subscribe
    //    public void refreshCenterEvent(RefreshCenterEvent event){
    //        mRefreshView.initData();
    //    }

    @Override
    protected void onDestroy() {
        HttpUtil.cancel(mTag);
        //        mRefreshView.setDataHelper(null);
        //        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.record:
                intent = new Intent(this, ZSFTradeRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.et_search:
                intent = new Intent(this, ZSFSearchActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.tv_send:
                intent = new Intent(this, ZSFSendActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 1) {
            mViewPager.setCurrentItem(0);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            mTradeInfo = (TradeCenterBean.TradeInfo) data.getSerializableExtra("bean");
            mViewPager.setCurrentItem(1);
        }
    }

    private void initTop(TradeCenterBean.DayData dayData) {
        total.setText(dayData.totalNum);
        danjia.setText(dayData.price);
        day.setText(dayData.tradenum);
        low.setText(dayData.lowprice);
        high.setText(dayData.highprice);
        fu.setText(dayData.rise);
        mCandy = dayData.candy;
    }

    private void initTrade() {
        if (mTradeInfo == null)
            return;
        mMine.setText("现有硕果数：" + mCandy);
        mPrice.setText("单价：￥" + mTradeInfo.price);
        mCount.setText("数量：" + mTradeInfo.number + "硕果");
        mTotal.setText("总价：￥" + mTradeInfo.totalprice);
    }

    private void sale() {

        String number = mSaleCount.getText().toString();
        String pwd = mSalePwd.getText().toString();

        if (TextUtils.isEmpty(number) || TextUtils.isEmpty(pwd)) {
            ToastUtil.show("请输入出售数量和密码");
            return;
        }
        HttpUtil.saleTrade(mTag, mTradeInfo.id, number, pwd, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (mViewPager.getCurrentItem() == 1) {
                    mViewPager.setCurrentItem(0);
                }
                ToastUtil.show(msg);
            }
        });
    }
}
