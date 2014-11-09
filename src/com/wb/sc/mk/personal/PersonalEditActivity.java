package com.wb.sc.mk.personal;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.common.widget.ToastHelper;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.internal.Utils;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.UserInfo;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.util.Constans;

public class PersonalEditActivity extends BaseHeaderActivity implements OnClickListener{
	
	private EditText contentEt;
	private Button submitBtn;
	
	private FeedbackAgent agent;
	private Conversation defaultConversation;
	
	private String title;
	private String keyword;
	private String jsonContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_edit);

//		initHeader(R.string.ac_feedbak);
		getIntentData();
		initView();		
	}
	
	@Override
	public void getIntentData() {
		Intent intent = this.getIntent();
		title  = intent.getStringExtra("title");
		keyword  = intent.getStringExtra("keyword");
		jsonContent  = intent.getStringExtra("jsonContent");
		initHeader(title);
	}

	@Override
	public void initView() {
		contentEt = (EditText) findViewById(R.id.content);
		submitBtn = (Button) findViewById(R.id.submit);
		submitBtn.setOnClickListener(this);
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
		String content = contentEt.getText().toString();
		if(TextUtils.isEmpty(content)) {
			ToastHelper.showToastInBottom(this, "输入内容不能为空哦~");
		} else {
			JSONObject obj;
			try {
				if ("mail".equals(title)) {
					if (com.wb.sc.util.Utils.isEmail(content)) {
						obj = new JSONObject(jsonContent);
						obj.putOpt(keyword, content);
						Intent intent  = new Intent(PersonalEditActivity.this, PersonalInfoActivity.class);
						intent.putExtra("jsonContent", obj.toString());
						setResult(Constans.SET_COMMUNITY_REQUEST_CODE, intent);
					} else {
						ToastHelper.showToastInBottom(this, "输入邮箱地址不合法哦~");
					}
				} else {
					obj = new JSONObject(jsonContent);
					obj.putOpt(keyword, content);
					Intent intent  = new Intent(PersonalEditActivity.this, PersonalInfoActivity.class);
					intent.putExtra("jsonContent", obj.toString());
					setResult(Constans.SET_COMMUNITY_REQUEST_CODE, intent);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finish();
		}
		
		
	}
}
