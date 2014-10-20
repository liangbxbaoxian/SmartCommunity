package com.wb.sc.mk.personal;

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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.util.PageInfo;
import com.wb.sc.R;
import com.wb.sc.R.id;
import com.wb.sc.R.layout;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.activity.base.ReloadListener;
import com.wb.sc.adapter.MsgListAdapter;
import com.wb.sc.adapter.PublicInfoListAdapter;
import com.wb.sc.bean.MsgList;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.config.RespParams;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.common.widget.helper.PullRefreshListViewHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

public class PublicInfoActivity extends BaseHeaderActivity implements 
	OnItemClickListener, ReloadListener{
	
	private Spinner typeSp;
	
	private PullToRefreshListView mPullListView;
	private PullRefreshListViewHelper mPullHelper;
	private ListView mListView;
	private PageInfo mPage = new PageInfo();
	private int loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE;
	private PublicInfoListAdapter mAdapter;
	private MsgList mMsgList;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_info);
		
		getIntentData();
		initHeader(getResources().getString(R.string.ac_public_info));
		initView();		
		
		test();
	}
			
	@Override
	public void getIntentData() {
		
	}
	
	@Override
	public void initView() {
		typeSp = (Spinner) findViewById(R.id.type);
		String[] types = getResources().getStringArray(R.array.msg_type);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
    			R.layout.spinner_text_layout, types);
    	adapter.setDropDownViewResource(R.layout.spinner_down_text_layout);
    	typeSp.setAdapter(adapter);
		
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
//				if(loadState == PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE && mMsgCenter.hasNextPage) {
//					loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOADING;
//					mPage.pageNo++;		
//					startMsgCenterRequest();
//				}
			}
		});
		
		//设置刷新时请允许滑动的开关使能  		
		mPullListView.setScrollingWhileRefreshingEnabled(true);
		
		//设置自动刷新功能
		mPullListView.setRefreshing(false);
		
		//设置拉动模式
		mPullListView.setMode(Mode.BOTH);
		
		mListView = mPullListView.getRefreshableView();
		mListView.setOnItemClickListener(this);
		
		mPage = new PageInfo();
		mPullHelper = new PullRefreshListViewHelper(this, mListView, mPage.pageSize);
		mPullHelper.setBottomClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(loadState == PullRefreshListViewHelper.BOTTOM_STATE_LOAD_FAIL) {
					//加载失败，点击重�?
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
		bulletin();
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
		//requestMsgCenter(Method.GET, "请求方法", getMsgCenterRequestParams(), this, this);
	}
	
	@Override
	public void onReload() {
		mPage.pageNo = 1;		
		showLoading();
		startMsgCenterRequest();
	}
	
	private void test() {
		mMsgList = new MsgList();
		mMsgList.datas = new ArrayList<MsgList.Item>();
		for(int i=0; i<10; i++) {
			mMsgList.datas.add(mMsgList.new Item());
		}
		mAdapter = new PublicInfoListAdapter(this, mMsgList);
		mListView.setAdapter(mAdapter);
	}
}
