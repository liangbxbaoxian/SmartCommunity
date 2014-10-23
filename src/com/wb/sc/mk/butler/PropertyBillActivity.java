package com.wb.sc.mk.butler;

import android.os.Bundle;
import android.widget.ListView;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.adapter.BillListAdapter;

public class PropertyBillActivity extends BaseHeaderActivity {
	
	private ListView billLv;
	private BillListAdapter billListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_property_bill);
		initHeader(getResources().getString(R.string.ac_property_bill));
		
		getIntentData();
		initView();
		
		showLoading();
	}

	@Override
	public void getIntentData() {
		
	}

	@Override
	public void initView() {
		billLv = (ListView) findViewById(R.id.list);
	}
	
	
}
