package com.faker.android;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import com.harvest.io.R;
public class WebViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unity_webview_layout);
        String title = getIntent().getStringExtra("title");
        if(title!=null){
            ((TextView)findViewById(R.id.title_text)).setText(title);
        }
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
    }
}
