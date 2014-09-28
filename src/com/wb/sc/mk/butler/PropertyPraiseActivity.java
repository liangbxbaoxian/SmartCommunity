package com.wb.sc.mk.butler;

import android.os.Bundle;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;

public class PropertyPraiseActivity extends BaseHeaderActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_property_praise);
		initHeader(getResources().getString(R.string.ac_property_praise));
	}

	@Override
	public void getIntentData() {
		
	}

	@Override
	public void initView() {
		
	}

}
