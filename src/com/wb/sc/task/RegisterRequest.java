package com.wb.sc.task;

import java.util.List;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsRequest;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.RegisterParser;
import com.wb.sc.bean.Register;

public class RegisterRequest extends ParamsRequest<Register> {
	public RegisterRequest (String url, List<String> params, 
			Listener<Register> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<Register> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		Register dataBean = new Register();	
		BaseParser.parse(dataBean, resultStr);			
		new RegisterParser().parse(dataBean);
		return Response.success(dataBean, getCacheEntry());
	}
}
