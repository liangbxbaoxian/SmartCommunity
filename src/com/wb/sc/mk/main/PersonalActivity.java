package com.wb.sc.mk.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.mk.personal.MsgCenterActivity;
import com.wb.sc.mk.personal.MyComplaintActivity;
import com.wb.sc.mk.personal.MyExpressActivity;
import com.wb.sc.mk.personal.MyPostActivity;
import com.wb.sc.mk.personal.MyRepairActivity;
import com.wb.sc.mk.personal.PersonalInfoActivity;
import com.wb.sc.mk.personal.RegisterInviteActivity;
import com.wb.sc.mk.personal.SettingActivity;

public class PersonalActivity extends BaseHeaderActivity {
	
	private PersonalFragment personalFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_header_layout, R.layout.activity_menu);
		
		getIntentData();
		
		initHeader(R.string.ac_personal);
		initView();
		
		personalFragment = new PersonalFragment();
		replaceFragment(personalFragment, false);
	}
	
	@Override
	public void getIntentData() {

	}

	@Override
	public void initView() {
		
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch(v.getId()) {
		case R.id.common_header_home:
			Intent intent = new Intent(this, MsgCenterActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (personalFragment != null) {
			personalFragment.onActivityResult(requestCode, resultCode, data);
		}
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
		Intent intent = new Intent(this, MyPostActivity.class);
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
