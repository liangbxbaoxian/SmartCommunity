package com.wb.sc.parser;


import java.util.ArrayList;

import com.wb.sc.bean.PhoneList;
import com.wb.sc.util.ParamsUtil;

public class PhoneListParser {

	public void parse(PhoneList dataBean) {	
					
		//进行数据解析处理
		dataBean.totalNum = ParamsUtil.getRespIntParamNext(dataBean, 4);
		dataBean.hasNextPage = ParamsUtil.getNextPageFlag(dataBean);
		dataBean.datas = new ArrayList<PhoneList.Item>();
		if(dataBean.totalNum == 0) return;
		
		int dataLength = dataBean.dataBytes.length - 9 - 1;
		String datasStr = ParamsUtil.getRespParam(dataBean, 9, dataLength);
		String[] itemsStr = datasStr.split(ParamsUtil.ITEMS_DIVIDER);		
		for(String itemStr : itemsStr) {
			PhoneList.Item item = dataBean.new Item();
			String[] values = itemStr.split(ParamsUtil.ITEM_DIVIDER);
			item.id = values[0];
			item.number = values[1];
			item.remarks = values[2];			
			dataBean.datas.add(item);
		}
	}
}