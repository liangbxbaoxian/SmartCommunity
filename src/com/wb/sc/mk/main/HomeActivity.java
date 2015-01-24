package com.wb.sc.mk.main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.net.volley.VolleyErrorHelper;
import com.common.util.PageInfo;
import com.common.widget.ToastHelper;
import com.umeng.message.ALIAS_TYPE;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.proguard.C.e;
import com.umeng.update.UmengUpdateAgent;
import com.viewpagerindicator.CirclePageIndicator;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.adapter.AdvAdapter;
import com.wb.sc.adapter.MenuAdapter;
import com.wb.sc.adapter.NoticeAdapter;
import com.wb.sc.adapter.PhoneMenuAdapter;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.Adv;
import com.wb.sc.bean.ComNotice;
import com.wb.sc.bean.Item;
import com.wb.sc.bean.Menu;
import com.wb.sc.bean.PhoneList;
import com.wb.sc.bean.User;
import com.wb.sc.config.ActionConfig;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.dialog.ToastLoginDialog;
import com.wb.sc.mk.personal.MsgCenterActivity;
import com.wb.sc.mk.post.PostListActivity;
import com.wb.sc.task.AdvRequest;
import com.wb.sc.task.ComNoticeRequest;
import com.wb.sc.task.PhoneListRequest;
import com.wb.sc.util.Constans;
import com.wb.sc.util.ParamsUtil;
import com.wb.sc.util.PreferencesUtils;
import com.wb.sc.widget.CustomDialog;
import com.wb.sc.widget.CustomDialog.DialogFinish;

/**
 * 
 * @描述：主页面
 * @作者：liang bao xian
 * @时间：2014年10月23日 上午10:19:21
 */

public class HomeActivity extends BaseActivity implements ErrorListener, PhoneMenuAdapter.MenuListener{
	
	
	//公告自动播放的时间间隔
	public static final int NOTICE_AUTO_MOVE_TIME = 1 * 3 * 1000;
	
	//标题栏相关
	private View phoneV;
	private View msgV;
	
	//菜单
	private GridView menuGv;
	private List<Menu> menuList;
	private MenuAdapter menuAdapter;
	
	//广告
	private ViewPager advVp;
	private CirclePageIndicator advIndicator;
	private AdvAdapter advAdapter;
	private AdvRequest mAdvRequest;
	private AdvListener mAdvListener = new AdvListener();
	private PageInfo advPgIf = new PageInfo();
	private AdvTimeCount advTimeCount;
	
	//社区公告
	private ViewPager noticeVp;
	private NoticeAdapter noticeAdapter;
	private ComNoticeRequest mComNoticeRequest;
	private ComNotice mComNotice;
	private ComNoticeListener mComNoticeListener = new ComNoticeListener();
	private PageInfo noticePgIf = new PageInfo();
	private NoticeTimeCount noticeTimeCount;
	
	//常用电话
	private PhoneListRequest mPhoneListRequest;
	private PhoneList mPhoneList;
	private PhoneListListener mPhoneListListener = new PhoneListListener();
	private PageInfo phonePgIf = new PageInfo(20, 1);

	//滑动菜单
	protected MenuDrawer mMenuDrawer;
	protected PhoneMenuAdapter mAdapter;
	protected ListView mList;
	private int mActivePosition = 0;
	private static final String STATE_ACTIVE_POSITION =
	            "net.simonvt.menudrawer.samples.LeftDrawerSample.activePosition";
	
	List<Object> items = new ArrayList<Object>();
	
