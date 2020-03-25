package com.qianhong.sflive.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qianhong.sflive.AppConfig;
import com.qianhong.sflive.Constants;
import com.qianhong.sflive.R;
import com.qianhong.sflive.activity.WebUploadImgActivity;
import com.qianhong.sflive.activity.ZSFHelpCenter;
import com.qianhong.sflive.activity.ZSFMissionActivity;
import com.qianhong.sflive.activity.ZSFMyCandyRecord;
import com.qianhong.sflive.activity.ZSFMyContribute;
import com.qianhong.sflive.activity.ZSFMyFriends;
import com.qianhong.sflive.activity.ZSFMyLevel;
import com.qianhong.sflive.activity.ZSFMyMoney;
import com.qianhong.sflive.activity.ZSFMyStar;
import com.qianhong.sflive.activity.ZSFRankActivity;
import com.qianhong.sflive.activity.ZSFTradeCenter;
import com.qianhong.sflive.bean.ChatUserBean;
import com.qianhong.sflive.bean.TradeUserCenter;
import com.qianhong.sflive.bean.UserBean;
import com.qianhong.sflive.http.HttpCallback;
import com.qianhong.sflive.http.HttpUtil;
import com.qianhong.sflive.utils.DateFormatUtil;
import com.qianhong.sflive.utils.DialogUitl;
import com.qianhong.sflive.utils.ProfitDialog;
import com.qianhong.sflive.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class TradeFragment extends AbsFragment implements View.OnClickListener {

    private String mTag;
    private TextView tv_level, tv_active, tv_contribute, tv_fans;
    private TextView tv_day, tv_total_mine, tv_total_little, tv_total_big, progress_tv;

    private ProgressBar progressBar;//进度条

    private TradeUserCenter mUserBean;
    //
    private String mShareUrl;

    private ChatUserBean sysBean;
    private List<ImageView> listImageView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_trade;
    }

    @Override
    protected void main() {
        mTag = this.toString();
        mRootView.findViewById(R.id.iv_candy).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_money).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_mission).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_friends).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_rank).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_share).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_question).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_help).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_trade).setOnClickListener(this);
        mRootView.findViewById(R.id.ll_level).setOnClickListener(this);
        mRootView.findViewById(R.id.ll_contribute).setOnClickListener(this);
        mRootView.findViewById(R.id.ll_star).setOnClickListener(this);
        mRootView.findViewById(R.id.ll_candy_day).setOnClickListener(this);
        mRootView.findViewById(R.id.ll_candy_total).setOnClickListener(this);
        mRootView.findViewById(R.id.store).setOnClickListener(this);
        mRootView.findViewById(R.id.life).setOnClickListener(this);
        mRootView.findViewById(R.id.carbage).setOnClickListener(this);
        mRootView.findViewById(R.id.city).setOnClickListener(this);
        mRootView.findViewById(R.id.liulanqi).setOnClickListener(this);
        mRootView.findViewById(R.id.yanzhengqi).setOnClickListener(this);

        tv_level = mRootView.findViewById(R.id.tv_level);
        tv_active = mRootView.findViewById(R.id.tv_active);
        tv_contribute = mRootView.findViewById(R.id.tv_contribute);
        tv_fans = mRootView.findViewById(R.id.tv_fans);
        tv_day = mRootView.findViewById(R.id.tv_day);
        tv_total_mine = mRootView.findViewById(R.id.tv_total_mine);
        tv_total_little = mRootView.findViewById(R.id.tv_total_little);
        tv_total_big = mRootView.findViewById(R.id.tv_total_big);
        progress_tv = mRootView.findViewById(R.id.progress_tv);
        progressBar = mRootView.findViewById(R.id.progressBar);

        listImageView = new ArrayList<>();
        listImageView.add((ImageView) mRootView.findViewById(R.id.star1));
        listImageView.add((ImageView) mRootView.findViewById(R.id.star2));
        listImageView.add((ImageView) mRootView.findViewById(R.id.star3));
        listImageView.add((ImageView) mRootView.findViewById(R.id.star4));
        listImageView.add((ImageView) mRootView.findViewById(R.id.star5));
        listImageView.add((ImageView) mRootView.findViewById(R.id.star6));
        getLastMessage();

    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            //            case R.id.iv_candy://存入糖果
            //                if (mUserBean != null) {
            //                    intent = new Intent(mContext, ZSFSaveCandy.class);
            //                    intent.putExtra("user", mUserBean);
            //                    startActivity(intent);
            //                }
            //                break;
            case R.id.tv_money://我的资产
                intent = new Intent(mContext, ZSFMyMoney.class);
                startActivity(intent);
                break;
            case R.id.tv_mission://我的任务
                intent = new Intent(mContext, ZSFMissionActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_friends://我的好友
                intent = new Intent(mContext, ZSFMyFriends.class);
                startActivity(intent);
                break;
            case R.id.tv_rank:
                intent = new Intent(mContext, ZSFRankActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_share://
                intent = new Intent(mContext, WebUploadImgActivity.class);
                intent.putExtra(Constants.URL, mShareUrl);
                startActivity(intent);
                break;
            case R.id.tv_question:
                intent = new Intent(mContext, WebUploadImgActivity.class);
                String url = AppConfig.HOST + "/index.php?g=Appapi&m=service&a=index";
                intent.putExtra(Constants.URL, url);
                startActivity(intent);
                break;
            case R.id.tv_help://ZSFHelpCenter
                intent = new Intent(mContext, ZSFHelpCenter.class);
                startActivity(intent);
                break;
            case R.id.tv_trade:
                intent = new Intent(mContext, ZSFTradeCenter.class);
                startActivity(intent);
                break;
            case R.id.ll_level:
                intent = new Intent(mContext, ZSFMyLevel.class);
                startActivity(intent);
                break;
            case R.id.ll_contribute:
                intent = new Intent(mContext, ZSFMyContribute.class);
                startActivity(intent);
                break;
            case R.id.ll_star:
                intent = new Intent(mContext, ZSFMyStar.class);
                startActivity(intent);
                break;
            case R.id.ll_candy_day:
                intent = new Intent(mContext, ZSFMyCandyRecord.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.ll_candy_total:
                intent = new Intent(mContext, ZSFMyCandyRecord.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.store:
            case R.id.life:
            case R.id.carbage:
            case R.id.city:
            case R.id.liulanqi:
            case R.id.yanzhengqi:
                ToastUtil.show("暂未开放，敬请期待！");
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refreshData();
            if (DateFormatUtil.isNoSameDayOfMillis() && mUserBean != null) {
                new ProfitDialog(getActivity(), mUserBean.fenhong, mUserBean.zuorichanguo).show();
            } else {
                if (sysBean != null && sysBean.getIsattent() == 0) {
                    DialogUitl.showSimpleDialog(mContext, "系统公告", sysBean.getLastMessage(), null);
                    sysBean.setIsattent(1);
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (AppConfig.getInstance().isLogin()){
            tradeCenterInfoRefresh();
        }
    }

    private void refreshData() {
        if (AppConfig.getInstance().isLogin()){
            tradeCenterInfoRefresh();
            baseInfoRefresh();
        }
    }

    private void tradeCenterInfoRefresh() {

        HttpUtil.getTradeCenterInfo(mTag, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    mUserBean = JSON.toJavaObject(obj, TradeUserCenter.class);
                    tv_level.setText(mUserBean.level);
                    tv_active.setText(mUserBean.activation + "+" + mUserBean.lower_activation);
                    tv_contribute.setText(mUserBean.contribution);
                    tv_fans.setText(String.valueOf(mUserBean.fans));
                    tv_day.setText(mUserBean.today_candy);
                    tv_total_mine.setText(mUserBean.candy);
                    tv_total_big.setText(mUserBean.big_activity);
                    tv_total_little.setText(mUserBean.small_activity);
                    int count = mUserBean.count > 10 ? 10 : mUserBean.count;
                    progress_tv.setText("任务进度:" + count + "/10   " + ((count == 10) ? "(任务完成)" : ""));
                    progressBar.setProgress(count * 10);
                    showStar(mUserBean.expert_num);
                    progress_tv.setVisibility((mUserBean.hastask == 1) ? View.VISIBLE : View.GONE);
                    progressBar.setVisibility((mUserBean.hastask == 1) ? View.VISIBLE : View.GONE);
                }
            }
        });
    }

    private void baseInfoRefresh() {
        HttpUtil.getBaseInfo(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    UserBean u = JSON.parseObject(info[0], UserBean.class);
                    mShareUrl = u.getQrcode() + "&uid=" + AppConfig.getInstance().getUid();
                }
            }
        });
    }

    private void showStar(int num) {
        for (int i = 0; i < 6; i++) {
            listImageView.get(i).setImageResource(R.mipmap.xxmal2);
        }
        for (int i = 0; i < num; i++) {
            if (i < 6) {
                listImageView.get(i).setImageResource(R.mipmap.xxmal);
            }
        }
    }


    private void getLastMessage() {
        if (AppConfig.getInstance().isLogin()) {
            HttpUtil.getLastMessage(new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if (code == 0 && info.length > 0) {
                        JSONObject obj = JSON.parseObject(info[0]);
                        if (obj.containsKey("officeInfo")) {
                            JSONObject officeInfo = obj.getJSONObject("officeInfo");
                            sysBean = new ChatUserBean();
                            sysBean.setLastMessage(officeInfo.getString("content"));
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HttpUtil.cancel(mTag);
    }
}
