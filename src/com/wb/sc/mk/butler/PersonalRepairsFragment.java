package com.wb.sc.mk.butler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.date.FormatDateTime;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.activity.base.BaseNetActivity;
import com.wb.sc.activity.base.BasePhotoActivity.PhotoUploadListener;
import com.wb.sc.activity.base.BasePhotoFragment;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.BaseBean;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.dialog.ToastLoginDialog;
import com.wb.sc.task.BaseRequest;
import com.wb.sc.util.ParamsUtil;

/**
 * 
 * @描述：个人报修
 * @作者：liang bao xian
 * @时间：2014年10月21日 下午5:14:46
 */
public class PersonalRepairsFragment extends BasePhotoFragment implements Listener<BaseBean>, 
	ErrorListener, PhotoUploadListener{
	
	private BaseActivity mActivity;
	
	private EditText houseInfoEt;
	private EditText phoneEt;
	private EditText descEt;
	private CheckBox shareCb;
	
	private Spinner dateSp;
	private Spinner timeSp;
	
	private View submitBtn;
	
	private String houseInfo;
	private String phone;
	private String desc;
	private Date selDate;
	private String date;
	private String time;
		
	private BaseRequest mBaseRequest;
	
	private List<Date> dateList;
	
	@Override
    public void onAttach(Activity activity) {
       super.onAttach(activity);
       mActivity = (BaseActivity) activity;
    }
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {
       return setContentView(inflater, R.layout.fragment_personal_repairs);
    }
 
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
       super.onViewCreated(view, savedInstanceState);
      
       initView(view);
    }
   
    private void initView(View view) {    
    	initPhoto(view, "FG38");
    	setUploadListener(this);
    	
    	houseInfoEt = (EditText) view.findViewById(R.id.house_info);
    	phoneEt = (EditText) view.findViewById(R.id.phone);
    	descEt = (EditText) view.findViewById(R.id.desc);
    	shareCb = (CheckBox) view.findViewById(R.id.share);
    	
    	dateSp = (Spinner) view.findViewById(R.id.date);
    	ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(getActivity(), 
    			R.layout.spinner_text_layout, getDateList());
    	dateAdapter.setDropDownViewResource(R.layout.spinner_down_text_layout);
    	dateSp.setAdapter(dateAdapter);
    	
    	timeSp = (Spinner) view.findViewById(R.id.time);
    	String[] types = getResources().getStringArray(R.array.property_repairs_time);
		ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(getActivity(), 
    			R.layout.spinner_text_layout, types);
		timeAdapter.setDropDownViewResource(R.layout.spinner_down_text_layout);
    	timeSp.setAdapter(timeAdapter);    	
    	
    	submitBtn = view.findViewById(R.id.submit);
    	submitBtn.setOnClickListener(this);
    }
    
    private List<String> getDateList() {    	
    	List<String> dateStrList = new ArrayList<String>(); 
    	dateList = new ArrayList<Date>();
    	Date date = new Date();		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		for(int i=0; i<7; i++) {			
			String time = FormatDateTime.date2String(cal.getTime(), "MM-dd");
			String weekDay = new SimpleDateFormat("EEEE").format(cal.getTime());			
			dateStrList.add(time + " " + weekDay);
			dateList.add(cal.getTime());
			cal.add(Calendar.DATE, 1);
		}
		
		return dateStrList;
    }
    
    @Override
    public void onClick(View v) {
    	super.onClick(v);
    	switch(v.getId()) {
    	case R.id.submit:
    		if(ToastLoginDialog.checkLogin(getActivity())) {
    			submit();
    		}
    		break;
    	}
    }
    
    private void submit() {
    	houseInfo = houseInfoEt.getText().toString();
    	phone = phoneEt.getText().toString();
    	desc = descEt.getText().toString();
    	selDate = dateList.get(dateSp.getSelectedItemPosition());
    	date = FormatDateTime.date2String(selDate, FormatDateTime.DATE_YMD_STR);
    	int hour = Integer.valueOf(timeSp.getSelectedItem().toString().split(":")[0]);
    	Calendar ca = Calendar.getInstance();
    	ca.setTime(selDate);
    	ca.set(Calendar.HOUR_OF_DAY, hour);
    	ca.set(Calendar.MINUTE, 00);
    	ca.set(Calendar.SECOND, 00);
    	time = FormatDateTime.date2String(ca.getTime(), FormatDateTime.DATETIME_YMDHMS_STR);
    	
    	if(TextUtils.isEmpty(houseInfo)) {
    		ToastHelper.showToastInBottom(getActivity(), "住房信息不能为空");
    		return;
    	}
    	
    	if(TextUtils.isEmpty(phone)) {
    		ToastHelper.showToastInBottom(getActivity(), "手机号码不能为空");
    		return;
    	}
    	
    	if(TextUtils.isEmpty(desc)) {
    		ToastHelper.showToastInBottom(getActivity(), "描述不能为空");
    		return;
    	}    	    	
    	
    	mActivity.showProcess("正在提交报修，请稍候...");
    	startUploadPhoto();
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
		params.add(ParamsUtil.getReqParam(phone, 15));
		params.add(ParamsUtil.getReqParam(desc, 140));
		params.add(ParamsUtil.getReqParam(imgsUrl, 1024));
		params.add(ParamsUtil.getReqParam(date, 8));
		params.add(ParamsUtil.getReqParam(time, 16));
		params.add(ParamsUtil.getReqParam("05", 2));
		params.add(ParamsUtil.getReqParam("", 100));
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
		((BaseActivity)getActivity()).dismissProcess();
		ToastHelper.showToastInBottom(getActivity(), VolleyErrorHelper.getErrorMessage(getActivity(), error));
	}
		
	/**
	 * 请求完成，处理UI更新
	 */
	@Override
	public void onResponse(BaseBean response) {		
		mActivity.dismissProcess();
		if(response.respCode.equals(RespCode.SUCCESS)) {
			ToastHelper.showToastInBottom(getActivity(), "您的报修已提交，我们会尽快处理~");
			getActivity().finish();
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
			imgsUrl += "-|";
		}
		requestBase(getBaseRequestParams(imgsUrl), this, this);
	}
}
