package com.wb.sc.mk.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.widget.CustomDialog;
import com.wb.sc.widget.CustomDialog.DialogFinish;

public class SettingActivity extends BaseHeaderActivity implements OnClickListener{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

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
	
	public void alterPasswd(View view) {
		Intent intent = new Intent(this, ModifyPasswordActivity.class);
		startActivity(intent);
	}
	
	public void updateApp(View view) {
		CustomDialog dialog = new CustomDialog(this, R.style.mystyle,
				R.layout.update_custom_dialog, new DialogFinish() {
					
					@Override
					public void getFinish() {
						
					}
				});
		dialog.show();
	}
	
	public void aboutMe(View view) {
		CustomDialog dialog = new CustomDialog(this, R.style.mystyle,
				R.layout.about_custom_dialog);
		dialog.show();
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
