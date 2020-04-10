package com.video.liveshow.http

/**
 * Created by xkai on 2020/4/8.
 */
class BaseBean<T> {
    var code:Int = 0
    var msg:String = ""
    var data: T? = null
}