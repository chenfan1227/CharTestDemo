package com.make.char_im.chenfan.activities;


import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.make.char_im.chenfan.R;
import com.make.char_im.chenfan.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * 作者：Administrator on 2018/7/2 0002 10:22
 * 功能： 更多
 * 作者：chenfan
 */

public class MoreActivity extends BaseActivity {
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.web_view_progress)
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
        setWebView();
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

    private void setWebView() {
        WebSettings webSettings = webView.getSettings();
        webView.loadUrl("http://www.baidu.com");
        // 屏幕自适应
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);//允许使用js
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
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
                loading();
                if (newProgress == 100) {
                    // 网页加载完成
                    webViewProgress.setVisibility(View.GONE);
                    cancel();
                } else {
                    // 加载中
                    webViewProgress.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
                cancel();
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


}
