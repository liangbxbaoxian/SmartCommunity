package com.wb.sc.mk.personal;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.activity.base.ReloadListener;
import com.wb.sc.adapter.MyComplaintAdpater;
import com.wb.sc.adapter.MyExpressAdpater;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.MyExpress;
import com.wb.sc.bean.SentHome;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.task.MyExpressRequest;
import com.wb.sc.util.ParamsUtil;

public class MyExpressActivity extends BaseHeaderActivity implements OnMenuItemClickListener, OnClickListener, Listener<MyExpress>, 
ErrorListener, ReloadListener{

	private PullToRefreshListView mPullToRefreshListView;
	private MyExpressAdpater mAdpter;
	
	private MyExpressRequest mMyExpressRequest;

	private String mKeyword;
	private String sId;

	private boolean hasNextPage;
	private String mDistrictName;

	private List<MyExpress.ExpressItem> list = new ArrayList<MyExpress.ExpressItem>();
	
	private List<MyExpress.ExpressItem> current_express_list = new ArrayList<MyExpress.ExpressItem>();
	private List<MyExpress.ExpressItem> deprecated_express_list = new ArrayList<MyExpress.ExpressItem>();
	private List<MyExpress.ExpressItem> history_express_list = new ArrayList<MyExpress.ExpressItem>();
	
	private boolean has_next_current_express;
	private boolean has_next_deprecated_express;
	private boolean has_next_history_express;
	

	private View current_express;
	private View deprecated_express;
	private View history_express;
	
	private int pageNo = 1;
	private int pageSize = 10;
	
	private String reqType = "GA06";
	
	private ProgressBar progressBar;
	
	private TextView noData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_express);
		initView();
		initHeader(R.string.ac_my_express);
		showLoading();		
		
		requestBase(getBaseRequestParams(), this, this);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	public void back (View view) {
		finish();
	}

	public void initView() {
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_scroll);
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new GetDataTask().execute();
			}
		});

		mPullToRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				if (hasNextPage) {
					requestBase(getBaseRequestParams(), MyExpressActivity.this, MyExpressActivity.this);
				}
			}
		});


		mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MyExpressActivity.this, MyExpressActivity.class);
				startActivity(intent);
			}
		});


		//		initData();
		mAdpter = new MyExpressAdpater(MyExpressActivity.this, list);  //效果圖未給有数据的显示，so  adapter 还没做好显示；
		mPullToRefreshListView.setDividerDrawable(null);
		mPullToRefreshListView.setAdapter(mAdpter);


		current_express = findViewById(R.id.current_express);
		current_express.setSelected(true);
		current_express.setOnClickListener(this);

		deprecated_express = findViewById(R.id.deprecated_express);
		deprecated_express.setSelected(false);
		deprecated_express.setOnClickListener(this);

		history_express = findViewById(R.id.history_express);
		history_express.setSelected(false);
		history_express.setOnClickListener(this);
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		noData = (TextView) findViewById(R.id.noData);

	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			mAdpter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mPullToRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
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
			Listener<MyExpress> listenre, ErrorListener errorListener) {			
		if(mMyExpressRequest != null) {
			mMyExpressRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mMyExpressRequest = new MyExpressRequest(url, paramsList, this, this);
		startRequest(mMyExpressRequest);		
	}


	public void getIntentData() {
		Intent intent = getIntent();
		mKeyword = intent.getStringExtra("mKeyword");
		pageNo = 1;
	}

	@Override
	public boolean onMenuItemClick(MenuItem arg0) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onClick(View v) {

		switch(v.getId()) {
		case R.id.deprecated_express:
			reqType = "GA22";
			//contentVp.setCurrentItem(0);
			deprecated_express.setSelected(true);
			current_express.setSelected(false);
			history_express.setSelected(false);
			
			pageNo = deprecated_express_list.size() / pageSize;
			if (pageNo == 0) {
				pageNo = 1;
//				showLoading();
				
				mPullToRefreshListView.setVisibility(View.GONE);
				progressBar.setVisibility(View.VISIBLE);
				requestBase(getBaseRequestParams(), this, this);
			}
			list.clear();
			list.addAll(deprecated_express_list);
			mAdpter.notifyDataSetChanged();
			
			if (has_next_deprecated_express) {
				mPullToRefreshListView.setMode(Mode.BOTH);
			}
	
			
			break;

		case R.id.current_express:
			reqType = "GA06";
			//contentVp.setCurrentItem(1);
			deprecated_express.setSelected(false);
			current_express.setSelected(true);
			history_express.setSelected(false);
			
			pageNo = current_express_list.size() / pageSize;
			if (pageNo == 0) {
				pageNo = 1;
//				showLoading();	
				mPullToRefreshListView.setVisibility(View.GONE);
				progressBar.setVisibility(View.VISIBLE);
				requestBase(getBaseRequestParams(), this, this);
			}
			list.clear();
			list.addAll(current_express_list);
			mAdpter.notifyDataSetChanged();
			
			if (has_next_current_express) {
				mPullToRefreshListView.setMode(Mode.BOTH);
			}
			
			
			break;
		case R.id.history_express:
			reqType = "GA23";
			//	contentVp.setCurrentItem(1);
			deprecated_express.setSelected(false);
			current_express.setSelected(false);
			history_express.setSelected(true);
			
			pageNo = history_express_list.size() / pageSize;
			if (pageNo == 0) {
				pageNo = 1;
//				showLoading();	
				mPullToRefreshListView.setVisibility(View.GONE);
				progressBar.setVisibility(View.VISIBLE);
				requestBase(getBaseRequestParams(), this, this);
			}
			list.clear();
			list.addAll(history_express_list);
			mAdpter.notifyDataSetChanged();

			if (has_next_history_express) {
				mPullToRefreshListView.setMode(Mode.BOTH);
			}
			
			break;			

		case R.id.common_header_back:
			finish();
			break;			
		}
	}
	
	/**
	 * 获取请求参数,请按照接口文档列表顺序排列
	 * @return
	 */
	private List<String> getBaseRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam(reqType, 4));
		params.add(ParamsUtil.getReqParam("X01_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00", 2));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().phone, 15));
		params.add(ParamsUtil.getReqIntParam(pageNo, 3));
		params.add(ParamsUtil.getReqIntParam(pageSize, 2));
		params.add(ParamsUtil.getReqParam("12345678900987654321123456789009", 20));
		
		return params;
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
	public void onResponse(MyExpress response) {
		if(response.respCode.equals(RespCode.SUCCESS)) {
			pageNo ++;
			hasNextPage = response.hasNextPage;
			if ("GA06".equals(reqType)) {
				has_next_current_express = response.hasNextPage;
				current_express_list.addAll(response.datas);
				list.clear();
				list.addAll(current_express_list);
			} else if ("GA23".equals(reqType)) {
				has_next_deprecated_express = response.hasNextPage;
				deprecated_express_list.addAll(response.datas);
				list.clear();
				list.addAll(deprecated_express_list);
			} else if ("GA22".equals(reqType)) {
				has_next_history_express = response.hasNextPage;
				history_express_list.addAll(response.datas);
				list.clear();
				list.addAll(history_express_list);
			}
			
			if(response.totalNum == 0) {  //显示空
//			    showEmpty();
				noData.setVisibility(View.VISIBLE);
			    return;
			}
			
//			list.clear();  list.addAll and then notification
			
//			mPullToRefreshListView.setDividerDrawable(null);
//			mPullToRefreshListView.setAdapter(mAdpter);

			// Call onRefreshComplete when the list has been refreshed.
			mPullToRefreshListView.onRefreshComplete();
			mPullToRefreshListView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			noData.setVisibility(View.GONE);
			mAdpter.notifyDataSetChanged();
			showContent();
		} else {
			showLoadError(this);
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}

}
