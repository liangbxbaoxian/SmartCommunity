package com.wb.sc.task;

import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.common.net.volley.ParamsEncryptRequest;
import com.wb.sc.parser.BaseParser;
import com.wb.sc.parser.SaleHouseListParser;
import com.wb.sc.bean.SaleHouseList;
import com.wb.sc.config.RespCode;

public class SaleHouseListRequest extends ParamsEncryptRequest<SaleHouseList> {
	public SaleHouseListRequest (String url, List<String> params, 
			Listener<SaleHouseList> listenre, ErrorListener errorListener) {
		super(url, params, listenre, errorListener);
	}
	
	@Override
	protected Response<SaleHouseList> parseNetworkResponse(NetworkResponse response) {
		String resultStr = new String(response.data);
		SaleHouseList dataBean = new SaleHouseList();	
		BaseParser.parse(dataBean, resultStr);		
		if(dataBean.respCode.equals(RespCode.SUCCESS)) {	
			new SaleHouseListParser().parse(dataBean);
		}
		return Response.success(dataBean, getCacheEntry());
	}
}
