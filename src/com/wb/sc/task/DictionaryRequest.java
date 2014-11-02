package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.bean.Dictionary;
import com.wb.sc.config.RespCode;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.DictionaryParser;

public class DictionaryRequest extends ParamsEncryptRequest<Dictionary> {
	public DictionaryRequest (String url, List<String> params, 
			Listener<Dictionary> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<Dictionary> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		Dictionary dataBean = new Dictionary();	
		BaseParser.parse(dataBean, resultStr);		
		if(dataBean.respCode.equals(RespCode.SUCCESS)) {	
			new DictionaryParser().parse(dataBean);
		}
		return Response.success(dataBean, getCacheEntry());
	}
}
