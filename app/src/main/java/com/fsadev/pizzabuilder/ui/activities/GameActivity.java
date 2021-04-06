package com.fsadev.pizzabuilder.ui.activities;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.fsadev.pizzabuilder.R;



public class GameActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        WebView webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        // Configure the webview so that the game will load
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);

        // Load in the game's HTML file
        webView.loadUrl("file:///android_asset/index.html");

    }

}