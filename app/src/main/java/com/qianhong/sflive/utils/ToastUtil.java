package com.qianhong.sflive.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.qianhong.sflive.AppContext;
import com.qianhong.sflive.R;

/**
 * Created by cxf on 2017/8/3.
 */

public class ToastUtil {

    private static Toast sToast;

    static {
        sToast = new Toast(AppContext.sInstance);
        sToast.setDuration(Toast.LENGTH_SHORT);
        sToast.setGravity(Gravity.CENTER, 0, 0);
        View view = LayoutInflater.from(AppContext.sInstance).inflate(R.layout.view_toast, null);
        sToast.setView(view);
    }

    public static void show(String s) {
        sToast.setText(s);
        sToast.show();
    }

    /**
     * 检测是否安装支付宝
     * @param context
     * @return
     */
    public static boolean checkAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

}
