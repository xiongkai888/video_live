package com.video.liveshow.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.video.liveshow.Constants;
import com.video.liveshow.R;
import com.video.liveshow.adapter.VideoChooseAdapter;
import com.video.liveshow.bean.VideoChooseBean;
import com.video.liveshow.custom.ItemDecoration;
import com.video.liveshow.interfaces.CommonCallback;
import com.video.liveshow.interfaces.OnItemClickListener;
import com.video.liveshow.utils.VideoUtil;
import com.video.liveshow.utils.WordUtil;

import java.util.List;

/**
 * Created by cxf on 2018/6/20.
 */

public class VideoChooseActivity extends AbsActivity implements OnItemClickListener<VideoChooseBean> {

    private RecyclerView mRecyclerView;
    private VideoUtil mVideoUtil;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_choose;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.local_video));
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 1, 1);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRecyclerView.addItemDecoration(decoration);
        mVideoUtil = new VideoUtil();
        mVideoUtil.getLocalVideoList(new CommonCallback<List<VideoChooseBean>>() {
            @Override
            public void callback(List<VideoChooseBean> videoList) {
                VideoChooseAdapter adapter = new VideoChooseAdapter(mContext, videoList);
                adapter.setOnItemClickListener(VideoChooseActivity.this);
                mRecyclerView.setAdapter(adapter);
            }
        });
    }


    @Override
    public void onItemClick(VideoChooseBean bean, int position) {
        Intent intent = new Intent();
        intent.putExtra(Constants.VIDEO_PATH, bean.getVideoPath());
        intent.putExtra(Constants.VIDEO_DURATION, bean.getDuration());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        mVideoUtil.release();
        super.onDestroy();
    }

}
