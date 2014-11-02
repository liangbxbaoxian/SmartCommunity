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
import com.wb.sc.bean.MyRepairDetail;
import com.wb.sc.config.RespCode;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.MyRepairDetailParser;

public class MyRepairRequestDetail extends ParamsEncryptRequest<MyRepairDetail> {
	public MyRepairRequestDetail (String url, List<String> params, 
			Listener<MyRepairDetail> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<MyRepairDetail> parseNetworkResponse(NetworkResponse response) {
		String resultStr = null;
		try {
			resultStr = new String(response.data, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		MyRepairDetail baseBean = new MyRepairDetail();
		BaseParser.parse(baseBean, resultStr);
		
		if(baseBean.respCode.equals(RespCode.SUCCESS)) {
			new MyRepairDetailParser().parse(baseBean);
		}
		
		return Response.success(baseBean, getCacheEntry());
	}
	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = super.getHeaders(); 
		
		return headers;
	}
}
