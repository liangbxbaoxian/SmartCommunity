package com.wb.sc.mk.personal;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.activity.base.ReloadListener;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.BaseBean;
import com.wb.sc.bean.PersonalInfo;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.mk.main.SetCommunityActivity;
import com.wb.sc.task.BaseRequest;
import com.wb.sc.task.PersonalInfoRequest;
import com.wb.sc.util.Constans;
import com.wb.sc.util.MetaUtil;
import com.wb.sc.util.ParamsUtil;

public class PersonalInfoActivity extends BaseActivity implements Listener<PersonalInfo>, ErrorListener, ReloadListener {


	private PersonalInfoRequest mBaseRequest;
	private PersonalInfo mPersonalInfo;
	
	private BaseRequest baseReq;
	
	private TextView phoneNum;
	private TextView localCommunity;
	private TextView accountName;
	private TextView birthday;
	private TextView sex;
	private TextView mail;
	private TextView weixinAccount;
	private TextView work;
	private TextView hobby;
	private TextView userStatue;
	private TextView btn_exit;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info);
		initView();
        showLoading();		
		
		requestBase(getBaseRequestParams(), this, this);
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
		if (SCApp.getInstance().getList().size() > 3) {
			localCommunity.setText(SCApp.getInstance().getList().get(3).dictionaryName);
			mPersonalInfo.localCommunity = SCApp.getInstance().getList().get(3).dictionaryId;
		}
		super.onResume();
	}



	public void initView() {

		phoneNum = (TextView) findViewById(R.id.phoneNum);
		localCommunity = (TextView) findViewById(R.id.localCommunity);
		accountName = (TextView) findViewById(R.id.accountName);
		birthday = (TextView) findViewById(R.id.birthday);
		sex = (TextView) findViewById(R.id.sex);
		mail = (TextView) findViewById(R.id.mail);
		weixinAccount = (TextView) findViewById(R.id.weixinAccount);
		work = (TextView) findViewById(R.id.work);
		hobby = (TextView) findViewById(R.id.hobby);
		userStatue = (TextView) findViewById(R.id.userStatue);

		userStatue.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
		
		btn_exit = (TextView) findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 showLoading();	
				requestBase(getSaveInfoRequestParams());
			}
		});
		
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
					ToastHelper.showToastInBottom(PersonalInfoActivity.this, "保存成功");
				} else {
					showLoadError(PersonalInfoActivity.this);
					ToastHelper.showToastInBottom(PersonalInfoActivity.this, response.respCodeMsg);
				}
			}
		}, this);
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
		params.add(ParamsUtil.getReqParam("FG04", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam(MetaUtil.readMeta(this, Constans.APP_CHANNEL), 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		String CommunityId = mPersonalInfo.localCommunity == null ? "" : mPersonalInfo.localCommunity;
		params.add(ParamsUtil.getReqParam(CommunityId, 64)); //新增社区id
		params.add(ParamsUtil.getReqParam(mail.getText() +"", 64));
		params.add(ParamsUtil.getReqParam(weixinAccount.getText() +"", 32));
		params.add(ParamsUtil.getReqParam(birthday.getText() +"", 8));
		
		String strSex = "2";
		if (sex.getText().equals("男")) {
			strSex = "1";
		}
		
		params.add(ParamsUtil.getReqParam(strSex, 64));
		params.add(ParamsUtil.getReqParam(work.getText() +"", 2));
		params.add(ParamsUtil.getReqParam(hobby.getText() +"", 64));
		params.add(ParamsUtil.getReqParam(accountName.getText() +"", 32));
		return params;
	}



	@Override
	public void onReload() {
		requestBase(getBaseRequestParams(), this, this);
	}



	@Override
	public void onErrorResponse(VolleyError error) {
		showLoadError(this);	
		ToastHelper.showToastInBottom(getApplicationContext(), VolleyErrorHelper.getErrorMessage(this, error));
	}
	
	public void setCommunity(View view) {
		Intent intent = new Intent(PersonalInfoActivity.this, SetCommunityActivity.class);
		startActivityForResult(intent, Constans.SET_COMMUNITY_REQUEST_CODE);
	}


	@Override
	public void onResponse(PersonalInfo response) {
		if(response.respCode.equals(RespCode.SUCCESS)) {
			mPersonalInfo = response;
			
			showContent();
			
			phoneNum.setText(response.phoneNum);
			localCommunity.setText(response.communityNam);
			accountName.setText(response.accountName);
			if (!"".equals(response.birthday)) {
//				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
//				birthday.setText(formatter.format(new Date(response.birthday)));
				birthday.setText(response.birthday);
			}
			
			if ("1".equals(response.sex)) {
				sex.setText("男");
			} else if ("2".equals(response.sex)) {
				sex.setText("女");
			}
			
			mail = (TextView) findViewById(R.id.mail);
			mail.setText(response.mail);
			
			weixinAccount = (TextView) findViewById(R.id.weixinAccount);
			weixinAccount.setText(response.weixinAccount);
			
			work = (TextView) findViewById(R.id.work);
			
			hobby = (TextView) findViewById(R.id.hobby);
			hobby.setText(response.hobby);
			
			userStatue = (TextView) findViewById(R.id.userStatue);
			if ("0".equals(response.sex)) {
				userStatue.setText("住户已认证");
			} else if ("1".equals(response.sex)) {
				userStatue.setText("住户未认证");
			}
			
			
		} else {
			showLoadError(this);
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}

}
