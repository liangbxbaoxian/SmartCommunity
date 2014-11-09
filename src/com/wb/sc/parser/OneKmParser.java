package com.wb.sc.parser;


import java.util.ArrayList;

import com.wb.sc.bean.OneKm;
import com.wb.sc.util.ParamsUtil;

public class OneKmParser {

	public void parse(OneKm dataBean) {	
			
		//进行数据解析处理		
		dataBean.totalNum = ParamsUtil.getRespIntParamNext(dataBean, 4);
		dataBean.hasNextPage = ParamsUtil.getNextPageFlag(dataBean);
		int dataLength = dataBean.dataBytes.length - 9 - 1;  //接口未定义有下一页
		String datasStr = ParamsUtil.getRespParam(dataBean, 9, dataLength);
		String[] itemsStr = datasStr.split(ParamsUtil.ITEMS_DIVIDER);
		dataBean.datas = new ArrayList<OneKm.MerchantItem>();
		if (dataBean.totalNum > 0) {
			for(String itemStr : itemsStr) {
				String[] values = itemStr.split(ParamsUtil.ITEM_DIVIDER);
				OneKm.MerchantItem item =  dataBean. new MerchantItem();
				item.merchantId = values[0];
				item.merchantName = values[1];
				item.merchantCategoryId = values[2];
				item.merchantCategoryName = values[3];
				item.merchantTel = values[4];
				item.longitude = values[5];
				item.latitude = values[6];
				item.merchantLogo = values[7];
				dataBean.datas.add(item);
			}
		}
		
	}
}