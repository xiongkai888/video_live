package com.video.liveshow;

import android.content.Context;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.aliyun.svideo.downloader.DownloaderManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.video.liveshow.http.HttpUtil;
import com.video.liveshow.jpush.JMessageUtil;
import com.video.liveshow.jpush.JPushUtil;
import com.video.liveshow.utils.SharedPreferencesUtil;

import cn.sharesdk.framework.ShareSDK;
import cn.tillusory.sdk.TiSDK;


/**
 * Created by cxf on 2017/8/3.
 */

public class AppContext extends MultiDexApplication {

    public static AppContext sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        //初始化腾讯bugly
        CrashReport.initCrashReport(getApplicationContext());
        //初始化http
        HttpUtil.init();
        //初始化ShareSdk
        ShareSDK.initSDK(this);
        //初始化极光推送
        JPushUtil.getInstance().init();
        //初始化极光IM
        JMessageUtil.getInstance().init();
        //初始化萌颜
        TiSDK.init(AppConfig.BEAUTY_KEY, this);
        //获取uid和token
        String[] uidAndToken = SharedPreferencesUtil.getInstance().readUidAndToken();
        if (uidAndToken != null) {
            AppConfig.getInstance().login(uidAndToken[0], uidAndToken[1]);
        }

        QupaiHttpFinal.getInstance().initOkHttpFinal();
        DownloaderManager.getInstance().init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(this);
        super.attachBaseContext(base);
    }

}
