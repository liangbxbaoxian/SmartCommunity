package com.wb.sc.mk.personal;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;

public class LoginActivity extends BaseActivity implements OnClickListener{
	
	private View loginBtn;
	private View registerBtn;
	private TextView forgetPasswordTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		getIntentData();
		initView();				
	}
	
	@Override
	public void getIntentData() {
		
	}

	@Override
	public void initView() {
		loginBtn = findViewById(R.id.login);
		loginBtn.setOnClickListener(this);
		registerBtn = findViewById(R.id.register);
		registerBtn.setOnClickListener(this);
		
		forgetPasswordTv = (TextView) findViewById(R.id.forget_password);
		forgetPasswordTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		forgetPasswordTv.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch(v.getId()) {
		case R.id.login:
			break;
			
		case R.id.register:{
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
		}break;
		
		case R.id.forget_password:{
			Intent intent = new Intent(this, ResetPasswordActivity.class);
			startActivity(intent);
		}
		}
	}
}
