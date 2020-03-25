package com.qianhong.sflive.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qianhong.sflive.Constants;
import com.qianhong.sflive.R;
import com.qianhong.sflive.adapter.MusicChooseAdapter;
import com.qianhong.sflive.bean.MusicChooseBean;
import com.qianhong.sflive.interfaces.CommonCallback;
import com.qianhong.sflive.interfaces.OnItemClickListener;
import com.qianhong.sflive.utils.MusicScanUtil;
import com.qianhong.sflive.utils.WordUtil;

import java.util.List;

/**
 * Created by cxf on 2018/8/6.
 */

public class ChooseMusicActivity extends AbsActivity implements OnItemClickListener<MusicChooseBean> {

    private MusicScanUtil mMusicScanUtil;
    private RecyclerView mRecyclerView;
    private MusicChooseAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_music;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.local_music));
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mMusicScanUtil = new MusicScanUtil();
        mMusicScanUtil.getLocalMusicList(new CommonCallback<List<MusicChooseBean>>() {
            @Override
            public void callback(List<MusicChooseBean> list) {
                if (list.size() > 0) {
                    mAdapter = new MusicChooseAdapter(mContext, list);
                    mAdapter.setOnItemClickListener(ChooseMusicActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    View noData = findViewById(R.id.no_data);
                    noData.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public void onItemClick(MusicChooseBean bean, int position) {
        Intent intent = new Intent(mContext, EditMusicActivity.class);
        intent.putExtra(Constants.MUSIC_PATH, bean.getPath());
        intent.putExtra(Constants.MUSIC_TITLE, bean.getTitle());
        intent.putExtra(Constants.MUSIC_DURATION, bean.getDuration());
        startActivity(intent);
        finish();
    }
}
