package com.wb.sc.mk.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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
import com.wb.sc.bean.PostList;
import com.wb.sc.config.IntentExtraConfig;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.config.RespParams;
import com.wb.sc.task.PostListRequest;

public class PostListActivity extends BaseHeaderActivity implements Listener<PostList>, 
	ErrorListener, OnItemClickListener, ReloadListener{
	
	private PullToRefreshListView mPullListView;
	private PullRefreshListViewHelper mPullHelper;
	private ListView mListView;
	private PostListAdapter mAdapter;
	private PageInfo mPage = new PageInfo();
	private int loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE;
		
	private PostListRequest mPostListRequest;
	private PostList mPostList;
	
	private int postType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_postlist);
		
		getIntentData();
		initHeader(getResources().getStringArray(R.array.post_type)[postType]);
		initView();	
		
		test();
	}
			
	@Override
	public void getIntentData() {
		postType = getIntent().getIntExtra(IntentExtraConfig.POST_TYPE, 
				IntentExtraConfig.POST_TYPE_SHARE);
	}
	
	@Override
	public void initView() {
		mPullListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);		
		mPullListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				//处理下拉刷新
				mPage.pageNo = 1;
				startPostListRequest();
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
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//此处设置菜单		
		
		startPostListRequest();
		
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * 菜单点击处理
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {			
		return super.onOptionsItemSelected(item);
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
		//requestPostList(Method.POST, "请求方法", getPostListRequestParams(), this, this);
	}
		
	/**
	 * 获取请求参数
	 * @return
	 */
	private Map<String, String> getPostListRequestParams() {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put(RespParams.PAGE_SIZE, mPage.pageSize+"");
		params.put(RespParams.PAGE_NO, mPage.pageNo+"");	
			
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
	private void requestPostList(int method, String methodUrl, Map<String, String> params,	 
			Listener<PostList> listenre, ErrorListener errorListener) {			
		if(mPostListRequest != null) {
			mPostListRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL + methodUrl;
		mPostListRequest = new PostListRequest(method, url, params, listenre, errorListener);
		startRequest(mPostListRequest);		
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
			ToastHelper.showToastInBottom(this, response.respMsg);
		}
	}
	
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
