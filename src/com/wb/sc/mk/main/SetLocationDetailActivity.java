package com.wb.sc.mk.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
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
import com.wb.sc.adapter.DictionaryAdapter;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.Community;
import com.wb.sc.bean.Dictionary;
import com.wb.sc.bean.DictionaryItem;
import com.wb.sc.bean.SentHome;
import com.wb.sc.bean.Community.CommunityItem;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.task.CommunityRequest;
import com.wb.sc.task.DictionaryRequest;
import com.wb.sc.util.Constans;
import com.wb.sc.util.MetaUtil;
import com.wb.sc.util.ParamsUtil;

public class SetLocationDetailActivity extends BaseActivity implements OnMenuItemClickListener, Listener<Dictionary>, 
ErrorListener, ReloadListener{

	private PullToRefreshListView mPullToRefreshListView;
	private DictionaryAdapter mAdpter;
	
	private String mKeyword;
	private String sId;
	
	private int pageNo;
	private int pageSize = 10;
	private boolean hasNextPage;
	private String mDistrictName;
	
	private DictionaryRequest mDictionaryRequest;
	private CommunityRequest mCommunityRequest;
	
	private List<Object> list = new ArrayList<Object>();
	
	private Spinner mSpinner;
	private Spinner mDistanceSpinner;
	
	public String longitude;   // 经度
	public String latitude;     // 维度
	public String merchantCategoryId;  // 商户类别
	private String merchantName;
	
	private LocationClient mLocationClient;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor="gcj02";
	
	private DictionaryItem item;
	private int pos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_location_detial);
		getIntentData();
		initView();
		
		showLoading();
		
		if (item.dictionaryId.equals("3")) {
			requestCommunity(getCommunityRequestParams());
		} else {
			requestBase(getBaseRequestParams(), this, this);
		}
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	
	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//设置定位模式
		option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
		int span=1000;
		option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	public void back (View view) {
		finish();
     }

	public void initView() {
		
		TextView title_tv = (TextView) findViewById(R.id.title_tv);
		if (item.dictionaryId.equals("0")) {
			title_tv.setText("选择省");
		} else if(item.dictionaryId.equals("1")) {
			title_tv.setText("选择市");
		} else if(item.dictionaryId.equals("2")) {
			title_tv.setText("选择区");
		} else if(item.dictionaryId.equals("3")) {
			title_tv.setText("选择社区");
		}
		
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_scroll);
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//				new GetDataTask().execute();
				pageNo = 1;
				list.clear();
				requestBase(getBaseRequestParams(), SetLocationDetailActivity.this, SetLocationDetailActivity.this);
			}
		});
		
		mPullToRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				requestBase(getBaseRequestParams(), SetLocationDetailActivity.this, SetLocationDetailActivity.this);
			}
		});
		
		
		mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				Intent intent = new Intent(SetLocationDetailActivity.this, SentHomeDetailActivity.class);
//				intent.putExtra("merchantTel", list.get(position).merchantTel);
//				intent.putExtra("merchantName", list.get(position).merchantName);
				String name = "";
				String code = "";
				String sid = "";
				if (list.get(position -1) instanceof DictionaryItem) {
					DictionaryItem item = (DictionaryItem) list.get(position -1);
					code = item.dictionaryCode;
		    		name = item.dictionaryName;
		    		sid = item.dictionaryId;
				} else {
					CommunityItem item = (CommunityItem) list.get(position -1);
					code = item.communityCode;
		    		name = item.communityName;
		    		sid = item.communityId;
				}
				item.dictionaryName = name;
				item.dictionaryCode = code;
				item.id = sid;
				if (pos + 1 < SCApp.getInstance().getList().size()) {
					DictionaryItem subItem = SCApp.getInstance().getList().get(pos + 1);
					subItem.superDictionaryId =  sid;
				}
				
				SetLocationDetailActivity.this.finish();
			}
		});
		
		
		initData();
		mAdpter = new DictionaryAdapter(SetLocationDetailActivity.this, list);
		mAdpter.setShowArrow(false);
		mPullToRefreshListView.setDividerDrawable(null);
		mPullToRefreshListView.setAdapter(mAdpter);
		
