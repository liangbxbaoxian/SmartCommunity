package com.wb.sc.activity.base;

import android.view.Menu;

/**
 * Activity基类
 * 
 * @author liangbx
 * 
 */
public abstract class BaseBackActivity extends BaseActivity {
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setDisplayHomeAsUpEnabled(true);
		
		return super.onCreateOptionsMenu(menu);
	}
}
