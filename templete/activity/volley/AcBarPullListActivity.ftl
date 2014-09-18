package ${PackageName};

import java.util.HashMap;
<#if isList == "false">
<#else>
import java.util.List;
</#if>
import java.util.Map;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.util.PageInfo;
import ${PackageName}.activity.base.BaseActivity;
import ${PackageName}.activity.base.ReloadListener;
import ${PackageName}.config.NetConfig;
import ${PackageName}.config.RespCode;
import ${PackageName}.config.RespParams;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.common.widget.helper.PullRefreshListViewHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import ${PackageName}.bean.${DataName};
import ${PackageName}.task.${TaskName};

<#if isList == "false">
public class ${ClassName} extends BaseActivity implements Listener<${DataName}>, 
	ErrorListener, OnItemClickListener, ReloadListener{
<#else>
public class ${ClassName} extends BaseActivity implements Listener<List<${DataName}>>, 
	ErrorListener, OnItemClickListener, ReloadListener{
</#if>	
	
	private PullToRefreshListView mPullListView;
	private PullRefreshListViewHelper mPullHelper;
	private ListView mListView;
	private PageInfo mPage;
	private int loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE;
		
	private ${TaskName} m${TaskName};
	<#if isList == "false">
	private ${DataName} m${DataName};
	<#else>
	private List<${DataName}> m${DataName}List;
	</#if>
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.${LayoutName});
		
		getIntentData();
		initView();			
	}
			
	@Override
	public void getIntentData() {
		
	}
	
	@Override
	public void initView() {
		mPullListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);		
		mPullListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				//处理下拉刷新
				mPage.pageNo = 1;
				start${DataName}Request();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				//处理上拉加载
			}
		});
		
		mPullListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				//滑动到底部的处理
				if(loadState == PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE && m${DataName}.hasNextPage) {
					loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOADING;
					mPage.pageNo++;		
					start${DataName}Request();
				}
			}
		});
		
		//设置刷新时请允许滑动的开关使能    		
		mPullListView.setScrollingWhileRefreshingEnabled(true);
		
		//设置自动刷新功能
		mPullListView.setRefreshing(false);
		
		//设置拉动模式
		mPullListView.setMode(Mode.PULL_FROM_START);
		
		mListView = mPullListView.getRefreshableView();
		mListView.setOnItemClickListener(this);
		
		mPage = new PageInfo();
		mPullHelper = new PullRefreshListViewHelper(this, mListView, mPage.pageSize);
		mPullHelper.setBottomClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(loadState == PullRefreshListViewHelper.BOTTOM_STATE_LOAD_FAIL) {
					//加载失败，点击重试
					loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOADING;
					mPullHelper.setBottomState(loadState);		
					start${DataName}Request();
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//此处设置菜单		
		setDisplayHomeAsUpEnabled(true);
		setDisplayShowHomeEnabled(false);
		
		start${DataName}Request();
		
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * 菜单点击处理
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {			
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 列表选项点击的处理
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	}
	
	/**
	 * 
	 * @描述:启动请求
	 */
	private void start${DataName}Request() {
		//request${DataName}(Method.${ReqType}, "请求方法", get${TaskName}Params(), this, this);
	}
		
	/**
	 * 获取请求参数
	 * @return
	 */
	private Map<String, String> get${TaskName}Params() {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put(RespParams.PAGE_SIZE, mPage.pageSize+"");
		params.put(RespParams.PAGE_NO, mPage.pageNo+"");	
			
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
	<#if isList == "false">
	private void request${DataName}(int method, String methodUrl, Map<String, String> params,	 
			Listener<${DataName}> listenre, ErrorListener errorListener) {			
	<#else>
			private void executeRequest(int method, String methodUrl, Map<String, String> params,		
			Listener<List<${DataName}>> listenre, ErrorListener errorListener) {
	</#if>
		if(m${TaskName} != null) {
			m${TaskName}.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL + methodUrl;
		m${TaskName} = new ${TaskName}(method, url, params, listenre, errorListener);
		startRequest(m${TaskName});		
	}
	
	/**
	 * 网络请求错误处理
	 *
	 */
	@Override
	public void onErrorResponse(VolleyError error) {	
		mPullListView.onRefreshComplete();		
		ToastHelper.showToastInBottom(getApplicationContext(), VolleyErrorHelper.getErrorMessage(this, error));
		
		if(mPage.pageNo == 1) {
			showLoadError(this);
		} else {
			loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_FAIL;
			mPullHelper.setBottomState(PullRefreshListViewHelper.BOTTOM_STATE_LOAD_FAIL, mPage.pageSize);
		}
	}
	
	@Override
	public void onReload() {
		mPage.pageNo = 1;		
		showLoading();
		start${DataName}Request();
	}
	
	/**
	 * 请求完成，处理UI更新
	 */
	@Override
	<#if isList == "false">
	public void onResponse(${DataName} response) {		
	<#else>
	public void onResponse(List<${DataName}> response) {		
	</#if>
		showContent();	
		if(response.respCode == RespCode.SUCCESS) {			
			if(response.datas.size() <= 0) {
				showEmpty();
				return;
			}
			
			if(mPage.pageNo == 1) {
				m${DataName} = response;
				// set adapter
				showContent();
			} else {
				m${DataName}.hasNextPage = response.hasNextPage;
				m${DataName}.datas.addAll(response.datas);
				//adapter notifyDataSetChanged
			}
			
			loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE;	
			if(m${DataName}.hasNextPage) {
				mPullHelper.setBottomState(PullRefreshListViewHelper.BOTTOM_STATE_LOADING);
			} else {
				mPullHelper.setBottomState(PullRefreshListViewHelper.BOTTOM_STATE_NO_MORE_DATE);
			}		
		} else {
			ToastHelper.showToastInBottom(this, response.respMsg);
		}
	}
}
