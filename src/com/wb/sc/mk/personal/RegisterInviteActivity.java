package com.wb.sc.mk.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;

public class RegisterInviteActivity extends BaseHeaderActivity implements OnClickListener{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_invite);

		initHeader(R.string.ac_setting);
		getIntentData();
		initView();		
		
	}
	
	public void lawRule(View view) {
		Intent intent = new Intent(this, LawRuleActivity.class);
		startActivity(intent);
	}
	
	public void feedback(View view) {
		Intent intent = new Intent(this, FeedbackActivity.class);
		startActivity(intent);
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
		case R.id.submit:
			break;
		}
	}
	
}
