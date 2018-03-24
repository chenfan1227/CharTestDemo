package com.ccy.lnb.en.activities;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;


import com.ccy.lnb.en.R;
import com.ccy.lnb.en.adapters.PlaySmallAdapter;
import com.ccy.lnb.en.base.BaseActivity;
import com.ccy.lnb.en.utils.pullToRefresh.PullAbleRecyclerView;
import com.ccy.lnb.en.utils.pullToRefresh.PullToRefreshLayout;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by meng on 16/12/15.
 */

public class PlaySmallActivity extends BaseActivity implements PullToRefreshLayout.OnPullListener {

    @Bind(R.id.pull_refresh)
    PullToRefreshLayout mPullToRefreshLayout;
    @Bind(R.id.recycler)
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
