package com.ccy.lnb.en.activities;

import android.app.Activity;

import com.ccy.lnb.en.R;
import com.ccy.lnb.en.base.BaseActivity;

/**
 * Created by Administrator on 2018/4/2 0002.
 */

public class AboutActivity extends BaseActivity {
    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_about);
        pageBack();
        swipeBackLayout.setEnablePullToBack(false);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setAllClick() {

    }

    @Override
    protected void process() {

    }
}
