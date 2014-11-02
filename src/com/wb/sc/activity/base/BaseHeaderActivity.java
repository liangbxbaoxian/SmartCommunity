package com.wb.sc.activity.base;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wb.sc.R;

public abstract class BaseHeaderActivity extends BaseActivity implements OnClickListener{
	
	protected View headerBackIv;
	protected View headerHomeIv;
	protected TextView headerTitleTv;
	
	public void initHeader(int titleId) {
		initHeader(getResources().getString(titleId));
	}
	
	public void initHeaderBack() {
		headerBackIv = findViewById(R.id.common_header_back);
		headerBackIv.setOnClickListener(this);
	}
		
	public void initHeader(String title) {
		headerBackIv = findViewById(R.id.common_header_back);
		headerBackIv.setOnClickListener(this);
		headerHomeIv = findViewById(R.id.common_header_home);
		headerTitleTv = (TextView)findViewById(R.id.common_header_title);
		headerTitleTv.setText(title);
		if(headerHomeIv != null) {
			headerHomeIv.setVisibility(View.GONE);
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
