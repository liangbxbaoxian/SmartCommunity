package com.wb.sc.mk.personal;

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
import com.wb.sc.adapter.MsgListAdapter;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.Msg;
import com.wb.sc.bean.PostType;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.task.MsgRequest;
import com.wb.sc.task.PostTypeRequest;
import com.wb.sc.util.Constans;
import com.wb.sc.util.MetaUtil;
import com.wb.sc.util.ParamsUtil;

public class MsgCenterActivity extends BaseHeaderActivity implements 
	OnItemClickListener, ReloadListener, Listener<Msg>, ErrorListener{
	
	private Spinner typeSp;
	private int  spinnerPosition = 0;
	private String reqType = "FG39";
	private String msgType = "00";
	
	private PullToRefreshListView mPullListView;
	private PullRefreshListViewHelper mPullHelper;
	private ListView mListView;
	private PageInfo mPage = new PageInfo();
	private int loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE;
	private MsgListAdapter mAdapter;
	private Msg msg;
	
	private MsgRequest MmsgCenterRequest;
	
	private PostTypeListener mPostTypeListener = new PostTypeListener();
	private PostTypeRequest mPostTypeRequest;
	private PostType mPostType;
	private String[] types;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_header_layout, R.layout.activity_msg_center);
		
		getIntentData();
		initHeader(getResources().getString(R.string.ac_msg_center));
		initView();		
	
		
		showLoading();
		
	    requestPostType(getPostTypeRequestParams("USER_MSG_TYPE"), mPostTypeListener, this);
//        showLoading();		        
		requestBase(getBaseRequestParams(), this, this);
	}
			
	@Override
	public void getIntentData() {
		
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
//		if(mPostTypeRequest != null) {
//			mPostTypeRequest.cancel();
//		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mPostTypeRequest = new PostTypeRequest(url, params, listenre, errorListener);
		startRequest(mPostTypeRequest);		
	}
	
	
	/**
	 * 获取帖子分类请求参数
	 * @return
	 */
	private List<String> getPostTypeRequestParams(String type) {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG21", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(type, 64));
		params.add(ParamsUtil.getReqParam("", 64));
		params.add(ParamsUtil.getReqParam("", 64));
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
//			showContent();	
			if(response.respCode.equals(RespCode.SUCCESS)) {			
				mPostType = response;
				types = new String[mPostType.datas.size()/* + 1*/];
//				types[0] = "全部"; 
				for(int i=0; i< mPostType.datas.size() ; i++) {
					types[i /*+ 1*/] = mPostType.datas.get(i).name;								
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(MsgCenterActivity.this, 
		    			R.layout.spinner_text_layout, types);
		    	adapter.setDropDownViewResource(R.layout.spinner_down_text_layout);
		    	typeSp.setAdapter(adapter);
//		    	adapter.notifyDataSetChanged();

			} else {
				ToastHelper.showToastInBottom(MsgCenterActivity.this, response.respCodeMsg);
			}
		}
	}
	
	@Override
	public void initView() {
		typeSp = (Spinner) findViewById(R.id.type);
//		String[] types = getResources().getStringArray(R.array.msg_type);
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
//    			R.layout.spinner_text_layout, types);
//    	adapter.setDropDownViewResource(R.layout.spinner_down_text_layout);
//    	typeSp.setAdapter(adapter);
    	
    	typeSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg2 != spinnerPosition) {
					spinnerPosition = arg2;
					mPage.pageNo = 1;
					msgType = mPostType.datas.get(arg2).id;
//					showLoading();	
					startMsgCenterRequest();
					mPullListView.setMode(Mode.PULL_FROM_END);
				}
			
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		mPullListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);		
		mPullListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				//处理下拉刷新
				mPage.pageNo = 1;
				startMsgCenterRequest();
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
				if(loadState == PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE && msg.hasNextPage) {
					loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOADING;
					mPage.pageNo++;		
					startMsgCenterRequest();
				}
			}
		});
		
		//设置刷新时请允许滑动的开关使能  		
		mPullListView.setScrollingWhileRefreshingEnabled(true);
		
		//设置自动刷新功能
		mPullListView.setRefreshing(false);
		
		//设置拉动模式
		mPullListView.setMode(Mode.PULL_FROM_END);
		
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
					startMsgCenterRequest();
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//此处设置菜单		
		setDisplayHomeAsUpEnabled(true);
		setDisplayShowHomeEnabled(false);
		
		startMsgCenterRequest();
		
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
		Msg.MgItem msgItem = msg.datas.get(position-1);
		int msgType = Integer.valueOf(msgItem.msgTypeNO);
//		switch(msgType) {
//		case 1:{
			Intent intent = new Intent(this, BulletinActivity.class);
			intent.putExtra("title", msgItem.msgTitle);
			intent.putExtra("content", msgItem.msgContent);
			intent.putExtra("time", msgItem.msgCreteTime);
			startActivity(intent);
//		}break;
//		
//		case 2:{
//		}break;
//		
//		case 3:{
//		}break;
//		
//		case 4:{
//		}break;
//		
//		case 5:{
//		}break;
//		
//		case 6:{
//		}break;
//		} 
	}
	
	public void bulletin() {
		Intent intent = new Intent(this, BulletinActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 
	 * @描述:启动请求
	 */
	private void startMsgCenterRequest() {
		requestBase(getBaseRequestParams(), MsgCenterActivity.this, MsgCenterActivity.this);
	}
	
	@Override
	public void onReload() {
		mPage.pageNo = 1;		
//		showLoading();
		startMsgCenterRequest();
	}
		
	/**
	 * 获取请求参数,请按照接口文档列表顺序排列
	 * @return
	 */
	private List<String> getBaseRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam(reqType, 4));  //修改系统消息
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam(MetaUtil.readMeta(this, Constans.APP_CHANNEL), 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId +"", 64));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().communityId +"", 64));
		params.add(ParamsUtil.getReqParam(msgType, 64));
		
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
	private void requestBase(List<String> paramsList,	 
			Listener<Msg> listenre, ErrorListener errorListener) {			
		if(MmsgCenterRequest != null) {
			MmsgCenterRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		MmsgCenterRequest = new MsgRequest(url, paramsList, this, this);
		startRequest(MmsgCenterRequest);		
	}
	

	@Override
	public void onErrorResponse(VolleyError error) {
		showLoadError(this);	
		ToastHelper.showToastInBottom(getApplicationContext(), VolleyErrorHelper.getErrorMessage(this, error));
	}

	@Override
	public void onResponse(Msg response) {
		if(response.respCode.equals(RespCode.SUCCESS)) {
			
			if(mPullListView.isRefreshing()) {
				mPullListView.onRefreshComplete();
			}
			
			if(response.totalNum == 0) {  //显示空
				if(msg != null && msg.datas != null && mAdapter != null) {
					msg.datas.clear();
					mAdapter.notifyDataSetChanged();
				}
			    return;
			}
			
			if (response.datas.size() > 0) {
				if(mPage.pageNo == 1) {
					msg = response;
					mAdapter = new MsgListAdapter(this, msg.datas);
					mListView.setAdapter(mAdapter);
					showContent();
				} else {					
					msg.hasNextPage = response.hasNextPage;
					msg.datas.addAll(response.datas);
					mAdapter.notifyDataSetChanged();
				}
			}

			loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE;	
			if(msg.hasNextPage) {
				mPullHelper.setBottomState(PullRefreshListViewHelper.BOTTOM_STATE_LOADING);
			} else {
				mPullHelper.setBottomState(PullRefreshListViewHelper.BOTTOM_STATE_NO_MORE_DATE);
			}					
		} else {
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}
}
