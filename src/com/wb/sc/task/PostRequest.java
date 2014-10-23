package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.PostParser;
import com.wb.sc.bean.Post;
import com.wb.sc.config.RespCode;

public class PostRequest extends ParamsEncryptRequest<Post> {
	public PostRequest (String url, List<String> params, 
			Listener<Post> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<Post> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		Post dataBean = new Post();	
		BaseParser.parse(dataBean, resultStr);	
		if(dataBean.respCode.equals(RespCode.SUCCESS)) {
			new PostParser().parse(dataBean);
		}
		return Response.success(dataBean, getCacheEntry());
	}
}
