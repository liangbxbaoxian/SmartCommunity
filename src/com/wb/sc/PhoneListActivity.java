package com.wb.sc;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.activity.base.ReloadListener;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;

import com.wb.sc.bean.PhoneList;
import com.wb.sc.task.PhoneListRequest;
import com.wb.sc.util.ParamsUtil;

public class PhoneListActivity extends BaseActivity implements Listener<PhoneList>, 
	ErrorListener, ReloadListener{
		
	private PhoneListRequest mPhoneListRequest;
	private PhoneList mPhoneList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_phonelist);
		
		getIntentData();
		initView();		
		
		showLoading();		
	}
			
	@Override
	public void getIntentData() {
		
	}
	
	@Override
	public void initView() {
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//此处设置菜单		
		setDisplayHomeAsUpEnabled(true);
		setDisplayShowHomeEnabled(false);
		
		//requestPhoneList(getPhoneListRequestParams(), this, this);		
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * 菜单点击处理
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {			
		return super.onOptionsItemSelected(item);
	}
		
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getPhoneListRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("填写接口文档中的消息类型", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
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
	private void requestPhoneList(List<String> params,	 
			Listener<PhoneList> listenre, ErrorListener errorListener) {			
		if(mPhoneListRequest != null) {
			mPhoneListRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mPhoneListRequest = new PhoneListRequest(url, params, listenre, errorListener);
		startRequest(mPhoneListRequest);		
	}
	
	/**
	 * 网络请求错误处理
	 *
	 */
	@Override
	public void onErrorResponse(VolleyError error) {	
		showLoadError(this);	
		ToastHelper.showToastInBottom(getApplicationContext(), VolleyErrorHelper.getErrorMessage(this, error));
	}
	
	/**
	 * 重新加载请求
	 */
	@Override
	public void onReload() {
		showLoading();
		//requestPhoneList(getPhoneListRequestParams(), this, this);			
	}
	
	/**
	 * 请求完成，处理UI更新
	 */
	@Override
	public void onResponse(PhoneList response) {		
		showContent();	
		if(response.respCode.equals(RespCode.SUCCESS)) {			
			mPhoneList = response;
		} else {
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}
}
