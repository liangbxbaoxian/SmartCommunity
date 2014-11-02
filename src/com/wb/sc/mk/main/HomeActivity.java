package com.wb.sc.mk.main;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.net.volley.VolleyErrorHelper;
import com.common.util.PageInfo;
import com.common.widget.ToastHelper;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;
import com.viewpagerindicator.CirclePageIndicator;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.adapter.AdvAdapter;
import com.wb.sc.adapter.LeftMenuAdapter;
import com.wb.sc.adapter.MenuAdapter;
import com.wb.sc.adapter.PhoneMenuAdapter;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.Adv;
import com.wb.sc.bean.Category;
import com.wb.sc.bean.ComNotice;
import com.wb.sc.bean.Item;
import com.wb.sc.bean.Menu;
import com.wb.sc.bean.PhoneList;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.mk.personal.MsgCenterActivity;
import com.wb.sc.mk.post.PostListActivity;
import com.wb.sc.task.AdvRequest;
import com.wb.sc.task.ComNoticeRequest;
import com.wb.sc.task.PhoneListRequest;
import com.wb.sc.util.ParamsUtil;

/**
 * 
 * @描述：主页面
 * @作者：liang bao xian
 * @时间：2014年10月23日 上午10:19:21
 */

public class HomeActivity extends BaseActivity implements ErrorListener, PhoneMenuAdapter.MenuListener{

	
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
	private Adv mAdv;
	private PageInfo advPgIf = new PageInfo();
	
	//社区公告
	private ComNoticeRequest mComNoticeRequest;
	private ComNotice mComNotice;
	private ComNoticeListener mComNoticeListener = new ComNoticeListener();
	private PageInfo noticePgIf = new PageInfo();
	
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_home);
		initMenuDraw(savedInstanceState);
		getIntentData();
		initView();
		setUmeng();
		
//		requestAdv(getAdvRequestParams(), mAdvListener, this);
//		requestComNotice(getComNoticeRequestParams(), mComNoticeListener, this);
		requestPhoneList(getPhoneListRequestParams(), mPhoneListListener, this);
	}

	public void getIntentData() {

	}
	
	public void initMenuDraw(Bundle inState) {
		  if (inState != null) {
	            mActivePosition = inState.getInt(STATE_ACTIVE_POSITION);
	        }
	        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, Position.START, MenuDrawer.MENU_DRAG_CONTENT);

	        List<Object> items = new ArrayList<Object>();
//	        items.add(new Category("常用电话"));
	        items.add(new Item("物业服务中心", "0591-12345678"));
	        items.add(new Item("夜间报修电话", "0591-12345678"));
	        items.add(new Item("洪山派出所", "0591-12345678"));
	        items.add(new Item("Item 4", "0591-12345678"));
//	        items.add(new Category("Cat 2"));
	        items.add(new Item("Item 5", "0591-12345678"));
	        items.add(new Item("Item 6", "0591-12345678"));
//	        items.add(new Category("Cat 3"));
	        items.add(new Item("Item 7", "0591-12345678"));
	        items.add(new Item("Item 8", "0591-12345678"));
//	        items.add(new Category("Cat 4"));
	        items.add(new Item("Item 9", "0591-12345678"));
	        items.add(new Item("Item 10", "0591-12345678"));
	        
	        View menuView = getLayoutInflater().inflate(R.layout.phone_list_layout, null);
	        mList = (ListView) menuView.findViewById(R.id.phone_list);

	        mAdapter = new PhoneMenuAdapter(this, items);
	        mAdapter.setListener(this);
	        mAdapter.setActivePosition(mActivePosition);

	        mList.setAdapter(mAdapter);
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
//          onMenuItemClicked(position, (Item) mAdapter.getItem(position));
      }
  };

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
		advAdapter = new AdvAdapter(mActivity);
		advVp.setAdapter(advAdapter);
		advIndicator.setViewPager(advVp);
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
			Intent intent = new Intent(mActivity, MsgCenterActivity.class);
			startActivity(intent);
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
		params.add(ParamsUtil.getReqParam("1", 2));
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
				mAdv = response;
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
}
