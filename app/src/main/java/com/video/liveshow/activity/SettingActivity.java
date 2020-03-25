package com.video.liveshow.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.video.liveshow.AppConfig;
import com.video.liveshow.Constants;
import com.video.liveshow.R;
import com.video.liveshow.bean.ConfigBean;
import com.video.liveshow.event.LogoutEvent;
import com.video.liveshow.event.NeedRefreshEvent;
import com.video.liveshow.http.HttpUtil;
import com.video.liveshow.interfaces.CommonCallback;
import com.video.liveshow.utils.DialogUitl;
import com.video.liveshow.utils.GlideCatchUtil;
import com.video.liveshow.utils.WordUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by cxf on 2018/6/14.
 * 设置
 */

public class SettingActivity extends AbsActivity {

    private Handler mHandler;
    private TextView mCacheSize;
    private TextView mVersion;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.setting));
        mHandler = new Handler();
        mCacheSize = (TextView) findViewById(R.id.cache_size);
        mVersion = (TextView) findViewById(R.id.version);
        mCacheSize.setText(getCacheSize());
        ConfigBean configBean = AppConfig.getInstance().getConfig();
        if (configBean == null) {
            HttpUtil.getConfig(this, new CommonCallback<ConfigBean>() {
                @Override
                public void callback(ConfigBean configBean) {
                    showUpdate(configBean);
                }
            });
        } else {
            showUpdate(configBean);
        }
    }

    private void showUpdate(ConfigBean configBean) {
        String des;
        try {
            int code = Integer.parseInt(configBean.getApk_ver());
            if (code > AppConfig.getInstance().getCode()) {
                des = "(" + WordUtil.getString(R.string.can_update) + ")";
            } else {
                des = "(" + WordUtil.getString(R.string.no_update) + ")";
            }
            mVersion.setText(AppConfig.getInstance().getVersion() + des);
//            Log.d("tagg",code+","+AppConfig.getInstance().getCode());
        } catch (Exception e) {

        }

    }

    public void settingClick(View v) {
        switch (v.getId()) {
            case R.id.btn_black_list:
                forwardBlackList();
                break;
            case R.id.btn_auth://身份认证
                //                ToastUtil.show("实名认证暂时关闭");
                forwardMyAuth();
                break;
            case R.id.btn_about:
                forwardAboutUs();
                break;
            case R.id.btn_check_update://版本更新
                checkUpdate();
                break;
            case R.id.btn_clear_cache://清除缓存
                clearCache();
                break;
            case R.id.btn_logout:
                logout();
                break;
            case R.id.btn_second_pwd:
                setSecondPwd();
                break;
            //            case R.id.btn_pay://支付方式功能屏蔽
            //                setPayInfo();
            //                break;
            case R.id.btn_pwd:
                Intent forgetIntent = new Intent(this, ZSFRegisterActivity.class);
                forgetIntent.putExtra("type", ZSFRegisterActivity.TYPE_SET);
                startActivity(forgetIntent);
                break;
        }
    }

    /**
     * 设置二级密码
     */
    private void setSecondPwd() {
        startActivity(new Intent(mContext, ZSFSetSecondPwdActivity.class));
    }

    /**
     * 设置支付方式
     */
    private void setPayInfo() {
        startActivity(new Intent(mContext, ZSFSetPayInfo2.class));
    }


    private void forwardBlackList() {
        startActivity(new Intent(mContext, BlackActivity.class));
    }


    /**
     * 我的认证
     */
    private void forwardMyAuth() {
        //        Intent intent = new Intent(mContext, WebUploadImgActivity.class);
        //        AppConfig appConfig = AppConfig.getInstance();
        //        intent.putExtra(Constants.URL, AppConfig.HOST + "/index.php?g=Appapi&m=Auth&a=index&uid=" + appConfig.getUid() + "&token=" + appConfig.getToken());
        //        startActivity(intent);
        Intent intent = new Intent(mContext, ZSFMyAuth.class);
        startActivity(intent);
    }


    /**
     * 关于我们
     */
    private void forwardAboutUs() {
        Intent intent = new Intent(mContext, WebUploadImgActivity.class);
        AppConfig appConfig = AppConfig.getInstance();
        String url = AppConfig.HOST + "/index.php?g=Appapi&m=About&a=index&uid=" + appConfig.getUid() + "&token=" + appConfig.getToken() + "&version=" + appConfig.getVersion();
        intent.putExtra(Constants.URL, url);
        startActivity(intent);
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        HttpUtil.getConfig(this, null);
    }

    /**
     * 清除缓存
     */
    private void clearCache() {
        final Dialog dialog = DialogUitl.loadingDialog(mContext, getString(R.string.clear_ing));
        dialog.show();
        GlideCatchUtil.getInstance().clearImageAllCache();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null) {
                    dialog.dismiss();
                }
                mCacheSize.setText(getCacheSize());
            }
        }, 2000);
    }

    private String getCacheSize() {
        String cache = GlideCatchUtil.getInstance().getCacheSize();
        if ("0.0Byte".equalsIgnoreCase(cache)) {
            cache = getString(R.string.no_cache);
        }
        return cache;
    }

    private void logout() {
        AppConfig.getInstance().logout();
        AppConfig.getInstance().logoutJPush();
        EventBus.getDefault().post(new NeedRefreshEvent());
        EventBus.getDefault().post(new LogoutEvent());
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
