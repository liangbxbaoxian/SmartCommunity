package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.bean.Register;
import com.wb.sc.config.RespCode;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.RegisterParser;

public class RegisterRequest extends ParamsEncryptRequest<Register> {
	public RegisterRequest (String url, List<String> params, 
			Listener<Register> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<Register> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		Register dataBean = new Register();	
		BaseParser.parse(dataBean, resultStr);		
		if(dataBean.respCode.equals(RespCode.SUCCESS)) {
			new RegisterParser().parse(dataBean);
		}
		return Response.success(dataBean, getCacheEntry());
	}
}
