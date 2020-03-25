package com.qianhong.sflive.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qianhong.sflive.AppConfig;
import com.qianhong.sflive.Constants;
import com.qianhong.sflive.R;
import com.qianhong.sflive.http.HttpCallback;
import com.qianhong.sflive.http.HttpUtil;
import com.qianhong.sflive.utils.MD5Util;
import com.qianhong.sflive.utils.ToastUtil;
import com.qianhong.sflive.utils.ValidateUitl;
import com.qianhong.sflive.utils.WordUtil;

public class ZSFSetSecondPwdActivity extends AbsActivity {

    EditText mPwd;
    private TextView mEditPhone;
    private EditText mEditCode;
    private TextView mBtnGetCode;
    private static final int TOTAL = 60;
    private int mCount = TOTAL;
    private Handler mHandler;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_secondpwd_layout;
    }

    @Override
    protected void main() {
        super.main();
        setTitle("修改二级密码");
        mPwd = findViewById(R.id.pwd);
        mEditPhone = (TextView) findViewById(R.id.edit_phone);
        mEditCode = (EditText) findViewById(R.id.edit_code);
        mBtnGetCode = (TextView) findViewById(R.id.btn_get_code);

        mEditPhone.setText(AppConfig.getInstance().getUserBean().getMobile());
//        mEditPhone.setFocusable(false);
//        mEditPhone.setFocusableInTouchMode(false);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mCount--;
                if (mCount > 0) {
                    mBtnGetCode.setText(mCount + "s");
                    mBtnGetCode.setTextColor(0xff646464);
                    if (mHandler != null) {
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                    }
                } else {
                    mBtnGetCode.setTextColor(0xffb4b4b4);
                    mBtnGetCode.setText(WordUtil.getString(R.string.get_valid_code_2));
                    mCount = TOTAL;
                    if (mBtnGetCode != null) {
                        mBtnGetCode.setEnabled(true);
                    }
                }
            }
        };

    }

    public void loginClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                login();
                break;
            case R.id.btn_get_code:
                getLoginCode();
                break;
        }
    }

    private void getLoginCode() {
        String mobile = mEditPhone.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtil.show("手机号为空，无法获取手机号");
            return;
        }
        if (!ValidateUitl.validateMobileNumber(mobile)) {
            ToastUtil.show(getString(R.string.phone_num_error));
            return;
        }
        mEditCode.requestFocus();
        String s = MD5Util.getMD5(mobile) + Constants.SIGN_1 + mobile + Constants.SIGN_2 + AppConfig.getInstance().getConfig().getDecryptSign() + Constants.SIGN_3;
        s = MD5Util.getMD5(s);
        HttpUtil.getLoginCode(mobile, s, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
                mBtnGetCode.setEnabled(false);
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(0);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        HttpUtil.cancel(HttpUtil.LOGIN);
        HttpUtil.cancel(HttpUtil.GET_LOGIN_CODE);
        super.onDestroy();
    }

    private void login() {
        String pwd = mPwd.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.show("请输入二级密码");
            return;
        }

        String mobile = mEditPhone.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtil.show("手机号为空，无法获取手机号");
            return;
        }
        if (!ValidateUitl.validateMobileNumber(mobile)) {
            ToastUtil.show(getString(R.string.phone_num_error));
            return;
        }
        String code = mEditCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            mEditCode.setError(WordUtil.getString(R.string.please_input_code));
            mEditCode.requestFocus();
            return;
        }
        String s = MD5Util.getMD5(code) + Constants.SIGN_1 + mobile + Constants.SIGN_2 + AppConfig.getInstance().getConfig().getDecryptSign() + Constants.SIGN_3;
        s = MD5Util.getMD5(s);
        HttpUtil.setSecondPwd(mobile, code, s, pwd, "", "two", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                onLoginSuccess(code, msg, info);
            }
        });
    }

    private void onLoginSuccess(int code, String msg, String[] info) {
        ToastUtil.show(msg);
        if (code == 0)
            finish();
    }

}
