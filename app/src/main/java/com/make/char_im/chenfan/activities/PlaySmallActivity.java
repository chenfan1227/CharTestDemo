package com.make.char_im.chenfan.activities;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;


import com.make.char_im.chenfan.R;
import com.make.char_im.chenfan.adapters.PlaySmallAdapter;
import com.make.char_im.chenfan.base.BaseActivity;
import com.make.char_im.chenfan.utils.pullToRefresh.PullAbleRecyclerView;
import com.make.char_im.chenfan.utils.pullToRefresh.PullToRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * Created by meng on 16/12/15.
 */

public class PlaySmallActivity extends BaseActivity implements PullToRefreshLayout.OnPullListener {

    @BindView(R.id.pull_refresh)
    PullToRefreshLayout mPullToRefreshLayout;
    @BindView(R.id.recycler)
    PullAbleRecyclerView mRecycler;

    private PlaySmallAdapter mAdapter;
    private Context mContext;

    @Override
    protected void process() {

    }

    @Override
    protected void setAllClick() {
        mPullToRefreshLayout.setOnPullListener(this);

    }

    @Override
    protected void initViews() {
        mContext = this;
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(manager);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("");
        }
        mAdapter = new PlaySmallAdapter(mContext, list);
        mRecycler.setAdapter(mAdapter);

    }

    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_play_small);

    }

    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        }, 1000);
    }

    @Override
    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }, 1000);
    }

    @Override
    public void onClick(View view) {

    }
}
