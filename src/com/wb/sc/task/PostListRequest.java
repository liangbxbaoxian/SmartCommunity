package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.PostListParser;
import com.wb.sc.bean.PostList;
import com.wb.sc.config.RespCode;

public class PostListRequest extends ParamsEncryptRequest<PostList> {
	public PostListRequest (String url, List<String> params, 
			Listener<PostList> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<PostList> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		PostList dataBean = new PostList();	
		BaseParser.parse(dataBean, resultStr);	
		if(dataBean.respCode.equals(RespCode.SUCCESS)) {
			new PostListParser().parse(dataBean);
		}
		return Response.success(dataBean, getCacheEntry());
	}
}
