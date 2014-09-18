package ${PackageName}.task;

import java.util.Map;
<#if isList == "true">
import java.util.List;
</#if>

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsRequest;
import ${PackageName}.config.DebugConfig;
import ${PackageName}.parser.${ParserName};
import ${PackageName}.bean.${DataClassName};

<#if isList == "false">
public class ${ClassName} extends ParamsRequest<${DataClassName}> {
	public ${ClassName} (int method, String url, Map<String, String> params, 
			Listener<${DataClassName}> listenre, ErrorListener errorListener) {
		super(method, url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<${DataClassName}> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		DebugConfig.showLog("volleyresponse", resultStr);
							
		${ParserName} parser = new ${ParserName}();
		return Response.success(parser.parse(resultStr), getCacheEntry());
	}
	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = super.getHeaders(); 
		
		return headers;
	}
}
<#else>
public class ${ClassName} extends ParamsRequest<List<${DataClassName}>> {
	public AccountRequest(int method, String url, Map<String, String> params, 
			Listener<List<${DataClassName}>> listenre, ErrorListener errorListener) {
		super(method, url, params, listenre, errorListener);
	}

	@Override
	protected Response<List<${DataClassName}>> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		DebugConfig.ShowLog("volleyresponse", resultStr);
		
		${ParserName} parser = new ${ParserName}();
		return Response.success(parser.parse(resultStr), getCacheEntry());
	}
	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = super.getHeaders(); 
		
		return headers;
	}
}
</#if>
