package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.CommentParser;
import com.wb.sc.bean.Comment;
import com.wb.sc.config.RespCode;

public class CommentRequest extends ParamsEncryptRequest<Comment> {
	public CommentRequest (String url, List<String> params, 
			Listener<Comment> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<Comment> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		Comment dataBean = new Comment();	
		BaseParser.parse(dataBean, resultStr);		
		if(dataBean.respCode.equals(RespCode.SUCCESS)) {	
			new CommentParser().parse(dataBean);
		}
		return Response.success(dataBean, getCacheEntry());
	}
}
