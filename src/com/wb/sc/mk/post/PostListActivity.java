package com.wb.sc.mk.post;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.net.volley.VolleyErrorHelper;
import com.common.util.PageInfo;
import com.common.widget.ToastHelper;
import com.common.widget.helper.PullRefreshListViewHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.activity.base.ReloadListener;
import com.wb.sc.adapter.PostListAdapter;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.PostList;
import com.wb.sc.bean.PostType;
import com.wb.sc.config.IntentExtraConfig;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.mk.main.PostActivity;
import com.wb.sc.task.PostListRequest;
import com.wb.sc.task.PostTypeRequest;
import com.wb.sc.util.ParamsUtil;

/**
 * 
 * @描述：社区帖子列表
 * @作者：liang bao xian
 * @时间：2014年10月25日 上午9:49:11
 */
public class PostListActivity extends BaseHeaderActivity implements Listener<PostList>, 
	ErrorListener, OnItemClickListener, ReloadListener{
	
	//获取帖子分类
	private Spinner typeSp;
	private int postType;
	private int currentTypePos;
	private PostTypeRequest mPostTypeRequest;
	private PostTypeListener mPostTypeListener = new PostTypeListener();
	private PostType mPostType;
	
	//获取帖子列表
	private PullToRefreshListView mPullListView;
	private PullRefreshListViewHelper mPullHelper;
	private ListView mListView;
	private PostListAdapter mAdapter;
	private PageInfo mPage = new PageInfo();
	private int loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE;
	private PostListRequest mPostListRequest;
	private PostList mPostList;
	
	//我要分享
	private Button shareBtn;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_postlist);
		
		getIntentData();
		initHeaderBack();
		initView();	
		
		showLoading();
		requestPostType(getPostTypeRequestParams(), mPostTypeListener, this);
	}
	
	@Override
	public void getIntentData() {
		postType = getIntent().getIntExtra(IntentExtraConfig.POST_TYPE, 
				IntentExtraConfig.POST_TYPE_SHARE);
	}
	
	@Override
	public void initView() {
		typeSp = (Spinner) findViewById(R.id.type);
		typeSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, 
					long id) {
				if(currentTypePos != position) {
					currentTypePos = position;	
					mPage.pageNo = 1;
					startPostListRequest();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
//		String[] types = getResources().getStringArray(R.array.post_type);
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
//    			R.layout.spinner_text_layout, types);
//    	adapter.setDropDownViewResource(R.layout.spinner_down_text_layout);
//    	typeSp.setAdapter(adapter);
		
		mPullListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);		
		mPullListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				//处理下拉刷新
				if(mPostType != null) {
					mPage.pageNo = 1;
					startPostListRequest();
				}
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
				if(loadState == PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE && mPostList.hasNextPage) {
					loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOADING;
					mPage.pageNo++;		
					startPostListRequest();
				}
			}
		});
		
		//设置刷新时请允许滑动的开关使能   		
		mPullListView.setScrollingWhileRefreshingEnabled(true);
		
		//设置自动刷新功能
		mPullListView.setRefreshing(false);
		
		//设置拉动模式
		mPullListView.setMode(Mode.PULL_FROM_START);
		
		mListView = mPullListView.getRefreshableView();
		mListView.setOnItemClickListener(this);
		
		mPullHelper = new PullRefreshListViewHelper(this, mListView, mPage.pageSize);
		mPullHelper.setBottomClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(loadState == PullRefreshListViewHelper.BOTTOM_STATE_LOAD_FAIL) {
					//加载失败，点击重试
					loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOADING;
					mPullHelper.setBottomState(loadState);		
					startPostListRequest();
				}
			}
		});
		
		shareBtn = (Button) findViewById(R.id.share);
		shareBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PostListActivity.this, PostActivity.class);
				startActivity(intent);
			}
		});
	}
		
	/**
	 * 列表选项点击的处理
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, PostDetailActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 
	 * @描述:启动请求
	 */
	private void startPostListRequest() {
		requestPostList(getPostListRequestParams(), this, this);
	}
	
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getPostListRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG34", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		params.add(ParamsUtil.getReqParam("0", 2));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().communityId, 64));
		params.add(ParamsUtil.getReqParam(mPostType.datas.get(currentTypePos).id, 8));
		params.add(ParamsUtil.getReqParam("01", 2));
		params.add(ParamsUtil.getReqIntParam(mPage.pageNo, 3));
		params.add(ParamsUtil.getReqIntParam(mPage.pageSize, 2));
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
	private void requestPostList(List<String> params,	 
			Listener<PostList> listenre, ErrorListener errorListener) {			
		if(mPostListRequest != null) {
			mPostListRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mPostListRequest = new PostListRequest(url, params, listenre, errorListener);
		startRequest(mPostListRequest);		
	}
	
	public void share(View view) {
		Intent intent = new Intent(PostListActivity.this, PostActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 网络请求错误处理
	 *
	 */
	@Override
	public void onErrorResponse(VolleyError error) {	
		mPullListView.onRefreshComplete();		
		ToastHelper.showToastInBottom(getApplicationContext(), VolleyErrorHelper.getErrorMessage(this, error));
		
		if(mPage.pageNo == 1) {
			showLoadError(this);
		} else {
			loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_FAIL;
			mPullHelper.setBottomState(PullRefreshListViewHelper.BOTTOM_STATE_LOAD_FAIL, mPage.pageSize);
		}
	}
	
	@Override
	public void onReload() {
		mPage.pageNo = 1;		
		showLoading();
		startPostListRequest();
	}
	
	/**
	 * 请求完成，处理UI更新
	 */
	@Override
	public void onResponse(PostList response) {		
		showContent();	
		if(response.respCode == RespCode.SUCCESS) {			
			if(response.datas.size() <= 0) {
				showEmpty();
				return;
			}
			
			if(mPage.pageNo == 1) {
				mPostList = response;
				// set adapter
				showContent();
			} else {
				mPostList.hasNextPage = response.hasNextPage;
				mPostList.datas.addAll(response.datas);
				//adapter notifyDataSetChanged
			}
			
			loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE;	
			if(mPostList.hasNextPage) {
				mPullHelper.setBottomState(PullRefreshListViewHelper.BOTTOM_STATE_LOADING);
			} else {
				mPullHelper.setBottomState(PullRefreshListViewHelper.BOTTOM_STATE_NO_MORE_DATE);
			}		
		} else {
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
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
				String[] types = new String[mPostType.datas.size()];
				for(int i=0; i<mPostType.datas.size(); i++) {
					types[i] = mPostType.datas.get(i).name;								
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(PostListActivity.this, 
		    			R.layout.spinner_text_layout, types);
		    	adapter.setDropDownViewResource(R.layout.spinner_down_text_layout);
		    	typeSp.setAdapter(adapter);
		    	currentTypePos = 0;	
		    	
		    	//请求帖子列表
		    	mPage.pageNo = 1;
		    	startPostListRequest();
			} else {
				ToastHelper.showToastInBottom(mActivity, response.respCodeMsg);
			}
		}
	}
	
	/**
	 * 
	 * @描述:测试数据
	 */
	private void test() {
		mPostList = new PostList();
		mPostList.datas = new ArrayList<PostList.Item>();
		for(int i=0; i<10; i++) {
			mPostList.datas.add(mPostList.new Item());
		}
		mAdapter = new PostListAdapter(this, mPostList);
		mListView.setAdapter(mAdapter);
	}
}
