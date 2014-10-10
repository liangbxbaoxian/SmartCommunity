package com.wb.sc.mk.personal;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;

public class ResetPasswordActivity extends BaseHeaderActivity implements OnClickListener{
	
	private View loginBtn;
	private View registerBtn;
	private TextView forgetPasswordTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);

		initHeader(R.string.ac_reset_password);
		getIntentData();
		initView();				
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
		}
	}
}
