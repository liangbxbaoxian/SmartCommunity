package com.wb.sc.mk.personal;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.widget.ToastHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.activity.base.ReloadListener;
import com.wb.sc.adapter.MsgAdapter;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.LawRule;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.task.LawRuleRequest;
import com.wb.sc.util.Constans;
import com.wb.sc.util.MetaUtil;
import com.wb.sc.util.ParamsUtil;

public class LawRuleActivity extends BaseHeaderActivity implements OnClickListener, Listener<LawRule>, 
ErrorListener, ReloadListener{
	
	private int pageNo;
	private int pageSize =10;
	
	private LawRuleRequest mLawRuleRequest;
	
	private LinearLayout main_view;
	private LinearLayout itemView;
	
	private PullToRefreshListView mPullListView;
	private ListView mListView;
	
	private MsgAdapter adapter;
	
	private List<LawRule.LawRuleItem> list = new ArrayList<LawRule.LawRuleItem>();
	
	
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
//		main_view = (LinearLayout) findViewById(R.id.main_view);
		mPullListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);		
		mPullListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				//处理下拉刷新
				pageNo = 1;
//				startMsgCenterRequest();
				requestBase(getBaseRequestParams(), LawRuleActivity.this, LawRuleActivity.this);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				//处理上拉加载
			}
		});
		
		mPullListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				//滑动到底部的处理
//				if(loadState == PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE && mMsgCenter.hasNextPage) {
//					loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOADING;
//					mPage.pageNo++;		
//					startMsgCenterRequest();
				showLoading();	
				requestBase(getBaseRequestParams(), LawRuleActivity.this, LawRuleActivity.this);
//				}
			}
		});
		
		//设置刷新时请允许滑动的开关使能  		
		mPullListView.setScrollingWhileRefreshingEnabled(true);
		
		//设置自动刷新功能
		mPullListView.setRefreshing(false);
		
		//设置拉动模式
		mPullListView.setMode(Mode.PULL_FROM_END);
		
		
		mListView = mPullListView.getRefreshableView();
		adapter = new MsgAdapter(this, list);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(LawRuleActivity.this, LawRuleDetialActivity.class);
				intent.putExtra("lawRuleId", list.get(arg2 -1).lawRuleId);
				startActivity(intent);
				
			}
		});
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
		params.add(ParamsUtil.getReqIntParam(pageNo, 3));
		params.add(ParamsUtil.getReqIntParam(pageSize, 2));
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
		if(response.respCode.equals(RespCode.SUCCESS)) {
			pageNo++;
			if(response.totalNum == 0) {  //显示空
			    showEmpty();
			    return;
			}
			showContent();
			list.addAll(response.datas);
			adapter.notifyDataSetChanged();
		} else {
			showLoadError(this);
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}
	
}
