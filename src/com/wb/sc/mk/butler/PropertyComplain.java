package com.wb.sc.mk.butler;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.common.widget.hzlib.HorizontalAdapterView.OnItemClickListener;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.activity.base.BasePhotoActivity;
import com.wb.sc.activity.base.BasePhotoActivity.PhotoUploadListener;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.PComplain;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.mk.personal.MyComplaintActivity;
import com.wb.sc.task.PComplainRequest;
import com.wb.sc.util.ParamsUtil;

/**
 * 
 * @描述：物业投诉
 * @作者：liang bao xian
 * @时间：2014年10月20日 下午7:21:04
 */
public class PropertyComplain extends BasePhotoActivity implements OnItemClickListener,
	Listener<PComplain>, ErrorListener, PhotoUploadListener{
	
	private View myComplainV;
	private Spinner typeSp;
	private EditText houseInfoEt;
	private EditText detailEt;
	private CheckBox shareCb;
	private Button submitBtn;	
	
	String houseInfo;
	String detail;
	int type;
	
	private PComplainRequest mPComplainRequest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_property_complain);
		initHeader(getString(R.string.ac_property_complain));
		
		getIntentData();
		initView();
	}
	
	@Override
	public void getIntentData() {
		
	}
	
	@Override
	public void initView() {
		myComplainV = findViewById(R.id.my_complain);
		myComplainV.setOnClickListener(this);
		initPhoto("FG37");
		setUploadListener(this);
		
		typeSp = (Spinner) findViewById(R.id.type);
		String[] types = getResources().getStringArray(R.array.property_complain_type);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
    			R.layout.spinner_text_layout, types);
    	adapter.setDropDownViewResource(R.layout.spinner_down_text_layout);
    	typeSp.setAdapter(adapter);
    	
    	houseInfoEt = (EditText) findViewById(R.id.house_info);
    	detailEt = (EditText) findViewById(R.id.detail);
    	shareCb = (CheckBox) findViewById(R.id.share);
    	
    	submitBtn = (Button) findViewById(R.id.submit);
    	submitBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch(v.getId()) {
		case R.id.my_complain:
			Intent intent = new Intent(this, MyComplaintActivity.class);
			startActivity(intent);
			break;
			
		case R.id.submit:
			submit();
			break;
		}
	}
	
	private void submit() {
		houseInfo = houseInfoEt.getText().toString();
		detail = detailEt.getText().toString();
		type = typeSp.getSelectedItemPosition();
		
		if(TextUtils.isEmpty(houseInfo)) {
			ToastHelper.showToastInBottom(this, "住户信息不能为空");
			return;
		}
		
		if(TextUtils.isEmpty(detail)) {
			ToastHelper.showToastInBottom(this, "详细信息不能为空");
			return;
		}
		
		showProcess("正在提交投诉，请稍候...");
		startUploadPhot();		
	}
	
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getPComplainRequestParams(String imgsUrl) {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG37", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().communityId, 64));
		params.add(ParamsUtil.getReqParam(houseInfo, 50));
		params.add(ParamsUtil.getReqParam("0"+(type+1), 2));
		params.add(ParamsUtil.getReqParam(detail, 512));
		params.add(ParamsUtil.getReqParam(imgsUrl, 1024));
		if(shareCb.isChecked()) {
			params.add(ParamsUtil.getReqParam("01", 2));
		} else {
			params.add(ParamsUtil.getReqParam("00", 2));
		}
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
	private void requestPComplain(List<String> params,	 
			Listener<PComplain> listenre, ErrorListener errorListener) {			
		if(mPComplainRequest != null) {
			mPComplainRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mPComplainRequest = new PComplainRequest(url, params, listenre, errorListener);
		startRequest(mPComplainRequest);		
	}
	
	/**
	 * 网络请求错误处理
	 *
	 */
	@Override
	public void onErrorResponse(VolleyError error) {	
		dismissProcess();
		ToastHelper.showToastInBottom(getApplicationContext(), VolleyErrorHelper.getErrorMessage(this, error));
	}
	
	/**
	 * 请求完成，处理UI更新
	 */
	@Override
	public void onResponse(PComplain response) {		
		dismissProcess();
		if(response.respCode.equals(RespCode.SUCCESS)) {	
			finish();
			ToastHelper.showToastInBottom(this, "您的投诉已提交，我们会尽快处理");
		} else {
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}
	
	@Override
	public void onUploadComplete(List<String> imgUrlList) {
		String imgsUrl = "";
		for(int i=0; i<imgUrlList.size(); i++) {
			imgsUrl += imgUrlList.get(i);
			if(i < imgUrlList.size()-1) {
				imgsUrl += "-|";
			}
		}
		requestPComplain(getPComplainRequestParams(imgsUrl), this, this);
	}
}
