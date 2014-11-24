package com.wb.sc.mk.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.common.zxing.CaptureActivity;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.activity.base.BaseHeaderActivity;

public class FindActivity extends BaseHeaderActivity {
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_header_layout, R.layout.activity_menu);
		
		getIntentData();
		initHeader(R.string.bottom_bar_discover);
		setHomeIcon(R.drawable.title_bar_icon_scan, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FindActivity.this, CaptureActivity.class);
				startActivity(intent);
			}
		});
		initView();
		
		replaceFragment(new FindFragment(), false);
	}
	
	@Override
	public void getIntentData() {

	}

	@Override
	public void initView() {
		
	}
	
	public void replaceFragment(Fragment fragment, boolean toBack) {
		try {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.replace(R.id.container, fragment);
			if(toBack) {
				ft.addToBackStack("");
			}
			ft.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
