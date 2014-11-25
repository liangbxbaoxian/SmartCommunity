package com.wb.sc.mk.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseExtraLayoutFragment;
import com.wb.sc.adapter.FindAdapter;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.Adv;
import com.wb.sc.bean.CategoryTable;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.task.AdvRequest;
import com.wb.sc.util.ParamsUtil;

public class FindFragment extends BaseExtraLayoutFragment implements OnClickListener {
	
	private List<CategoryTable> categoryTableList = new ArrayList<CategoryTable>();
	private FindAdapter yipayGriAdapter;
	
	private AdvRequest mAdvRequest;
	
	private com.android.volley.toolbox.NetworkImageView one_img;
	private com.android.volley.toolbox.NetworkImageView two_img;
	private com.android.volley.toolbox.NetworkImageView three_img;
		
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestAdv(getAdvRequestParams(), new Listener<Adv>() {

			@Override
			public void onResponse(Adv response) {
				
				if(response.respCode.equals(RespCode.SUCCESS)) {
					if (response.datas != null  && response.datas.size() > 3) {
						if(response.datas.get(0).linkUrl != null && !"".equals(response.datas.get(0).linkUrl)) {
							one_img.setImageUrl(NetConfig.getPictureUrl(response.datas.get(0).linkUrl), 
									SCApp.getInstance().getImageLoader());
						}
						
						if(response.datas.get(1).linkUrl != null && !"".equals(response.datas.get(1).linkUrl)) {
							two_img.setImageUrl(NetConfig.getPictureUrl(response.datas.get(0).linkUrl), 
									SCApp.getInstance().getImageLoader());
						}
						
						if(response.datas.get(2).linkUrl != null && !"".equals(response.datas.get(2).linkUrl)) {
							three_img.setImageUrl(NetConfig.getPictureUrl(response.datas.get(0).linkUrl), 
									SCApp.getInstance().getImageLoader());
						}
					}
				}
				
				
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return setContentView(inflater, R.layout.fragment_find);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initData();
		initView(view);
	}
	
	private void initData() {
		categoryTableList.clear();
		int resId [] = {R.drawable.huishenghuo, R.drawable.driver_selector, R.drawable.tuan_selector,  R.drawable.coupon_selector/*,
		R.drawable.trade_selector, R.drawable.together_selector, R.drawable.car_selector*/};

        String categoryname [] = {"汇.商城", "一公里", "天天团", "捡便宜", 
		"惠商城", "一起玩", "来拼车"};
		
		for (int i = 0; i < resId.length; i++) {
			CategoryTable category = new CategoryTable();
			category.setId(resId[i]);
			category.setCategoryname(categoryname[i]);
			categoryTableList.add(category);
		}
	}
	
	private void initView(View view) {
		yipayGriAdapter = new FindAdapter(getActivity(), categoryTableList);
		final GridView yipay_server = (GridView) view.findViewById(R.id.yipay_server);
		yipay_server.setSelector(R.color.transparent);
		yipay_server.setAdapter(yipayGriAdapter);
		
		 one_img = (com.android.volley.toolbox.NetworkImageView) view.findViewById(R.id.one_img);
		 two_img = (com.android.volley.toolbox.NetworkImageView) view.findViewById(R.id.two_img);
		 three_img = (com.android.volley.toolbox.NetworkImageView) view.findViewById(R.id.three_img);
	}
	
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getAdvRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("03", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().communityId, 64));
		params.add(ParamsUtil.getReqParam("0", 2));
		params.add(ParamsUtil.getReqIntParam(1, 3));
		params.add(ParamsUtil.getReqIntParam(3, 2));
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
		RequestQueue mQueue = SCApp.getInstance().getRequestQueue();	
		String requestTag = SCApp.getInstance().getRequestTag();
		mAdvRequest.setTag(requestTag);
		mQueue.add(mAdvRequest);
		mQueue.start();
	}
}
