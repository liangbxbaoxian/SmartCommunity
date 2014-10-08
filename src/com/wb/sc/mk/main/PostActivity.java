package com.wb.sc.mk.main;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.wb.sc.R;

public class PostActivity extends FragmentActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		initView();
	}


	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}



	public void getIntentData() {
		
	}



	public void initView() {
		
		PostFragment postFragment = new PostFragment();
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_containner, postFragment).commit();
		
	}

}
