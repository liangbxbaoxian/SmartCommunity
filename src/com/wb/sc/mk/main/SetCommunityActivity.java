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

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.adapter.DictionaryAdapter;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.Dictionary;
import com.wb.sc.bean.Dictionary.DictionaryItem;
import com.wb.sc.bean.SentHome;
import com.wb.sc.task.OneKmRequest;

public class SetCommunityActivity extends BaseActivity implements OnMenuItemClickListener {

	private PullToRefreshListView mPullToRefreshListView;
	private DictionaryAdapter mAdpter;
	
	private String mKeyword;
	private int pageNo;
	private int pageSize = 10;
	private OneKmRequest mOneKmRequest;
	
	private List<DictionaryItem> list = new ArrayList<DictionaryItem>();
	
	public String longitude;   // 经度
	public String latitude;     // 维度
	public String merchantCategoryId;  // 商户类别
	private String merchantName;
	
	private LocationClient mLocationClient;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor="gcj02";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_location);
		getIntentData();
		initItem();
		initView();
		
//		getGps();
		
//		showLoading();
		
//		requestBase(getBaseRequestParams(), this, this);
	}
	
	@Override
	protected void onStop() {
//		mLocationClient.stop();
		super.onStop();
	}
	
	private void initItem() {
		String name [] = {"选择省", "选择市", "选择区", "选择社区"};
		Dictionary dictionary = new Dictionary();
		for (int i = 0; i < name.length; i++) {
			DictionaryItem item = dictionary.new DictionaryItem();
			item.dictionaryId = "" + i;
			item.dictionaryName = name[i];
			list.add(item);
		}
	}
	
	private void getGps() {
		mLocationClient = SCApp.getInstance().mLocationClient;
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				mLocationClient.stop();
				longitude = arg0.getLongitude() + "";
				latitude = arg0.getLatitude() + "";
//				requestBase(getBaseRequestParams(), SetCommunityActivity.this, SetCommunityActivity.this);
			}
			
		});
		InitLocation();
		mLocationClient.start();
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
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_scroll);
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//				new GetDataTask().execute();
				pageNo = 1;
				list.clear();
//				requestBase(getBaseRequestParams(), SetCommunityActivity.this, SetCommunityActivity.this);
			}
		});
		
		mPullToRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
//				requestBase(getBaseRequestParams(), SetCommunityActivity.this, SetCommunityActivity.this);
			}
		});
		
		
		mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(SetCommunityActivity.this, SetLocationDetailActivity.class);
				DictionaryItem obj = list.get(position -1);
				Bundle bundle = new Bundle();
				bundle.putSerializable("user", obj);
				intent.putExtras(bundle);
//				intent.putExtra("merchantName", list.get(position).merchantName);
				startActivity(intent);
			}
		});
		
		
		initData();
		mAdpter = new DictionaryAdapter(SetCommunityActivity.this, list);
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
		mKeyword = intent.getStringExtra("mKeyword");
		pageNo = 1;
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
//	private void requestBase(List<String> paramsList,	 
//			Listener<OneKm> listenre, ErrorListener errorListener) {			
//		if(mOneKmRequest != null) {
//			mOneKmRequest.cancel();
//		}	
//		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
//		mOneKmRequest = new OneKmRequest(url, paramsList, this, this);
//		startRequest(mOneKmRequest);		
//	}
	
	/**
	 * 获取请求参数,请按照接口文档列表顺序排列
	 * @return
	 */
//	private List<String> getBaseRequestParams() {
//		List<String> params = new ArrayList<String>();
//		params.add(ParamsUtil.getReqParam("FG46", 4));
//		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
//		params.add(ParamsUtil.getReqParam(MetaUtil.readMeta(this, Constans.APP_CHANNEL), 20));
//		params.add(ParamsUtil.getReqParam(merchantName, 32));
//		params.add(ParamsUtil.getReqParam(longitude, 128));
//		params.add(ParamsUtil.getReqParam(latitude, 128));
//		params.add(ParamsUtil.getReqParam(merchantCategoryId, 32));
//		params.add(ParamsUtil.getReqParam(pageNo + "", 3));
//		params.add(ParamsUtil.getReqParam(pageSize + "", 2));
//		
//		return params;
//	}


//	@Override
//	public void onReload() {
//		requestBase(getBaseRequestParams(), this, this);
//	}

}
