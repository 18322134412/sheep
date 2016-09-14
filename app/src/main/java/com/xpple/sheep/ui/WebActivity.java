package com.xpple.sheep.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseActivity;
import com.xpple.sheep.view.ShareDialog;

public class WebActivity extends BaseActivity {
    private WebView mWebView;
    private String mUrl, mTitle;
    private RelativeLayout rl_webView, mErrorFrame;

    @Override
    public int bindLayout() {
        return R.layout.activity_webview;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView(View view) {
        setTintManager();
        rl_webView = (RelativeLayout) findViewById(R.id.rl_webView);
        mErrorFrame = (RelativeLayout) findViewById(R.id.errNetLayout);
        TextView network_load = (TextView) findViewById(R.id.network_load);
        network_load.setOnClickListener(v ->
                refresh());
        mWebView = new WebView(getApplicationContext());
        rl_webView.addView(mWebView);
        mUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        settings.setSavePassword(false);
        //设置webview不保存密码
        mWebView.setWebChromeClient(new ChromeClient());
        mWebView.setWebViewClient(new LoveClient());
        mWebView.loadUrl(mUrl != null ? mUrl : "http://www.neday.cn/404");
        if (mTitle != null) setTitle(mTitle);
    }


    /**
     * Using newIntent trick, return WebActivity Intent, to avoid `public static`
     * constant
     * variable everywhere
     *
     * @return Intent to start WebActivity
     */
    public static Intent newIntent(Context context, String extraURL, String extraTitle) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", extraURL);
        intent.putExtra("title", extraTitle);
        return intent;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        initTopBarForBoth(title.toString(), "关闭", "分享", () ->
                new ShareDialog(mContext).builder(mTitle, "", "", mUrl).show());
    }


    private void refresh() {
        mErrorFrame.setVisibility(View.GONE);
        mWebView.reload();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            rl_webView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }


    @Override
    public void onPause() {
        if (mWebView != null) mWebView.onPause();
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) mWebView.onResume();
    }


    private class ChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }
    }

    private class LoveClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if (url != null) view.loadUrl(url);
            return false;
        }
    }
}
