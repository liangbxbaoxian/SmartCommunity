package com.wb.sc.mk.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
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
import com.wb.sc.bean.ComNotice;
import com.wb.sc.bean.Menu;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.mk.personal.MsgCenterActivity;
import com.wb.sc.mk.post.PostListActivity;
import com.wb.sc.task.AdvRequest;
import com.wb.sc.task.ComNoticeRequest;
import com.wb.sc.util.ParamsUtil;

/**
 * 
 * @描述：主页面
 * @作者：liang bao xian
 * @时间：2014年10月23日 上午10:19:21
 */
public class HomeActivity extends BaseActivity implements ErrorListener{
	
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
	private Adv mAdv;
	private PageInfo advPageInfo;
	
	//社区公告
	private ComNoticeRequest mComNoticeRequest;
	private ComNotice mComNotice;
	
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
		super.onClick(v);
		
		switch(v.getId()) {
		case R.id.msg:{
			Intent intent = new Intent(mActivity, MsgCenterActivity.class);
			startActivity(intent);
		}break;
		
		case R.id.phone:{
			
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
}
