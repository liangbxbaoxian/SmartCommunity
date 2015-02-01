package com.wb.sc.mk.butler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.activity.base.ReloadListener;
import com.wb.sc.adapter.BillListAdapter;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.Bill;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.task.BillRequest;
import com.wb.sc.util.ParamsUtil;

/**
 * 
 * @描述：物业账单
 * @作者：liang bao xian
 * @时间：2014年11月9日 下午2:30:04
 */
public class PropertyBillActivity extends BaseHeaderActivity implements
	Listener<Bill>, ErrorListener, ReloadListener{
	
	private ListView billLv;
	private BillListAdapter billListAdapter;
	
	private BillRequest mBillRequest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_header_layout, R.layout.activity_property_bill);
		initHeader(getResources().getString(R.string.ac_property_bill));
		
		getIntentData();
		initView();
		
		if(!TextUtils.isEmpty(SCApp.getInstance().getUser().houseId)) {		
			showLoading();
			requestBill(getBillRequestParams(), this, this);
		} else {
			ToastHelper.showToastInBottom(this, "无可查询物业费的房屋");
			finish();
		}
	}

	@Override
	public void getIntentData() {
		
	}

	@Override
	public void initView() {
		billLv = (ListView) findViewById(R.id.list);
	}
	
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getBillRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG24", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().houseId, 64));
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
	private void requestBill(List<String> params,	 
			Listener<Bill> listenre, ErrorListener errorListener) {			
		if(mBillRequest != null) {
			mBillRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mBillRequest = new BillRequest(url, params, listenre, errorListener);
		startRequest(mBillRequest);		
	}
	
	/**
	 * 网络请求错误处理
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
		requestBill(getBillRequestParams(), this, this);			
	}
	
	/**
	 * 请求完成，处理UI更新
	 */
	@Override
	public void onResponse(Bill response) {		
		showContent();	
		if(response.respCode.equals(RespCode.SUCCESS)) {			
			List<Map<String, String>> billList = new ArrayList<Map<String,String>>();
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", "业主姓名");
			map.put("value", SCApp.getInstance().getUser().account);
			billList.add(map);
			
			map = new HashMap<String, String>();
			map.put("name", "所在小区");
			map.put("value", SCApp.getInstance().getUser().communityName);
			billList.add(map);
			
			map = new HashMap<String, String>();
			map.put("name", "楼栋号");
			map.put("value", SCApp.getInstance().getUser().houseNum);
			billList.add(map);
			
			map = new HashMap<String, String>();
			map.put("name", "房号");
			map.put("value", SCApp.getInstance().getUser().roomNum);
			billList.add(map);
			
			map = new HashMap<String, String>();
			map.put("name", "面积");
			map.put("value", "");
			billList.add(map);
			
			map = new HashMap<String, String>();
			map.put("name", "物业单价");
			map.put("value", "");
			billList.add(map);
			
			map = new HashMap<String, String>();
			map.put("name", "起止时间");
			map.put("value", "");
			billList.add(map);
			
			map = new HashMap<String, String>();
			map.put("name", "费用合计");
			map.put("value", response.amount);
			billList.add(map);
			
			billListAdapter = new BillListAdapter(mActivity, billList);
			billLv.setAdapter(billListAdapter);
		} else {
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}
}
