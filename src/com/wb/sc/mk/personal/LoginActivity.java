package com.wb.sc.mk.personal;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.format.HexStringBytes;
import com.common.net.volley.VolleyErrorHelper;
import com.common.security.Des3Tools;
import com.common.widget.ToastHelper;
import com.umeng.message.ALIAS_TYPE;
import com.umeng.message.PushAgent;
import com.umeng.message.proguard.C.e;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.User;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.db.DbHelper;
import com.wb.sc.mk.main.HomeActivity;
import com.wb.sc.mk.main.MainActivity;
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
	private CheckBox savePwdCb;
	
	private LoginRequest mLoginRequest;
	private User mUser;
	
	private String userPhone;
	private String password;
	
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
//		userphoneEt.setText("18657436598");
		passwordEt = (EditText) findViewById(R.id.password);
//		passwordEt.setText("123456");
		
		savePwdCb = (CheckBox) findViewById(R.id.save_password);
		
		User user = SCApp.getInstance().getUser();
		
		if(!TextUtils.isEmpty(user.phone)) {
			userphoneEt.setText(user.phone);
		}
		
		if(!TextUtils.isEmpty(user.pssword)) {
			byte[] result = null;
	    	try {
	    		result = Des3Tools.deTripleDES(SCApp.getInstance().getDes3Key(), 
	    				HexStringBytes.String2Bytes(user.pssword));  
	    		String pwd = new String(result).trim();
	    		passwordEt.setText(pwd);	    		
	    	} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
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
		userPhone = userphoneEt.getText().toString();
		password = passwordEt.getText().toString();
		
		if(userPhone == null || userPhone.equals("")) {
			ToastHelper.showToastInBottom(this, R.string.username_empty_toast);
			return;
		}
		
		if(password == null || password.equals("")) {
			ToastHelper.showToastInBottom(this, R.string.password_empty_toast);
			return;
		}
		
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
			mUser.isLogin = 1;
			mUser.phone = userPhone;
			//先写死一个UserId 和 社区ID
//			mUser.userId = "9b489d54-91fe-4183-9d90-c6d8ca0c8fa0";
			mUser.communityId = "db8eeb11-3e04-4eae-9c05-fd572abb1733";
			if(savePwdCb.isChecked()) {
				savePassword(mUser, password);
			} else {
				mUser.pssword = "";
			}
			SCApp.getInstance().setUser(mUser);
			DbHelper.saveUser(mUser);
													
			// 设置推送User Id	
			new Thread() {
				
				@Override
				public void run() {
					PushAgent mPushAgent = PushAgent.getInstance(LoginActivity.this);
					try {
						mPushAgent.addAlias(mUser.userId, ALIAS_TYPE.BAIDU);
					} catch (e e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}.start();
			
			finish();
//			startActivity(new Intent(this, HomeActivity.class));
		} else {
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}
	
	/**
	 * 
	 * @描述:选择保存密码，则把密码保存在本地
	 */
	private void savePassword(User user, String password) {
		// 3DES 加密
    	byte[] des3Result = null;
		try {
			des3Result = Des3Tools.enTripleDES(SCApp.getInstance().getDes3Key(), 
					Des3Tools.extendMsgForDes(password.getBytes()));
			user.pssword = HexStringBytes.bytes2HexString(des3Result);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