	//退出计数器
	private int exitCount;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_home);
		initMenuDraw(savedInstanceState);
		getIntentData();
		initView();
		setUmeng();
		
		requestAdv(getAdvRequestParams(), mAdvListener, this);
		requestComNotice(getComNoticeRequestParams(), mComNoticeListener, this);
		requestPhoneList(getPhoneListRequestParams(), mPhoneListListener, this);
		
		noticeTimeCount = new NoticeTimeCount(NOTICE_AUTO_MOVE_TIME, NOTICE_AUTO_MOVE_TIME);
		advTimeCount = new AdvTimeCount(NOTICE_AUTO_MOVE_TIME, NOTICE_AUTO_MOVE_TIME);
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ActionConfig.ACTION_REFRESH_COMMUNITY);
		registerReceiver(mReceiver, intentFilter); 
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		String communityName = SCApp.getInstance().getUser().communityName;
		if (!"".equals(communityName)) {
			TextView name = (TextView) findViewById(R.id.name);
			name.setText(SCApp.getInstance().getUser().communityName + ">");
		}
		
	}

	public void getIntentData() {

	}
	
	public void createShortcut () {
		boolean isCancel = PreferencesUtils.getBoolean(this, Constans.CANCEL_SHORT_CUT_REMIND);
		if (!hasShortCut() && isCancel) {
			CustomDialog dialog = new CustomDialog(this, R.style.mystyle, R.layout.shortcut_dialog, new DialogFinish(){

				@Override
				public void getFinish() {
					addShortcut();
				}});
			dialog.show();
			dialog.setOnDismissListener(new OnDismissListener() {       //临时处理
				
				@Override
				public void onDismiss(DialogInterface arg0) {
					PreferencesUtils.putBoolean(HomeActivity.this, Constans.CANCEL_SHORT_CUT_REMIND, true);
				}
			});
		}
	}
	
	public  boolean hasShortCut() { 
		  Uri uri = null; 
		  String spermi =  getAuthorityFromPermission(this,"READ_SETTINGS"); 
		  if(getSystemVersion() < 8){ 
		      uri = Uri.parse("content://"+spermi+"/favorites?notify=true"); 
		  }else{
		      uri = Uri.parse("content://"+spermi+"/favorites?notify=true"); 
		  } 
		  final ContentResolver cr = getContentResolver();
		  Cursor cursor = cr.query(uri,new String[] {"title","iconResource" },"title=?", new String[] {getString(R.string.app_name)}, null); 
		  if (cursor != null&& cursor.getCount() > 0) { 
			  cursor.close(); 
			  return true; 
		  }else {
		      return false; 
		  }
	}

	public  String getAuthorityFromPermission(Context context, String permission){
		
		if (permission == null) return null;  
		List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);   
		if (packs != null) {  
			for (PackageInfo pack : packs) {
				
				ProviderInfo[] providers = pack.providers; 
				if (providers != null) {
					for (ProviderInfo provider : providers) {
						if (provider.readPermission != null) {
							if ((provider.readPermission).contains(permission)){
								return provider.authority;
							}
						}
					}					
				}
			}

		}

		return null;

	}

	public int getSystemVersion(){ 
		return android.os.Build.VERSION.SDK_INT; 
	} 

	private void addShortcut(){  
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");  

		//快捷方式的名称  
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));  
		shortcut.putExtra("duplicate", false); //不允许重复创建  
		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
		shortcutIntent.setClassName(this, this.getClass().getName());
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

		//快捷方式的图标  
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher);  
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);  

		sendBroadcast(shortcut);  
	} 
	
	
	public void initMenuDraw(Bundle inState) {
		  if (inState != null) {
	            mActivePosition = inState.getInt(STATE_ACTIVE_POSITION);
	        }
	        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, Position.START, MenuDrawer.MENU_DRAG_CONTENT);
	        
	        View menuView = getLayoutInflater().inflate(R.layout.phone_list_layout, null);
	        mList = (ListView) menuView.findViewById(R.id.phone_list);

	        mAdapter = new PhoneMenuAdapter(this, items);
	        mAdapter.setListener(this);
	        mAdapter.setActivePosition(mActivePosition);
	        mList.setOnItemClickListener(mItemClickListener);

	        mMenuDrawer.setMenuView(menuView);
	        
	        mMenuDrawer.setContentView(R.layout.activity_home);
	        mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
	        mMenuDrawer.setSlideDrawable(R.drawable.ic_drawer);
	        mMenuDrawer.setDrawerIndicatorEnabled(true);
	        final float density = getResources().getDisplayMetrics().density;
	        int size = (int) (180 * density);
	        mMenuDrawer.setMenuSize(size);
	}
	
  private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          mActivePosition = position;
          mMenuDrawer.setActiveView(view, position);
          mAdapter.setActivePosition(position);
          createAlterDialog(((Item)items.get(position)).name, ((Item)items.get(position)).phone);
