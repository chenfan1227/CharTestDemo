package com.make.char_im.chenfan.activities;

import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;

import com.make.char_im.chenfan.R;
import com.make.char_im.chenfan.base.BaseActivity;

/**
 * 作者：Administrator on 2018/7/2 0002 10:22
 * 功能： 启动页
 * 作者：chenfan
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
        }, 3000);

    }
}
