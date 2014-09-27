package com.wb.sc.mk.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;

public class MainActivity extends BaseActivity implements OnClickListener{
	
	private Class fragments[] = {HomeFragment.class, FindFragment.class, PostFragment.class,
			ButlerFragment.class, PersonalFragment.class,};
	
	private ViewGroup homeVg;
	private ViewGroup discoverVg;
	private ViewGroup postVg;
	private ViewGroup stewardVg;
	private ViewGroup centerVg;
	private List<View> menuViewList;
	
	private ViewPager contentVp;
	private MenuAdapter menuAdapter;
	private Fragment[] instanceFragments;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getIntentData();
		initView();
		setUmeng();
	}

	public void getIntentData() {

	}

	public void initView() {
		menuViewList = new ArrayList<View>();
		
		homeVg = (ViewGroup) findViewById(R.id.home);
		homeVg.setOnClickListener(this);
		menuViewList.add(homeVg);
		discoverVg = (ViewGroup) findViewById(R.id.discover);
		discoverVg.setOnClickListener(this);
		menuViewList.add(discoverVg);
		postVg = (ViewGroup) findViewById(R.id.post);
		postVg.setOnClickListener(this);
		menuViewList.add(postVg);
		stewardVg = (ViewGroup) findViewById(R.id.steward);
		stewardVg.setOnClickListener(this);
		menuViewList.add(stewardVg);
		centerVg = (ViewGroup) findViewById(R.id.center);
		centerVg.setOnClickListener(this);
		menuViewList.add(centerVg);
		
		setBottomState(homeVg);
		
		instanceFragments = new Fragment[fragments.length];
		contentVp = (ViewPager) findViewById(R.id.content_pager);
		menuAdapter = new MenuAdapter(getSupportFragmentManager());
		contentVp.setAdapter(menuAdapter);
		contentVp.setOffscreenPageLimit(1);
		contentVp.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				setBottomState(menuViewList.get(position));
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}

	private void setUmeng() {
		// 检测更新
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setUpdateAutoPopup(true);

		// 开启消息推送
		PushAgent mPushAgent = PushAgent.getInstance(this);
		if(!mPushAgent.isEnabled()) {
			mPushAgent.enable(mRegisterCallback);
		}
	}
	
	public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {

		@Override
		public void onRegistered(String registrationId) {
			
		}
		
	};
	
	public IUmengUnregisterCallback mUnregisterCallback = new IUmengUnregisterCallback() {
		
		@Override
		public void onUnregistered(String registrationId) {
			
		}
	};

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.home:
			setBottomState(homeVg);
			contentVp.setCurrentItem(0);
			break;
		case R.id.discover:
			setBottomState(discoverVg);
			contentVp.setCurrentItem(1);
			break;
			
		case R.id.post:
			setBottomState(postVg);
			contentVp.setCurrentItem(2);
			break;
			
		case R.id.steward:
			setBottomState(stewardVg);
			contentVp.setCurrentItem(3);
			break;
		case R.id.center:
			setBottomState(centerVg);
			contentVp.setCurrentItem(4);
			break;
		}
			
	}

	private void setBottomState(View v) {
		for(View view : menuViewList) {
			view.setSelected(false);			
		}		
		v.setSelected(true);
	}
	
//	public void replaceFragment(Fragment fragment, boolean toBack) {
//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//		ft.replace(R.id.realtabcontent, fragment);
//		if(toBack) {
//			ft.addToBackStack("");
//		}
//		ft.commit();
//	}
	
	class MenuAdapter extends FragmentPagerAdapter {
		
		public MenuAdapter(android.support.v4.app.FragmentManager fm) {
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
		public void destroyItem(ViewGroup container, int position, Object object) {
			instanceFragments[position] = null;
			super.destroyItem(container, position, object);
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
