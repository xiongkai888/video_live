package com.video.liveshow.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.video.liveshow.R;
import com.video.liveshow.bean.UserBean;
import com.video.liveshow.http.HttpCallback;
import com.video.liveshow.http.HttpUtil;
import com.video.liveshow.utils.ToastUtil;
import com.video.liveshow.utils.ValidatePhoneUtil;

import java.util.ArrayList;
import java.util.List;

public class ZSFMyAuth extends AbsActivity {

    private EditText mAccount, mName, pay_account;
    private View mAuth, mAuthed;
    private TextView mTvName, mTvCard, payAccountName;
    private UserBean mUser;
    private String mOrderId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myauth;
    }

    @Override
    protected void main() {
        super.main();
        setTitle("身份认证");
        mAuth = findViewById(R.id.groupauth);
        mAuthed = findViewById(R.id.groupauthed);
        mTvName = findViewById(R.id.tv_name);
        mTvCard = findViewById(R.id.tv_card);
        mAccount = findViewById(R.id.account);
        mName = findViewById(R.id.name);
        payAccountName = findViewById(R.id.pay_account_name);//支付宝账号
        pay_account = findViewById(R.id.pay_account);
        findViewById(R.id.auth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUser == null)
                    return;
                if ("1".equals(mUser.real_order)) {//已支付认证费用，直接调认证接口
                    auth();
                    return;
                }
                if (!TextUtils.isEmpty(mOrderId)) {
                    auth();
                    return;
                }
                getOrderId();
            }
        });
    }

    private void initPage() {
        HttpUtil.getBaseInfo(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    mUser = JSON.parseObject(info[0], UserBean.class);
                    show();
                }
            }
        });
    }

    private void show() {
        if (mUser == null)
            return;
        if (TextUtils.isEmpty(mUser.id_card)) {
            mAuth.setVisibility(View.VISIBLE);
            mAuthed.setVisibility(View.GONE);
        } else {//已认证显示认证过的状态
            mAuthed.setVisibility(View.VISIBLE);
            mAuth.setVisibility(View.GONE);
            mTvCard.setText(mUser.id_card);
            mTvName.setText(mUser.getAlipay_name());
            payAccountName.setText(mUser.getAlipay_num());//
        }
        if (!TextUtils.isEmpty(mOrderId)) {
            auth();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPage();
    }

    private void getOrderId() {
        List<String> list = getList();
        if (list == null) {
            return;
        }
        HttpUtil.getMyOrder(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {//orderid
                    JSONObject obj = JSON.parseObject(info[0]);
                    mOrderId = obj.getString("orderid");
                    String pay_url = obj.getString("pay_url");
                    try {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(pay_url)));
                    } catch (ActivityNotFoundException localActivityNotFoundException) {
                        Toast.makeText(ZSFMyAuth.this, pay_url+" 。", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public List<String> getList() {
        String account = pay_account.getText().toString();
        if (TextUtils.isEmpty(account)) {
            pay_account.setError("请输入支付宝绑定的手机号");
            pay_account.requestFocus();
            return null;
        }

        if (!ValidatePhoneUtil.validateMobileNumber(account)) {
            pay_account.setError(getString(R.string.phone_num_error));
            pay_account.requestFocus();
            return null;
        }

        String name = mName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mName.setError("请输入真实姓名");
            mName.requestFocus();
            return null;
        }
        String id_card_number = mAccount.getText().toString();
        if (TextUtils.isEmpty(id_card_number) || id_card_number.length() < 18) {
            mAccount.setError("请输入18位身份证号");
            mAccount.requestFocus();
            return null;
        }

        if (!ToastUtil.checkAliPayInstalled(this)) {
            Toast.makeText(ZSFMyAuth.this, "请先安装支付宝app", Toast.LENGTH_SHORT).show();
            return null;
        }
        List<String> list = new ArrayList<>();
        list.add(account);
        list.add(id_card_number);
        list.add(name);
        return list;
    }

    private void auth() {
        if (mUser == null)
            return;
        List<String> list = getList();
        if (list == null) {
            return;
        }
        HttpUtil.getMyAuth(list.get(0), list.get(1), list.get(2), new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {//
                    String i = info[0];
                    if (TextUtils.isEmpty(i)) {
                        ToastUtil.show("认证链接为空，无法认证！");
                    } else {
                        try {
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(i)));
                        } catch (ActivityNotFoundException localActivityNotFoundException) {
                            Toast.makeText(ZSFMyAuth.this, i+" 。。", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
