package com.wb.sc.mk.main;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.GridView;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
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
import com.wb.sc.adapter.MenuAdapter;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.Adv;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.mk.post.PostListActivity;
import com.wb.sc.parser.Menu;
import com.wb.sc.task.AdvRequest;
import com.wb.sc.util.ParamsUtil;

public class HomeActivity extends BaseActivity implements ErrorListener{
	
	//菜单
	private GridView menuGv;
	private List<Menu> menuList;
	private MenuAdapter menuAdapter;
	
	//广告
	private ViewPager advVp;
	private CirclePageIndicator advIndicator;
	private AdvAdapter advAdapter;
	private AdvRequest mAdvRequest;
	private Adv mAdv;
	private PageInfo advPageInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		getIntentData();
		initView();
		setUmeng();
	}

	public void getIntentData() {

	}

	public void initView() {
		menuGv = (GridView) findViewById(R.id.menu);
		initMenu();
		menuAdapter = new MenuAdapter(this, menuList);
		menuGv.setAdapter(menuAdapter);
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
		params.add(ParamsUtil.getReqParam(advPageInfo.pageNo+"", 3));
		params.add(ParamsUtil.getReqParam(advPageInfo.pageSize+"", 2));
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
}
