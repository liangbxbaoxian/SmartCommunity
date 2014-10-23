package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.PComplainParser;
import com.wb.sc.bean.PComplain;

public class PComplainRequest extends ParamsEncryptRequest<PComplain> {
	public PComplainRequest (String url, List<String> params, 
			Listener<PComplain> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<PComplain> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		PComplain dataBean = new PComplain();	
		BaseParser.parse(dataBean, resultStr);			
		new PComplainParser().parse(dataBean);
		return Response.success(dataBean, getCacheEntry());
	}
}
