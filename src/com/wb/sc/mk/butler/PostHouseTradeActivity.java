package com.wb.sc.mk.butler;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.BaseBean;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.dialog.ToastLoginDialog;
import com.wb.sc.task.BaseRequest;
import com.wb.sc.util.ParamsUtil;

/**
 * 
 * @描述：发布房屋出租、出售信息
 * @作者：liang bao xian
 * @时间：2014年11月9日 下午3:15:15
 */
public class PostHouseTradeActivity extends BaseHeaderActivity implements Listener<BaseBean>, 
	ErrorListener {	
	
	private EditText houseInfoEt;
	private EditText phoneEt;
	private Spinner typeSp;
	
	private View submitBtn;
	
	private String houseInfo;
	private String phone;
	
	private BaseRequest mBaseRequest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_header_layout, R.layout.activity_house_trade);
		initHeader(getResources().getString(R.string.ac_house_trade));
		
		getIntentData();
		initView();
	}

	@Override
	public void getIntentData() {
		
	}

	@Override
	public void initView() {
		houseInfoEt = (EditText) findViewById(R.id.house_info);
		phoneEt = (EditText) findViewById(R.id.phone);
		
		typeSp = (Spinner) findViewById(R.id.type);
		String[] types = getResources().getStringArray(R.array.house_trade_type);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
    			R.layout.spinner_text_layout, types);
    	adapter.setDropDownViewResource(R.layout.spinner_down_text_layout);
    	typeSp.setAdapter(adapter);
    	
    	submitBtn = findViewById(R.id.submit);
    	submitBtn.setOnClickListener(this);	
	}
	
	@Override
    public void onClick(View v) {
		super.onClick(v);
		
    	switch(v.getId()) {
    	case R.id.submit:
    		if(ToastLoginDialog.checkLogin(this)) {
    			submit();
    		}
    		break;
    	}
    }
    
    private void submit() {
    	houseInfo = houseInfoEt.getText().toString();
    	phone = phoneEt.getText().toString();
    	
    	if(TextUtils.isEmpty(houseInfo)) {
    		ToastHelper.showToastInBottom(this, "住房信息不能为空");
    		return;
    	}
    	
    	if(TextUtils.isEmpty(phone)) {
    		ToastHelper.showToastInBottom(this, "手机号码不能为空");
    		return;
    	}
    	
    	showProcess("正在提交房屋委托信息，请稍候...");
    	requestBase(getBaseRequestParams(), this, this);
    }
    
    /**
	 * 获取请求参数,请按照接口文档列表顺序排列
	 * @return
	 */
	private List<String> getBaseRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG25", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().communityId, 64));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().getHouseId(), 64));
		params.add(ParamsUtil.getReqParam(houseInfo, 256));
		params.add(ParamsUtil.getReqParam(phone, 15));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().getAccount(), 32));
		String type = getResources().getStringArray(R.array.house_trade_type_index)[typeSp.getSelectedItemPosition()];
   		params.add(ParamsUtil.getReqParam(type, 8));
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
		ToastHelper.showToastInBottom(this, VolleyErrorHelper.getErrorMessage(this, error));
	}
		
	/**
	 * 请求完成，处理UI更新
	 */
	@Override
	public void onResponse(BaseBean response) {		
		dismissProcess();
		if(response.respCode.equals(RespCode.SUCCESS)) {
			ToastHelper.showToastInBottom(this, "您申请了委托，我们的置业顾问会尽快与你联系！");			
			finish();
		} else {
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}
}
