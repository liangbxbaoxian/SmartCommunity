package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.bean.Community;
import com.wb.sc.config.RespCode;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.CommunityParser;

public class CommunityRequest extends ParamsEncryptRequest<Community> {
	public CommunityRequest (String url, List<String> params, 
			Listener<Community> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<Community> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		Community dataBean = new Community();	
		BaseParser.parse(dataBean, resultStr);		
		if(dataBean.respCode.equals(RespCode.SUCCESS)) {	
			new CommunityParser().parse(dataBean);
		}
		return Response.success(dataBean, getCacheEntry());
	}
}
