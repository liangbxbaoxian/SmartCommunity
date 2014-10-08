package com.wb.sc.mk.personal;


import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;

public class PersonalInfoActivity extends BaseActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info);
		initView();
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();

	}



	public void getIntentData() {

	}



	public void initView() {
       TextView zhuhuAuth = (TextView) findViewById(R.id.zhuhuAuth);
       zhuhuAuth.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
	}
	
	public void back(View view) {
		this.finish();
	}

}
