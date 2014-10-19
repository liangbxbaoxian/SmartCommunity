package com.wb.sc.mk.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;

public class PersonalActivity extends BaseActivity {
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		getIntentData();
		initView();
		
		replaceFragment(new PersonalFragment(), false);
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
