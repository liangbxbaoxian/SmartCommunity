package com.wb.sc.mk.personal;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.VolleyErrorHelper;
import com.common.phone.TelephonyHelper;
import com.common.widget.ToastHelper;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.bean.ResetPwd;
import com.wb.sc.bean.VerifyCode;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.mk.personal.RegisterActivity.VerifyCodeListener;
import com.wb.sc.task.ResetPwdRequest;
import com.wb.sc.task.VerifyCodeRequest;
import com.wb.sc.util.ParamsUtil;

/**
 * 
 * @描述：重置密码
 * @作者：liang bao xian
 * @时间：2014年10月23日 上午11:31:28
 */
public class ResetPasswordActivity extends BaseHeaderActivity implements OnClickListener,
	Listener<ResetPwd>, ErrorListener{
	
	private EditText phoneEt;
	private EditText verifyCodeEt;
	private EditText pwdEt;
	private EditText pwdAgainEt;
	private Button getVerifyCodeBtn;
	private Button timeBtn;
	
	private View confirmBtn;
	
	private String phone;
	private String verifyCode;
	private String pwd;
	private String pwdAgain;
	
	private VerifyCodeTimeCount verifyCodeTimeCount;
	
	//获取验证码
	private VerifyCodeRequest mGetVerifyCodeRequest;
	//重置密码
	private ResetPwdRequest mResetPwdRequest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);

		initHeader(R.string.ac_reset_password);
		getIntentData();
		initView();	
		
		verifyCodeTimeCount = new VerifyCodeTimeCount(RegisterActivity.REFRESH_TIME, 1000);
	}
	
	@Override
	public void getIntentData() {
		
	}

	@Override
	public void initView() {
		phoneEt = (EditText) findViewById(R.id.phone);
		verifyCodeEt = (EditText) findViewById(R.id.verify_code);
		pwdEt = (EditText) findViewById(R.id.password);
		pwdAgainEt = (EditText) findViewById(R.id.password_again);
		getVerifyCodeBtn = (Button) findViewById(R.id.get_verify_code);
		getVerifyCodeBtn.setOnClickListener(this);
		timeBtn = (Button) findViewById(R.id.time);	
		timeBtn.setVisibility(View.GONE);
		confirmBtn = findViewById(R.id.confirm);
		confirmBtn.setOnClickListener(this);
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
			requestGetVerifyCode(getVerifyCodeRequestParams(), new VerifyCodeListener(), this);
			break;
			
		case R.id.confirm:
			resetPwd();
			break;
		}
	}
	
	private void resetPwd() {
//		phone = phoneEt.getText().toString();
		verifyCode = verifyCodeEt.getText().toString();
		pwd = pwdEt.getText().toString();
		pwdAgain = pwdAgainEt.getText().toString();
		
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
		
		if(TextUtils.isEmpty(pwdAgain)) {
			ToastHelper.showToastInBottom(this, R.string.password_again_empty_toast);
			return;
		}
		
		if(!pwd.equals(pwdAgain)) {
			ToastHelper.showToastInBottom(this, R.string.password_no_equasl);
			return;
		}
		
		requestResetPwd(getResetPwdRequestParams(), this, this);
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
	private List<String> getResetPwdRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG53", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(phone, 15));
		params.add(ParamsUtil.getReqRsaParam(pwd, 256));
		params.add(ParamsUtil.getReqParam(verifyCode, 6));		
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
	private void requestResetPwd(List<String> params,	 
			Listener<ResetPwd> listenre, ErrorListener errorListener) {			
		if(mResetPwdRequest != null) {
			mResetPwdRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mResetPwdRequest = new ResetPwdRequest(url, params, listenre, errorListener);
		startRequest(mResetPwdRequest);		
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
	public void onResponse(ResetPwd response) {		
		showContent();	
		if(response.respCode.equals(RespCode.SUCCESS)) {		
			finish();
			ToastHelper.showToastInBottom(this, "密码重置成功");
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
		params.add(ParamsUtil.getReqParam("01", 2));
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
			showContent();	
			if(response.respCode.equals(RespCode.SUCCESS)) {
				verifyCodeTimeCount.start();
				getVerifyCodeBtn.setVisibility(View.GONE);
				timeBtn.setVisibility(View.VISIBLE);
				timeBtn.setText("60秒");	
				ToastHelper.showToastInBottom(mActivity, R.string.verify_send_success);
			} else {
				ToastHelper.showToastInBottom(mActivity, response.respCodeMsg);
			}
		}
	}
}
