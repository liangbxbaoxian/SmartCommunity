package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.UserParser;
import com.wb.sc.bean.User;

public class LoginRequest extends ParamsEncryptRequest<User> {
	public LoginRequest (String url, List<String> params, 
			Listener<User> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<User> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		User dataBean = new User();	
		BaseParser.parse(dataBean, resultStr);			
		new UserParser().parse(dataBean);
		return Response.success(dataBean, getCacheEntry());
	}
}
