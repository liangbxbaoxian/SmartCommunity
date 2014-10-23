package com.wb.sc.mk.personal;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.ModifyPwd;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.task.ModifyPwdRequest;
import com.wb.sc.task.VerifyCodeRequest;
import com.wb.sc.util.ParamsUtil;

/**
 * 
 * @描述：修改密码
 * @作者：liang bao xian
 * @时间：2014年10月23日 上午11:31:18
 */
public class ModifyPasswordActivity extends BaseHeaderActivity implements OnClickListener,
	Listener<ModifyPwd>, ErrorListener{	
	
	private EditText oldPwdEt;
	private EditText newPwdEt;
	private EditText newPwd2Et;
	private Button submitBtn;
	
	private final int passwordMinLenth = 6;
	
	private String oldPwd;
	private String newPwd;
		
	//修改密码
	private ModifyPwdRequest mModifyPwdRequest;
	
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
		
		requestModifyPwd(getModifyPwdRequestParams(), this, this);
	}
	
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getModifyPwdRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG52", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		params.add(ParamsUtil.getReqRsaParam(newPwd, 256));
		params.add(ParamsUtil.getReqRsaParam(oldPwd, 256));
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
	private void requestModifyPwd(List<String> params,	 
			Listener<ModifyPwd> listenre, ErrorListener errorListener) {			
		if(mModifyPwdRequest != null) {
			mModifyPwdRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mModifyPwdRequest = new ModifyPwdRequest(url, params, listenre, errorListener);
		startRequest(mModifyPwdRequest);		
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
	public void onResponse(ModifyPwd response) {			
		if(response.respCode.equals(RespCode.SUCCESS)) {			
			ToastHelper.showToastInBottom(this, "密码修改成功");
			finish();
		} else {
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}
}
