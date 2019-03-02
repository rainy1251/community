package com.service.community.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.service.community.R;
import com.service.community.ui.base.BaseActivity;

import butterknife.BindView;

public class AdActivity extends BaseActivity {

    @BindView(R.id.mwebview)
    WebView mWebview;

    @Override
    public int getLayoutId() {
        return R.layout.activity_find;
    }

    @Override
    public void initView() {
        setToolbarNoEN(R.id.toolbar,"精选");
        WebSettings webSettings = mWebview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置 缓存模式
        mWebview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        mWebview.getSettings().setDomStorageEnabled(true);

    }

    @Override
    public void initData() {
        mWebview.loadUrl("http://vue.zhangxiaofu.cn/webview/productlist.html");
        mWebview.setWebViewClient(new MyWebViewClient());
    }

    @Override
    protected void initListener() {

    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

//            if (url.contains("https://a.app.qq.com/o/simple.jsp?pkgname=com.aiyaopai.yaopai")) {
//                Intent intent = new Intent(getActivity(), Ariticle_EditTitle_Activity.class);
//                startActivity(intent);
//                yaopaiWebView.goBack();
//            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            //  yaopaiWebView.loadUrl("www.baidu.com");
            super.onPageFinished(view, url);

        }
    }
    @Override
    public void onDestroy() {
        if (mWebview != null) {

            mWebview.destroy();
        }
        super.onDestroy();
    }
}
