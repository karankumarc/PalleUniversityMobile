package com.techpalle.poc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class PalleWebActivity extends AppCompatActivity {
    WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palle_web);
        Toast.makeText(this, "Wait, while we are redirecting to palleuniversity.com !!!", Toast.LENGTH_LONG).show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        wv = (WebView) findViewById(R.id.webView1);

        class MyWebViewClient extends WebViewClient {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                wv.loadUrl(url);
                return true;
            };
        }

        wv.setWebViewClient(new MyWebViewClient());
        wv.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                wv.invalidate();
            }
            ;
        });
        wv.loadUrl("http://palleuniversity.com/Login.aspx");
        wv.getSettings().setJavaScriptEnabled(true);
    }
}
