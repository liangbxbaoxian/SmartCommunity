package com.wb.sc.task;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.bean.OneKm;
import com.wb.sc.config.RespCode;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.OneKmParser;

public class OneKmRequest extends ParamsEncryptRequest<OneKm> {
	public OneKmRequest (String url, List<String> params, 
			Listener<OneKm> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<OneKm> parseNetworkResponse(NetworkResponse response) {
		String resultStr = null;
		try {
			resultStr = new String(response.data, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		OneKm baseBean = new OneKm();
		BaseParser.parse(baseBean, resultStr);
		
		if(baseBean.respCode.equals(RespCode.SUCCESS)) {
			new OneKmParser().parse(baseBean);
		}
		
		return Response.success(baseBean, getCacheEntry());
	}
	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = super.getHeaders(); 
		
		return headers;
	}
}
