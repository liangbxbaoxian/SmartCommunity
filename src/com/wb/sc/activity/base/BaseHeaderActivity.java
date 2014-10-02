package com.wb.sc.activity.base;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wb.sc.R;

public abstract class BaseHeaderActivity extends BaseActivity implements OnClickListener{
	
	protected View backIv;
	protected View homeIv;
	protected TextView titleTv;
		
	public void initHeader(String title) {
		backIv = findViewById(R.id.common_header_back);
		backIv.setOnClickListener(this);
		homeIv = findViewById(R.id.common_header_home);
		titleTv = (TextView)findViewById(R.id.common_header_title);
		titleTv.setText(title);
		if(homeIv != null) {
			homeIv.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch(v.getId()) {
		case R.id.common_header_back:
			finish();
			break;			
		}
	}
}
