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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

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
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.activity.base.ReloadListener;
import com.wb.sc.adapter.MyComplaintAdpater;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.MyRepair;
import com.wb.sc.bean.MyRepair.MyRepairItem;
import com.wb.sc.bean.SentHome;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.task.MyRepairRequest;
import com.wb.sc.util.Constans;
import com.wb.sc.util.MetaUtil;
import com.wb.sc.util.ParamsUtil;

public class MyRepairActivity extends BaseActivity implements OnMenuItemClickListener, OnClickListener, Listener<MyRepair>, 
ErrorListener, ReloadListener{

	private PullToRefreshListView mPullToRefreshListView;
	private MyComplaintAdpater mAdpter;
	
	private String mKeyword;
	private String sId;
	
	private int pageNo;
	private int pageSize = 10;
	private MyRepairRequest MmyRepairRequest;
	
	private boolean hasNextPage;
	private String mDistrictName;
	
	private List<MyRepairItem> list = new ArrayList<MyRepairItem>();
	
	private Spinner mSpinner;
	
	private View personalV;
	private View publicV;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_repair);
		getIntentData();
		initView();
		
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
			}
		});
		
		
		mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MyRepairActivity.this, MyRepairActivity.class);
				startActivity(intent);
			}
		});
		
		
		initData();
		mAdpter = new MyComplaintAdpater(MyRepairActivity.this, list);
		mPullToRefreshListView.setDividerDrawable(null);
		mPullToRefreshListView.setAdapter(mAdpter);
		
	      // 初始化控件
		mSpinner = (Spinner) findViewById(R.id.spinner1);
		// 建立数据源
		String[] mItems = getResources().getStringArray(R.array.my_complaint);
		// 建立Adapter并且绑定数据源
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
    			R.layout.spinner_text_layout, mItems);
    	adapter.setDropDownViewResource(R.layout.spinner_down_text_layout);
		mSpinner.setAdapter(adapter);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg2 == 1) {
					mAdpter.stateFilter(true);
				} else {
					mAdpter.stateFilter(false);
				}
				mAdpter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		personalV = findViewById(R.id.personal_repairs);
		personalV.setSelected(true);
		personalV.setOnClickListener(this);
		publicV = findViewById(R.id.public_repairs);
		publicV.setOnClickListener(this);
		
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
	
	private void initData() {
		String [] name = {"缇斯西饼(洪山桥)", "安德鲁森(洪山桥太阳城店)", "陌上花开(仓山店)", "比哥鸡排", "那时花开"};
		String []  category = {"餐饮", "餐饮", "花店", "餐饮", "花店"};
		int [] resId = {R.drawable.xibing, R.drawable.mianbao, R.drawable.huadian, R.drawable.jipai, R.drawable.nashihuadian};
		for (int i = 0; i < resId.length; i++) {
			SentHome sentHome = new SentHome();
			sentHome.name = name [i];
			sentHome.category = category [i];
			sentHome.resId = resId [i];
//			list.add(sentHome);
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
			Listener<MyRepair> listenre, ErrorListener errorListener) {			
		if(MmyRepairRequest != null) {
			MmyRepairRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		MmyRepairRequest = new MyRepairRequest(url, paramsList, this, this);
		startRequest(MmyRepairRequest);		
	}
	
	
	/**
	 * 获取请求参数,请按照接口文档列表顺序排列
	 * @return
	 */
	private List<String> getBaseRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG40", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam(MetaUtil.readMeta(this, Constans.APP_CHANNEL), 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId +"", 64));
		params.add(ParamsUtil.getReqParam(pageNo + "", 3));
		params.add(ParamsUtil.getReqParam(pageSize + "", 2));;
		
		return params;
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
		case R.id.personal_repairs:
//			contentVp.setCurrentItem(0);
			personalV.setSelected(true);
			publicV.setSelected(false);
			break;
			
		case R.id.public_repairs:
//			contentVp.setCurrentItem(1);
			personalV.setSelected(false);
			publicV.setSelected(true);
			break;					
		}
	}


	@Override
	public void onReload() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onErrorResponse(VolleyError error) {
		showLoadError(this);	
		ToastHelper.showToastInBottom(getApplicationContext(), VolleyErrorHelper.getErrorMessage(this, error));
	}


	@Override
	public void onResponse(MyRepair response) {
		if(response.respCode.equals(RespCode.SUCCESS)) {
			if (response.totalNum > 0) {
				pageNo ++;
				list.addAll(response.datas);
			}

			// Call onRefreshComplete when the list has been refreshed.
			mPullToRefreshListView.onRefreshComplete();
			mAdpter.notifyDataSetChanged();
			if (!response.hasNextPage) {
				mPullToRefreshListView.setMode(Mode.DISABLED);
			}
			showContent();
		} else {
			showLoadError(this);
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}
	
	

}
