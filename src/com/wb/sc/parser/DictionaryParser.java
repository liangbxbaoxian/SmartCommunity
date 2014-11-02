package com.wb.sc.parser;


import java.util.ArrayList;

import com.wb.sc.bean.Dictionary;
import com.wb.sc.bean.DictionaryItem;
import com.wb.sc.util.ParamsUtil;

public class DictionaryParser {

	public void parse(Dictionary dataBean) {	

		//进行数据解析处理		
		dataBean.totalNum = ParamsUtil.getRespIntParamNext(dataBean, 4);
		dataBean.hasNextPage = ParamsUtil.getNextPageFlag(dataBean);
		int dataLength = dataBean.dataBytes.length - 9 - 1;  //接口未定义有下一页
		String datasStr = ParamsUtil.getRespParam(dataBean, 9, dataLength);
		String[] itemsStr = datasStr.split(ParamsUtil.ITEMS_DIVIDER);
		dataBean.datas = new ArrayList<DictionaryItem>();
		for(String itemStr : itemsStr) {
			String[] values = itemStr.split(ParamsUtil.ITEM_DIVIDER);
			DictionaryItem item =   new DictionaryItem();
			item.dictionaryId = values[0];
			item.dictionaryCode = values[1];
			item.superDictionaryId = values[2];
			item.dictionaryName = values[3];
			item.dictionarySort = values[4];
			dataBean.datas.add(item);
		}
	}
}