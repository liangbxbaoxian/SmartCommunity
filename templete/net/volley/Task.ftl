package ${PackageName}.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import ${PackageName}.parser.BaseParser;
import ${PackageName}.parser.${ParserName};
import ${PackageName}.bean.${DataClassName};

<#if isList == "false">
public class ${ClassName} extends ParamsEncryptRequest<${DataClassName}> {
	public ${ClassName} (String url, List<String> params, 
			Listener<${DataClassName}> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<${DataClassName}> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		${DataClassName} dataBean = new ${DataClassName}();	
		BaseParser.parse(dataBean, resultStr);			
		new ${ParserName}().parse(dataBean);
		return Response.success(dataBean, getCacheEntry());
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
		${DataClassName} dataBean = new ${DataClassName}();	
		BaseParser.parse(dataBean, resultStr);			
		new ${ParserName}().parse(dataBean);
		return Response.success(dataBean, getCacheEntry());
	}
}
</#if>
