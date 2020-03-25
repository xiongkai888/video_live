package com.video.liveshow.activity;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.video.liveshow.AppConfig;
import com.video.liveshow.Constants;
import com.video.liveshow.R;
import com.video.liveshow.http.HttpCallback;
import com.video.liveshow.http.HttpUtil;
import com.video.liveshow.utils.DialogUitl;
import com.video.liveshow.utils.MD5Util;
import com.video.liveshow.utils.ToastUtil;
import com.video.liveshow.utils.ValidatePhoneUtil;
import com.video.liveshow.utils.ValidateUitl;
import com.video.liveshow.utils.WordUtil;

/**
 * Created by cxf on 2018/9/25.
 */

public class ZSFRegisterActivity extends AbsActivity {

    public static final int TYPE_REGIST = 1;
    public static final int TYPE_FORGET = 2;
    public static final int TYPE_SET = 3;

    private EditText mEditPhone;
    private EditText mEditCode;
    private EditText mEditPwd1;
    private EditText mEditPwd2;
    private EditText mEditInvite;
    private TextView mBtnCode;
    private TextView mBtnRegister;
    private Handler mHandler;
    private static final int TOTAL = 60;
    private int mCount = TOTAL;
    private String mGetCode;
    private String mGetCodeAgain;
    private Dialog mDialog;
    private boolean mFirstLogin;//是否是第一次登录
    private boolean mShowInvite;

