package com.qianhong.sflive.activity;

import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qianhong.sflive.R;
import com.qianhong.sflive.bean.TradeUserCenter;
import com.qianhong.sflive.http.HttpCallback;
import com.qianhong.sflive.http.HttpUtil;
import com.qianhong.sflive.utils.ToastUtil;

public class ZSFSaveCandy extends AbsActivity {

    TextView tv_candy_today, tv_active, tv_candy_total;
    private String mTag;
    private TradeUserCenter mUserBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_save_candy;
    }

    @Override
    protected void main() {
        mTag = this.toString();
        setTitle("存入硕果");
        tv_candy_today = findViewById(R.id.tv_candy_today);
        tv_active = findViewById(R.id.tv_active);
        tv_candy_total = findViewById(R.id.tv_candy_total);
        findViewById(R.id.iv_candy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        mUserBean = (TradeUserCenter) getIntent().getSerializableExtra("user");
        tv_candy_today.setText(mUserBean.today_candy);
        tv_active.setText(mUserBean.activation + "+" + mUserBean.lower_activation);
        tv_candy_total.setText(mUserBean.candy);
    }

    private void save() {
        HttpUtil.storeTradeTodayInfo(mTag, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {

                if (code == 0 && info.length > 0) {
                    ToastUtil.show("存储成功！");
                    JSONObject obj = JSON.parseObject(info[0]);
                    String candy = obj.getString("candy");
                    String active = obj.getString("activation");
                    tv_candy_today.setText(String.valueOf(0));
                    tv_active.setText(active);
                    tv_candy_total.setText(candy);

                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpUtil.cancel(mTag);
    }
}
