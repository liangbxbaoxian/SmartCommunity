package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.PhoneListParser;
import com.wb.sc.bean.PhoneList;
import com.wb.sc.config.RespCode;

public class PhoneListRequest extends ParamsEncryptRequest<PhoneList> {
	public PhoneListRequest (String url, List<String> params, 
			Listener<PhoneList> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<PhoneList> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		PhoneList dataBean = new PhoneList();	
		BaseParser.parse(dataBean, resultStr);		
		if(dataBean.respCode.equals(RespCode.SUCCESS)) {	
			new PhoneListParser().parse(dataBean);
		}
		return Response.success(dataBean, getCacheEntry());
	}
}
