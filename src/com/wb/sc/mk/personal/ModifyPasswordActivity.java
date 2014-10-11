package com.wb.sc.mk.personal;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.common.widget.ToastHelper;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;

public class ModifyPasswordActivity extends BaseHeaderActivity implements OnClickListener{
	
	private EditText oldPwdEt;
	private EditText newPwdEt;
	private EditText newPwd2Et;
	private Button submitBtn;
	
	private final int passwordMinLenth = 6;
	
	private String oldPwd;
	private String newPwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_password);

		initHeader(R.string.ac_modify_password);
		getIntentData();
		initView();				
	}
	
	@Override
	public void getIntentData() {
		
	}

	@Override
	public void initView() {
		oldPwdEt = (EditText) findViewById(R.id.old_password);
		newPwdEt = (EditText) findViewById(R.id.new_password);
		newPwd2Et = (EditText) findViewById(R.id.new_password2);
		submitBtn = (Button) findViewById(R.id.submit);
		submitBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch(v.getId()) {
		case R.id.submit:
			submit();
			break;
		}
	}
	
	private void submit() {
		oldPwd = oldPwdEt.getText().toString();
		newPwd = newPwdEt.getText().toString();
		String newPwd2 = newPwd2Et.getText().toString();
		
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		
		if(oldPwd.equals("")) {
			oldPwdEt.startAnimation(shake);
			ToastHelper.showToastInBottom(this, R.string.password_empty_toast);
			return;
		}
		
		if(oldPwd.length() < passwordMinLenth) {
			String toast = getResources().getString(R.string.password_no_length);
			ToastHelper.showToastInBottom(this, String.format(toast, passwordMinLenth));
			oldPwdEt.startAnimation(shake);
			return;
		}
				
		if(newPwd.equals("")) {
			ToastHelper.showToastInBottom(this, R.string.password_empty_toast);
			newPwdEt.startAnimation(shake);
			return;
		}
		
		if(newPwd.length() < passwordMinLenth) {
			String toast = getResources().getString(R.string.password_no_length);
			ToastHelper.showToastInBottom(this, String.format(toast, passwordMinLenth));
			newPwdEt.startAnimation(shake);
			return;
		}
		
		if(newPwd2.equals("")) {
			ToastHelper.showToastInBottom(this, R.string.password_empty_toast);
			newPwd2Et.startAnimation(shake);
			return;
		}
		
		if(newPwd2.length() < passwordMinLenth) {
			String toast = getResources().getString(R.string.password_no_length);
			ToastHelper.showToastInBottom(this, String.format(toast, passwordMinLenth));
			newPwd2Et.startAnimation(shake);
			return;
		}
		
		if(!newPwd.equals(newPwd2)) {
			ToastHelper.showToastInBottom(this, R.string.password_no_equasl);
			newPwdEt.startAnimation(shake);
			newPwd2Et.startAnimation(shake);
			return;
		}
	}
}
