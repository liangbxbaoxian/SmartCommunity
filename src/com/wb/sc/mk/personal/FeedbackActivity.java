package com.wb.sc.mk.personal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.common.widget.ToastHelper;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.UserInfo;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;

public class FeedbackActivity extends BaseHeaderActivity implements OnClickListener{
	
	private EditText contentEt;
	private Button submitBtn;
	
	private FeedbackAgent agent;
	private Conversation defaultConversation;
	
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
		String content = contentEt.getText().toString();
		if(TextUtils.isEmpty(content)) {
			ToastHelper.showToastInBottom(this, "反馈内容不能为空哦~");
			return;
		}
		
		contentEt.getEditableText().clear();
		defaultConversation.addUserReply(content);	
		agent.sync();
		
		ToastHelper.showToastInBottom(this, "感谢您提出的宝贵意见~");
		finish();
	}
}
