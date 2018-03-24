package com.ccy.lnb.en.activities;

import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;

import com.ccy.lnb.en.R;
import com.ccy.lnb.en.base.BaseActivity;

/**
 * 启动页
 * Created by MJie on 2017/4/20.
 */

public class LauncherActivity extends BaseActivity {
    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_launcher);
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

        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void run() {
               intentAc(MainActivity.class);
                finish();
            }
        }, 1000);

    }
}
