package com.wb.sc.mk.butler;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.activity.base.BasePhotoActivity;

public class PropertyRepairsActivity extends BaseHeaderActivity implements OnClickListener{
	
	private Class fragments[] = {PersonalRepairsFragment.class, PublicRepairsFragment.class};
	
	private View personalV;
	private View publicV;
	private ViewPager contentVp;
	private RepairsAdapter adapter;
	
	private Fragment[] instanceFragments;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_property_repairs);
					
		getIntentData();
		initHeader(getResources().getString(R.string.ac_property_repairs));	
		initView();
	}
	
	@Override
	public void getIntentData() {
		
	}

	@Override
	public void initView() {
		personalV = findViewById(R.id.personal_repairs);
		personalV.setSelected(true);
		personalV.setOnClickListener(this);
		publicV = findViewById(R.id.public_repairs);
		publicV.setOnClickListener(this);
		
		instanceFragments = new Fragment[fragments.length];
		contentVp = (ViewPager) findViewById(R.id.content_pager);
		adapter = new RepairsAdapter(getSupportFragmentManager());
		contentVp.setAdapter(adapter);
		contentVp.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if(position == 0) {
					personalV.setSelected(true);
					publicV.setSelected(false);
				} else {
					personalV.setSelected(false);
					publicV.setSelected(true);
				}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch(v.getId()) {
		case R.id.personal_repairs:
			contentVp.setCurrentItem(0);
			personalV.setSelected(true);
			publicV.setSelected(false);
			break;
			
		case R.id.public_repairs:
			contentVp.setCurrentItem(1);
			personalV.setSelected(false);
			publicV.setSelected(true);
			break;
		}
	}
	
	class RepairsAdapter extends FragmentPagerAdapter {
		
		public RepairsAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public Fragment getItem(int position) {
			Fragment fg = null;
			try {
				fg = (Fragment) Class.forName(fragments[position].getName()).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			instanceFragments[position] = fg;
			return fg;
		}

		@Override
		public int getCount() {
			return fragments.length;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		int position = contentVp.getCurrentItem();
		Fragment fragment = instanceFragments[position];
		if(fragment != null) {
			fragment.onActivityResult(requestCode, requestCode, data);
		}
	}
	
	/**
	 * 处理在拍照时屏幕翻转的问题
	 */
	public void onConfigurationChanged(Configuration newConfig) {  

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {   
            Configuration o = newConfig;  
            o.orientation = Configuration.ORIENTATION_PORTRAIT;  
            newConfig.setTo(o);  
        }   
        super.onConfigurationChanged(newConfig);  
    }
}
