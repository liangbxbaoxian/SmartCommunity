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
import com.google.gson.Gson;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.activity.base.ReloadListener;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.LawRule;
import com.wb.sc.bean.MsgCenter;
import com.wb.sc.util.Constans;
import com.wb.sc.util.MetaUtil;
import com.wb.sc.util.ParamsUtil;

public class LawRuleDetialActivity extends BaseHeaderActivity implements OnClickListener,  Listener<LawRule>, 
ErrorListener, ReloadListener{
	
	private TextView bulletinTitle;
	private TextView bulletinContent;
	private TextView notifier;
	private TextView notifyTime;
	private TextView notifyShortTime;
	
	private MsgCenter.MsgItem item;
	private int pageNo;
	private int pageSize =10;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bulletin);

		initHeader(R.string.ac_law_rule_detial);
		getIntentData();
		initView();		
		
		showLoading();		
		
//		requestBase(getBaseRequestParams(), this, this);
		
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
		item = new Gson().fromJson(msg, MsgCenter.MsgItem.class);
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
		params.add(ParamsUtil.getReqParam("FG48", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam(MetaUtil.readMeta(this, Constans.APP_CHANNEL), 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId + "", 64));
		params.add(ParamsUtil.getReqParam(pageNo + "", 3));
		params.add(ParamsUtil.getReqParam(pageSize + "", 2));
		return params;
	}

	@Override
	public void onReload() {
		
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		
	}

	@Override
	public void onResponse(LawRule response) {
		
	}
	
}
