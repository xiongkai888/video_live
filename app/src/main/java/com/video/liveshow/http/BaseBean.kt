package com.video.liveshow.http

/**
 * Created by xkai on 2020/4/8.
 */
data class BaseBean<T>(val code:Int,val msg:String, val data: T)