package com.pioneeriot.pioneeriot.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.pioneeriot.pioneeriot.R;
import com.pioneeriot.pioneeriot.constant.Constant;
import com.pioneeriot.pioneeriot.utils.ActivityController;
import com.pioneeriot.pioneeriot.utils.NetUtils;
import com.pioneeriot.pioneeriot.widget.MyToolbar;

/**
 * Created by Li Yuliang on 2017/07/20 0020.
 * 通用HTML展示页面
 *
 * @author LiYuliang
 * @version 2017/10/27
 */
public class HtmlActivity extends BaseActivity {

    private Context context;
    private ProgressBar progressBarWebView;
    private WebView webViewProtocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        context = this;
        setStatusBar(findViewById(R.id.status_bar));
        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("URL");
        MyToolbar toolbar = findViewById(R.id.toolbar_protocol);
        toolbar.initToolBar(this, toolbar, title, R.drawable.back_white, -1, onClickListener);
        webViewProtocol = findViewById(R.id.webView_protocol);
        progressBarWebView = findViewById(R.id.progress_bar_webView);
        initSettings();
        loadProtocol(url);
    }

    /**
     * 加载页面配置
     */
    private void initSettings() {
        WebSettings settings = webViewProtocol.getSettings();
        settings.setJavaScriptEnabled(true);
        // 设置缓存模式
        if (NetUtils.isNetworkAvailable(context)) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        // 提高渲染的优先级
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 支持多窗口
        settings.setSupportMultipleWindows(true);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        // 开启 Application Caches 功能
        settings.setAppCacheEnabled(true);
    }

    private void loadProtocol(String url) {
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webViewProtocol.loadUrl(url);
        webViewProtocol.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        //监听网页加载
        webViewProtocol.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == Constant.PROGRESS_WEBVIEW) {
                    // 网页加载完成
                    progressBarWebView.setVisibility(View.GONE);
                } else {
                    // 加载中
                    progressBarWebView.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    private View.OnClickListener onClickListener = (v) -> {
        switch (v.getId()) {
            case R.id.iv_left:
                ActivityController.finishActivity(this);
                break;
            default:
                break;
        }
    };

}
