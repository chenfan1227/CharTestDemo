package com.ccy.lnb.en.activities;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ccy.lnb.en.R;
import com.ccy.lnb.en.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by Administrator on 2018/4/2 0002.
 */

public class MoreActivity extends BaseActivity {
    @Bind(R.id.web_view)
    WebView webView;
    @Bind(R.id.web_view_progress)
    ProgressBar webViewProgress;

    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_more);
        pageBack();
        ButterKnife.bind(this);
        swipeBackLayout.setEnablePullToBack(false);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void initViews() {
        WebSettings webSettings = webView.getSettings();
        webView.loadUrl("http://www.baidu.com");
        // 清除浏览器缓存
        webView.clearCache(true);
        // 屏幕自适应
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);//允许使用js
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //是否可以前进
        webView.canGoForward();
        //是否可以后退
        webView.canGoBack();
        //后退网页
        webView.goBack();
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {//监听网页加载
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                    webViewProgress.setVisibility(View.GONE);
                } else {
                    // 加载中
                    webViewProgress.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    setTopBarTitle(title);
                }
            }
        });


    }

    @Override
    protected void setAllClick() {

    }

    @Override
    protected void process() {

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
