package com.video.liveshow.http;

import com.lzy.okgo.model.Response;
import com.video.liveshow.AppConfig;
import com.video.liveshow.utils.L;
import com.video.liveshow.utils.ToastUtil;

/**
 * Created by cxf on 2018/7/12.
 */

public abstract class CheckTokenCallback extends HttpCallback {


    @Override
    public void onSuccess(Response<JsonBean> response) {
        JsonBean bean = response.body();
        if (bean != null && 200 == bean.getRet()) {
            Data data = bean.getData();
            if (data != null) {
                if (700 == data.getCode()) {
                    AppConfig.getInstance().logout();
                }else if (702 == data.getCode()){
                    ToastUtil.show(data.getMsg());
                }else {
                    onSuccess(data.getCode(), data.getMsg(), data.getInfo());
                }
            } else {
                L.e("服务器返回值异常--->ret: " + bean.getRet() + " msg: " + bean.getMsg());
            }

        } else {
            L.e("服务器返回值异常--->ret: " + bean.getRet() + " msg: " + bean.getMsg());
        }
    }

}
