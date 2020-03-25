package com.qianhong.sflive.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qianhong.sflive.AppConfig;
import com.qianhong.sflive.R;
import com.qianhong.sflive.bean.TradeCenterBean;
import com.qianhong.sflive.fragment.ChooseImgFragment;
import com.qianhong.sflive.glide.ImgLoader;
import com.qianhong.sflive.http.HttpCallback;
import com.qianhong.sflive.http.HttpUtil;
import com.qianhong.sflive.interfaces.CommonCallback;
import com.qianhong.sflive.utils.DialogUitl;
import com.qianhong.sflive.utils.ToastUtil;

import java.io.File;

public class ZSFRecordDetailActivity extends AbsActivity implements View.OnClickListener {

    private String mTag;
    private String mType = "";
    private File mFile;
    TradeCenterBean.TradeInfo mInfo;
    private ChooseImgFragment mFragment;
    TextView mMoney, mPrice, mCount, mOrderNo, mOrderTime, mAliName, mAliNum;
    View mUpload;
    ImageView mImage;
    EditText mPassword;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_detail;
    }

    @Override
    protected void main() {
        mTag = this.toString();
        mInfo = (TradeCenterBean.TradeInfo) getIntent().getSerializableExtra("order");
//        type = getIntent().getStringExtra("type");
        mMoney = findViewById(R.id.money);
        mPrice = findViewById(R.id.price);
        mCount = findViewById(R.id.count);
        mOrderNo = findViewById(R.id.orderno);
        mOrderTime = findViewById(R.id.ordertime);
        mAliName = findViewById(R.id.aliname);
        mAliNum = findViewById(R.id.alinumber);
        mUpload = findViewById(R.id.upload);
        mImage = findViewById(R.id.image);
        mPassword = findViewById(R.id.password);
        setTitle("交易详情");
        if (mInfo == null)
            return;
        if (mInfo.gid.equals(AppConfig.getInstance().getUid())) {//卖家
            mType = ZSFTradeRecordActivity.TYPE_SALE;
            mUpload.setVisibility(View.GONE);
        } else if (mInfo.uid.equals(AppConfig.getInstance().getUid())) {//买家
            mType = ZSFTradeRecordActivity.TYPE_BUY;
            mUpload.setVisibility(View.VISIBLE);
            mUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editAvatar();
                }
            });
        }
        findViewById(R.id.commit).setOnClickListener(this);
        findViewById(R.id.copy_order_tv).setOnClickListener(this);
        findViewById(R.id.copy_pay_tv).setOnClickListener(this);
        findViewById(R.id.appeal).setOnClickListener(this);
        mMoney.setText("￥" + mInfo.totalprice);
        mPrice.setText("￥" + mInfo.price);
        mCount.setText(mInfo.number + "硕果");
        mOrderNo.setText(mInfo.ordernum);
        mOrderTime.setText(mInfo.addtime);
        mAliName.setText(mInfo.gid_name);
        mAliNum.setText(mInfo.gid_alipay_num);
        if (!TextUtils.isEmpty(mInfo.img_url)) {
            ImgLoader.display(mInfo.img_url, mImage);
        }
        mFragment = new ChooseImgFragment();
        mFragment.setOnCompleted(new CommonCallback<File>() {
            @Override
            public void callback(final File file) {
                mFile = file;
                if (mFile != null)
                    ImgLoader.display(mFile, mImage);
            }
        });
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(mFragment, "ChooseImgFragment").commit();
    }

    private void editAvatar() {
        DialogUitl.showStringArrayDialog(mContext, new String[]{"相机", "相册"}, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int position) {
                if (mFragment != null) {
                    if (position == 0) {
                        mFragment.forwardCamera();
                    } else {
                        mFragment.forwardAlumb();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.copy_order_tv://复制订单号
                copy(mInfo.ordernum);
                break;
            case R.id.copy_pay_tv://复制支付宝账号
                copy(mInfo.gid_alipay_num);
                break;
            case R.id.commit:
                confirm();
                break;
            case R.id.appeal:
                appeal();
                break;
        }
    }

    private void copy(String text) {
        if (TextUtils.isEmpty(text)){
            ToastUtil.show("复制的内容不能为空");
            return;
        }
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
// 将文本内容放到系统剪贴板里。
        cm.setPrimaryClip(ClipData.newPlainText("text", text));//text也可以是"null"
        ToastUtil.show("复制成功");
    }

    private void confirm() {
        if (mInfo == null)
            return;
        if (mType.equals(ZSFTradeRecordActivity.TYPE_BUY)) {
            if (mFile == null) {
                ToastUtil.show("请上传支付凭证");
                return;
            }
            String pwd = mPassword.getText().toString();
            if (TextUtils.isEmpty(pwd)) {
                ToastUtil.show("请输入交易密码");
                return;
            }
            HttpUtil.confirmTradeBuy(mTag, mInfo.id, "confirm", mFile, pwd, new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {

                    if (code == 0) {
                        ToastUtil.show("发送成功");
                        finish();
                    } else {
                        ToastUtil.show(msg);
                    }

                }
            });
        } else if (mType.equals(ZSFTradeRecordActivity.TYPE_SALE)) {
            String pwd = mPassword.getText().toString();
            if (TextUtils.isEmpty(pwd)) {
                ToastUtil.show("请输入交易密码");
                return;
            }
            HttpUtil.confirmTradeSale(mTag, mInfo.id, pwd, new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {

                    if (code == 0) {
                        ToastUtil.show("确认成功");
                        finish();
                    } else {
                        ToastUtil.show(msg);
                    }
                }
            });
        }
    }

    private void appeal() {
//        if (mInfo == null)
//            return;
//        HttpUtil.confirmTradeBuy(mTag, mInfo.id, "appeal", "","", new HttpCallback() {
//            @Override
//            public void onSuccess(int code, String msg, String[] mInfo) {
//                if (code == 0) {
//                    ToastUtil.show("申诉成功");
//                    finish();
//                } else {
//                    ToastUtil.show(msg);
//                }
//
//            }
//        });
        DialogUitl.showSimpleDialog(this, "", "请联系客服", new DialogUitl.SimpleDialogCallback() {
            @Override
            public void onComfirmClick() {

            }
        });
    }
}
