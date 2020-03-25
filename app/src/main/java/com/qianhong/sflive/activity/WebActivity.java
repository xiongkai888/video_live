package com.qianhong.sflive.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.qianhong.sflive.Constants;
import com.qianhong.sflive.R;
import com.qianhong.sflive.utils.L;

/**
 * Created by cxf on 2017/8/9.
 * 通用的WebView页面
 */

public class WebActivity extends AbsActivity {

    private ProgressBar mProgressBar;
    private WebView mWebView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected void main() {
        String url = getIntent().getStringExtra(Constants.URL);
        LinearLayout rootView = (LinearLayout) findViewById(R.id.rootView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mWebView = new WebView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(params);
        rootView.addView(mWebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setProgress(newProgress);
                }
            }
        });
        final String[] platformapiUrl = {""};
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                L.e("url:" + url);
                if (url.startsWith("alipays://platformapi/startapp")) {
                    platformapiUrl[0] = url;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
                //网页上点击了"继续支付"
                if (url.startsWith("https://mclient.alipay.com/h5Continue.htm?")) {
                    Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(platformapiUrl[0]));
                    startActivity(intent2);
                }
                return false;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                setTitle(view.getTitle());
            }
        });
        mWebView.loadUrl(url);
    }

//    @Override
//    public void onBackPressed() {
//        if (mWebView != null && mWebView.canGoBack()) {
//            mWebView.goBack();
//        } else {
//            super.onBackPressed();
//        }
//    }

}
