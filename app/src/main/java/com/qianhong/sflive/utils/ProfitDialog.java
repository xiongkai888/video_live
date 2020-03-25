package com.qianhong.sflive.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.qianhong.sflive.R;

/***
 * 分红弹框
 */
public class ProfitDialog extends Dialog implements View.OnClickListener {

    public ProfitDialog(Activity activity,long fenhong,long zuorichanguo) {
        super(activity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initDialog(fenhong,zuorichanguo);
    }

    private void initDialog(long fenhong,long zuorichanguo) {
        setContentView(R.layout.dialog_profit);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        getWindow().setWindowAnimations(R.style.animation_dialog);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        TextView furlTv = findViewById(R.id.furl_tv);
//        furlTv.setText("恭喜您昨日获得 "+fenhong+" 个硕果分红");
        furlTv.setText("恭喜您昨日产 "+zuorichanguo+" 个硕果，"+"获得 "+fenhong+" 个硕果分红。");
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.horizontalMargin = 0;
        getWindow().setAttributes(lp);
//        setCancelable(false);
        findViewById(R.id.image_close).setOnClickListener(this);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_close:
                dismiss();
                break;
        }
    }

}
