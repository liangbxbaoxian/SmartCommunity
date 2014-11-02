package com.wb.sc.mk.personal;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.activity.base.ReloadListener;
import com.wb.sc.bean.LawRuleDetial;
import com.wb.sc.bean.MsgCenter;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.task.LawRuleDetialRequest;
import com.wb.sc.util.Constans;
import com.wb.sc.util.MetaUtil;
import com.wb.sc.util.ParamsUtil;

public class LawRuleDetialActivity extends BaseHeaderActivity implements OnClickListener,  Listener<LawRuleDetial>, 
ErrorListener, ReloadListener{
	
	private TextView bulletinTitle;
	private TextView bulletinContent;
	private TextView notifier;
	private TextView notifyTime;
	private TextView notifyShortTime;
	
	private MsgCenter.MsgItem item;
	private int pageNo;
	private int pageSize =10;
	
	private LawRuleDetialRequest mLawRuleDetialRequest;
	private String lawRuleId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bulletin);

		initHeader(R.string.ac_law_rule_detial);
		getIntentData();
		initView();		
		
		showLoading();		
		
		requestBase(getBaseRequestParams(), this, this);
		
	}
	
	public void lawRule(View view) {
		Intent intent = new Intent(this, LawRuleActivity.class);
		startActivity(intent);
	}
	
	public void feedback(View view) {
		Intent intent = new Intent(this, FeedbackActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void getIntentData() {
		String msg = getIntent().getStringExtra("msg");
//		item = new Gson().fromJson(msg, MsgCenter.MsgItem.class);
		lawRuleId = getIntent().getStringExtra("lawRuleId");
		pageNo = 10;
	}

	@Override
	public void initView() {
		bulletinTitle = (TextView) findViewById(R.id.bulletinTitle);
		bulletinContent = (TextView) findViewById(R.id.bulletinContent);
		notifier = (TextView) findViewById(R.id.notifier);
		notifyTime = (TextView) findViewById(R.id.notifyTime);
		notifyShortTime = (TextView) findViewById(R.id.notifyShortTime);
		if (item != null) {
			bulletinTitle.setText(item.bulletinTitle);
			bulletinContent.setText(item.bulletinContent);
			notifier.setText(item.notifier);
			notifyTime.setText(item.notifyTime);
			notifyShortTime.setText(item.notifyTime);
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch(v.getId()) {
		case R.id.submit:
			break;
		}
	}
	
	/**
	 * 获取请求参数,请按照接口文档列表顺序排列
	 * @return
	 */
	private List<String> getBaseRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG49", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam(MetaUtil.readMeta(this, Constans.APP_CHANNEL), 20));
		params.add(ParamsUtil.getReqParam(lawRuleId+ "", 64));
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
	private void requestBase(List<String> paramsList,	 
			Listener<LawRuleDetial> listenre, ErrorListener errorListener) {			
		if(mLawRuleDetialRequest != null) {
			mLawRuleDetialRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mLawRuleDetialRequest = new LawRuleDetialRequest(url, paramsList, listenre, errorListener);
		startRequest(mLawRuleDetialRequest);		
	}

	@Override
	public void onReload() {
		showLoading();
		requestBase(getBaseRequestParams(), this, this);
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		showLoadError(this);	
		ToastHelper.showToastInBottom(getApplicationContext(), VolleyErrorHelper.getErrorMessage(this, error));
	}

	@Override
	public void onResponse(LawRuleDetial response) {
		if(response.respCode.equals(RespCode.SUCCESS)) {
			showContent();
//			mBase = response;
			bulletinTitle.setText(response.lawTitle);
			bulletinContent.setText(response.lawContent);
			notifyTime.setText(response.releaseTime);
//			notifyShortTime.setText(item.notifyTime);
			
		} else {
			showLoadError(this);
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}


	
}
