package com.wb.sc.mk.personal;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.User;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.db.DbHelper;
import com.wb.sc.task.LoginRequest;
import com.wb.sc.util.ParamsUtil;

/**
 * 
 * @描述：登录
 * @作者：liang bao xian
 * @时间：2014年10月23日 上午11:37:15
 */
public class LoginActivity extends BaseActivity implements OnClickListener,
	Listener<User>, ErrorListener{
	
	private View loginBtn;
	private View registerBtn;
	private TextView forgetPasswordTv;
	private EditText userphoneEt;
	private EditText passwordEt;
	
	private LoginRequest mLoginRequest;
	private User mUser;
	
	private String userphone;
	
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
		
		userphoneEt = (EditText) findViewById(R.id.userPhone);
		passwordEt = (EditText) findViewById(R.id.password);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch(v.getId()) {
		case R.id.login:
			login();
			break;
			
		case R.id.register:{
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
		}break;
		
		case R.id.forget_password:{
			Intent intent = new Intent(this, ResetPasswordActivity.class);
			startActivity(intent);
		}break;
		}
	}
	
	private void login() {
		String userPhone = userphoneEt.getText().toString();
		String password = passwordEt.getText().toString();
		
		if(userPhone == null || userPhone.equals("")) {
			ToastHelper.showToastInBottom(this, R.string.username_empty_toast);
			return;
		}
		
		if(password == null || password.equals("")) {
			ToastHelper.showToastInBottom(this, R.string.password_empty_toast);
			return;
		}
		
		this.userphone = userPhone;
		showProcess(R.string.loging_toast);
		requestLogin(getLoginRequestParams(userPhone, password), this, this);
	}
	
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getLoginRequestParams(String userPhone, String password) {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG51", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(userPhone, 15));
		params.add(ParamsUtil.getReqRsaParam(password, 256));		
		return params;
	}
	
	/**
	 * 执行任务请求
	 * @param method
	 * @param url
	 * @param params
	 * @param listenre
	 * @param errorListener
	 */	
	private void requestLogin(List<String> params,	 
			Listener<User> listenre, ErrorListener errorListener) {			
		if(mLoginRequest != null) {
			mLoginRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mLoginRequest = new LoginRequest(url, params, listenre, errorListener);
		startRequest(mLoginRequest);		
	}
	
	/**
	 * 网络请求错误处理
	 *
	 */
	@Override
	public void onErrorResponse(VolleyError error) {	
		dismissProcess();
		ToastHelper.showToastInBottom(getApplicationContext(), VolleyErrorHelper.getErrorMessage(this, error));
	}
		
	/**
	 * 请求完成，处理UI更新
	 */
	@Override
	public void onResponse(User response) {			
		dismissProcess();
		if(response.respCode.equals(RespCode.SUCCESS)) {			
			mUser = response;
			SCApp.getInstance().setUser(mUser);
			DbHelper.saveUser(mUser);
			finish();
		} else {
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}
}
