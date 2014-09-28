package com.wb.sc.mk.butler;

import android.os.Bundle;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;

public class PropertyBillActivity extends BaseHeaderActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_property_bill);
		initHeader(getResources().getString(R.string.ac_property_bill));
	}

	@Override
	public void getIntentData() {
		
	}

	@Override
	public void initView() {
		
	}
}
