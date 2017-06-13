package com.actiknow.isdental.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.actiknow.isdental.R;
import com.actiknow.isdental.utils.Utils;

import static com.actiknow.isdental.R.id.ivBack;

/**
 * Created by l on 13/06/2017.
 */

public class ShopOnlineActivity extends AppCompatActivity {
    WebView htmlWebView;
    ImageView ivBack;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_shop_online);
        initView ();
        initData ();
        initListener();


    }
    private void initView() {
        htmlWebView = (WebView)findViewById(R.id.webView);
        ivBack=(ImageView)findViewById(R.id.ivBack);
    }
    private void initData() {
        progressDialog = new ProgressDialog (ShopOnlineActivity.this);
        Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), false);
        getWebView();
    }
    private void initListener () {
        ivBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void getWebView() {
        Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);

        htmlWebView.setWebViewClient(new CustomWebViewClient());
        WebSettings webSetting = htmlWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDisplayZoomControls(true);
        htmlWebView.loadUrl("https://www.indiasupply.com/");
    }

    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
