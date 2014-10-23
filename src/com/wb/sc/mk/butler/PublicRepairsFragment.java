package com.wb.sc.mk.butler;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseNetActivity;
import com.wb.sc.activity.base.BasePhotoFragment;
import com.wb.sc.activity.base.BasePhotoActivity.PhotoUploadListener;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.BaseBean;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.task.BaseRequest;
import com.wb.sc.util.ParamsUtil;

public class PublicRepairsFragment extends BasePhotoFragment implements Listener<BaseBean>, 
	ErrorListener, PhotoUploadListener{
	
	private EditText houseInfoEt;
	private EditText addressEt;;
	private EditText descEt;
	private CheckBox shareCb;
	
	private View submitBtn;
	
	public String houseInfo;
	public String address;
	public String desc;
	
	private BaseRequest mBaseRequest;
	
	@Override
    public void onAttach(Activity activity) {
       super.onAttach(activity);
    }
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {
       return setContentView(inflater, R.layout.fragment_public_repairs);
    }
 
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
       super.onViewCreated(view, savedInstanceState);
      
       initView(view);
    }
   
    private void initView(View view) {
    	initPhoto(view);
    	    	
    	houseInfoEt = (EditText) view.findViewById(R.id.house_info);
    	addressEt = (EditText) view.findViewById(R.id.address);
    	descEt = (EditText) view.findViewById(R.id.desc);
    	shareCb = (CheckBox) view.findViewById(R.id.share);
    	
    	submitBtn = view.findViewById(R.id.submit);
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
    	address = addressEt.getText().toString();
    	desc = descEt.getText().toString();
    	
    	if(TextUtils.isEmpty(houseInfo)) {
    		ToastHelper.showToastInBottom(getActivity(), "住房信息不能为空");
    		return;
    	}
    	
    	if(TextUtils.isEmpty(address)) {
    		ToastHelper.showToastInBottom(getActivity(), "故障地点不能为空");
    		return;
    	}
    	
    	if(TextUtils.isEmpty(desc)) {
    		ToastHelper.showToastInBottom(getActivity(), "描述不能为空");
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
   		params.add(ParamsUtil.getReqParam("FG38", 4));
   		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
   		params.add(ParamsUtil.getReqParam("00001", 20));
   		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
   		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().communityId, 64));
   		params.add(ParamsUtil.getReqParam(houseInfo, 50));
   		params.add(ParamsUtil.getReqParam("", 15));
   		params.add(ParamsUtil.getReqParam(desc, 140));
   		params.add(ParamsUtil.getReqParam(imgsUrl, 1024));
   		params.add(ParamsUtil.getReqParam("", 8));
   		params.add(ParamsUtil.getReqParam("", 16));
   		params.add(ParamsUtil.getReqIntParam(6, 2));
   		params.add(ParamsUtil.getReqParam(address, 100));
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
   		((BaseNetActivity)getActivity()).startRequest(mBaseRequest);		
   	}
   	
   	/**
   	 * 网络请求错误处理
   	 *
   	 */
   	@Override
   	public void onErrorResponse(VolleyError error) {		
   		ToastHelper.showToastInBottom(getActivity(), VolleyErrorHelper.getErrorMessage(getActivity(), error));
   	}
   		
   	/**
   	 * 请求完成，处理UI更新
   	 */
   	@Override
   	public void onResponse(BaseBean response) {		
   		if(response.respCode.equals(RespCode.SUCCESS)) {
   			ToastHelper.showToastInBottom(getActivity(), "您的报修已提交，我们会尽快处理~");
   		} else {
   			ToastHelper.showToastInBottom(getActivity(), response.respCodeMsg);
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
