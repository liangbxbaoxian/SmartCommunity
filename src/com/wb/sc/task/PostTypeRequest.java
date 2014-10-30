package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.PostTypeParser;
import com.wb.sc.bean.PostType;
import com.wb.sc.config.RespCode;

public class PostTypeRequest extends ParamsEncryptRequest<PostType> {
	public PostTypeRequest (String url, List<String> params, 
			Listener<PostType> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<PostType> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		PostType dataBean = new PostType();	
		BaseParser.parse(dataBean, resultStr);		
		if(dataBean.respCode.equals(RespCode.SUCCESS)) {	
			new PostTypeParser().parse(dataBean);
		}
		return Response.success(dataBean, getCacheEntry());
	}
}
