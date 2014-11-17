package com.wb.sc.mk.personal;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.VolleyErrorHelper;
import com.common.phone.TelephonyHelper;
import com.common.widget.ToastHelper;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.bean.VerifyCode;
import com.wb.sc.bean.Register;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.task.VerifyCodeRequest;
import com.wb.sc.task.RegisterRequest;
import com.wb.sc.util.ParamsUtil;

public class RegisterActivity extends BaseHeaderActivity implements OnClickListener,
	Listener<Register>, ErrorListener{
	
	public static final int REFRESH_TIME = 60 * 1000;
	
	private EditText nameEt;
	private EditText phoneEt;
	private EditText verifyCodeEt;
	private EditText pwdEt;
	private Button getVerifyCodeBtn;
	private Button timeBtn;
	private CheckBox showPwdCb;
	private CheckBox checkCb;
	private View registerBtn;
	
	//获取验证码
	private VerifyCodeRequest mGetVerifyCodeRequest;
	
	//注册
	private RegisterRequest mRegisterRequest;
		
	private String name;
	private String phone;
	private String verifyCode;
	private String pwd;
	
	private VerifyCodeTimeCount verifyCodeTimeCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		initHeader(R.string.ac_register);
		getIntentData();
		initView();	
		
		verifyCodeTimeCount = new VerifyCodeTimeCount(REFRESH_TIME, 1000);
	}
	
	@Override
	public void getIntentData() {
		
	}
	
	@Override
	public void initView() {
		nameEt = (EditText) findViewById(R.id.name);
		phoneEt = (EditText) findViewById(R.id.phone);
		verifyCodeEt = (EditText) findViewById(R.id.verify_code);
		pwdEt = (EditText) findViewById(R.id.password);
		showPwdCb = (CheckBox) findViewById(R.id.show_password);
		getVerifyCodeBtn = (Button) findViewById(R.id.get_verify_code);
		getVerifyCodeBtn.setOnClickListener(this);
		timeBtn = (Button) findViewById(R.id.time);	
		timeBtn.setVisibility(View.GONE);
		checkCb = (CheckBox) findViewById(R.id.check);
		registerBtn = findViewById(R.id.register);
		registerBtn.setOnClickListener(this);
		
		showPwdCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					pwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else {
					pwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch(v.getId()) {		
		case R.id.get_verify_code:
			phone = phoneEt.getText().toString();
			if(TextUtils.isEmpty(phone)) {
				ToastHelper.showToastInBottom(this, R.string.phone_empty_toast);
				return;
			}
			showProcess("正在请求发送验证码，请稍候...");
			requestGetVerifyCode(getVerifyCodeRequestParams(), new VerifyCodeListener(), this);
			break;
		
		case R.id.register:
			register();
			break;
		}
	}
	
	private void register() {
		name = nameEt.getText().toString();
//		phone = phoneEt.getText().toString();
		verifyCode = verifyCodeEt.getText().toString();
		pwd = pwdEt.getText().toString();
		boolean isCheck = checkCb.isChecked();
		
		if(TextUtils.isEmpty(name)) {
			ToastHelper.showToastInBottom(this, R.string.username_empty_toast);
			return;
		}
		
		if(TextUtils.isEmpty(phone)) {
			ToastHelper.showToastInBottom(this, R.string.phone_empty_toast);
			return;
		}
		
		if(TextUtils.isEmpty(verifyCode)) {
			ToastHelper.showToastInBottom(this, R.string.verify_empty_toast);
			return;
		}

		if(TextUtils.isEmpty(pwd)) {
			ToastHelper.showToastInBottom(this, R.string.password_empty_toast);
			return;
		}
		
		if(!isCheck) {
			ToastHelper.showToastInBottom(this, R.string.protocol_no_check);
			return;
		}
		
		showProcess("正在注册中，请稍候...");
		requestRegister(getRegisterRequestParams(), this, this);
	}
	
	/**
	 * 验证码计时器
	 * @author liangbx
	 *
	 */
	class VerifyCodeTimeCount extends CountDownTimer {

		public VerifyCodeTimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture+1000, countDownInterval);
		}
		
		@Override
		public void onTick(long millisUntilFinished) {
			timeBtn.setText(millisUntilFinished / 1000 + "秒");
		}
		
		@Override
		public void onFinish() {
			getVerifyCodeBtn.setVisibility(View.VISIBLE);
			timeBtn.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getRegisterRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG02", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(verifyCode, 6));
		params.add(ParamsUtil.getReqParam(phone, 15));
		params.add(ParamsUtil.getReqParam(name, 32));
		params.add(ParamsUtil.getReqRsaParam(pwd, 256));
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
	private void requestRegister(List<String> params,	 
			Listener<Register> listenre, ErrorListener errorListener) {			
		if(mRegisterRequest != null) {
			mRegisterRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mRegisterRequest = new RegisterRequest(url, params, listenre, errorListener);
		startRequest(mRegisterRequest);		
	}
	
	/**
	 * 网络请求错误处理
	 *
	 */
	@Override
	public void onErrorResponse(VolleyError error) {	
		ToastHelper.showToastInBottom(getApplicationContext(), VolleyErrorHelper.getErrorMessage(this, error));
	}
	
	/**
	 * 请求完成，处理UI更新
	 */
	@Override
	public void onResponse(Register response) {		
		dismissProcess();
		if(response.respCode.equals(RespCode.SUCCESS)) {			
			ToastHelper.showToastInBottom(this, "注册成功");
			finish();
		} else {
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}
	
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getVerifyCodeRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG01", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam("00", 2));
		params.add(ParamsUtil.getReqParam(phone, 15));
		String imei = TelephonyHelper.getInstance(this).getDeviceId();
		params.add(ParamsUtil.getReqParam(imei, 32));
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
	private void requestGetVerifyCode(List<String> params,	 
			Listener<VerifyCode> listenre, ErrorListener errorListener) {			
		if(mGetVerifyCodeRequest != null) {
			mGetVerifyCodeRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mGetVerifyCodeRequest = new VerifyCodeRequest(url, params, listenre, errorListener);
		startRequest(mGetVerifyCodeRequest);		
	}
	
	class VerifyCodeListener implements Listener<VerifyCode> {
		/**
		 * 请求完成，处理UI更新
		 */
		@Override
		public void onResponse(VerifyCode response) {	
			dismissProcess();
			showContent();	
			if(response.respCode.equals(RespCode.SUCCESS)) {
				verifyCodeTimeCount.start();
				getVerifyCodeBtn.setVisibility(View.GONE);
				timeBtn.setVisibility(View.VISIBLE);
				timeBtn.setText("60秒");	
				ToastHelper.showToastInBottom(RegisterActivity.this, R.string.verify_send_success);
			} else {
				ToastHelper.showToastInBottom(RegisterActivity.this, response.respCodeMsg);
			}
		}
	}
}
