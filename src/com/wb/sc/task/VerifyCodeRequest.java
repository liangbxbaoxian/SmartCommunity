package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.VerifyCodeParser;
import com.wb.sc.bean.VerifyCode;
import com.wb.sc.config.RespCode;

public class VerifyCodeRequest extends ParamsEncryptRequest<VerifyCode> {
	public VerifyCodeRequest (String url, List<String> params, 
			Listener<VerifyCode> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<VerifyCode> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		VerifyCode dataBean = new VerifyCode();	
		BaseParser.parse(dataBean, resultStr);			
		if(dataBean.respCode.equals(RespCode.SUCCESS)) {
			new VerifyCodeParser().parse(dataBean);
		}
		return Response.success(dataBean, getCacheEntry());
	}
}
