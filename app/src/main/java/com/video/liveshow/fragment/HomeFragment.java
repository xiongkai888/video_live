package com.video.liveshow.fragment;

import android.graphics.Typeface;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.video.liveshow.R;
import com.video.liveshow.activity.MainActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by cxf on 2018/6/5.
 * 首页
 */

public class HomeFragment extends AbsFragment implements View.OnClickListener {

    private static final int RECOMMEND = 0;
    private static final int HOT = 1;
    private static final int NEAR = 2;
    private HomeRecommendFragment mRecommendFragment;
    private HomeHotFragment mHotFragment;
    private HomeNearFragment mNearFragment;
    private SparseArray<Fragment> mSparseArray;
    private int mCurKey;//当前选中的fragment的key
    private FragmentManager mFragmentManager;
    private RadioButton mBtnRecommend;
    private RadioButton mBtnHot;
    private RadioButton mBtnNear;
    private SparseArray<RadioButton> mButtonSparseArray;
    private RadioGroup radioGroup;
    private boolean isHot = false;//防止第一次点击请求两次
    private boolean isNear = false;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void main() {
        mCurKey = RECOMMEND;
        mRecommendFragment = new HomeRecommendFragment();
        mHotFragment = new HomeHotFragment();
        mNearFragment = new HomeNearFragment();
        mSparseArray = new SparseArray<>();
        mSparseArray.put(RECOMMEND, mRecommendFragment);
        mSparseArray.put(HOT, mHotFragment);
        mSparseArray.put(NEAR, mNearFragment);
        mFragmentManager = getChildFragmentManager();
        FragmentTransaction tx = mFragmentManager.beginTransaction();
        for (int i = 0, size = mSparseArray.size(); i < size; i++) {
            Fragment fragment = mSparseArray.valueAt(i);
            tx.add(R.id.replaced, fragment);
            if (mSparseArray.keyAt(i) == mCurKey) {
                tx.show(fragment);
            } else {
                tx.hide(fragment);
            }
        }
        tx.commit();

        mBtnRecommend = (RadioButton) mRootView.findViewById(R.id.btn_recommend);
        mBtnHot = (RadioButton) mRootView.findViewById(R.id.btn_hot);
        mBtnNear = (RadioButton) mRootView.findViewById(R.id.btn_near);
        radioGroup = (RadioGroup) mRootView.findViewById(R.id.radioGroup);
        mButtonSparseArray = new SparseArray<>();
        mButtonSparseArray.put(RECOMMEND, mBtnRecommend);
        mButtonSparseArray.put(HOT, mBtnHot);
        mButtonSparseArray.put(NEAR, mBtnNear);
        for (int i = 0, size = mButtonSparseArray.size(); i < size; i++) {
            RadioButton button = mButtonSparseArray.valueAt(i);
            button.setOnClickListener(this);
            if (mButtonSparseArray.keyAt(i) == mCurKey) {
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            } else {
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                button.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            }
        }
    }

    public void refreshHome(){
        radioGroup.check(R.id.btn_recommend);
        toggleRecommend();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recommend:
                toggleRecommend();
                break;
            case R.id.btn_hot:
                toggleHot();
                isHot = true;
                break;
            case R.id.btn_near:
                toggleNear();
                isNear = true;
                break;
        }
    }


    private void toggleRecommend() {
        mRecommendFragment.init();
        toggle(RECOMMEND);
        if (mRecommendFragment != null) {
            mRecommendFragment.hiddenChanged(false);
        }
        ((MainActivity) mContext).setCanScroll(true);
    }

    private void toggleHot() {
        if (isHot){
            mHotFragment.init();
        }
        toggle(HOT);
        if (mRecommendFragment != null) {
            mRecommendFragment.hiddenChanged(true);
        }
        ((MainActivity) mContext).setCanScroll(false);
    }

    private void toggleNear() {
        if (isNear){
            mNearFragment.init();
        }
        toggle(NEAR);
        if (mRecommendFragment != null) {
            mRecommendFragment.hiddenChanged(true);
        }
        ((MainActivity) mContext).setCanScroll(false);
    }


    private void toggle(int key) {
        if (key == mCurKey) {
            return;
        }
        mCurKey = key;
        FragmentTransaction tx = mFragmentManager.beginTransaction();
        for (int i = 0, size = mSparseArray.size(); i < size; i++) {
            Fragment fragment = mSparseArray.valueAt(i);
            if (mSparseArray.keyAt(i) == mCurKey) {
                tx.show(fragment);
            } else {
                tx.hide(fragment);
            }
        }
        tx.commit();
        for (int i = 0, size = mButtonSparseArray.size(); i < size; i++) {
            RadioButton button = mButtonSparseArray.valueAt(i);
            if (mButtonSparseArray.keyAt(i) == mCurKey) {
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            } else {
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                button.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hiddenChanged(hidden);
    }

    public void hiddenChanged(boolean hidden) {
        if (mCurKey == RECOMMEND && mRecommendFragment != null) {
            mRecommendFragment.hiddenChanged(hidden);
        }
    }

    public boolean isRecommend(){
        return mCurKey==RECOMMEND;
    }
}
