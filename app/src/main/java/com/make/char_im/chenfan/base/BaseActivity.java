package com.make.char_im.chenfan.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.make.char_im.chenfan.AppContext;
import com.make.char_im.chenfan.R;
import com.make.char_im.chenfan.dialogs.LoadingDialog;
import com.make.char_im.chenfan.utils.ActivityTool;

import butterknife.ButterKnife;


/**
 * 自定义基类AppCompatActivity
 * Created by chen on 2015/12/7.
 */
public abstract class BaseActivity extends SwipeBackActivity implements View.OnClickListener {

    public LoadingDialog mLoadingDialog;//加载dialog

    protected final String TAG = getClass().getSimpleName();

    //声明AppContext对象
    public AppContext mApp;
    protected Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建AppContext对象
        mApp = (AppContext) getApplication();
        mContext = this;
        loadLayout();
        initViews();
        setAllClick();
        process();
    }


    /**
     * 加载布局
     */
    protected abstract void loadLayout();

    /**
     * 获取所有组件
     */
    protected abstract void initViews();

    /**
     * 设置所有监听
     */
    protected abstract void setAllClick();

    /**
     * 业务逻辑过程
     */
    protected abstract void process();


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }


    /**
     * 加载条
     */
    public void loading() {
        try {
            if (mLoadingDialog == null) {
                mLoadingDialog = new LoadingDialog(mContext, R.style.dialog_loading);
            }
            mLoadingDialog.show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 关闭加载条
     */
    public void cancel() {
        try {
            if (mLoadingDialog != null && mLoadingDialog.isShowing())
                mLoadingDialog.dismiss();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //显示没有数据的页面
    public void showEmptyView() {
        ActivityTool.showEmpty(mContext, R.layout.no_data);
    }

    //显示没有数据的页面并包括有点击事情
    public View showEmptyViewOnClick() {
        return ActivityTool.showEmpty(mContext, R.layout.no_data);
    }

    //显示加载失败界面
    public View showFailViewOnClick() {
        return ActivityTool.showEmpty(mContext, R.layout.loading_fail);
    }


    /**
     * 点击返回按钮返回
     */
    public void pageBack() {
        ImageView imgBack = (ImageView) ((Activity) mContext).findViewById(R.id.top_bar_img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).finish();
            }
        });
    }

    /**
     * 设置topBar的标题
     *
     * @param title --标题
     */
    public void setTopBarTitle(String title) {
        TextView tvTitle = (TextView) ((Activity) mContext).findViewById(R.id.top_bar_tv_title);
        tvTitle.setText(title);
    }

    //一般不带参数的跳转activity
    public void intentAc(Class<?> cls) {
        Intent intent = new Intent(mContext, cls);
        startActivity(intent);
    }

}
