package com.maxrescuerinc.myandroidapplication.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

import com.maxrescuerinc.myandroidapplication.Functions.MyWebViewClient;
import com.maxrescuerinc.myandroidapplication.R;

public class WebActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        Uri url = getIntent().getData();
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(url.toString());
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
