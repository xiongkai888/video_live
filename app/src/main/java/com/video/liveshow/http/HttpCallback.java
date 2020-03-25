package com.video.liveshow.http;

import android.app.Dialog;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.video.liveshow.AppContext;
import com.video.liveshow.activity.LoginActivity;
import com.video.liveshow.utils.L;
import com.video.liveshow.utils.ToastUtil;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

/**
 * Created by cxf on 2017/8/7.
 */

public abstract class HttpCallback extends AbsCallback<JsonBean> {

    private Dialog mLoadingDialog;

    @Override
    public JsonBean convertResponse(okhttp3.Response response) throws Throwable {
        String result = response.body().string();
        L.d("result:" + result);
        JsonBean bean = JSON.parseObject(result, JsonBean.class);
        response.close();
        return bean;
    }

    @Override
    public void onSuccess(Response<JsonBean> response) {
        JsonBean bean = response.body();
        if (bean != null && 200 == bean.getRet()) {
            Data data = bean.getData();
            if (data != null) {
                if (0 == data.getCode()) {
                    onSuccess(data.getCode(), data.getMsg(), data.getInfo());
                } else if (700 == data.getCode()) {
                    //token过期，重新登录
                    Intent intent = new Intent(AppContext.sInstance, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    AppContext.sInstance.startActivity(intent);
                    ToastUtil.show(data.getMsg());
                } else {
                    ToastUtil.show(data.getMsg());
                }
            } else {
                L.e("服务器返回值异常--->ret: " + bean.getRet() + " msg: " + bean.getMsg());
            }
        }
    }

    @Override
    public void onError(Response<JsonBean> response) {
        Throwable t = response.getException();
        L.e("网络请求错误---->" + t.getClass() + " : " + t.getMessage());
        if (t instanceof ConnectException || t instanceof UnknownHostException || t instanceof UnknownServiceException || t instanceof SocketException || t instanceof HttpException) {
            ToastUtil.show("无法连接服务器" + (response.getRawResponse() != null ? ("："+response.getRawResponse().code()) : " "));
        }
        if (showLoadingDialog() && mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        onError();
    }

    public void onError() {

    }


    public abstract void onSuccess(int code, String msg, String[] info);

    @Override
    public void onStart(Request<JsonBean, ? extends Request> request) {
        onStart();
    }

    public void onStart() {
        if (showLoadingDialog()) {
            if (mLoadingDialog == null) {
                mLoadingDialog = createLoadingDialog();
            }
            if (mLoadingDialog != null) {
                mLoadingDialog.show();
            }
        }
    }

    @Override
    public void onFinish() {
        if (showLoadingDialog() && mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    public Dialog createLoadingDialog() {
        return null;
    }

    public boolean showLoadingDialog() {
        return false;
    }

}
