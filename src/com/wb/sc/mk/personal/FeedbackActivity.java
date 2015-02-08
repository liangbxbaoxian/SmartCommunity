package com.wb.sc.mk.personal;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.Conversation;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.BaseBean;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.task.BaseRequest;
import com.wb.sc.util.ParamsUtil;

public class FeedbackActivity extends BaseHeaderActivity implements OnClickListener,
	Listener<BaseBean>, ErrorListener{
	
	private EditText contentEt;
	private Button submitBtn;
	
	private FeedbackAgent agent;
	private Conversation defaultConversation;
	
	private BaseRequest mBaseRequest;
	private String content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		initHeader(R.string.ac_feedbak);
		getIntentData();
		initView();		
		
		agent = new FeedbackAgent(this);
		defaultConversation = agent.getDefaultConversation();
	}
	
	@Override
	public void getIntentData() {
		contentEt = (EditText) findViewById(R.id.content);
		submitBtn = (Button) findViewById(R.id.submit);
		submitBtn.setOnClickListener(this);
	}

	@Override
	public void initView() {
		
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch(v.getId()) {
		case R.id.submit:
			feedback();
			break;
		}
	}
	
	private void feedback() {
		content = contentEt.getText().toString();
		if(TextUtils.isEmpty(content)) {
			ToastHelper.showToastInBottom(this, "反馈内容不能为空哦~");
			return;
		}
		
//		contentEt.getEditableText().clear();
//		defaultConversation.addUserReply(content);	
//		agent.sync();
//		
//		ToastHelper.showToastInBottom(this, "感谢您提出的宝贵意见~");
//		finish();
		
		showProcess("正在提交...");
		requestBase(getBaseRequestParams(), this, this);
	}
	
	/**
	 * 获取请求参数,请按照接口文档列表顺序排列
	 * @return
	 */
	private List<String> getBaseRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG07", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(content, 512));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		
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
		ToastHelper.showToastInBottom(getApplicationContext(), VolleyErrorHelper.getErrorMessage(this, error));
	}
	
	/**
	 * 请求完成，处理UI更新
	 */
	@Override
	public void onResponse(BaseBean response) {		
		if(response.respCode.equals(RespCode.SUCCESS)) {
			ToastHelper.showToastInBottom(this, "感谢您提出的宝贵意见~");
			finish();
		} else {
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}
}
