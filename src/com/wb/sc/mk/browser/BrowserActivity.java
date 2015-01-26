package com.wb.sc.mk.browser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
		//设置WebView属性，能够执行JavaScript脚本
        webSettings.setJavaScriptEnabled(true); 
        //如果要播放Flash，需要加上这一句 
        webSettings.setPluginState(PluginState.ON);         
        
        String databasePath = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setDatabaseEnabled(true);
        webSettings.setDatabasePath(databasePath);
        
        webSettings.setDomStorageEnabled(true);
        
        //优先选择本地缓存
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        
        webView.setWebViewClient(new WebViewClient(){       
            public boolean shouldOverrideUrlLoading(WebView view, String url) {    
            	//在本页面中响应链接
                view.loadUrl(url);       
                return true;       
            }

			@Override
			public void onPageFinished(WebView view, String url) {
				
			}  
												           
        });
	}

}
