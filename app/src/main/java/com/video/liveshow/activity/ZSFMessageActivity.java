package com.video.liveshow.activity;

import android.support.v4.app.FragmentTransaction;

import com.video.liveshow.R;
import com.video.liveshow.fragment.MessageFragment;

public class ZSFMessageActivity extends AbsActivity {

    MessageFragment mMessageFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.container_layout;
    }

    @Override
    protected void main() {
        super.main();
        mMessageFragment = new MessageFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, mMessageFragment);
        transaction.commit();
    }
}