//	      // 初始化控件
//		mSpinner = (Spinner) findViewById(R.id.spinner1);
//		// 建立数据源
//		String[] mItems = getResources().getStringArray(R.array.spinnername);
//		// 建立Adapter并且绑定数据源
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
//    			R.layout.spinner_text_layout, mItems);
//    	adapter.setDropDownViewResource(R.layout.spinner_down_text_layout);
//		mSpinner.setAdapter(adapter);
//		
//		// 初始化控件
//		mDistanceSpinner = (Spinner) findViewById(R.id.spinner2);
//		// 建立数据源
//		String[] distances = getResources().getStringArray(R.array.spinner_distance);
//		// 建立Adapter并且绑定数据源
//		ArrayAdapter<String> distanceAdapter = new ArrayAdapter<String>(this, 
//				R.layout.spinner_text_layout, distances);
//		distanceAdapter.setDropDownViewResource(R.layout.spinner_down_text_layout);
//		mDistanceSpinner.setAdapter(distanceAdapter);
		
		
		
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
		String []  distance = {"100米", "100米", "100米", "100米", "100米"};
		int [] resId = {R.drawable.xibing, R.drawable.mianbao, R.drawable.huadian, R.drawable.jipai, R.drawable.nashihuadian};
		for (int i = 0; i < resId.length; i++) {
			SentHome sentHome = new SentHome();
			sentHome.name = name [i];
			sentHome.category = category [i];
			sentHome.resId = resId [i];
			sentHome.distance = distance[i];
//			list.add(sentHome);
		}
 	}


	public void getIntentData() {
		Intent intent = getIntent();
		mKeyword = intent.getStringExtra("keyword");
		pageNo = 1;
		pos = intent.getIntExtra("position", 0);
		item = SCApp.getInstance().getList().get(pos);
//		item = (DictionaryItem)intent.getSerializableExtra("obj");
	}

	@Override
	public boolean onMenuItemClick(MenuItem arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	/**
	 * 执行任务请求
	 * @param method
	 * @param url
	 * @param params
	 * @param listenre
	 * @param errorListener
	 */	
	private void requestCommunity(List<String> paramsList) {			
		if(mCommunityRequest != null) {
			mCommunityRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mCommunityRequest = new CommunityRequest(url, paramsList, new Listener<Community> () {

			@Override
			public void onResponse(Community response) {
				if(response.respCode.equals(RespCode.SUCCESS)) {
					
					if(response.totalNum == 0) {
						showEmpty();
						return;
					}
					
					pageNo ++;

					list.addAll(response.datas);
					// Call onRefreshComplete when the list has been refreshed.
					mPullToRefreshListView.onRefreshComplete();
					if (!response.hasNextPage) {
						mPullToRefreshListView.setMode(Mode.DISABLED);
					}
					mAdpter.notifyDataSetChanged();
					showContent();
				} else {
					showLoadError(SetLocationDetailActivity.this);
					ToastHelper.showToastInBottom(SetLocationDetailActivity.this, response.respCodeMsg);
				}
			}
			
		}, this);
		startRequest(mCommunityRequest);		
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
			Listener<Dictionary> listenre, ErrorListener errorListener) {			
		if(mDictionaryRequest != null) {
			mDictionaryRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mDictionaryRequest = new DictionaryRequest(url, paramsList, this, this);
		startRequest(mDictionaryRequest);		
	}
	
	/**
	 * 获取请求参数,请按照接口文档列表顺序排列
	 * @return
	 */
	private List<String> getBaseRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG21", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam(MetaUtil.readMeta(this, Constans.APP_CHANNEL), 20));
		String type = "";
		String id = "";
		String code = "";
		if (item.dictionaryId.equals("0")) {
			type = "province";
		} else if(item.dictionaryId.equals("1")) {
//			type = "city";
			id = item.superDictionaryId;
		} else if(item.dictionaryId.equals("2")) {
//			type = "country";
			id = item.superDictionaryId;
		} else if (item.dictionaryId.equals("3")) {
			id = item.superDictionaryId;
		}
		
		params.add(ParamsUtil.getReqParam(type, 64));
		params.add(ParamsUtil.getReqParam(id, 64));
		params.add(ParamsUtil.getReqParam(code, 64));
		
		params.add(ParamsUtil.getReqIntParam(pageNo, 3));
		params.add(ParamsUtil.getReqIntParam(pageSize, 2));
		
		return params;
	}
	
	private List<String> getCommunityRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG18", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam(MetaUtil.readMeta(this, Constans.APP_CHANNEL), 20));
		List<DictionaryItem> list = SCApp.getInstance().getList();
		String provinceCode = list.get(0).getDictionaryCode() == null ? "" : list.get(0).getDictionaryCode();
		String cityCode = list.get(1).getDictionaryCode() == null ? "" : list.get(1).getDictionaryCode();
		String communityCode = list.get(2).getDictionaryCode() == null ? "" : list.get(2).getDictionaryCode();
		
		params.add(ParamsUtil.getReqParam(provinceCode, 4));
		params.add(ParamsUtil.getReqParam(cityCode, 6));
		params.add(ParamsUtil.getReqParam(communityCode, 4));
		mKeyword = mKeyword == null ? "" : mKeyword;
		params.add(ParamsUtil.getReqParam(mKeyword, 128));
		
		params.add(ParamsUtil.getReqIntParam(pageNo, 3));
		params.add(ParamsUtil.getReqIntParam(pageSize, 2));
		
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


	@Override
	public void onResponse(Dictionary response) {
		if(response.respCode.equals(RespCode.SUCCESS)) {
			pageNo ++;
			if(response.totalNum == 0) {
				showEmpty();
				return;
			}
			list.addAll(response.datas);
			// Call onRefreshComplete when the list has been refreshed.
			mPullToRefreshListView.onRefreshComplete();
			if (!response.hasNextPage) {
				mPullToRefreshListView.setMode(Mode.DISABLED);
			}
			mAdpter.notifyDataSetChanged();
			showContent();
		} else {
			showLoadError(this);
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}
	

}
