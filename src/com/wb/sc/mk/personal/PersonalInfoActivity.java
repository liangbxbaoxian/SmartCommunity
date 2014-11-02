package com.wb.sc.mk.personal;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.activity.base.ReloadListener;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.PersonalInfo;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.mk.main.SetCommunityActivity;
import com.wb.sc.task.PersonalInfoRequest;
import com.wb.sc.util.Constans;
import com.wb.sc.util.MetaUtil;
import com.wb.sc.util.ParamsUtil;

public class PersonalInfoActivity extends BaseActivity implements Listener<PersonalInfo>, ErrorListener, ReloadListener {


	private PersonalInfoRequest mBaseRequest;
	
	private TextView phoneNum;
	private TextView localCommunity;
	private TextView accountName;
	private TextView birthday;
	private TextView sex;
	private TextView mail;
	private TextView weixinAccount;
	private TextView work;
	private TextView hobby;
	private TextView userStatue;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info);
		initView();
        showLoading();		
		
		requestBase(getBaseRequestParams(), this, this);
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();

	}



	public void getIntentData() {

	}



	public void initView() {

		phoneNum = (TextView) findViewById(R.id.phoneNum);
		localCommunity = (TextView) findViewById(R.id.localCommunity);
		accountName = (TextView) findViewById(R.id.accountName);
		birthday = (TextView) findViewById(R.id.birthday);
		sex = (TextView) findViewById(R.id.sex);
		mail = (TextView) findViewById(R.id.mail);
		weixinAccount = (TextView) findViewById(R.id.weixinAccount);
		work = (TextView) findViewById(R.id.work);
		hobby = (TextView) findViewById(R.id.hobby);
		userStatue = (TextView) findViewById(R.id.userStatue);

		userStatue.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
	}
	
	public void back(View view) {
		this.finish();
	}
	
	/**
	 * 执行任务请求
	 * @param method
	 * @param url
	 * @param params
	 * @param listenre
	 * @param errorListener
	 */	
	private void requestBase(List<String> paramsList,	 
			Listener<PersonalInfo> listenre, ErrorListener errorListener) {
		showLoading();	
		if(mBaseRequest != null) {
			mBaseRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mBaseRequest = new PersonalInfoRequest(url, paramsList, listenre, errorListener);
		startRequest(mBaseRequest);		
	}
	
	/**
	 * 获取请求参数,请按照接口文档列表顺序排列
	 * @return
	 */
	private List<String> getBaseRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG03", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam(MetaUtil.readMeta(this, Constans.APP_CHANNEL), 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		
		return params;
	}



	@Override
	public void onReload() {
		requestBase(getBaseRequestParams(), this, this);
	}



	@Override
	public void onErrorResponse(VolleyError error) {
		showLoadError(this);	
		ToastHelper.showToastInBottom(getApplicationContext(), VolleyErrorHelper.getErrorMessage(this, error));
	}
	
	public void setCommunity(View view) {
		Intent intent = new Intent(PersonalInfoActivity.this, SetCommunityActivity.class);
		startActivityForResult(intent, Constans.SET_COMMUNITY_REQUEST_CODE);
	}


	@Override
	public void onResponse(PersonalInfo response) {
		if(response.respCode.equals(RespCode.SUCCESS)) {
			showContent();
			
			phoneNum.setText(response.phoneNum);
			localCommunity.setText(response.localCommunity);
			accountName.setText(response.accountName);
			if (!"".equals(response.birthday)) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				birthday.setText(formatter.format(new Date(response.birthday)));
			}
			
			if ("1".equals(response.sex)) {
				sex.setText("男");
			} else if ("2".equals(response.sex)) {
				sex.setText("女");
			}
			
			mail = (TextView) findViewById(R.id.mail);
			mail.setText(response.mail);
			
			weixinAccount = (TextView) findViewById(R.id.weixinAccount);
			weixinAccount.setText(response.weixinAccount);
			
			work = (TextView) findViewById(R.id.work);
			
			hobby = (TextView) findViewById(R.id.hobby);
			hobby.setText(response.hobby);
			
			userStatue = (TextView) findViewById(R.id.userStatue);
			if ("0".equals(response.sex)) {
				userStatue.setText("住户已认证");
			} else if ("1".equals(response.sex)) {
				userStatue.setText("住户未认证");
			}
			
			
		} else {
			showLoadError(this);
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}

}
