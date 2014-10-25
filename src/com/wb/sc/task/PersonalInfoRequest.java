package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.bean.PersonalInfo;
import com.wb.sc.config.RespCode;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.PersonalInfoParser;

public class PersonalInfoRequest extends ParamsEncryptRequest<PersonalInfo> {
	public PersonalInfoRequest (String url, List<String> params, 
			Listener<PersonalInfo> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<PersonalInfo> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		PersonalInfo dataBean = new PersonalInfo();	
		BaseParser.parse(dataBean, resultStr);
		if(dataBean.respCode.equals(RespCode.SUCCESS)) {
			new PersonalInfoParser().parse(dataBean);
		}
		return Response.success(dataBean, getCacheEntry());
	}
}
