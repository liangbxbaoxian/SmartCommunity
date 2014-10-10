package com.wb.sc.mk.personal;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;

public class RegisterActivity extends BaseHeaderActivity implements OnClickListener{
	
	private View loginBtn;
	private View registerBtn;
	private TextView forgetPasswordTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		initHeader(R.string.ac_register);
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
