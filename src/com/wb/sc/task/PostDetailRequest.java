package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.bean.PostDetail;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.PostDetailParser;

public class PostDetailRequest extends ParamsEncryptRequest<PostDetail> {
	public PostDetailRequest (String url, List<String> params, 
			Listener<PostDetail> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<PostDetail> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		PostDetail dataBean = new PostDetail();	
		BaseParser.parse(dataBean, resultStr);			
		new PostDetailParser().parse(dataBean);
		return Response.success(dataBean, getCacheEntry());
	}
}
