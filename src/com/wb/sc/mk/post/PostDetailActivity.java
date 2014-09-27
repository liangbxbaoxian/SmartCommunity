package com.wb.sc.mk.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.wb.sc.R;
import com.wb.sc.R.layout;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.activity.base.ReloadListener;
import com.wb.sc.adapter.CommentListAdapter;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.config.RespParams;
import com.common.net.volley.VolleyErrorHelper;
import com.common.util.PageInfo;
import com.common.widget.ToastHelper;
import com.common.widget.helper.PullRefreshListViewHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.wb.sc.bean.CommentList;
import com.wb.sc.bean.PostDetail;
import com.wb.sc.task.CommentListRequest;
import com.wb.sc.task.PostDetailRequest;

public class PostDetailActivity extends BaseHeaderActivity implements Listener<PostDetail>, 
	ErrorListener, ReloadListener, OnItemClickListener{
	
	//帖子详情
	private PostDetailRequest mPostDetailRequest;
	private PostDetail mPostDetail;
	
	//评论
	private PullToRefreshListView mPullListView;
	private PullRefreshListViewHelper mPullHelper;
	private ListView mListView;
	private CommentListAdapter mAdapter;
	private PageInfo mPage = new PageInfo();
	private int loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE;
		
	private CommentListRequest mCommentListRequest;
	private CommentList mCommentList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_postdetail);
		
		getIntentData();
		initHeader(getResources().getString(R.string.ac_post_detail));
		initView();				
		
		test();
	}
			
	@Override
	public void getIntentData() {
		
	}
	
	@Override
	public void initView() {
		mPullListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);		
		mPullListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				//处理下拉刷新
				mPage.pageNo = 1;
				startCommentRequest();
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
				if(loadState == PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE && mCommentList.hasNextPage) {
					loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOADING;
					mPage.pageNo++;		
					startCommentRequest();
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
		
		mPage = new PageInfo();
		mPullHelper = new PullRefreshListViewHelper(this, mListView, mPage.pageSize);
		mPullHelper.setBottomClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(loadState == PullRefreshListViewHelper.BOTTOM_STATE_LOAD_FAIL) {
					//加载失败，点击重试
					loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOADING;
					mPullHelper.setBottomState(loadState);		
					startCommentRequest();
				}
			}
		});
	}
	
	/**
	 * 列表选项点击的处理
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	}
		
	/**
	 * 获取请求参数
	 * @return
	 */
	private Map<String, String> getPostDetailRequestParams() {
		Map<String, String> params = new HashMap<String, String>();
				
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
	private void requestPostDetail(int method, String methodUrl, Map<String, String> params,	 
			Listener<PostDetail> listenre, ErrorListener errorListener) {			
		if(mPostDetailRequest != null) {
			mPostDetailRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL + methodUrl;
		mPostDetailRequest = new PostDetailRequest(method, url, params, listenre, errorListener);
		startRequest(mPostDetailRequest);		
	}
	
	/**
	 * 网络请求错误处理
	 *
	 */
	@Override
	public void onErrorResponse(VolleyError error) {	
		showLoadError(this);	
		ToastHelper.showToastInBottom(getApplicationContext(), VolleyErrorHelper.getErrorMessage(this, error));
	}
	
	/**
	 * 重新加载请求
	 */
	@Override
	public void onReload() {
		showLoading();
		//requestPostDetail(Method.GET, "请求方法", getPostDetailRequestParams(), this, this);			
	}
	
	/**
	 * 请求完成，处理UI更新
	 */
	@Override
	public void onResponse(PostDetail response) {		
		showContent();	
		if(response.respCode == RespCode.SUCCESS) {			
				mPostDetail = response;
		} else {
			ToastHelper.showToastInBottom(this, response.respMsg);
		}
	}
	
	/**
	 * 
	 * @描述:启动请求
	 */
	private void startCommentRequest() {
		//requestComment(Method.GET, "请求方法", getCommentRequestParams(), this, this);
	}
	
	/**
	 * 获取请求参数
	 * @return
	 */
	private Map<String, String> getCommentRequestParams() {
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
	private void requestComment(int method, String methodUrl, Map<String, String> params,	 
			Listener<CommentList> listenre, ErrorListener errorListener) {			
		if(mCommentListRequest != null) {
			mCommentListRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL + methodUrl;
		mCommentListRequest = new CommentListRequest(method, url, params, listenre, errorListener);
		startRequest(mCommentListRequest);		
	}
	
	/**
	 * 
	 * @描述：最新评论处理
	 * @作者：liang bao xian
	 * @时间：2014年9月27日 上午11:27:15
	 */
	class CommentListener implements Listener<CommentList>, ErrorListener, ReloadListener {
		/**
		 * 请求完成，处理UI更新
		 */
		@Override
		public void onResponse(CommentList response) {		
			showContent();	
			if(response.respCode == RespCode.SUCCESS) {			
				if(response.datas.size() <= 0) {
					showEmpty();
					return;
				}
				
				if(mPage.pageNo == 1) {
					mCommentList = response;
					// set adapter
					showContent();
				} else {
					mCommentList.hasNextPage = response.hasNextPage;
					mCommentList.datas.addAll(response.datas);
					//adapter notifyDataSetChanged
				}
				
				loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE;	
				if(mCommentList.hasNextPage) {
					mPullHelper.setBottomState(PullRefreshListViewHelper.BOTTOM_STATE_LOADING);
				} else {
					mPullHelper.setBottomState(PullRefreshListViewHelper.BOTTOM_STATE_NO_MORE_DATE);
				}		
			} else {
				ToastHelper.showToastInBottom(mActivity, response.respMsg);
			}
		}
		
		@Override
		public void onReload() {
			mPage.pageNo = 1;		
			showLoading();
			startCommentRequest();
		}
	
		/**
		 * 网络请求错误处理
		 *
		 */
		@Override
		public void onErrorResponse(VolleyError error) {	
			mPullListView.onRefreshComplete();		
			ToastHelper.showToastInBottom(getApplicationContext(), 
					VolleyErrorHelper.getErrorMessage(mActivity, error));
		
			if(mPage.pageNo == 1) {
				showLoadError(this);
			} else {
				loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_FAIL;
				mPullHelper.setBottomState(PullRefreshListViewHelper.BOTTOM_STATE_LOAD_FAIL, mPage.pageSize);
			}
		}
	}
	
	private void test() {
		mCommentList = new CommentList();
		mCommentList.datas = new ArrayList<CommentList.Item>();
		for(int i=0; i<10; i++) {
			mCommentList.datas.add(mCommentList.new Item());
		}
		mAdapter = new CommentListAdapter(this, mCommentList);
		mListView.setAdapter(mAdapter);
	}
}
