package com.make.char_im.chenfan.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.make.char_im.chenfan.R;
import com.make.char_im.chenfan.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * chen
 * 更多
 */
public class MoreFrag extends BaseFragment {

    @BindView(R.id.web_view_progress)
    ProgressBar webViewProgress;
    @BindView(R.id.web)
    WebView web;

    @Override
    protected View loadLayout(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_more, null);
        pageBack(view);
        goneBack(view);
        setTopBarTitle(view, mContext.getString(R.string.my_more));
        return view;
    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    protected void setAllClick() {
    }

    @Override
    protected void process() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

}
