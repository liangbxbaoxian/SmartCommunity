package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.BillParser;
import com.wb.sc.bean.Bill;
import com.wb.sc.config.RespCode;

public class BillRequest extends ParamsEncryptRequest<Bill> {
	public BillRequest (String url, List<String> params, 
			Listener<Bill> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<Bill> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		Bill dataBean = new Bill();	
		BaseParser.parse(dataBean, resultStr);		
		if(dataBean.respCode.equals(RespCode.SUCCESS)) {	
			new BillParser().parse(dataBean);
		}
		return Response.success(dataBean, getCacheEntry());
	}
}
