package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.bean.LawRule;
import com.wb.sc.config.RespCode;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.LawRuleParser;

public class LawRuleRequest extends ParamsEncryptRequest<LawRule> {
	public LawRuleRequest (String url, List<String> params, 
			Listener<LawRule> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<LawRule> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		LawRule dataBean = new LawRule();	
		BaseParser.parse(dataBean, resultStr);
		if(dataBean.respCode.equals(RespCode.SUCCESS)) {
			new LawRuleParser().parse(dataBean);
		}
		return Response.success(dataBean, getCacheEntry());
	}
}
