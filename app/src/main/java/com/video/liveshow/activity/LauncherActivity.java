package com.video.liveshow.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.video.liveshow.AppConfig;
import com.video.liveshow.R;
import com.video.liveshow.bean.AdMsg;
import com.video.liveshow.bean.UserBean;
import com.video.liveshow.glide.ImgLoader;
import com.video.liveshow.http.CheckTokenCallback;
import com.video.liveshow.http.HttpUtil;
import com.video.liveshow.utils.SharedPreferencesUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 启动页面
 */
public class LauncherActivity extends AbsActivity implements View.OnClickListener {
    private int recLen = 4;//跳过倒计时提示5秒
    private Handler mHandler;
    private TextView tv;
    private int viewid;
    private RelativeLayout adtime;
    private boolean isload = true;
    private String url;
    private ImageView adimg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void main() {
        tv = findViewById(R.id.tv);//跳过
        adimg = findViewById(R.id.adimg);//跳过
        tv.setOnClickListener(this);//跳过监听
        adimg.setOnClickListener(this);//跳过监听
        timer.schedule(task, 0, 1000);//等待时间一秒，停顿时间一秒
        HttpUtil.ADMsg(new CheckTokenCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                List<AdMsg> list = JSON.parseArray(Arrays.toString(info), AdMsg.class);
                url = list.get(0).getUrl();
//                ImgLoader.display(list.get(0).getPic(), adimg);
                ImgLoader.display(url, adimg);
            }
        });
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isload) {
                    if (!AppConfig.getInstance().isLogin()) {
                        //检查Token是否过期
                        HttpUtil.ifToken(new CheckTokenCallback() {
                            @Override
                            public void onSuccess(int code, String msg, String[] info) {
                                if (code == 0 && info.length > 0) {
                                    UserBean userBean = JSON.parseObject(info[0], UserBean.class);
                                    if (userBean != null) {
                                        SharedPreferencesUtil.getInstance().saveUserBeanJson(info[0]);
                                        AppConfig.getInstance().setUserBean(userBean);
                                        forwardMainActivity();
                                    }
                                } else if (code == 700) {
                                    AppConfig.getInstance().logout();
                                    AppConfig.getInstance().logoutJPush();
                                    forwardMainActivity();
                                }
                            }
                        });
                    } else {
                        forwardMainActivity();
                    }
                }

            }
        }, 10000);

    }

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() { // UI thread
                @Override
                public void run() {
                    recLen--;
                    tv.setText("跳过 " + recLen + "s");
                    if (recLen < 1) {
                        forwardMainActivity2();
                        if (recLen < 0) {
                            timer.cancel();
                            tv.setVisibility(View.GONE);//倒计时到-1隐藏字体
                        }
                    }
                }
            });
        }
    };

    private void forwardMainActivity() {
        mHandler.removeMessages(0x100);
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        HttpUtil.cancel(HttpUtil.GET_CONFIG);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != viewid) {
            switch (v.getId()) {
                case R.id.tv:
                    forwardMainActivity2();
                    break;
                case R.id.adimg:
                    if (url != null) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                    break;
            }
            viewid = v.getId();
        }
    }

    private void forwardMainActivity2() {
        if (isload) {
            isload = false;
            if (!AppConfig.getInstance().isLogin()) {
                HttpUtil.ifToken(new CheckTokenCallback() {//检查Token是否过期
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0 && info.length > 0) {
                            UserBean userBean = JSON.parseObject(info[0], UserBean.class);
                            if (userBean != null) {
                                SharedPreferencesUtil.getInstance().saveUserBeanJson(info[0]);
                                AppConfig.getInstance().setUserBean(userBean);
                                forwardMainActivity();
                            }
                        } else if (code == 700) {
                            AppConfig.getInstance().logout();
                            AppConfig.getInstance().logoutJPush();
                            forwardMainActivity();
                        }
                    }
                });
            } else {
                forwardMainActivity();
            }
        }
    }
}
