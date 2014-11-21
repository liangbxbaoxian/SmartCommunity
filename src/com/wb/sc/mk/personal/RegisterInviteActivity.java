package com.wb.sc.mk.personal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;

public class RegisterInviteActivity extends BaseHeaderActivity implements OnClickListener{


	private TextView content;
	private EditText phone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_invite);

		initHeader(R.string.ac_setting);
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
	}

	@Override
	public void initView() {
		
		content = (TextView) findViewById(R.id.content);
		phone = (EditText) findViewById(R.id.phone);

	}

	private void sendSMS(String smsBody, String phone) {

		Uri smsToUri = Uri.parse("smsto:");

		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

		intent.putExtra("sms_body", smsBody);

		startActivity(intent);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch(v.getId()) {
		case R.id.submit:

			sendSMS(content.getText() + "", phone.getText() + "");

			break;
		}
	}

}
