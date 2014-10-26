package com.wb.sc.mk.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.bean.MsgCenter;

public class BulletinActivity extends BaseHeaderActivity implements OnClickListener{
	
	private TextView bulletinTitle;
	private TextView bulletinContent;
	private TextView notifier;
	private TextView notifyTime;
	private TextView notifyShortTime;
	
	private MsgCenter.MsgItem item;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bulletin);

		initHeader(R.string.ac_bulletin);
		getIntentData();
		initView();		
		
	}
	
	public void lawRule(View view) {
		Intent intent = new Intent(this, LawRuleActivity.class);
		startActivity(intent);
	}
	
	public void feedback(View view) {
		Intent intent = new Intent(this, FeedbackActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void getIntentData() {
		String msg = getIntent().getStringExtra("msg");
		item = new Gson().fromJson(msg, MsgCenter.MsgItem.class);
	}

	@Override
	public void initView() {
		bulletinTitle = (TextView) findViewById(R.id.bulletinTitle);
		bulletinContent = (TextView) findViewById(R.id.bulletinContent);
		notifier = (TextView) findViewById(R.id.notifier);
		notifyTime = (TextView) findViewById(R.id.notifyTime);
		notifyShortTime = (TextView) findViewById(R.id.notifyShortTime);
		if (item != null) {
			bulletinTitle.setText(item.bulletinTitle);
			bulletinContent.setText(item.bulletinContent);
			notifier.setText(item.notifier);
			notifyTime.setText(item.notifyTime);
			notifyShortTime.setText(item.notifyTime);
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch(v.getId()) {
		case R.id.submit:
			break;
		}
	}
	
}
