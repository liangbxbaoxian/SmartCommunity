package com.wb.sc.mk.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.mk.personal.MyComplaintActivity;
import com.wb.sc.mk.personal.MyExpressActivity;
import com.wb.sc.mk.personal.MyForumActivity;
import com.wb.sc.mk.personal.MyRepairActivity;
import com.wb.sc.mk.personal.PersonalInfoActivity;
import com.wb.sc.mk.personal.RegisterInviteActivity;
import com.wb.sc.mk.personal.SettingActivity;

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
	
	public void personalInfo(View view) {
		Intent intent = new Intent(this, PersonalInfoActivity.class);
		startActivity(intent);
	}
	
	public void myComplaint(View view) {
		Intent intent = new Intent(this, MyComplaintActivity.class);
		startActivity(intent);
	}
	
	public void myRepair(View view) {
		Intent intent = new Intent(this, MyRepairActivity.class);
		startActivity(intent);
	}
	
	public void myForum(View view) {
		Intent intent = new Intent(this, MyForumActivity.class);
		startActivity(intent);
	}
	
	public void myExpress(View view) {
		Intent intent = new Intent(this, MyExpressActivity.class);
		startActivity(intent);
	}
	
	public void setting(View view) {
		Intent intent = new Intent(this, SettingActivity.class);
		startActivity(intent);
	}
	
	public void registerInvite(View view) {
		Intent intent = new Intent(this, RegisterInviteActivity.class);
		startActivity(intent);
	}
	
	public void back(View view) {
		finish();
	}
}
