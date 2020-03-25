package com.video.liveshow.activity;

import android.content.Intent;
import android.view.View;

import com.video.liveshow.AppConfig;
import com.video.liveshow.Constants;
import com.video.liveshow.R;

public class ZSFHelpCenter extends AbsActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_help_center;
    }

    @Override
    protected void main() {
        super.main();
        setTitle("帮助中心");
    }

    public void settingClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.question:
                intent = new Intent(mContext, WebUploadImgActivity.class);
                String url = AppConfig.HOST + "/index.php?g=portal&m=page&a=questions";
                intent.putExtra(Constants.URL, url);
                startActivity(intent);
                break;
            case R.id.feedback:
                intent = new Intent(mContext, WebUploadImgActivity.class);
                AppConfig appConfig = AppConfig.getInstance();
                String feedbackurl = AppConfig.HOST + "/index.php?g=Appapi&m=feedback&a=index&uid=" + appConfig.getUid() + "&token=" + appConfig.getToken() + "&version=" + appConfig.getVersion();
                intent.putExtra(Constants.URL, feedbackurl);
                startActivity(intent);
                break;
        }
    }
}
