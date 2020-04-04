package com.video.liveshow.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.video.liveshow.Constants;
import com.video.liveshow.R;
import com.video.liveshow.fragment.UserFragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by cxf on 2018/6/30.
 * 别人的个人中心页面
 */

public class OtherUserActivity extends AbsActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_other_user;
    }

    @Override
    protected void main() {
        UserFragment userFragment = new UserFragment();
        Bundle bundle = getIntent().getExtras();
        bundle.putBoolean(Constants.IS_MAIN_USER_CENTER, false);
        userFragment.setArguments(bundle);
        userFragment.setOnBackClickListener(new UserFragment.OnBackClickListener() {
            @Override
            public void onBackClick() {
                OtherUserActivity.this.onBackPressed();
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.replace(R.id.replaced, userFragment).commit();
    }

    public static void forwardOtherUser(Context context, String touid) {
        Intent intent = new Intent(context, OtherUserActivity.class);
        intent.putExtra(Constants.UID, touid);
        context.startActivity(intent);
    }
}
