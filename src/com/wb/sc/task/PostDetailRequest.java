package com.wb.sc.task;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsRequest;
import com.wb.sc.config.DebugConfig;
import com.wb.sc.parser.PostDetailParser;
import com.wb.sc.bean.PostDetail;

public class PostDetailRequest extends ParamsRequest<PostDetail> {
	public PostDetailRequest (int method, String url, Map<String, String> params, 
			Listener<PostDetail> listenre, ErrorListener errorListener) {
		super(method, url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<PostDetail> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		DebugConfig.showLog("volleyresponse", resultStr);
							
		PostDetailParser parser = new PostDetailParser();
		return Response.success(parser.parse(resultStr), getCacheEntry());
	}
	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = super.getHeaders(); 
		
		return headers;
	}
}
