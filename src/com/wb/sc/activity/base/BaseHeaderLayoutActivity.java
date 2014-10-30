package com.wb.sc.activity.base;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wb.sc.R;

/**
 * 
 * @描述：插入通用标题栏，并进行初始化
 * @作者：liang bao xian
 * @时间：2014年10月27日 上午9:04:08
 */
public abstract class BaseHeaderLayoutActivity extends BaseActivity implements OnClickListener{
	
	protected View backIv;
	protected View homeIv;
	protected TextView titleTv;
	
	public void initHeader(int titleId) {
		initHeader(getResources().getString(titleId));
	}
	
	public void initHeaderBack() {
		backIv = findViewById(R.id.common_header_back);
		backIv.setOnClickListener(this);
	}
		
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
