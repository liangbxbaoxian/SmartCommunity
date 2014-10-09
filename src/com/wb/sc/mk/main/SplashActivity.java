package com.wb.sc.mk.main;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;

public class SplashActivity extends BaseActivity {

	private static final int SPLASH_TIME = 3000;
//	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();
			}
		}, SPLASH_TIME);
	}


	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}



	@Override
	public void getIntentData() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}

}
