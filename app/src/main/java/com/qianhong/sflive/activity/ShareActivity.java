package com.qianhong.sflive.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.qianhong.sflive.AppConfig;
import com.qianhong.sflive.R;
import com.qianhong.sflive.adapter.VideoShareAdapter;
import com.qianhong.sflive.bean.ConfigBean;
import com.qianhong.sflive.bean.ShareBean;
import com.qianhong.sflive.bean.UserBean;
import com.qianhong.sflive.http.HttpUtil;
import com.qianhong.sflive.interfaces.CommonCallback;
import com.qianhong.sflive.interfaces.OnItemClickListener;
import com.qianhong.sflive.utils.SharedSdkUitl;

import java.util.List;

import cn.sharesdk.framework.Platform;

/**
 * Created by cxf on 2018/8/8.
 */

public class ShareActivity extends AbsActivity implements OnItemClickListener<ShareBean> {

    private RecyclerView mRecyclerView;
    private TextView mTip;
    private SharedSdkUitl mSharedSdkUitl;
    private ConfigBean mConfigBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share;
    }

    @Override
    protected void main() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mSharedSdkUitl = new SharedSdkUitl();
        mTip = (TextView) findViewById(R.id.tip);
        HttpUtil.getConfig(this,new CommonCallback<ConfigBean>() {
            @Override
            public void callback(ConfigBean configBean) {
                mConfigBean = configBean;
                List<ShareBean> list = mSharedSdkUitl.getShareList(configBean.getShare_type());
                if (list != null && list.size() > 0) {
                    VideoShareAdapter adapter = new VideoShareAdapter(mContext, list);
                    adapter.setOnItemClickListener(ShareActivity.this);
                    mRecyclerView.setAdapter(adapter);
                }
                mTip.setText("邀请注册并下载即可领取" + configBean.getInvite_tacket() + "QKL数诚股份");
            }
        });
    }

    @Override
    public void onItemClick(ShareBean bean, int position) {
        if (mConfigBean != null) {
            UserBean u = AppConfig.getInstance().getUserBean();
            String avatar = u.getAvatar_thumb();
            if (TextUtils.isEmpty(avatar) || avatar.endsWith("api.hongyuqkl.com/default_thumb.jpg")) {
                avatar = "http://shanghailidsp.yunbaozhibo.com/default_thumb.jpg";
            }
            mSharedSdkUitl.share(
                    bean.getType(),//分享的类型
                    mConfigBean.getVideo_share_title(),//分享的标题
                    u.getUser_nicename() + mConfigBean.getVideo_share_des(),//分享的话术
                    avatar,//图片
                    AppConfig.HOST + "/index.php?g=Appapi&m=Sharelogin&a=index&uid=" + u.getId(),//链接
                    new SharedSdkUitl.ShareListener() {
                        @Override
                        public void onSuccess(Platform platform) {
                            //ToastUtil.show(WordUtil.getString(R.string.share_success));
                        }

                        @Override
                        public void onError(Platform platform) {
                            // ToastUtil.show(WordUtil.getString(R.string.share_fail));
                        }

                        @Override
                        public void onCancel(Platform platform) {
                            //ToastUtil.show(WordUtil.getString(R.string.share_cancel));
                        }

                        @Override
                        public void onShareFinish() {

                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        if (mSharedSdkUitl != null) {
            mSharedSdkUitl.cancelListener();
        }
        super.onDestroy();
    }
}
