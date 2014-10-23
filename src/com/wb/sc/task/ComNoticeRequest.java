package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.ComNoticeParser;
import com.wb.sc.bean.ComNotice;

public class ComNoticeRequest extends ParamsEncryptRequest<ComNotice> {
	public ComNoticeRequest (String url, List<String> params, 
			Listener<ComNotice> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<ComNotice> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		ComNotice dataBean = new ComNotice();	
		BaseParser.parse(dataBean, resultStr);			
		new ComNoticeParser().parse(dataBean);
		return Response.success(dataBean, getCacheEntry());
	}
}
