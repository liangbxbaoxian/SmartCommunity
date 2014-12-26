package com.wb.sc.mk.butler;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.wb.sc.activity.base.BaseExtraLayoutFragment;
import com.wb.sc.activity.base.BaseNetActivity;
import com.wb.sc.activity.base.ReloadListener;
import com.wb.sc.adapter.SaleHouseListAdapter;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.SaleHouseList;
import com.wb.sc.bean.SaleHouseList.Item;
import com.wb.sc.config.IntentExtraConfig;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.dialog.ConfirmDialog;
import com.wb.sc.task.SaleHouseListRequest;
import com.wb.sc.util.ParamsUtil;

/**
 * 
 * @描述：可出售/出租房源列表
 * @作者：liang bao xian
 * @时间：2014年11月9日 下午3:58:37
 */
public class HouseTradeListFragment extends BaseExtraLayoutFragment implements Listener<SaleHouseList>, 
	ErrorListener, OnItemClickListener, ReloadListener{
	
	private PullToRefreshListView mPullListView;
	private PullRefreshListViewHelper mPullHelper;
	private ListView mListView;
	private SaleHouseListAdapter adapter;
	private PageInfo mPage = new PageInfo();
	private int loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE;
		
	private SaleHouseListRequest mSaleHouseListRequest;
	private SaleHouseList mSaleHouseList;
	
	private BaseNetActivity mActivity;
	
	int tradeType;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (BaseNetActivity) activity;
		
		tradeType = getArguments().getInt(IntentExtraConfig.HOUSE_TRADE_TYPE);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	
		return setContentView(inflater, R.layout.fragment_sale_list);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);	
		
		initView(view);
		showLoading();
	}
	
	private void initView(View view) {
		mPullListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);		
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
		
		//设置刷新时请允许滑动的开关使能   		
		mPullListView.setScrollingWhileRefreshingEnabled(true);
		
		//设置自动刷新功能
		mPullListView.setRefreshing(false);
		
		//设置拉动模式
		mPullListView.setMode(Mode.PULL_FROM_START);
		
		mListView = mPullListView.getRefreshableView();
		mListView.setOnItemClickListener(this);
		
		mPage = new PageInfo();
		mPullHelper = new PullRefreshListViewHelper(mActivity, mListView, mPage.pageSize);
		mPullHelper.setBottomClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(loadState == PullRefreshListViewHelper.BOTTOM_STATE_LOAD_FAIL) {
					//加载失败，点击重试
					loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOADING;
					mPullHelper.setBottomState(loadState);		
					startSaleHouseListRequest();
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
//		final Item item = mSaleHouseList.datas.get(position-1);
//		new ConfirmDialog().getDialog(mActivity, "呼叫", item.phone, 
//				new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+item.phone));  
//				mActivity.startActivity(intent);  
//			}
//		}).show();
	}
		
	/**
	 * 
	 * @描述:启动请求
	 */
	private void startSaleHouseListRequest() {
		requestSaleHouseList(getSaleHouseListRequestParams(), this, this);
	}
	
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getSaleHouseListRequestParams() {
		List<String> params = new ArrayList<String>();
		if(tradeType == IntentExtraConfig.HOUSE_TRADE_TYPE_SALE) {
			params.add(ParamsUtil.getReqParam("FG27", 4));
		} else {
			params.add(ParamsUtil.getReqParam("FG26", 4));
		}
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().communityId, 64));
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
	private void requestSaleHouseList(List<String> params,	 
			Listener<SaleHouseList> listenre, ErrorListener errorListener) {			
		if(mSaleHouseListRequest != null) {
			mSaleHouseListRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mSaleHouseListRequest = new SaleHouseListRequest(url, params, listenre, errorListener);
		mActivity.startRequest(mSaleHouseListRequest);		
	}
	
	/**
	 * 网络请求错误处理
	 *
	 */
	@Override
	public void onErrorResponse(VolleyError error) {	
		mPullListView.onRefreshComplete();		
		ToastHelper.showToastInBottom(mActivity, VolleyErrorHelper.getErrorMessage(mActivity, error));
		
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
		if(mPullListView.isRefreshing()) {
			mPullListView.onRefreshComplete();
		}
		if(response.respCode.equals(RespCode.SUCCESS)) {			
			if(response.datas.size() <= 0) {
				showEmpty();
				return;
			}
			
			if(mPage.pageNo == 1) {
				mSaleHouseList = response;
				// set adapter
				showContent();
				adapter = new SaleHouseListAdapter(mActivity, mSaleHouseList);
				mListView.setAdapter(adapter);
			} else {
				mSaleHouseList.hasNextPage = response.hasNextPage;
				mSaleHouseList.datas.addAll(response.datas);
				//adapter notifyDataSetChanged
				adapter.notifyDataSetChanged();
			}
			
			loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE;	
			if(mSaleHouseList.hasNextPage) {
				mPullHelper.setBottomState(PullRefreshListViewHelper.BOTTOM_STATE_LOADING);
			} else {
				mPullHelper.setBottomState(PullRefreshListViewHelper.BOTTOM_STATE_NO_MORE_DATE);
			}		
		} else {
			ToastHelper.showToastInBottom(mActivity, response.respCodeMsg);
		}
	}
}
