package com.wb.sc;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.activity.base.ReloadListener;
import com.wb.sc.bean.BaseBean;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.task.BaseRequest;
import com.wb.sc.util.ParamsUtil;

/**
 * 
 * @描述：用于接口调试
 * @作者：liang bao xian
 * @时间：2014年10月11日 下午2:39:11
 */
public class BaseBeanActivity extends BaseActivity implements Listener<BaseBean>, 
	ErrorListener, ReloadListener{
		
	private BaseRequest mBaseRequest;
	private BaseBean mBase;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		getIntentData();
		initView();		
		
		showLoading();		
		
		requestBase(getBaseRequestParams(), this, this);
	}
	
	@Override
	public void getIntentData() {
		
	}
	
	@Override
	public void initView() {
		
	}
	
	/**
	 * 获取请求参数,请按照接口文档列表顺序排列
	 * @return
	 */
	private List<String> getBaseRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG01", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam("00", 2));
		params.add(ParamsUtil.getReqParam("13675013092", 15));
		params.add(ParamsUtil.getReqParam("12345678900987654321123456789009", 32));
		
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
			Listener<BaseBean> listenre, ErrorListener errorListener) {			
		if(mBaseRequest != null) {
			mBaseRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mBaseRequest = new BaseRequest(url, paramsList, listenre, errorListener);
		startRequest(mBaseRequest);		
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
		requestBase(getBaseRequestParams(), this, this);
	}
	
	/**
	 * 请求完成，处理UI更新
	 */
	@Override
	public void onResponse(BaseBean response) {		
		if(response.respCode.equals(RespCode.SUCCESS)) {
			showContent();
			mBase = response;
		} else {
			showLoadError(this);
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}
}
