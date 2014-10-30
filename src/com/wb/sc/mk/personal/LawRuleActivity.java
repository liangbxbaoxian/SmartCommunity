package com.wb.sc.mk.personal;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.activity.base.ReloadListener;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.LawRule;
import com.wb.sc.config.NetConfig;
import com.wb.sc.task.LawRuleRequest;
import com.wb.sc.util.Constans;
import com.wb.sc.util.MetaUtil;
import com.wb.sc.util.ParamsUtil;

public class LawRuleActivity extends BaseHeaderActivity implements OnClickListener, Listener<LawRule>, 
ErrorListener, ReloadListener{
	
	private int pageNo;
	private int pageSize =10;
	
	private LawRuleRequest mLawRuleRequest;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_law_rule);

		initHeader(R.string.ac_law_rule);
		getIntentData();
		initView();		
		
		showLoading();		
		
		requestBase(getBaseRequestParams(), this, this);
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
	
	/**
	 * 执行任务请求
	 * @param method
	 * @param url
	 * @param params
	 * @param listenre
	 * @param errorListener
	 */	
	private void requestBase(List<String> paramsList,	 
			Listener<LawRule> listenre, ErrorListener errorListener) {			
		if(mLawRuleRequest != null) {
			mLawRuleRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mLawRuleRequest = new LawRuleRequest(url, paramsList, listenre, errorListener);
		startRequest(mLawRuleRequest);		
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
