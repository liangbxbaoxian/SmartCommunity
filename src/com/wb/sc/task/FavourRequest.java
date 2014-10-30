package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.FavourParser;
import com.wb.sc.bean.Favour;
import com.wb.sc.config.RespCode;

public class FavourRequest extends ParamsEncryptRequest<Favour> {
	public FavourRequest (String url, List<String> params, 
			Listener<Favour> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<Favour> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		Favour dataBean = new Favour();	
		BaseParser.parse(dataBean, resultStr);		
		if(dataBean.respCode.equals(RespCode.SUCCESS)) {	
			new FavourParser().parse(dataBean);
		}
		return Response.success(dataBean, getCacheEntry());
	}
}
