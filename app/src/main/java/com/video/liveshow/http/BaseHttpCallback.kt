package com.video.liveshow.http

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONException
import com.lzy.okgo.callback.AbsCallback
import com.lzy.okgo.exception.HttpException
import com.lzy.okgo.model.Response
import com.video.liveshow.utils.L
import com.video.liveshow.utils.ToastUtil
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException
import java.net.UnknownServiceException

/**
 * Created by xkai on 2020/4/8.
 */
abstract class BaseHttpCallback<T> : AbsCallback<BaseBean<T>>() {

    override fun onSuccess(response: Response<BaseBean<T>>?) {
        L.d("onSuccess:$response")
        response?.let {
            if (it.isSuccessful) {
                it.body()?.let { bean ->
                    if (bean.code == 1) {
                        onSuccess(bean.code, bean.msg, bean.data)
                    } else {
                        onFail(bean.code, bean.msg)
                    }
                }
            } else {
                onFail(it.code(), it.message())
            }
        }
    }

    override fun convertResponse(response: okhttp3.Response?): BaseBean<T> {
        val result = response!!.body()!!.string()
        L.d("result:$result")
        val bean = JSON.parseObject<BaseBean<T>>(result, BaseBean::class.java)
        response.close()
        return bean
    }

    abstract fun onSuccess(code: Int, msg: String?, data: T?)

    open fun onFail(code: Int, msg: String?) {
        L.d("onFail:$code $msg")
        ToastUtil.show(msg)
    }


    override fun onError(response: Response<BaseBean<T>>) {
        val t = response.exception
        L.e("网络请求错误---->" + t.javaClass + " : " + t.message)
        if (t is ConnectException || t is UnknownHostException || t is UnknownServiceException || t is SocketException || t is HttpException) {
            ToastUtil.show("无法连接服务器" + if (response.rawResponse != null) "：" + response.rawResponse.code() else " ")
        }
        if (t is JSONException){
            ToastUtil.show("数据解析异常")
        }
        onError()
    }

    open fun onError() {

    }

}