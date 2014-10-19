package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsRequest;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.AdvParser;
import com.wb.sc.bean.Adv;

public class AdvRequest extends ParamsRequest<Adv> {
	public AdvRequest (String url, List<String> params, 
			Listener<Adv> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<Adv> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		Adv dataBean = new Adv();	
		BaseParser.parse(dataBean, resultStr);			
		new AdvParser().parse(dataBean);
		return Response.success(dataBean, getCacheEntry());
	}
}
