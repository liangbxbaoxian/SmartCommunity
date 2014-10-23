package com.wb.sc.task;

import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.bean.CommentList;
import com.wb.sc.config.RespCode;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.CommentListParser;

public class CommentListRequest extends ParamsEncryptRequest<CommentList> {
	public CommentListRequest (String url, List<String> params, 
			Listener<CommentList> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<CommentList> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);		
		CommentList commentList = new CommentList();
		
		BaseParser.parse(commentList, resultStr);
		new CommentListParser().parse(commentList);		
		return Response.success(commentList, getCacheEntry());
	}
	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = super.getHeaders(); 
		
		return headers;
	}
}
