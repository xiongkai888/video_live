package com.video.liveshow.event;

import com.video.liveshow.bean.VideoBean;

/**
 * Created by cxf on 2018/7/30.
 */

public class VideoDeleteEvent {
    private VideoBean mVideoBean;

    public VideoDeleteEvent(VideoBean videoBean) {
        mVideoBean = videoBean;
    }

    public VideoBean getVideoBean() {
        return mVideoBean;
    }
}
