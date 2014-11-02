package com.wb.sc.mk.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.widget.ToastHelper;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.activity.base.ReloadListener;
import com.wb.sc.bean.BaseBean;
import com.wb.sc.task.UpdateAppRequest;
import com.wb.sc.widget.CustomDialog;

public class SettingActivity extends BaseHeaderActivity implements OnClickListener, Listener<BaseBean>, 
ErrorListener, ReloadListener{
	
	private UpdateAppRequest request;
	private BaseBean mBase;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		initHeader(R.string.ac_setting);
		getIntentData();
		initView();		
		
	}
	
	public void lawRule(View view) {
		Intent intent = new Intent(this, LawRuleActivity.class);
		startActivity(intent);
	}
	
	public void feedback(View view) {
		Intent intent = new Intent(this, FeedbackActivity.class);
		startActivity(intent);
	}
	
	public void alterPasswd(View view) {
		Intent intent = new Intent(this, ModifyPasswordActivity.class);
		startActivity(intent);
	}
	
	public void updateApp(View view) {           //使用友盟自动更新！！
//		CustomDialog dialog = new CustomDialog(this, R.style.mystyle,
//				R.layout.update_custom_dialog, new DialogFinish() {
//					
//					@Override
//					public void getFinish() {
//						
//					}
//				});
//		dialog.show();
		
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			
			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse UpdateStatus) {
				if(updateStatus == 1) {
					ToastHelper.showToastInBottom(mActivity, "已是最新版本");
				}
			}
		});
		UmengUpdateAgent.update(mActivity);	
		UmengUpdateAgent.forceUpdate(mActivity);
		UmengUpdateAgent.setUpdateAutoPopup(true);
		ToastHelper.showToastInBottom(mActivity, "版本检测中");

	}
	
	public void aboutMe(View view) {
		CustomDialog dialog = new CustomDialog(this, R.style.mystyle,
				R.layout.about_custom_dialog);
		dialog.show();
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
		case R.id.submit:
			break;
		}
	}

	@Override
	public void onReload() {
		
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		
	}

	@Override
	public void onResponse(BaseBean response) {
		
	}

	
}