//          onMenuItemClicked(position, (Item) mAdapter.getItem(position));
      }
  };
  
  
	private void createAlterDialog(String name, final String phoneNum) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage(phoneNum);

		builder.setTitle(name);

		builder.setPositiveButton("呼叫", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				callPhone(phoneNum.split("/")[0]);
				//				dialog.dismiss();

			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	private void callPhone(String phoneNum) {
		Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNum));  
		this.startActivity(intent);  
	}

	public void initView() {
		phoneV = findViewById(R.id.phone);
		phoneV.setOnClickListener(this);
		msgV = findViewById(R.id.msg);
		msgV.setOnClickListener(this);
		
		menuGv = (GridView) findViewById(R.id.menu);
		initMenu();
		menuAdapter = new MenuAdapter(this, menuList);
		menuGv.setAdapter(menuAdapter);
		
		advVp = (ViewPager) findViewById(R.id.adv_pager);
		advIndicator = (CirclePageIndicator) findViewById(R.id.adv_indicator);
		
		TextView name = (TextView) findViewById(R.id.name);
		
		name.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				changeCommunity(arg0);
			}
		});
		
		noticeVp = (ViewPager) findViewById(R.id.notice_pager);
	}
	
	private void initMenu() {
		menuList = new ArrayList<Menu>();
		Menu menu = new Menu();
		menu.menuClass = FindActivity.class;
		menu.name = "发现";
		menu.resId = R.drawable.find_menu_selector;
		menu.type = 0;
		menuList.add(menu);
		
		menu = new Menu();
		menu.menuClass = PostListActivity.class;
		menu.name = "社区互动";
		menu.resId = R.drawable.community_menu_selector;
		menu.type = 0;
		menuList.add(menu);
		
		menu = new Menu();
		menu.menuClass = ButlerActivity.class;
		menu.name = "物业服务";
		menu.resId = R.drawable.property_menu_selector;
		menu.type = 0;
		menuList.add(menu);
		
		menu = new Menu();
		menu.menuClass = null;
		menu.name = "融汇新品";
		menu.resId = R.drawable.new_menu_selector;
		menu.type = 0;
		menuList.add(menu);
		
		menu = new Menu();
		menu.menuClass = PersonalActivity.class;
		menu.name = "个人中心";
		menu.resId = R.drawable.personal_menu_selector;
		menu.type = 0;
		menuList.add(menu);
		
	}
	
	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
		case R.id.msg:{
			if(ToastLoginDialog.checkLogin(mActivity)) {
				Intent intent = new Intent(mActivity, MsgCenterActivity.class);
				startActivity(intent);
			}
		}break;
		
		case R.id.phone:{
			mMenuDrawer.toggleMenu();
		}break;
		}
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
		mPushAgent.onAppStart();
		
		//如果登录状态，则传入APP ID
		new Thread() {
			
			@Override
			public void run() {
				User user = SCApp.getInstance().getUser();
				PushAgent mPushAgent = PushAgent.getInstance(HomeActivity.this);
				if(user.isLogin == 1) {
					try {
						mPushAgent.addAlias(user.userId, ALIAS_TYPE.BAIDU);
					} catch (e e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					try {
						mPushAgent.removeAlias(user.userId, ALIAS_TYPE.BAIDU);
					} catch (e e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
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
	
	/**
	 * 网络请求错误处理
	 *
	 */
	@Override
	public void onErrorResponse(VolleyError error) {	
		ToastHelper.showToastInBottom(getApplicationContext(), VolleyErrorHelper.getErrorMessage(this, error));
	}
	
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getAdvRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG20", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().communityId, 64));
		params.add(ParamsUtil.getReqParam("02", 2));
		params.add(ParamsUtil.getReqIntParam(advPgIf.pageNo, 3));
		params.add(ParamsUtil.getReqIntParam(advPgIf.pageSize, 2));
		return params;
	}
	
	/**
	 * 执行任务请求
	 * @param method
	 * @param url
	 * @param params
	 * @param listenre
	 * @param errorListener
	 */	
	private void requestAdv(List<String> params,	 
			Listener<Adv> listenre, ErrorListener errorListener) {			
		if(mAdvRequest != null) {
			mAdvRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mAdvRequest = new AdvRequest(url, params, listenre, errorListener);
		startRequest(mAdvRequest);		
	}
	
	/**
	 * 
	 * @描述：广告监听
	 * @作者：liang bao xian
	 * @时间：2014年10月23日 上午10:20:51
	 */
	class AdvListener implements Listener<Adv> {
		/**
		 * 请求完成，处理UI更新
		 */
		@Override
		public void onResponse(Adv response) {		
			if(response.respCode.equals(RespCode.SUCCESS)) {			
				advAdapter = new AdvAdapter(mActivity, response);
				advVp.setAdapter(advAdapter);
				advIndicator.setViewPager(advVp);
				
				advTimeCount.cancel();
				advTimeCount.start();
			} else {
				ToastHelper.showToastInBottom(mActivity, response.respCodeMsg);
			}
		}
	}
	
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getComNoticeRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG12", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().getCommunityId(), 64));
		params.add(ParamsUtil.getReqIntParam(noticePgIf.pageNo, 3));
		params.add(ParamsUtil.getReqIntParam(noticePgIf.pageSize, 2));
		return params;
	}
	
	/**
	 * 执行任务请求
	 * @param method
	 * @param url
	 * @param params
	 * @param listenre
	 * @param errorListener
	 */	
	private void requestComNotice(List<String> params,	 
			Listener<ComNotice> listenre, ErrorListener errorListener) {			
		if(mComNoticeRequest != null) {
			mComNoticeRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mComNoticeRequest = new ComNoticeRequest(url, params, listenre, errorListener);
		startRequest(mComNoticeRequest);		
	}
	
	/**
	 * 
	 * @描述：社区通知监听
	 * @作者：liang bao xian
	 * @时间：2014年10月23日 上午10:20:32
	 */
	class ComNoticeListener implements Listener<ComNotice> {
		/**
		 * 请求完成，处理UI更新
		 */
		@Override
		public void onResponse(ComNotice response) {			
			if(response.respCode.equals(RespCode.SUCCESS)) {			
				mComNotice = response;
				noticeAdapter = new NoticeAdapter(mActivity, mComNotice);
				noticeVp.setAdapter(noticeAdapter);
				noticeTimeCount.cancel();
				noticeTimeCount.start();
			} else {
				ToastHelper.showToastInBottom(mActivity, response.respCodeMsg);
			}
		}
	}

	
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getPhoneListRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG14", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().getCommunityId(), 64));
		params.add(ParamsUtil.getReqIntParam(phonePgIf.pageNo, 3));
		params.add(ParamsUtil.getReqIntParam(phonePgIf.pageSize, 2));
		return params;
	}
	
	/**
	 * 执行任务请求
	 * @param method
	 * @param url
	 * @param params
	 * @param listenre
	 * @param errorListener
	 */	
	private void requestPhoneList(List<String> params,	 
			Listener<PhoneList> listenre, ErrorListener errorListener) {			
		if(mPhoneListRequest != null) {
			mPhoneListRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mPhoneListRequest = new PhoneListRequest(url, params, listenre, errorListener);
		startRequest(mPhoneListRequest);		
	}
	
	/**
	 * 
	 * @描述：电话列表监听
	 * @作者：liang bao xian
	 * @时间：2014年11月1日 上午10:41:31
	 */
	class PhoneListListener implements Listener<PhoneList> {
		
		/**
		 * 请求完成，处理UI更新
		 */
		@Override
		public void onResponse(PhoneList response) {		
			if(response.respCode.equals(RespCode.SUCCESS)) {			
				mPhoneList = response;
				if(mPhoneList.datas.size() > 0) {
				List<Object> items = new ArrayList<Object>();
					for(com.wb.sc.bean.PhoneList.Item item : response.datas) {
						items.add(new Item(item.remarks, item.number));
					}
					HomeActivity.this.items = items;
					mAdapter = new PhoneMenuAdapter(mActivity, items);
					mList.setAdapter(mAdapter);
				}
			} else {
				ToastHelper.showToastInBottom(mActivity, response.respCodeMsg);
			}
		}
	}
	
	@Override
	public void onActiveViewChanged(View v) {
		
	}
	
	public void changeCommunity(View v) {
		Intent intent = new Intent(HomeActivity.this, SetCommunityActivity.class);
		startActivityForResult(intent, Constans.SET_COMMUNITY_REQUEST_CODE);
	}
	
	/**
	 * 社区公告通知的时间
	 * @author Administrator
	 *
	 */
	class NoticeTimeCount extends CountDownTimer {

		public NoticeTimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		
		@Override
		public void onFinish() {
			int currentItem = noticeVp.getCurrentItem();
			if(currentItem < noticeVp.getChildCount() - 1) {
				currentItem++;
			} else {
				currentItem = 0;
			}
			noticeVp.setCurrentItem(currentItem);
			noticeTimeCount.cancel();
			noticeTimeCount.start();
		}
		
		@Override
		public void onTick(long millisUntilFinished) {
			
		}		
	}
	
	class AdvTimeCount extends CountDownTimer {

		public AdvTimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			int currentItem = advVp.getCurrentItem();
			if(currentItem < advVp.getChildCount() - 1) {
				currentItem++;
			} else {
				currentItem = 0;
			}
			advIndicator.setCurrentItem(currentItem);
			advTimeCount.cancel();
			advTimeCount.start();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			
		}
	}
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
			
	    @Override
		public void onReceive(Context context, Intent intent) {
		    if(intent.getAction().equals(ActionConfig.ACTION_REFRESH_COMMUNITY)) {
		    	requestAdv(getAdvRequestParams(), mAdvListener, HomeActivity.this);
				requestComNotice(getComNoticeRequestParams(), mComNoticeListener, HomeActivity.this);
		    } 
		}
	};
	
	/**
	 * 2秒内按两次退出程序
	 */
	@Override
	public void onBackPressed() {
		
		if(exitCount==0) {
			ToastHelper.showToastInBottom(this, R.string.exit_toast);
			
			new Thread() {

				@Override
				public void run() {
					try {
						sleep(2000);
						exitCount = 0;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}.start();
			
			exitCount++;
		} else {
			finish();
		}
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}
}
