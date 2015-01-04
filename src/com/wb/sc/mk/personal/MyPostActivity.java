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
import com.wb.sc.adapter.MyForumAdpater;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.MyPost;
import com.wb.sc.bean.PostType;
import com.wb.sc.bean.SentHome;
import com.wb.sc.config.IntentExtraConfig;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.mk.main.PostActivity;
import com.wb.sc.mk.post.PostDetailActivity;
import com.wb.sc.task.MyPostRequest;
import com.wb.sc.task.PostTypeRequest;
import com.wb.sc.util.Constans;
import com.wb.sc.util.MetaUtil;
import com.wb.sc.util.ParamsUtil;

public class MyPostActivity extends BaseActivity implements OnMenuItemClickListener, OnClickListener,  Listener<MyPost>, 
ErrorListener, ReloadListener{

	private PullToRefreshListView mPullToRefreshListView;
	private MyForumAdpater mAdpter;
	
	private String mKeyword;
	private String sId;
	
	private int pageNo;
	private int pageSize = 10;
	private boolean hasNextPage;
	private String mDistrictName;
	private MyPostRequest mMyPostRequest;
	
	private List<MyPost.MyPostItem> list = new ArrayList<MyPost.MyPostItem>();
	
	private Spinner mSpinner;
	
	private View personalV;
	private View publicV;
	
	//发帖类型
	private PostTypeRequest mPostTypeRequest;
	private PostTypeListener mPostTypeListener = new PostTypeListener();
	private PostType mPostType;
	
	private int postion = 0;
	private String[] types;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_forum);
		getIntentData();
		initView();
		
		
		showLoading();		
	    requestPostType(getPostTypeRequestParams(), mPostTypeListener, this);
		requestBase(getBaseRequestParams(), this, this);
	}
	
	
	/**
	 * 获取帖子分类请求参数
	 * @return
	 */
	private List<String> getPostTypeRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG33", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		params.add(ParamsUtil.getReqIntParam(1, 3));
		params.add(ParamsUtil.getReqIntParam(10, 2));
		return params;
	}
	

    
    /**
	 * 
	 * @描述：帖子分类监听
	 * @作者：liang bao xian
	 * @时间：2014年10月27日 上午8:51:09
	 */
	class PostTypeListener implements Listener<PostType>{
		/**
		 * 请求完成，处理UI更新
		 */
		@Override
		public void onResponse(PostType response) {		
			showContent();	
			if(response.respCode.equals(RespCode.SUCCESS)) {			
				mPostType = response;
				types = new String[mPostType.datas.size() + 1];
				types[0] = "全部"; 
				for(int i=0; i< mPostType.datas.size() ; i++) {
					types[i + 1] = mPostType.datas.get(i).name;								
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyPostActivity.this, 
		    			R.layout.spinner_text_layout, types);
		    	adapter.setDropDownViewResource(R.layout.spinner_down_text_layout);
		    	mSpinner.setAdapter(adapter);

			} else {
				ToastHelper.showToastInBottom(MyPostActivity.this, response.respCodeMsg);
			}
		}
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	public void back (View view) {
		finish();
     }
	
	/**
	 * 执行任务请求
	 * @param method
	 * @param url
	 * @param params
	 * @param listenre
	 * @param errorListener
	 */	
	private void requestPostType(List<String> params,	 
			Listener<PostType> listenre, ErrorListener errorListener) {			
		if(mPostTypeRequest != null) {
			mPostTypeRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mPostTypeRequest = new PostTypeRequest(url, params, listenre, errorListener);
		startRequest(mPostTypeRequest);		
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
				requestBase(getBaseRequestParams(), MyPostActivity.this, MyPostActivity.this);
			}
		});
		
		
		mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MyPost.MyPostItem item  = list.get(position-1);
				Intent intent = new Intent(MyPostActivity.this, PostDetailActivity.class);		
				intent.putExtra(IntentExtraConfig.DETAIL_ID, item.postId);
				intent.putExtra(IntentExtraConfig.POST_TYPE, item.postType);
				startActivity(intent);
			}
		});
		
		
		initData();
		mAdpter = new MyForumAdpater(MyPostActivity.this, list);
		mPullToRefreshListView.setDividerDrawable(null);
		mPullToRefreshListView.setMode(Mode.BOTH);
		mPullToRefreshListView.setAdapter(mAdpter);
		
	      // 初始化控件
		mSpinner = (Spinner) findViewById(R.id.spinner1);
//		// 建立数据源
//		String[] mItems = getResources().getStringArray(R.array.msg_type);
//		// 建立Adapter并且绑定数据源
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
//    			R.layout.spinner_text_layout, mItems);
//    	adapter.setDropDownViewResource(R.layout.spinner_down_text_layout);
//		mSpinner.setAdapter(adapter);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				if (postion != arg2) {
					postion = arg2;
					pageNo = 1;
					list.clear();
					mAdpter.notifyDataSetChanged();
					requestBase(getBaseRequestParams(), MyPostActivity.this, MyPostActivity.this);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
//		personalV = findViewById(R.id.personal_repairs);
//		personalV.setSelected(true);
//		personalV.setOnClickListener(this);
//		publicV = findViewById(R.id.public_repairs);
//		publicV.setOnClickListener(this);
		
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
	
	public void share(View view) {
		Intent intent = new Intent(this, PostActivity.class);
		startActivity(intent);
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
		// TODO Auto-generated method stub
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
	
	/**
	 * 执行任务请求
	 * @param method
	 * @param url
	 * @param params
	 * @param listenre
	 * @param errorListener
	 */	
	private void requestBase(List<String> paramsList,	 
			Listener<MyPost> listenre, ErrorListener errorListener) {			
		if(mMyPostRequest != null) {
			mMyPostRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mMyPostRequest = new MyPostRequest(url, paramsList, this, this);
		startRequest(mMyPostRequest);		
	}
	
	
	/**
	 * 获取请求参数,请按照接口文档列表顺序排列
	 * @return
	 */
	private List<String> getBaseRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG22", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam(MetaUtil.readMeta(this, Constans.APP_CHANNEL), 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId +"", 64));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().communityId +"", 64));  //暂时不知道这个id 是不是社区id
		if (postion != 0) {
			params.add(ParamsUtil.getReqParam(mPostType.datas.get(postion - 1).id +"", 8));  //暂时不知道这个id 是不是社区id
		} else {
			params.add(ParamsUtil.getReqParam("", 8));  //暂时不知道这个id 是不是社区id
		}
		params.add(ParamsUtil.getReqIntParam(pageNo, 3));
		params.add(ParamsUtil.getReqIntParam(pageSize, 2));
		
		return params;
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
	public void onResponse(MyPost response) {
		if(response.respCode.equals(RespCode.SUCCESS)) {
			pageNo ++;

			list.addAll(response.datas);
			// Call onRefreshComplete when the list has been refreshed.
			
//			if (postion >= 1) {
//				mAdpter.getFilter(types[postion]);
//			} else {
//				mAdpter.stateFilter(false);
//			}
			
			mPullToRefreshListView.onRefreshComplete();
			if (!response.hasNextPage) {
				mPullToRefreshListView.setMode(Mode.DISABLED);
			}
			mAdpter.notifyDataSetChanged();
			if(response.totalNum == 0) {  //显示空
			    showEmpty();
			    return;
			 } else {
				 showContent();
			 }
			
		} else {
			showLoadError(this);
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}
	
	

}