    private int mType = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }


    @Override
    protected void main() {
        mGetCode = "获取验证码";
        mGetCodeAgain = "重新获取";
        mBtnCode = (TextView) findViewById(R.id.btn_code);
        mBtnRegister = findViewById(R.id.btn_register);
        mEditPhone = (EditText) findViewById(R.id.edit_phone);
        mEditCode = (EditText) findViewById(R.id.edit_code);
        mEditPwd1 = (EditText) findViewById(R.id.edit_pwd_1);
        mEditPwd2 = (EditText) findViewById(R.id.edit_pwd_2);
        mEditInvite = (EditText) findViewById(R.id.edit_invite);

        mType = getIntent().getIntExtra("type", 0);
        switch (mType) {
            case TYPE_REGIST:
                setTitle("注册");
                mEditInvite.setVisibility(View.VISIBLE);
                mBtnRegister.setText("立即注册");
                break;
            case TYPE_FORGET:
                setTitle("忘记密码");
                mEditInvite.setVisibility(View.GONE);
                mBtnRegister.setText("确定");
                break;
            case TYPE_SET:
                setTitle("修改密码");
                mEditInvite.setVisibility(View.GONE);
                mBtnRegister.setText("确定");
                mEditPhone.setText(AppConfig.getInstance().getUserBean().getMobile());
                mEditPhone.setFocusable(false);
                mEditPhone.setFocusableInTouchMode(false);
//                mEditPhone.
                break;
        }
        mEditPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s) && s.length() == 11) {
                    mBtnCode.setEnabled(true);
                } else {
                    mBtnCode.setEnabled(false);
                }
                changeEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mEditCode.addTextChangedListener(textWatcher);
        mEditPwd1.addTextChangedListener(textWatcher);
        mEditPwd2.addTextChangedListener(textWatcher);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mCount--;
                if (mCount > 0) {
                    mBtnCode.setText(mGetCodeAgain + "(" + mCount + "s)");
                    if (mHandler != null) {
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                    }
                } else {
                    mBtnCode.setText(mGetCode);
                    mCount = TOTAL;
                    if (mBtnCode != null) {
                        mBtnCode.setEnabled(true);
                    }
                }
            }
        };
        mDialog = DialogUitl.loadingDialog(mContext, "请稍候...");
    }

    private void changeEnable() {
        String phone = mEditPhone.getText().toString();
        String code = mEditCode.getText().toString();
        String pwd1 = mEditPwd1.getText().toString();
        String pwd2 = mEditPwd2.getText().toString();
        mBtnRegister.setEnabled(!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(code) && !TextUtils.isEmpty(pwd1) && !TextUtils.isEmpty(pwd2));
    }

    public void loginClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_code) {
            getCode();
        } else if (i == R.id.btn_register) {
            switch (mType) {
                case TYPE_REGIST://注册
                    register();
                    break;
                case TYPE_FORGET://忘记密码
//                    findPwd();
//                    break;
                case TYPE_SET://修改密码
                    findPwd();
                    break;
            }
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        String mobile = mEditPhone.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            mEditPhone.setError(WordUtil.getString(R.string.please_input_mobile));
            mEditPhone.requestFocus();
            return;
        }
        if (!ValidateUitl.validateMobileNumber(mobile)) {
            mEditPhone.setError(getString(R.string.phone_num_error));
            mEditPhone.requestFocus();
            return;
        }
        mEditCode.requestFocus();
        String s = MD5Util.getMD5(mobile) + Constants.SIGN_1 + mobile + Constants.SIGN_2 + AppConfig.getInstance().getConfig().getDecryptSign() + Constants.SIGN_3;
        s = MD5Util.getMD5(s);
        HttpUtil.getLoginCode(mobile, s, mGetCodeCallback);
    }

    private HttpCallback mGetCodeCallback = new HttpCallback() {
        @Override
        public void onSuccess(int code, String msg, String[] info) {
            mBtnCode.setEnabled(false);
            if (mHandler != null) {
                mHandler.sendEmptyMessage(0);
            }
            if (!TextUtils.isEmpty(msg) && msg.contains("123456")) {
                ToastUtil.show(msg);
            }
        }
    };

    /**
     * 注册
     */
    private void register() {
        final String phoneNum = mEditPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            mEditPhone.setError(WordUtil.getString(R.string.please_input_mobile));
            mEditPhone.requestFocus();
            return;
        }
        if (!ValidatePhoneUtil.validateMobileNumber(phoneNum)) {
            mEditPhone.setError(WordUtil.getString(R.string.phone_num_error));
            mEditPhone.requestFocus();
            return;
        }
        String code = mEditCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            mEditCode.setError(WordUtil.getString(R.string.please_input_code));
            mEditCode.requestFocus();
            return;
        }
        final String pwd = mEditPwd1.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            mEditPwd1.setError("请输入密码");
            mEditPwd1.requestFocus();
            return;
        }
        String pwd2 = mEditPwd2.getText().toString().trim();
        if (TextUtils.isEmpty(pwd2)) {
            mEditPwd2.setError("请确认密码");
            mEditPwd2.requestFocus();
            return;
        }
        if (!pwd.equals(pwd2)) {
            mEditPwd2.setError("两次密码输入不一致");
            mEditPwd2.requestFocus();
            return;
        }
        String invite = mEditInvite.getText().toString().trim();
        if (TextUtils.isEmpty(invite)) {
            mEditInvite.setError("请输入邀请码");
            mEditInvite.requestFocus();
            return;
        }
        if (mDialog != null) {
            mDialog.show();
        }

        HttpUtil.register(phoneNum, MD5Util.getMD5(pwd), MD5Util.getMD5(pwd2), code, invite, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                if (code == 0) {
                    //注册完回去登录
                    ToastUtil.show("注册成功，请登录");
                    finish();
                } else {
                    ToastUtil.show(msg);
                }
            }

            @Override
            public void onError() {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            }
        });
    }

    /**
     * 忘记密码
     */
    private void findPwd() {
        final String phoneNum = mEditPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            mEditPhone.setError(WordUtil.getString(R.string.please_input_mobile));
            mEditPhone.requestFocus();
            return;
        }
        if (!ValidatePhoneUtil.validateMobileNumber(phoneNum)) {
            mEditPhone.setError(WordUtil.getString(R.string.phone_num_error));
            mEditPhone.requestFocus();
            return;
        }
        String code = mEditCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            mEditCode.setError(WordUtil.getString(R.string.please_input_code));
            mEditCode.requestFocus();
            return;
        }
        final String pwd = mEditPwd1.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            mEditPwd1.setError("请输入密码");
            mEditPwd1.requestFocus();
            return;
        }
        String pwd2 = mEditPwd2.getText().toString().trim();
        if (TextUtils.isEmpty(pwd2)) {
            mEditPwd2.setError("请确认密码");
            mEditPwd2.requestFocus();
            return;
        }
        if (!pwd.equals(pwd2)) {
            mEditPwd2.setError("两次密码输入不一致");
            mEditPwd2.requestFocus();
            return;
        }
        if (mDialog != null) {
            mDialog.show();
        }

        HttpUtil.findPWD(phoneNum, MD5Util.getMD5(pwd), MD5Util.getMD5(pwd2), code, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                if (code == 0) {
                    //忘记密码或重置密码
                    if (mType == TYPE_FORGET) {
                        ToastUtil.show("重置密码成功，请登录");
                        finish();
                    } else if (mType == TYPE_SET) {
                        ToastUtil.show("重置密码成功");
                    }
                } else {
                    ToastUtil.show(msg);
                }
            }

            @Override
            public void onError() {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        HttpUtil.cancel(HttpUtil.GET_LOGIN_CODE);
        HttpUtil.cancel(HttpUtil.LOGIN);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDestroy();
    }
}
