package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.ModifyPwdParser;
import com.wb.sc.bean.ModifyPwd;
import com.wb.sc.config.RespCode;

public class ModifyPwdRequest extends ParamsEncryptRequest<ModifyPwd> {
	public ModifyPwdRequest (String url, List<String> params, 
			Listener<ModifyPwd> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<ModifyPwd> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		ModifyPwd dataBean = new ModifyPwd();	
		BaseParser.parse(dataBean, resultStr);		
		if(dataBean.respCode.equals(RespCode.SUCCESS)) {	
			new ModifyPwdParser().parse(dataBean);
		}
		return Response.success(dataBean, getCacheEntry());
	}
}
