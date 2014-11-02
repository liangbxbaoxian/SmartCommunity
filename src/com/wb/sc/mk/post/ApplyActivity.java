package com.wb.sc.mk.post;

import android.os.Bundle;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.activity.base.BaseHeaderActivity;

/**
 * 
 * @描述：报名
 * @作者：liang bao xian
 * @时间：2014年11月1日 下午10:36:23
 */
public class ApplyActivity extends BaseHeaderActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply);
		initHeader(getResources().getString(R.string.ac_apply));
		
		getIntentData();
		initView();
	}

	
	@Override
	public void getIntentData() {
		
	}

	@Override
	public void initView() {
		
	}

}
