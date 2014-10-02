package com.wb.sc.mk.butler;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;

public class HouseTradeActivity extends BaseHeaderActivity {
	
	private Spinner typeSp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_house_trade);
		initHeader(getResources().getString(R.string.ac_house_trade));
		
		getIntentData();
		initView();
	}

	@Override
	public void getIntentData() {
		
	}

	@Override
	public void initView() {
		typeSp = (Spinner) findViewById(R.id.type);
		String[] types = getResources().getStringArray(R.array.house_trade_type);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
    			R.layout.spinner_text_layout, types);
    	adapter.setDropDownViewResource(R.layout.spinner_down_text_layout);
    	typeSp.setAdapter(adapter);
	}
}
