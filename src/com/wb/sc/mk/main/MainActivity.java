package com.wb.sc.mk.main;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
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
import android.widget.AdapterView;
import android.widget.ListView;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.adapter.LeftMenuAdapter;
import com.wb.sc.bean.Item;
import com.wb.sc.mk.personal.MyComplaintActivity;
import com.wb.sc.mk.personal.MyExpressActivity;
import com.wb.sc.mk.personal.MyPostActivity;
import com.wb.sc.mk.personal.MyRepairActivity;
import com.wb.sc.mk.personal.PersonalInfoActivity;
import com.wb.sc.mk.personal.RegisterInviteActivity;
import com.wb.sc.mk.personal.SettingActivity;

public class MainActivity extends BaseActivity implements OnClickListener, LeftMenuAdapter.MenuListener {
	
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
	
    protected MenuDrawer mMenuDrawer;

    protected LeftMenuAdapter mAdapter;
    protected ListView mList;
    private int mActivePosition = 0;
    private static final String STATE_ACTIVE_POSITION =
            "net.simonvt.menudrawer.samples.LeftDrawerSample.activePosition";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		
		initMenuDraw(savedInstanceState);
		
		getIntentData();
		initView();
		setUmeng();
	}

	public void getIntentData() {

	}
	
	public void initMenuDraw(Bundle inState) {
		  if (inState != null) {
	            mActivePosition = inState.getInt(STATE_ACTIVE_POSITION);
	        }
	        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, Position.START, MenuDrawer.MENU_DRAG_CONTENT);

	        List<Object> items = new ArrayList<Object>();
	        items.add(new Item("Item 1", R.drawable.ic_launcher));
	        items.add(new Item("Item 2", R.drawable.ic_launcher));
//	        items.add(new Category("Cat 1"));
	        items.add(new Item("Item 3", R.drawable.ic_launcher));
	        items.add(new Item("Item 4", R.drawable.ic_launcher));
//	        items.add(new Category("Cat 2"));
	        items.add(new Item("Item 5", R.drawable.ic_launcher));
	        items.add(new Item("Item 6", R.drawable.ic_launcher));
//	        items.add(new Category("Cat 3"));
	        items.add(new Item("Item 7", R.drawable.ic_launcher));
	        items.add(new Item("Item 8", R.drawable.ic_launcher));
//	        items.add(new Category("Cat 4"));
	        items.add(new Item("Item 9", R.drawable.ic_launcher));
	        items.add(new Item("Item 10", R.drawable.ic_launcher));

	        mList = new ListView(this);

	        mAdapter = new LeftMenuAdapter(this, items);
	        mAdapter.setListener(this);
	        mAdapter.setActivePosition(mActivePosition);

	        mList.setAdapter(mAdapter);
	        mList.setOnItemClickListener(mItemClickListener);

	        mMenuDrawer.setMenuView(mList);
	        
	        mMenuDrawer.setContentView(R.layout.activity_main);
	        mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
	        mMenuDrawer.setSlideDrawable(R.drawable.ic_drawer);
	        mMenuDrawer.setDrawerIndicatorEnabled(true);
	}
	
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mActivePosition = position;
            mMenuDrawer.setActiveView(view, position);
            mAdapter.setActivePosition(position);
//            onMenuItemClicked(position, (Item) mAdapter.getItem(position));
        }
    };

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
//			instanceFragments[position] = null;
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
	
	public void personalInfo(View view) {
		Intent intent = new Intent(this, PersonalInfoActivity.class);
		startActivity(intent);
	}
	
	public void myComplaint(View view) {
		Intent intent = new Intent(this, MyComplaintActivity.class);
		startActivity(intent);
	}
	
	public void myRepair(View view) {
		Intent intent = new Intent(this, MyRepairActivity.class);
		startActivity(intent);
	}
	
	public void myForum(View view) {
		Intent intent = new Intent(this, MyPostActivity.class);
		startActivity(intent);
	}
	
	public void myExpress(View view) {
		Intent intent = new Intent(this, MyExpressActivity.class);
		startActivity(intent);
	}
	
	public void setting(View view) {
		Intent intent = new Intent(this, SettingActivity.class);
		startActivity(intent);
	}
	
	public void registerInvite(View view) {
		Intent intent = new Intent(this, RegisterInviteActivity.class);
		startActivity(intent);
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

	@Override
	public void onActiveViewChanged(View v) {
		// TODO Auto-generated method stub
		
	}
}
