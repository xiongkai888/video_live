package com.qianhong.sflive.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qianhong.sflive.AppConfig;
import com.qianhong.sflive.R;
import com.qianhong.sflive.event.RefreshCenterEvent;
import com.qianhong.sflive.http.HttpCallback;
import com.qianhong.sflive.http.HttpUtil;
import com.qianhong.sflive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;

public class ZSFSendActivity extends AbsActivity {
    private String mTag;
    EditText price, count, password;
    TextView qujian, total;

    private float mPrice = 0.0f;
    private float mCount = 0.0f;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_send;
    }

    @Override
    protected void main() {
        mTag = this.toString();
        setTitle("发单");
        qujian = findViewById(R.id.qujian);
        price = findViewById(R.id.price);
        count = findViewById(R.id.count);
        total = findViewById(R.id.total);
        password = findViewById(R.id.password);
        findViewById(R.id.commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
        //区间展示
        qujian.setText(AppConfig.getInstance().getConfig().candy_price);

        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String p = price.getText().toString();
                if (!TextUtils.isEmpty(p)) {
                    mPrice = Float.parseFloat(p);
                } else {
                    mPrice = 0;
                }
                setTotal(mPrice, mCount);

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String c = count.getText().toString();
                if (!TextUtils.isEmpty(c)) {
                    mCount = Float.parseFloat(c);
                } else {
                    mCount = 0;
                }
                setTotal(mPrice, mCount);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setTotal(float price, float count) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String strPrice = decimalFormat.format(price*count);//返回字符串
        total.setText(strPrice);
    }

    private void send() {
        String p = price.getText().toString();
        String c = count.getText().toString();
        String t = total.getText().toString();
        String pwd = password.getText().toString();
        if (TextUtils.isEmpty(p) || TextUtils.isEmpty(c) || TextUtils.isEmpty(t) || TextUtils.isEmpty(pwd)) {
            ToastUtil.show("输入有误，请检查");
            return;
        }
        HttpUtil.sendTrade(mTag, 0, p, c, t, pwd, "", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    ToastUtil.show("发布成功");
                    EventBus.getDefault().post(new RefreshCenterEvent());
                    finish();
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
