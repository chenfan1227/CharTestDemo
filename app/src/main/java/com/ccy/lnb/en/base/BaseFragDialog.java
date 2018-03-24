package com.ccy.lnb.en.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ccy.lnb.en.AppContext;

import butterknife.ButterKnife;

/**
 * fragmentDialog的基类
 * Created by meng on 16/12/19.
 */

public abstract class BaseFragDialog extends DialogFragment {

    protected final String TAG = getClass().getSimpleName();

    //声明AppContext对象
    public AppContext mApp;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //创建AppContext对象
        mApp = (AppContext) getActivity().getApplication();
        View view = loadLayout(inflater);
        mContext= getActivity();
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


}
