package com.wb.sc.mk.butler;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.config.IntentExtraConfig;
import com.wb.sc.dialog.ToastLoginDialog;
import com.wb.sc.mk.butler.PropertyRepairsActivity.RepairsAdapter;
import com.wb.sc.mk.personal.MyRepairActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;

/**
 * 
 * @描述：房屋交易信息列表
 * @作者：liang bao xian
 * @时间：2014年11月9日 下午3:19:02
 */
public class HouseTradeListActivity extends BaseHeaderActivity{

	private Class fragments[] = {HouseTradeListFragment.class, HouseTradeListFragment.class};
	
	private View personalV;
	private View publicV;
	private ViewPager contentVp;
	private TypeAdapter adapter;
	private Button postBtn;
	
	private Fragment[] instanceFragments;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_header_layout, R.layout.activity_house_trade_list);
					
		getIntentData();
		initHeader(getResources().getString(R.string.ac_house_trade_list));	
		initView();
	}
	
	@Override
	public void getIntentData() {
		
	}

	@Override
	public void initView() {		
		personalV = findViewById(R.id.sale);
		personalV.setSelected(true);
		personalV.setOnClickListener(this);
		publicV = findViewById(R.id.lease);
		publicV.setOnClickListener(this);
		
		postBtn = (Button) findViewById(R.id.post);
		postBtn.setOnClickListener(this);
		
		instanceFragments = new Fragment[fragments.length];
		contentVp = (ViewPager) findViewById(R.id.content_pager);
		adapter = new TypeAdapter(getSupportFragmentManager());
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
		case R.id.post:
			Intent intent = new Intent(this, PostHouseTradeActivity.class);
			startActivity(intent);
			break;
			
		case R.id.sale:
			contentVp.setCurrentItem(0);
			personalV.setSelected(true);
			publicV.setSelected(false);
			break;
			
		case R.id.lease:
			contentVp.setCurrentItem(1);
			personalV.setSelected(false);
			publicV.setSelected(true);
			break;
		}
	}
	
	class TypeAdapter extends FragmentPagerAdapter {
		
		public TypeAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public Fragment getItem(int position) {
			Fragment fg = null;
			try {
				fg = (Fragment) Class.forName(fragments[position].getName()).newInstance();
				Bundle bundle = new Bundle();
				bundle.putInt(IntentExtraConfig.HOUSE_TRADE_TYPE, position);
				fg.setArguments(bundle);
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
}
