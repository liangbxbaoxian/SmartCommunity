package com.wb.sc.mk.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.VolleyErrorHelper;
import com.common.util.PageInfo;
import com.common.widget.ToastHelper;
import com.viewpagerindicator.CirclePageIndicator;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.activity.base.BaseExtraLayoutFragment;
import com.wb.sc.adapter.AdvAdapter;
import com.wb.sc.adapter.CategoryAdapter;
import com.wb.sc.adapter.CategoryAdapter.ItemClickListener;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.Adv;
import com.wb.sc.bean.CategoryTable;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.dialog.ToastLoginDialog;
import com.wb.sc.mk.butler.HouseTradeListActivity;
import com.wb.sc.mk.butler.PropertyBillActivity;
import com.wb.sc.mk.butler.PropertyComplain;
import com.wb.sc.mk.butler.PropertyRepairsActivity;
import com.wb.sc.task.AdvRequest;
import com.wb.sc.util.ParamsUtil;

public class ButlerFragment extends BaseExtraLayoutFragment implements ItemClickListener,
	ErrorListener{

	private ViewPager advVp;
	private CirclePageIndicator advIndicator;
	private AdvAdapter advAdapter;
	private AdvRequest mAdvRequest;
	private AdvListener mAdvListener = new AdvListener();
	private PageInfo advPgIf = new PageInfo();
	
	private List<CategoryTable> categoryTableList = new ArrayList<CategoryTable>();
	private CategoryAdapter yipayGriAdapter;
	
	private Activity mActivity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return setContentView(inflater, R.layout.fragment_butler);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initData();
		
		initHead(view, getString(R.string.bottom_bar_steward));
		initView(view);
		
		requestAdv(getAdvRequestParams(), mAdvListener, this);
	}
	
	private void initData() {
		categoryTableList.clear();
		int resId [] = {R.drawable.complain_selector, R.drawable.repair_selector, /*R.drawable.praise_selector,*/
				R.drawable.business_selector, R.drawable.bill_selector};
		
		String categoryname [] = {"物业投诉", "物业报修", /*"物业表扬",*/
				"房屋交易", "物业账单"};
		
		for (int i = 0; i < resId.length; i++) {
			CategoryTable category = new CategoryTable();
			category.setId(resId[i]);
			category.setCategoryname(categoryname[i]);
			categoryTableList.add(category);
		}
	}
	
	private void initHead(View view, String title) {
		initHeader(view, title);
	}

	private void initView(View view) {
		advVp = (ViewPager) view.findViewById(R.id.adv_pager);
		advIndicator = (CirclePageIndicator) view.findViewById(R.id.adv_indicator);		
		yipayGriAdapter = new CategoryAdapter(getActivity(), categoryTableList);
		final GridView yipay_server = (GridView) view.findViewById(R.id.yipay_server);
		yipay_server.setSelector(R.color.transparent);
		yipay_server.setAdapter(yipayGriAdapter);
		yipayGriAdapter.setListener(this);
	}

	@Override
	public void onItemClick(int position) {
		switch(position) {
		case 0:{
			Intent intent = new Intent(getActivity(), PropertyComplain.class);
			startActivity(intent);
		}break;
		
		case 1:{
			Intent intent = new Intent(getActivity(), PropertyRepairsActivity.class);
			startActivity(intent);
		}break;
		
		case 2:{
			Intent intent = new Intent(getActivity(), HouseTradeListActivity.class);
			startActivity(intent);
		}break;
		
		case 3:{
			if(ToastLoginDialog.checkLogin(getActivity())) {
				Intent intent = new Intent(getActivity(), PropertyBillActivity.class);
				startActivity(intent);
			}
//			
//			Intent intent = new Intent(getActivity(), HouseTradeListActivity.class);
//			startActivity(intent);
		}break;
		
//		case 4:{
//			Intent intent = new Intent(getActivity(), PropertyBillActivity.class);
//			startActivity(intent);
//		}break;
		}
	}
	
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getAdvRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG20", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().communityId, 64));
		params.add(ParamsUtil.getReqParam("1", 2));
		params.add(ParamsUtil.getReqIntParam(advPgIf.pageNo, 3));
		params.add(ParamsUtil.getReqIntParam(advPgIf.pageSize, 2));
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
	private void requestAdv(List<String> params,	 
			Listener<Adv> listenre, ErrorListener errorListener) {			
		if(mAdvRequest != null) {
			mAdvRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mAdvRequest = new AdvRequest(url, params, listenre, errorListener);
		((BaseActivity)mActivity).startRequest(mAdvRequest);		
	}
	
	/**
	 * 
	 * @描述：广告监听
	 * @作者：liang bao xian
	 * @时间：2014年10月23日 上午10:20:51
	 */
	class AdvListener implements Listener<Adv> {
		/**
		 * 请求完成，处理UI更新
		 */
		@Override
		public void onResponse(Adv response) {		
			if(response.respCode.equals(RespCode.SUCCESS)) {			
				advAdapter = new AdvAdapter(mActivity, response);
				advVp.setAdapter(advAdapter);
				advIndicator.setViewPager(advVp);
			} else {
				ToastHelper.showToastInBottom(mActivity, response.respCodeMsg);
			}
		}
	}
	
	/**
	 * 网络请求错误处理
	 *
	 */
	@Override
	public void onErrorResponse(VolleyError error) {	
		ToastHelper.showToastInBottom(mActivity, VolleyErrorHelper.getErrorMessage(mActivity, error));
	}
}
