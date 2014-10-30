package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.CommentListParser;
import com.wb.sc.bean.CommentList;
import com.wb.sc.config.RespCode;

public class CommentListRequest extends ParamsEncryptRequest<CommentList> {
	public CommentListRequest (String url, List<String> params, 
			Listener<CommentList> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<CommentList> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		CommentList dataBean = new CommentList();	
		BaseParser.parse(dataBean, resultStr);		
		if(dataBean.respCode.equals(RespCode.SUCCESS)) {	
			new CommentListParser().parse(dataBean);
		}
		return Response.success(dataBean, getCacheEntry());
	}
}
