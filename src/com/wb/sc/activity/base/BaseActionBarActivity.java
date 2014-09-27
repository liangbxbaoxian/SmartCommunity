package com.wb.sc.activity.base;

import java.lang.reflect.Field;




import com.wb.sc.R;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuPopupHelper;
import android.support.v7.internal.view.menu.MenuPresenter;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.TextView;

//import android.util.Log;

/**
 * 封装ActionBar的一些常用功能
 * @author liangbx
 *
 */
public class BaseActionBarActivity extends ActionBarActivity implements MenuBuilder.Callback, MenuPresenter.Callback{
	
//	private static final String TAG = "BaseActionBarActivity";
	private static final int ID_ACTION_OVERFLOW = 0X7FAAAAA;
	
	//默认不显示溢出菜单
	private boolean mShowOverflowMenu = false;
	private MenuBuilder mMenuBuilder;
	private MenuPopupHelper mMenuHelper;
	private int mMenuId;
	private int mOverflowMenuIconId;
	private OnMenuItemClickListener mMenuItemClickListener;
	
	private TextView titleTv;
				
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		hideActionBar();
	}
	
	@Override
	protected void onStart() {
		super.onStart();	
		setSupportProgressBarIndeterminateVisibility(false);
	}

	/**
	 * 控制溢出菜单是否显和显示的数据资源
	 * @param menuId 放置在menu文件夹的资源文件ID
	 * @param listener 菜单项点击监听事件
	 */
	public void setOverflowMenu(int menuId, int iconId, OnMenuItemClickListener listener) {
		mShowOverflowMenu = true;
		mMenuId = menuId;
		mOverflowMenuIconId = iconId;
		mMenuItemClickListener = listener;
		hidSystemOverflowMenu();					
	}
	
	/**
	 * 设置ActionBar上显示的图标按钮
	 * @param menu
	 * @param id
	 * @param titleId
	 * @param iconId
	 */
	public MenuItem setActionBarItem(Menu menu, int id, int titleId, int iconId) {
		MenuItem menuItem = menu.add(0, id, 0, titleId);
		menuItem.setIcon(iconId);
		MenuItemCompat.setShowAsAction(menuItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
				
		return menuItem;
	}
	
	/**
	 * 设置ActionBar上显示的文本按钮
	 * @param menu
	 * @param id
	 * @param titleId
	 */
	public MenuItem setActionBarItem(Menu menu, int id, int titleId) {
		MenuItem menuItem = menu.add(0, id, 0, titleId);
		MenuItemCompat.setShowAsAction(menuItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
		return menuItem;
	}
	
	/**
	 * 设置ActionBar上的分割线
	 * @param menu
	 */
	public void setActionBarDivier(Menu menu) {
		MenuItem menuItem = menu.add(0, 0, 0, "");
		menuItem.setEnabled(false);
		MenuItemCompat.setShowAsAction(menuItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
	}
	
	/**
	 * 隐藏ActionBar
	 */
	public void hideActionBar() {
		getSupportActionBar().hide();
	}
	
	/**
	 * 显示ActionBar
	 */
	public void showActionBar() {
		getSupportActionBar().show();
	}
	
	/**
	 * 设置是否显示返回按钮
	 * @param showHomeAsUp
	 */
	public void setDisplayHomeAsUpEnabled(boolean enable) {
		getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
	}
	
	public void setDisplayShowCustomEnabled(boolean enable) {
		getSupportActionBar().setDisplayShowCustomEnabled(enable);				
	}
	
	public void setDisplayShowHomeEnabled(boolean enable) {
		getSupportActionBar().setDisplayShowHomeEnabled(enable);
	}
	
	public void setDisplayShowTitleEnabled(boolean enable) {
		getSupportActionBar().setDisplayShowTitleEnabled(enable);
	}
	
	public void setDisplayUseLogoEnabled(boolean enable) {
		getSupportActionBar().setDisplayUseLogoEnabled(enable);
	}
	
	public void setIndeterminateBarVisibility(boolean visible) {
		setSupportProgressBarIndeterminateVisibility(visible);
	}
	
	public void setLogo(int resId) {
		getSupportActionBar().setLogo(resId);
	}
	
	public void setLogo(Drawable logo) {
		getSupportActionBar().setLogo(logo);
	}
	
	public void setIcon(int resId) {
		getSupportActionBar().setIcon(resId);
	}
	
	public void setIcon(Drawable icon) {
		getSupportActionBar().setIcon(icon);
	}
	
	/**
	 * 设置标题
	 */
	public void setTitle(int resId) {
		titleTv.setText(resId);
	}
	
	/**
	 * 设置标题
	 */
	public void setTitle(CharSequence title) {
		titleTv.setText(title);
	}
	
	/**
	 * 设置标题颜色
	 */
	public void setTitleColor(int color) {
		int titleId = Resources.getSystem().getIdentifier(  
                "action_bar_title", "id", "android");
		if(titleId <= 0) {
			titleId = android.support.v7.appcompat.R.id.action_bar_title;
		}
		TextView titleTextView = (TextView) findViewById(titleId);
		titleTextView.setTextColor(color);
	}
	
	public void setTitleColorById(int resId) {
		setTitleColor(getResources().getColor(resId));
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {	
		getSupportActionBar().setTitle("");
		
		MenuItem menuItem = menu.add("title");
		MenuItemCompat.setActionView(menuItem, R.layout.action_bar_title_layout);
		MenuItemCompat.setShowAsAction(menuItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
		initTitleView(MenuItemCompat.getActionView(menuItem));
				
		if(mShowOverflowMenu) {
			MenuItem moreItem = menu.add(0, ID_ACTION_OVERFLOW, 0, R.string.action_more);         
	        moreItem.setIcon(mOverflowMenuIconId);
	        MenuItemCompat.setShowAsAction(moreItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);		 
	        
	        mMenuBuilder = new MenuBuilder(this);
			mMenuBuilder.setCallback(this);
			MenuInflater inflater = new MenuInflater(this);			
			inflater.inflate(mMenuId, mMenuBuilder);
		}
				
		return super.onCreateOptionsMenu(menu);
	}
	
	private void initTitleView(View view) {
		titleTv = (TextView) view.findViewById(R.id.title);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()) {
		case ID_ACTION_OVERFLOW:
			showOverflowMenu();
			return true;
			
		case android.R.id.home:
			finish();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyUp(final int keyCode, final KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if(mShowOverflowMenu)
				showOverflowMenu();
			return true;
		}
		
		return super.onKeyUp(keyCode, event);
	}
	
	/**
	 *强制隐藏系统默认的溢出菜单
	 */
	private void hidSystemOverflowMenu() {		 
	     try {
	        ViewConfiguration config = ViewConfiguration.get(this);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if(menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, true);
	        }	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	private void showOverflowMenu() {
		if(mMenuHelper == null) {			
			mMenuHelper = new MenuPopupHelper(this, mMenuBuilder, findViewById(ID_ACTION_OVERFLOW), true);
			mMenuHelper.setForceShowIcon(true);
			mMenuHelper.setCallback(this);
			mMenuHelper.show();		
		} else {
			mMenuHelper.show();
		}
	}
	
	/**
	 * 获取更多菜单中的Item
	 * @param index
	 * @return
	 */
	public MenuItem getOverflowMenuItem(int index) {
		if(mMenuBuilder != null) {
			return mMenuBuilder.getItem(index);
		}
		return null; 
	}

	@Override
	public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
//		Log.d(TAG, "onMenuItemSelected");
		if(mMenuItemClickListener != null)
			mMenuItemClickListener.onMenuItemClick(item);
		return false;
	}

	@Override
	public void onMenuModeChange(MenuBuilder menu) {	
//		Log.d(TAG, "onMenuModeChange");
	}

	@Override
	public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
//		Log.d(TAG, "onCloseMenu");		
	}

	@Override
	public boolean onOpenSubMenu(MenuBuilder menu) {
//		Log.d(TAG, "onOpenSubMenu");
		return false;
	}
}
