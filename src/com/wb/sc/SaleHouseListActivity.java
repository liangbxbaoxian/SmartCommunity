package com.wb.sc;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.util.PageInfo;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.activity.base.ReloadListener;
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
import com.wb.sc.bean.SaleHouseList;
import com.wb.sc.task.SaleHouseListRequest;
import com.wb.sc.util.ParamsUtil;

public class SaleHouseListActivity extends BaseActivity implements Listener<SaleHouseList>, 
	ErrorListener, OnItemClickListener, ReloadListener{
	
	private PullToRefreshListView mPullListView;
	private PullRefreshListViewHelper mPullHelper;
	private ListView mListView;
	private PageInfo mPage = new PageInfo();
	private int loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE;
		
	private SaleHouseListRequest mSaleHouseListRequest;
	private SaleHouseList mSaleHouseList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_salehouselist);
		
		getIntentData();
		initView();			
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
				startSaleHouseListRequest();
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
				if(loadState == PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE && mSaleHouseList.hasNextPage) {
					loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOADING;
					mPage.pageNo++;		
					startSaleHouseListRequest();
				}
			}
		});
		
		//设置刷新时请允许滑动的开关使�?   		
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
					//加载失败，点击重�?
					loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOADING;
					mPullHelper.setBottomState(loadState);		
					startSaleHouseListRequest();
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//此处设置菜单		
		setDisplayHomeAsUpEnabled(true);
		setDisplayShowHomeEnabled(false);
		
		startSaleHouseListRequest();
		
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
	 * 列表选项点击的处�?
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	}
	
	/**
	 * 
	 * @描述:启动请求
	 */
	private void startSaleHouseListRequest() {
		//requestSaleHouseList(getSaleHouseListRequestParams(), this, this);
	}
	
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getSaleHouseListRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("填写接口文档中的消息类型", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
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
	private void requestSaleHouseList(List<String> params,	 
			Listener<SaleHouseList> listenre, ErrorListener errorListener) {			
		if(mSaleHouseListRequest != null) {
			mSaleHouseListRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mSaleHouseListRequest = new SaleHouseListRequest(url, params, listenre, errorListener);
		startRequest(mSaleHouseListRequest);		
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
		startSaleHouseListRequest();
	}
	
	/**
	 * 请求完成，处理UI更新
	 */
	@Override
	public void onResponse(SaleHouseList response) {		
		showContent();	
		if(response.respCode.equals(RespCode.SUCCESS)) {			
			if(response.datas.size() <= 0) {
				showEmpty();
				return;
			}
			
			if(mPage.pageNo == 1) {
				mSaleHouseList = response;
				// set adapter
				showContent();
			} else {
				mSaleHouseList.hasNextPage = response.hasNextPage;
				mSaleHouseList.datas.addAll(response.datas);
				//adapter notifyDataSetChanged
			}
			
			loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE;	
			if(mSaleHouseList.hasNextPage) {
				mPullHelper.setBottomState(PullRefreshListViewHelper.BOTTOM_STATE_LOADING);
			} else {
				mPullHelper.setBottomState(PullRefreshListViewHelper.BOTTOM_STATE_NO_MORE_DATE);
			}		
		} else {
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}
}
