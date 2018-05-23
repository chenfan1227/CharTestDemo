package com.ccy.lnb.en.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.ccy.lnb.en.R;
import com.ccy.lnb.en.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MoreFrag extends BaseFragment {

    @Bind(R.id.web_view_progress)
    ProgressBar webViewProgress;
    @Bind(R.id.web)
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
