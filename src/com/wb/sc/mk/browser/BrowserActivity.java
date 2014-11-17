package com.wb.sc.mk.browser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.config.IntentExtraConfig;


public class BrowserActivity extends BaseHeaderActivity implements OnClickListener {
		
	private WebView webView;
	
	private String title;
	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_header_layout, R.layout.activity_browser);
		getIntentData();
		initHeader(title);
		initView();
		initWebView();
	}

	public void initView() {
		webView = (WebView) findViewById(R.id.webview);
		webView.loadUrl(url);	
	}
	
	@Override
	public void getIntentData() {
		title = getIntent().getStringExtra(IntentExtraConfig.BROWSER_TITLE);
		url = getIntent().getStringExtra(IntentExtraConfig.BROWSER_URL);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		WebSettings webSettings = webView.getSettings();
		
        webSettings.setJavaScriptEnabled(true); 
        //���Ҫ����Flash����Ҫ������һ��  
        webSettings.setPluginState(PluginState.ON);         
        
        String databasePath = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setDatabaseEnabled(true);
        webSettings.setDatabasePath(databasePath);
        
        webSettings.setDomStorageEnabled(true);
        
        //����ѡ�񱾵ػ���
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        
//        //�ڱ�ҳ������Ӧ����
//        webView.setWebViewClient(new WebViewClient(){       
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {       
//                view.loadUrl(url);       
//                return true;       
//            }
//
//			@Override
//			public void onPageFinished(WebView view, String url) {
//				setViewEnable(refreshBtn, true);
//				pageMoveable();
//				Log.d("webview_url", url);
//			}  
//												           
//        });
	}

}
