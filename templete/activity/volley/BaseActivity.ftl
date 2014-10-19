package ${PackageName};

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.common.config.DebugConfig;
import com.common.config.NetConfig;
import com.common.net.volley.ParamsRequest;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;

import ${PackageName}.bean.${DataName};
import ${PackageName}.task.${TaskName};
import com.wb.sc.util.ParamsUtil;

<#if isList == "false">
public class ${ClassName} extends Activity implements Listener<${DataName}>, ErrorListener{
<#else>
public class ${ClassName} extends Activity implements Listener<List<${DataName}>>, ErrorListener{
</#if>	
	private ViewGroup loadLayout;
	private ViewGroup contentLayout;
	
	private RequestQueue mQueue;	
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
		
		//request${DataName}(get${TaskName}Params(), this, this);					
	}
	
	/**
	 * 获取传入的Intent数据
	 */
	private void getIntentData() {
		
	}
	
	/**
	 * 初始化视图
	 */
	private void initView() {
		loadLayout = (ViewGroup) findViewById(R.id.loadLayout);
		contentLayout = (ViewGroup) findViewById(R.id.contentLayout);
		contentLayout.setVisibility(View.GONE);		
	}
	
	/**
	 * 获取请求参数
	 * @return
	 */
	private Map<String, String> get${TaskName}Params() {
		Map<String, String> params = new HashMap<String, String>();
		
		//请在这里填写请求参数
		
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
	private void executeRequest(int method, String methodUrl, Map<String, String> params,	 
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
		mQueue.add(m${TaskName});
		mQueue.start();
	}
	
	/**
	 * 请求错误处理，提示错误信息或者显示错误页面
	 */
	@Override
	public void onErrorResponse(VolleyError error) {		
		loadLayout.setVisibility(View.GONE);
		contentLayout.setVisibility(View.VISIBLE);
		
		ToastHelper.showToastInBottom(getApplicationContext(), VolleyErrorHelper.getErrorMessage(error));
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
			<#if isList == "false">
				m${DataName} = response;
			<#else>
				m${DataName}List = response;
			</#if>
		} else {
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}
	
}
