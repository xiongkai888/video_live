package com.video.liveshow.http

import com.alibaba.fastjson.JSON
import com.lzy.okgo.callback.AbsCallback
import com.lzy.okgo.model.Response
import com.video.liveshow.utils.L

/**
 * Created by xkai on 2020/4/8.
 */
abstract class BaseHttpCallback<T> : AbsCallback<BaseBean<T>>() {
    override fun onSuccess(response: Response<BaseBean<T>>?) {
        L.d("onSuccess:$response")
        response?.let {
            if (it.isSuccessful) {
                it.body()?.let { bean ->
                    if (bean.code == 200) {
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

    abstract fun onSuccess(code: Int, msg: String?, data: T)

    fun onFail(code: Int, msg: String) {
        L.d("onFail:$code $msg")
    }
}