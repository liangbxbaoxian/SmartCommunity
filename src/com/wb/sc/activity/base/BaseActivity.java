package com.wb.sc.activity.base;

import com.umeng.message.PushAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View.OnClickListener;

/**
 * Activity基类
 * 
 * @author liangbx
 * 
 */
public abstract class BaseActivity extends BaseNetActivity {
	
	private ProgressDialog pDialog;
	
	protected Activity mActivity;
	
	/**
	 * 获取Intent数据
	 */
	public abstract void getIntentData();

	/**
	 * 初始化控件
	 */
	public abstract void initView();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		PushAgent.getInstance(this).onAppStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	
	public void showProcess(int id) {
		showProcess(getResources().getString(id));
	}
	
	public void showProcess(String message) {
		pDialog = new ProgressDialog(this);
		pDialog.setIndeterminate(true);
		pDialog.setMessage(message);
		pDialog.setCancelable(false);
		pDialog.show();
	}
	
	public void dismissProcess () {
		if(pDialog != null) {
			pDialog.dismiss();
		}
	}
	
	public void setViewOnClick(int id, OnClickListener listener) {
		findViewById(id).setOnClickListener(listener);
	}
}
