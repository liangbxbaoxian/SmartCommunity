package com.wb.sc.mk.personal;


import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.BaseBean;
import com.wb.sc.bean.PersonalInfo;
import com.wb.sc.bean.User;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.db.DbHelper;
import com.wb.sc.mk.main.SetCommunityActivity;
import com.wb.sc.task.BaseRequest;
import com.wb.sc.task.PersonalInfoRequest;
import com.wb.sc.util.Constans;
import com.wb.sc.util.MetaUtil;
import com.wb.sc.util.ParamsUtil;
import com.wb.sc.util.Utils;

public class SubmitAuthActivity extends BaseActivity  {


	private PersonalInfoRequest mBaseRequest;
	private PersonalInfo mPersonalInfo;
	
	private BaseRequest baseReq;
	
	private TextView communityName;
	private TextView btn_exit;
	
	private EditText input_building_num;
	private EditText input_room_num;
	private EditText input_real_name;
	private EditText input_id;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_auth);
		initView();
		
	}
	


	@Override
	protected void onDestroy() {
		SCApp.getInstance().getList().clear();
		super.onDestroy();
	}



	public void getIntentData() {

	}
	
	@Override
	protected void onResume() {
		if (SCApp.getInstance().getList().size() > 3 ) {
			communityName.setText(SCApp.getInstance().getList().get(3).dictionaryName);
		}
		super.onResume();
	}



	public void initView() {

		communityName = (TextView) findViewById(R.id.communityName);
		
		
		input_building_num = (EditText) findViewById(R.id.input_building_num);
		input_room_num = (EditText) findViewById(R.id.input_room_num);
		input_real_name = (EditText) findViewById(R.id.input_real_name);
		input_id = (EditText) findViewById(R.id.input_id);
		
		
		
		btn_exit = (TextView) findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (inputValid()) {
					showLoading();	
					requestBase(getSaveInfoRequestParams());
				}

			}
		});
		
	}
	
	private boolean inputValid() {
		String building_num = input_building_num.getText().toString();
		String room_num = input_room_num.getText().toString();
		String real_name = input_real_name.getText().toString();
		String id = input_id.getText().toString();
		boolean isId = Utils.isId(id);
		boolean isSuccess = true;
		if(TextUtils.isEmpty(building_num)) {
			isSuccess = false;
			ToastHelper.showToastInBottom(this, "请输入楼栋号~");
		} else if(TextUtils.isEmpty(room_num)) {
			isSuccess = false;
			ToastHelper.showToastInBottom(this, "请输入房号~");
		} else if(TextUtils.isEmpty(real_name)) {
			isSuccess = false;
			ToastHelper.showToastInBottom(this, "请输入户主真实姓名~");
		} else if(TextUtils.isEmpty(id)) {
			isSuccess = false;
			ToastHelper.showToastInBottom(this, "请输入户主身份证号码~");
		} else if (!isId) {
			isSuccess = false;
			ToastHelper.showToastInBottom(this, "请输入有效户主身份证号码~");
		}
		
		return isSuccess;
	}
	
	public void back(View view) {
		this.finish();
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
			Listener<PersonalInfo> listenre, ErrorListener errorListener) {
		showLoading();	
		if(mBaseRequest != null) {
			mBaseRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mBaseRequest = new PersonalInfoRequest(url, paramsList, listenre, errorListener);
		startRequest(mBaseRequest);		
	}
	
	private void requestBase(List<String> paramsList) {			
		if(baseReq != null) {
			baseReq.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		baseReq = new BaseRequest(url, paramsList, new Listener<BaseBean>() {

			@Override
			public void onResponse(BaseBean response) {
				if(response.respCode.equals(RespCode.SUCCESS)) {
					showContent();
					User user = SCApp.getInstance().getUser();
					user.auth = "01";
					DbHelper.saveUser(user);
					
					ToastHelper.showToastInBottom(SubmitAuthActivity.this, "认证成功");
					SubmitAuthActivity.this.finish();
				} else {
					ToastHelper.showToastInBottom(SubmitAuthActivity.this, response.respCodeMsg);
				}
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				ToastHelper.showToastInBottom(getApplicationContext(), VolleyErrorHelper.getErrorMessage(SubmitAuthActivity.this, error));
			}
		});
		startRequest(baseReq);		
	}
	
	/**
	 * 获取请求参数,请按照接口文档列表顺序排列
	 * @return
	 */
	private List<String> getBaseRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG03", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam(MetaUtil.readMeta(this, Constans.APP_CHANNEL), 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		
		return params;
	}
	
	/**
	 * 获取请求参数,请按照接口文档列表顺序排列
	 * @return
	 */
	private List<String> getSaveInfoRequestParams() {
		
		
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG23", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam(MetaUtil.readMeta(this, Constans.APP_CHANNEL), 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		
		params.add(ParamsUtil.getReqParam(input_real_name.getText() +"", 32));
		params.add(ParamsUtil.getReqParam(input_id.getText() +"", 32));
		String detail = SCApp.getInstance().getUser().getCommunityName() +  input_building_num.getText().toString() + input_room_num.getText().toString() + input_real_name.getText().toString() + input_id.getText().toString() + "-|" ;
		params.add(ParamsUtil.getReqParam(detail, 512));
		
		return params;
	}



	
	public void setCommunity(View view) {
		Intent intent = new Intent(SubmitAuthActivity.this, SetCommunityActivity.class);
		startActivityForResult(intent, Constans.SET_COMMUNITY_REQUEST_CODE);
	}


}
