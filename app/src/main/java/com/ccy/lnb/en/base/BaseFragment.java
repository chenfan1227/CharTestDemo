package com.ccy.lnb.en.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccy.lnb.en.AppContext;
import com.ccy.lnb.en.R;
import com.ccy.lnb.en.dialogs.LoadingDialog;
import com.ccy.lnb.en.utils.ActivityTool;

import butterknife.ButterKnife;


/**
 * 自定义基类Fragment
 * Created by meng on 2015/12/8.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener{
    protected final String TAG = getClass().getSimpleName();

    //声明AppContext对象
    public AppContext mApp;

    public LoadingDialog mLoadingDialog;//加载dialog
    protected Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //创建AppContext对象
        mApp = (AppContext) getActivity().getApplication();
        mContext= getActivity();
        View view = loadLayout(inflater);
        ButterKnife.bind(this, view);
        initViews(view);
        setAllClick();
        process();
        return view;
    }


    /**
     *加载布局
     * @param inflater
     * @return
     */
    protected abstract View loadLayout(LayoutInflater inflater);

    /**
     *获取所有组件
     * @param view
     */
    protected abstract void initViews(View view);

    /**
     *设置所有监听
     */
    protected abstract void setAllClick();

    /**
     *业务逻辑过程
     */
    protected abstract void process();


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
    public void pageBack(View view){
        ImageView imgBack = (ImageView) view.findViewById(R.id.top_bar_img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)mContext).finish();
            }
        });
    }

    /**
     * 设置topBar的标题
     * @param title --标题
     */
    public void setTopBarTitle(View view,String title){
        TextView tvTitle = (TextView) view.findViewById(R.id.top_bar_tv_title);
        tvTitle.setText(title);
    }

    /**
     * 隐藏返回键
     */
    public void goneBack(View view){
        ImageView imgBack = (ImageView) view.findViewById(R.id.top_bar_img_back);
        imgBack.setVisibility(View.GONE);
    }

    //跳转activity
    public void intentAc(Class<?> cls) {
        Intent intent = new Intent(mContext, cls);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
