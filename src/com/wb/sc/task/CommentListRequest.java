package com.wb.sc.task;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsRequest;
import com.wb.sc.config.DebugConfig;
import com.wb.sc.parser.CommentListParser;
import com.wb.sc.bean.CommentList;

public class CommentListRequest extends ParamsRequest<CommentList> {
	public CommentListRequest (int method, String url, Map<String, String> params, 
			Listener<CommentList> listenre, ErrorListener errorListener) {
		super(method, url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<CommentList> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		DebugConfig.showLog("volleyresponse", resultStr);
							
		CommentListParser parser = new CommentListParser();
		return Response.success(parser.parse(resultStr), getCacheEntry());
	}
	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = super.getHeaders(); 
		
		return headers;
	}
}
