package com.wb.sc.mk.butler;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.wb.sc.R;
import com.wb.sc.activity.base.BasePhotoActivity;
import com.wb.sc.activity.base.BasePhotoActivity.PhotoUploadListener;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.BaseBean;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.task.BaseRequest;
import com.wb.sc.util.ParamsUtil;

/**
 * 
 * @描述：物业表扬
 * @作者：liang bao xian
 * @时间：2014年10月22日 下午8:09:39
 */
public class PropertyPraiseActivity extends BasePhotoActivity implements Listener<BaseBean>, 
	ErrorListener, PhotoUploadListener {
	
	private EditText houseInfoEt;
	private EditText descEt;
	private CheckBox shareCb;
	
	private View submitBtn;
	
	private String houseInfo;
	private String desc;
	
	private BaseRequest mBaseRequest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_property_praise);
		initHeader(getResources().getString(R.string.ac_property_praise));
		
		getIntentData();
		initView();
	}

	@Override
	public void getIntentData() {
		
	}

	@Override
	public void initView() {				
		initPhoto();
		
		houseInfoEt = (EditText) findViewById(R.id.house_info);
		descEt = (EditText) findViewById(R.id.desc);
		shareCb = (CheckBox) findViewById(R.id.share);
		
		submitBtn = findViewById(R.id.submit);
    	submitBtn.setOnClickListener(this);		
	}
	
	@Override
    public void onClick(View v) {
    	switch(v.getId()) {
    	case R.id.submit:
    		submit();
    		break;
    	}
    }
    
    private void submit() {
    	houseInfo = houseInfoEt.getText().toString();
    	desc = descEt.getText().toString();
    	
    	if(TextUtils.isEmpty(houseInfo)) {
    		ToastHelper.showToastInBottom(this, "住房信息不能为空");
    		return;
    	}
    	
    	if(TextUtils.isEmpty(desc)) {
    		ToastHelper.showToastInBottom(this, "描述不能为空");
    		return;
    	}  
    	
    	startUploadPhot();
    }
    
    /**
	 * 获取请求参数,请按照接口文档列表顺序排列
	 * @return
	 */
	private List<String> getBaseRequestParams(String imgsUrl) {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG36", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().communityId, 64));
		params.add(ParamsUtil.getReqParam(houseInfo, 50));
		params.add(ParamsUtil.getReqParam(desc, 512));
		params.add(ParamsUtil.getReqParam(desc, 140));
		params.add(ParamsUtil.getReqParam(imgsUrl, 1024));
		if(shareCb.isChecked()) {
   			params.add(ParamsUtil.getReqIntParam(1, 2));
   		} else {
   			params.add(ParamsUtil.getReqIntParam(0, 2));
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
		if(response.respCode.equals(RespCode.SUCCESS)) {
			ToastHelper.showToastInBottom(this, "感谢您的表扬，我们会做的更好~");
		} else {
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}
	
	/**
	 * 照片上传监听
	 */
	@Override
	public void onUploadComplete(List<String> imgUrlList) {
		String imgsUrl = "";
		for(int i=0; i<imgUrlList.size(); i++) {
			imgsUrl += imgUrlList.get(i);
			if(i < imgUrlList.size()-1) {
				imgsUrl += "-|";
			}
		}
		requestBase(getBaseRequestParams(imgsUrl), this, this);
	}
}
