package com.wb.sc.mk.personal;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.google.gson.Gson;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.activity.base.ReloadListener;
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
		if (SCApp.getInstance().getList().size() > 3 && mPersonalInfo != null) {
			localCommunity.setText(SCApp.getInstance().getList().get(3).dictionaryName);
			mPersonalInfo.localCommunity = SCApp.getInstance().getList().get(3).id;
		}
		if ("01".equals(SCApp.getInstance().getUser().auth)) {
			userStatue.setText("已提交认证");
		} else if ("02".equals(SCApp.getInstance().getUser().auth)) {
			userStatue.setText("住户已认证");
		} else if ("03".equals(SCApp.getInstance().getUser().auth)){
			userStatue.setText("认证失败");
		} else {
			userStatue.setText("住户未认证");
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
		
		userStatue.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(PersonalInfoActivity.this, SubmitAuthActivity.class);
				startActivity(intent);
				
			}
		});
		
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
					User user = SCApp.getInstance().getUser();
					user.communityName = mPersonalInfo.communityName;
					user.communityId = mPersonalInfo.localCommunity;
					DbHelper.saveUser(user);
					
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
	
	
	public void changeUserName(View view) {
		Intent intent = new Intent(PersonalInfoActivity.this, PersonalEditActivity.class);
		intent.putExtra("title", "修改用户名");
		intent.putExtra("keyword", "hobby");
		intent.putExtra("jsonContent", new  Gson().toJson(mPersonalInfo).toString());
		startActivityForResult(intent, Constans.SET_COMMUNITY_REQUEST_CODE);
	}
	
	
	public void changeWork(View view) {
		Intent intent = new Intent(PersonalInfoActivity.this, PersonalEditActivity.class);
		intent.putExtra("title", "修改职业");
		intent.putExtra("jsonContent", new  Gson().toJson(mPersonalInfo).toString());
		startActivityForResult(intent, Constans.SET_COMMUNITY_REQUEST_CODE);
	}
	
	
	public void changeMail(View view) {
		Intent intent = new Intent(PersonalInfoActivity.this, PersonalEditActivity.class);
		intent.putExtra("title", "修改邮箱地址");
		intent.putExtra("jsonContent", new  Gson().toJson(mPersonalInfo).toString());
		startActivityForResult(intent, Constans.SET_COMMUNITY_REQUEST_CODE);
	}
	
	
	public void changeWeixin(View view) {
		Intent intent = new Intent(PersonalInfoActivity.this, PersonalEditActivity.class);
		intent.putExtra("title", "修改微信账号");
		intent.putExtra("jsonContent", new  Gson().toJson(mPersonalInfo).toString());
		startActivityForResult(intent, Constans.SET_COMMUNITY_REQUEST_CODE);
	}
	
	
	public void changeHobby(View view) {
		Intent intent = new Intent(PersonalInfoActivity.this, PersonalEditActivity.class);
		intent.putExtra("title", "修改兴趣爱好");
		intent.putExtra("keyword", "hobby");
		intent.putExtra("jsonContent", new  Gson().toJson(mPersonalInfo).toString());
		startActivityForResult(intent, Constans.SET_COMMUNITY_REQUEST_CODE);
	}
	
	public void changeSex(View view) {
		int defaultPostion =Integer.parseInt(mPersonalInfo.sex) -1;
		defaultPostion = defaultPostion >= 0 ? defaultPostion : 0 ; 
		defaultPostion = defaultPostion <= 1 ? defaultPostion : 1 ;
		new AlertDialog.Builder(this)
		.setTitle("请选择性别")
		.setIcon(android.R.drawable.ic_dialog_info)                
		.setSingleChoiceItems(new String[] {"男","女"}, (defaultPostion), 
		  new DialogInterface.OnClickListener() {
		                            
		     public void onClick(DialogInterface dialog, int which) {
		    	 mPersonalInfo.sex = (which + 1) + "";
		    		if ("1".equals(mPersonalInfo.sex)) {
		    			sex.setText("男");
		    		} else if ("2".equals(mPersonalInfo.sex)) {
		    			sex.setText("女");
		    		}
		        dialog.dismiss();
		     }
		  }
		)
		.setNegativeButton("取消", null)
		.show();
	}
	
	public void changeBirthday(View view) {
		final Calendar c = Calendar.getInstance();

		int anio = c.get(Calendar.YEAR);
		int mes = c.get(Calendar.MONTH);
		int dia = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dpd = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				mPersonalInfo.birthday =  year + ""
                        + monthOfYear + ""
                        + dayOfMonth;
				birthday.setText(mPersonalInfo.birthday);
			}

		}, anio, mes, dia);
		dpd.show();
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, intent);
		if (intent != null) {
			String jsonContent  = intent.getStringExtra("jsonContent");
			mPersonalInfo = new Gson().fromJson(jsonContent, PersonalInfo.class);
			User user = SCApp.getInstance().getUser();
			user.communityName = mPersonalInfo.communityName;
			user.communityId = mPersonalInfo.localCommunity;
			UpdateView(mPersonalInfo);
		}

	}
	


	@Override
	public void onResponse(PersonalInfo response) {
		if(response.respCode.equals(RespCode.SUCCESS)) {
			mPersonalInfo = response;
			
			showContent();
			
			UpdateView(response);
			
		} else {
			showLoadError(this);
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}



	private void UpdateView(PersonalInfo response) {
		phoneNum.setText(response.phoneNum);
		localCommunity.setText(SCApp.getInstance().getUser().communityName);
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
		if ("01".equals(SCApp.getInstance().getUser().auth)) {
			userStatue.setText("已提交认证");
		} else if ("02".equals(SCApp.getInstance().getUser().auth)) {
			userStatue.setText("住户已认证");
		} else if ("03".equals(SCApp.getInstance().getUser().auth)){
			userStatue.setText("认证失败");
		} else {
			userStatue.setText("住户未认证");
		}
	}

}
