package com.service.community.ui.fragment;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.service.community.R;
import com.service.community.ui.base.BaseFragment;
import com.service.community.ui.view.ChangeHomeEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class FindFragment extends BaseFragment {

    @BindView(R.id.mwebview)
    WebView mWebview;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_ch)
    TextView tvCh;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    public void initView() {
        tvCh.setText("发现");
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

        mWebview.loadUrl("http://vue.zhangxiaofu.cn/webview/list.html");
        mWebview.setWebViewClient(new MyWebViewClient());
    }

    @Override
    protected void initListener() {

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        if (mWebview.canGoBack()) {
            mWebview.goBack();
        }else{
            EventBus.getDefault().post(new ChangeHomeEvent("gohome"));
        }
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {


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
